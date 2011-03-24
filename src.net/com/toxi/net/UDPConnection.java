package com.toxi.net;

import java.io.*;
import java.net.*;

/**
 * @author Karsten Schmidt <info@postspectacular.com>
 */

class UDPConnection {

    /**
     * Default time-to-live duration
     */
    static int TTL = 10000;

    protected InetAddress ip;
    protected int port;

    protected long lastUpdate;

    UDPConnection(InetAddress ip, int port) {
        this.ip = ip;
        this.port = port;
        lastUpdate = System.currentTimeMillis();
    }

    public boolean isAlive() {
        return System.currentTimeMillis() - lastUpdate < TTL;
    }

    public void update() {
        lastUpdate = System.currentTimeMillis();
    }

    public void send(DatagramSocket socket, byte[] data) throws IOException {
        DatagramPacket sendPacket = new DatagramPacket(data, data.length, ip,
                port);
        socket.send(sendPacket);
    }

    public InetAddress getIP() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public String toString() {
        return UDPConnection.buildHash(ip, port);
    }

    static final void setTTL(int ttl) {
        TTL = ttl;
    }

    public static final String buildHash(InetAddress ip, int port) {
        return ip.getHostAddress() + ":" + port;
    }
}