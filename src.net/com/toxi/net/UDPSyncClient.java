package com.toxi.net;

import java.net.*;
import java.util.logging.Logger;

class UDPSyncClient extends Thread {
    private ClientListener listener;

    protected String serverName;
    protected int port;
    protected int clientID;
    protected int ttl;

    private int frameCount;
    protected long lastUpdate;

    private Logger logger;

    UDPSyncClient(String serverName, int port, int clientID, int ttl) {
        this.serverName = serverName;
        this.port = port;
        this.clientID = clientID;
        this.ttl = ttl;
    }

    public void run() {
        DatagramSocket clientSocket = null;
        lastUpdate = System.currentTimeMillis();
        try {
            clientSocket = new DatagramSocket();
            InetAddress ip = InetAddress.getByName(serverName);
            byte[] sendData = new byte[] { 1 };
            byte[] receiveData = new byte[8];
            // do initial sign on by sending a message to the server
            DatagramPacket sendPacket = new DatagramPacket(sendData,
                    sendData.length, ip, port);
            clientSocket.send(sendPacket);
            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData,
                        receiveData.length);
                clientSocket.receive(receivePacket);
                String payload = new String(receivePacket.getData(), 0,
                        receivePacket.getLength());
                if (logger != null)
                    logger.finest(payload);
                int newFrameCount = Integer.parseInt(payload);
                if (newFrameCount > 0) {
                    while (frameCount < newFrameCount) {
                        if (listener != null)
                            listener.triggerUpdate();
                        frameCount++;
                    }
                }
                if (listener != null)
                    listener.triggerFrame();
                long now = System.currentTimeMillis();
                if (now - lastUpdate > ttl) {
                    sendPacket = new DatagramPacket(sendData, sendData.length,
                            ip, port);
                    clientSocket.send(sendPacket);
                    lastUpdate = now;
                    if (logger != null)
                        logger.finer("TTL updated on: " + lastUpdate);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (clientSocket != null)
                clientSocket.close();
        }
    }

    public void setListener(ClientListener l) {
        listener = l;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }
}