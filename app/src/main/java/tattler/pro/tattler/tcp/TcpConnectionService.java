package tattler.pro.tattler.tcp;


import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.orhanobut.logger.Logger;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ConcurrentLinkedQueue;

import tattler.pro.tattler.common.AppPreferences;
import tattler.pro.tattler.common.DatabaseManager;
import tattler.pro.tattler.common.NotificationBroadcastReceiver;
import tattler.pro.tattler.common.NotificationBuilder;
import tattler.pro.tattler.messages.Message;
import tattler.pro.tattler.messages.MessageFactory;
import tattler.pro.tattler.security.MessageCrypto;


public class TcpConnectionService extends Service {
    private static final String SERVER_IP = "10.2.14.101";
    private static final int SERVER_PORT = 50000;
    private static final int SLEEP_MS = 5000;

    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    private ServerWriter tcpSender;
    private ServerReader tcpReceiver;

    private TcpServiceBinder tcpServiceBinder;
    private MessageFactory messageFactory;
    private TcpMessageHandler tcpMessageHandler;
    private MessageCrypto messageCrypto;

    public TcpConnectionService() {
        super();
    }

    @Override
    public void onCreate() {
        Logger.d("Creating TcpConnectionService.");
        super.onCreate();

        socket = null;
        inputStream = null;
        outputStream = null;

        tcpReceiver = new ServerReader();
        tcpSender = new ServerWriter();

        tcpServiceBinder = new TcpServiceBinder();
        DatabaseManager databaseManager = OpenHelperManager.getHelper(this, DatabaseManager.class);
        messageFactory = new MessageFactory(this);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationBuilder notificationBuilder = new NotificationBuilder(this, databaseManager);

        tcpMessageHandler = new TcpMessageHandler(
                this,
                AppPreferences.getInstance(this),
                databaseManager,
                messageFactory,
                notificationBuilder,
                notificationManager);

        messageCrypto = new MessageCrypto(databaseManager);

        NotificationBroadcastReceiver broadcastReceiver = new NotificationBroadcastReceiver(
                this,
                databaseManager,
                messageFactory);
        registerReceiver(broadcastReceiver, broadcastReceiver.createBroadcastIntentFilter());

        new Thread(() -> {
            establishConnection();
            tcpReceiver.start();
            tcpSender.start();
        }).start();
    }

    @Override
    public void onDestroy() {
        Logger.d("Destroying TcpConnectionService.");
        super.onDestroy();
        OpenHelperManager.releaseHelper();
        stopService();
        closeConnection();
        stopSelf();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return tcpServiceBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    public void sendMessage(Message message) {
        Logger.d("Passing new message to send: " + message.toString());
        tcpSender.queueMessage(message);
    }

    private synchronized void closeConnection() {
        try {
            socket.close();
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void stopService() {
        Logger.d("Closing connection with server.");
        try {
            tcpReceiver.interrupt();
            tcpSender.interrupt();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private synchronized boolean isConnected() {
        return socket != null && !socket.isClosed();
    }

    private synchronized void establishConnection() {
        while (!isConnected()) {
            Logger.d("Trying to establish connection with server.");
            try {
                socket = new Socket(SERVER_IP, SERVER_PORT);
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.flush();
                inputStream = new ObjectInputStream(socket.getInputStream());
                break;
            } catch (IOException e) {
                Logger.d("Could not establish connection... Next try in " + SLEEP_MS / 1000 + " seconds.");
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException ignored) {}
        }

        Logger.d("Connection has been already established.");
        sendMessage(messageFactory.createLoginRequest());
    }

    public class TcpServiceBinder extends Binder {
        public TcpConnectionService getService() {
            return TcpConnectionService.this;
        }
    }

    private class ServerWriter extends Thread {
        private final ConcurrentLinkedQueue<Message> messages;

        private ServerWriter() {
            messages = new ConcurrentLinkedQueue<>();
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                if (!isConnected()) {
                    establishConnection();
                    continue;
                }

                Message message = messages.poll();
                if (message != null) {
                    sendMessage(message);
                }
            }
        }

        private void sendMessage(Message message) {
            try {
                Logger.d("Sending message: " + message.toString());
                message = messageCrypto.encryptMessage(message);
                outputStream.writeObject(message);
                outputStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void queueMessage(Message message) {
            messages.add(message);
        }
    }

    private class ServerReader extends Thread {
        private void readAndHandleMessage() {
            Message message;
            try {
                message = (Message) inputStream.readObject();
                Logger.d("Received message: " + message.toString());

                message = messageCrypto.decryptMessage(message);
                tcpMessageHandler.handle(message);
            } catch (EOFException | SocketException e) {
                Logger.w("Exception while receiving message: " + e.getMessage());
                closeConnection();
                establishConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                if (!isConnected()) {
                    establishConnection();
                    continue;
                }
                readAndHandleMessage();
            }
        }


    }
}