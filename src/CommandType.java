public enum CommandType {
    /* Bully Algorithm */
    ELECTION(MulticastUtils.COMMAND_ELECTION),
    ANSWER_ELECTION(MulticastUtils.ANSWER_ELECTION),
    COORDINATOR_VICTORY(MulticastUtils.COMMAND_COORDINATOR_VICTORY),
    UPDATE_CLOCK(MulticastUtils.COMMAND_UPDATE_CLOCK),
    KILL_PROCESS(MulticastUtils.COMMAND_KILL_PROCESS);

    private final String command;

    CommandType(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
