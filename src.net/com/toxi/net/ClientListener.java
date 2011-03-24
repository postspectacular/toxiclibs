package com.toxi.net;

public interface ClientListener {
    /**
     * Callback to trigger an update of the server managed process. This might
     * be called several times in a row if the client has fallen behind in time.
     * There should be NO rendering related tasks being called from this
     * callback.
     */
    public void triggerUpdate();

    /**
     * Callback to trigger the rendering of a new frame. This method will ALWAYS
     * be called after {{@link #triggerUpdate()} and should be purely focused on
     * drawing/rendering specific tasks, but no CPU intensive model updates.
     */
    public void triggerFrame();

    /**
     * Callback to confirm client has successfully connected
     */
    public void notifyConnected();

    /**
     * Callback to notify client has disconnected from the server.
     */
    public void notifyDisconnected();

    /**
     * Callback to give the client a chance to react to the encountered
     * exception.
     * 
     * @param e
     *            the exception which caused the callback
     */
    public void handleError(Exception e);
}
