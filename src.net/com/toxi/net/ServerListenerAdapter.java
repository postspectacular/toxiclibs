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

	@Override
	public void clientConnected(UDPConnection conn) {

	}

	@Override
	public void clientDisconnected(UDPConnection conn) {
	}

	@Override
	public void clientUpdated(UDPConnection conn, DatagramPacket receivePacket) {
	}

	@Override
	public byte[] getSyncPayload() {
		return new byte[0];
	}

	@Override
	public void serverError(Exception e) {
	}

	@Override
	public void serverShutdown() {
	}

	@Override
	public void serverStarted() {
	}

	@Override
	public void serverStateChanged(ServerState s) {
	}
}
