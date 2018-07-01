package tattler.pro.tattler.tcp;


import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import tattler.pro.tattler.messages.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;


public class TcpConnectionService extends IntentService {
    final static private String SERVICE_NAME = "TCP_CONNECTION_SERVICE";
    private static final String SERVER_IP = "10.2.14.101";
    private static final int SERVER_PORT = 50000;

    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    private ServerWriter tcpSender;
    private ServerReader tcpReceiver;

    private TcpServiceBinder tcpServiceBinder;
    private MessageHandler messageHandler;

    @SuppressWarnings("unused")
    public TcpConnectionService() {
        this(SERVICE_NAME);
    }

    public TcpConnectionService(String name) {
        super(name);
        socket = null;
        inputStream = null;
        outputStream = null;

        tcpReceiver = new ServerReader();
        tcpSender = new ServerWriter();

        tcpServiceBinder = new TcpServiceBinder();
        messageHandler = new MessageHandler(this);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeConnection();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        establishConnection();

        tcpReceiver.start();
        tcpSender.start();

        try {
            tcpSender.join();
            tcpReceiver.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Message message) {
        tcpSender.queueMessage(message);
    }

    private void closeConnection() {
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
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.flush();
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
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
                if (!isConnected())
                    establishConnection(); // TODO: Needs sending LoginRequest to reestablish connection
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
                        messageHandler.handleReceivedMessage(message);
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                        trySleep();
                        establishConnection();
                    }
                } else {
                    trySleep();
                    establishConnection();
                }
            }
        }

        private void trySleep() {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}