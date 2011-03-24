package com.toxi.net;

import java.net.DatagramPacket;

public interface ServerListener {

    /**
     * Notifies listener immediately after the server socket has been created.
     */
    public void serverStarted();

    /**
     * Notifies the listener server is shutting down.
     */
    public void serverShutdown();

    /**
     * Gives listener a chance to react to any fatal server errors.
     * 
     * @param e
     */
    public void serverError(Exception e);

    /**
     * Notifies the listener of a new server state. The state info itself is
     * attached.
     * 
     * @param s
     */
    public void serverStateChanged(ServerState s);

    /**
     * Notifies listener of a new client connection. Details are attached.
     * 
     * @param conn
     */
    public void clientConnected(UDPConnection conn);

    /**
     * Notifies listener a client has disconnected. Connection details are
     * attached.
     * 
     * @param conn
     */
    public void clientDisconnected(UDPConnection conn);

    /**
     * Notifies and forwards received client data to the listener for further
     * analysis.
     * 
     * @param conn
     *            client connection details
     * @param receivePacket
     *            data sent by the client
     */
    public void clientUpdated(UDPConnection conn, DatagramPacket receivePacket);

    /**
     * Gives the listener a chance to add extra data to the payload packet
     * distributed to each client.
     * 
     * @return additional data, or null if none required
     */
    public byte[] getSyncPayload();

}
