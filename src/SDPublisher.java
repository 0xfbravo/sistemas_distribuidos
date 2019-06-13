import java.io.IOException;

public interface SDPublisher {
    void publishCoordinatorVictory() throws IOException;
    void publishUpdatedClock(Double newClockValue) throws IOException;
}
