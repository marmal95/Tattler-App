package tattler.pro.tattler.tcp;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.orhanobut.logger.Logger;
import tattler.pro.tattler.common.AppPreferences;
import tattler.pro.tattler.common.DatabaseManager;
import tattler.pro.tattler.messages.LoginRequestFactory;
import tattler.pro.tattler.messages.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ConcurrentLinkedQueue;


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
    private DatabaseManager databaseManager;
    private TcpMessageHandler tcpMessageHandler;

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
        databaseManager = OpenHelperManager.getHelper(this, DatabaseManager.class);
        tcpMessageHandler = new TcpMessageHandler(this, AppPreferences.getInstance(this), databaseManager);

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
        closeConnection();
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
        tcpSender.queueMessage(message);
    }

    private synchronized void closeConnection() {
        Logger.d("Closing connection with server.");
        try {
            tcpReceiver.interrupt();
            tcpReceiver.interrupt();
            socket.close();
            inputStream.close();
            outputStream.close();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    private synchronized boolean isConnected() {
        return socket != null && !socket.isClosed();
    }

    private synchronized void establishConnection() {
        Logger.d("Trying to establish connection with server.");
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.flush();
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        logConnectionStatus();
        sendMessage(new LoginRequestFactory().create(this));
    }

    private void logConnectionStatus() {
        if (!isConnected()) {
            Logger.w("Could not connect to the server.");
        } else {
            Logger.d("Connection to the server has been established.");
        }
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
                sendMessages();
            }
        }

        private void queueMessage(Message message) {
            messages.add(message);
        }

        private void sendMessages() {
            Message message = messages.poll();
            if (message != null) {
                if (!isConnected()) {
                    establishConnection();
                }
                sendMessage(message);
            }

        }

        private void sendMessage(Message message) {
            try {
                outputStream.writeObject(message);
                outputStream.flush();
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    private class ServerReader extends Thread {
        @Override
        public void run() {
            Message message;
            while (!Thread.currentThread().isInterrupted()) {
                if (isConnected()) {
                    try {
                        message = (Message) inputStream.readObject();
                        tcpMessageHandler.onMessageReceived(message);
                    } catch (SocketException e) {
                        trySleep();
                        establishConnection();
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    trySleep();
                    establishConnection();
                }
            }
        }

        private void trySleep() {
            try {
                Thread.sleep(SLEEP_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}