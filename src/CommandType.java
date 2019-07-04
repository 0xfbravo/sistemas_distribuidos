public enum CommandType {
    /* Bully Algorithm */
    ELECTION(MulticastUtils.COMMAND_ELECTION),
    ANSWER_ELECTION(MulticastUtils.COMMAND_ANSWER_ELECTION),
    COORDINATOR_VICTORY(MulticastUtils.COMMAND_COORDINATOR_VICTORY),
    UPDATE_CLOCK(MulticastUtils.COMMAND_UPDATE_CLOCK),
    SYNC_CLOCKS(MulticastUtils.COMMAND_SYNC_CLOCKS),
    ANSWER_CLOCK_SYNC(MulticastUtils.COMMAND_ANSWER_CLOCK_SYNC),
    KILL_PROCESS(MulticastUtils.COMMAND_KILL_PROCESS);

    private final String command;

    CommandType(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
