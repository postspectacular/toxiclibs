package com.toxi.nio;

import java.net.SocketAddress;

public class UDPClientState {
    SocketAddress addr;
    boolean isReady;
    long lastUpdate;

    public UDPClientState(SocketAddress a) {
        addr = a;
        isReady = true;
        lastUpdate = System.currentTimeMillis();
        System.out.println("new client: " + addr);
    }

    public SocketAddress getAddress() {
        return addr;
    }
}
