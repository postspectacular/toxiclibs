package toxi.music;

public interface QuantizedTimeHandler {

    void handleBar(int beatCount);

    void handleBeat(int beatCount);

    void handleTick(int ticks);

}
