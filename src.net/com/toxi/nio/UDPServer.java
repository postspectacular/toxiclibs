package com.toxi.nio;

import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.Iterator;

public class UDPServer extends Thread {
    public final static int DEFAULT_PORT = 9876;
    public final static int MAX_PACKET_SIZE = 32;

    private int port;
    private int packetSize;
    private int numClients;

    ArrayList<UDPClientState> clients;
    private int frameCount = 0;
    private int frameDuration;

    public static void main(String[] args) {
        new UDPServer(1, 25).start();
        try {
            while (true) {
                Thread.sleep(1000);
                // System.out.println("running.");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public UDPServer(int numClients, int fps) {
        this(DEFAULT_PORT, numClients, MAX_PACKET_SIZE, fps);
    }

    public UDPServer(int port, int numClients, int maxPacketSize, int fps) {
        this.port = port;
        this.numClients = numClients;
        this.packetSize = maxPacketSize;
        frameDuration = 1000 / fps;
        clients = new ArrayList<UDPClientState>();
    }

    public void run() {
        DatagramChannel channel = null;
        DatagramSocket socket = null;
        ByteBuffer buffer = null;
        try {
            channel = DatagramChannel.open();
            socket = channel.socket();
            SocketAddress address = new InetSocketAddress(
                    InetAddress.getLocalHost(), port);
            socket.bind(address);
            buffer = ByteBuffer.allocateDirect(packetSize);
            System.out.println("Server running: " + address);
            System.out.println("Heartbeat interval: " + frameDuration);
            System.out.println("waiting for clients...");

            // get the addresses-initialisation phase
            while (clients.size() < numClients) {
                clients.add(new UDPClientState(channel.receive(buffer)));
                buffer.flip();
                buffer.clear();
            }
            // server now knows the clients

            // MAIN LOOP-server is working for the clients
            while (true) {
                long beginSynch = System.currentTimeMillis();
                buffer.clear();
                buffer.putInt(frameCount++);
                buffer.flip();
                Iterator<UDPClientState> iter = clients.iterator();
                while (iter.hasNext()) {
                    channel.send(buffer, iter.next().getAddress());
                }
                long endSynch = System.currentTimeMillis();
                if (endSynch - beginSynch < frameDuration) {
                    int sleep = frameDuration - (int) (endSynch - beginSynch);
                    // System.out.println("sleeping: " + sleep);
                    Thread.sleep(sleep);
                } else {
                    Thread.sleep(2);
                }
            }
        } catch (Exception ex) {
            System.err.println(ex);
        } finally {
            if (socket != null)
                socket.close();
            if (buffer != null)
                buffer = null;
            System.out.println("server shutdown.");
        }
    }
}