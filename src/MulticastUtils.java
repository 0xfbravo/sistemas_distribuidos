class MulticastUtils {

    /* Multicast Config */
    static final Integer socketPort = 4446;
    static final String groupAddress = "230.0.0.0";

    /* Available Commands */
    static final String COMMAND_ELECTION = "ELECTION";
    static final String ANSWER_ELECTION = "ANSWER_ELECTION";
    static final String COMMAND_COORDINATOR_VICTORY = "COORDINATOR_VICTORY";
    static final String COMMAND_GET_HIGHER_PROCESS = "GET_HIGHER_PROCESS";
    static final String COMMAND_UPDATE_CLOCK = "UPDATE_CLOCK";
    static final String COMMAND_KILL_PROCESS = "KILL_PROCESS";

    /**
     * Gets next available processId
     */
    private static Integer currentProcessId = 0;
    static Integer nextAvailableProcessId() {
        return currentProcessId++;
    }

    /**
     * Generates a valid multicast message separating command from each attribute by "::"
     * @param commandType command to be executed on process
     * @param messageAttrs an array with all attributes to be sent with command
     * @return a valid multicast message like "select_leader::0"
     */
    static String generateCommandMsg(CommandType commandType, String... messageAttrs) {
        StringBuilder stringBuilder = new StringBuilder(commandType.getCommand());

        for (String attr : messageAttrs) {
            stringBuilder.append("::").append(attr);
        }

        return stringBuilder.toString();
    }

}
