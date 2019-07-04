import java.io.IOException;

public interface SDPublisher {
    // Bully Algorithm
    void publishStartElection() throws IOException;
    void publishElectionAnswer() throws IOException;
    void publishCoordinatorVictory() throws IOException;
    void publishUpdatedClock(Double newClockValue) throws IOException;
}
