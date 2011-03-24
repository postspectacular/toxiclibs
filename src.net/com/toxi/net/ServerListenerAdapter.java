/**
 * 
 */
package com.toxi.net;

import java.net.DatagramPacket;

/**
 * @author toxi
 * 
 */
public class ServerListenerAdapter implements ServerListener {

    public void clientConnected(UDPConnection conn) {

    }

    public void clientDisconnected(UDPConnection conn) {
    }

    public void clientUpdated(UDPConnection conn, DatagramPacket receivePacket) {
    }

    public byte[] getSyncPayload() {
        return new byte[0];
    }

    public void serverError(Exception e) {
    }

    public void serverShutdown() {
    }

    public void serverStarted() {
    }

    public void serverStateChanged(ServerState s) {
    }
}
