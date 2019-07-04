import java.io.IOException;
import java.net.*;
import java.util.Random;

public class SDProcess implements Runnable, SDPublisher {

    /* Multicast Configurations */
    private MulticastSocket socket;
    private InetAddress group;
    private byte[] multicastMsgBuffer = new byte[1000];

    /* Process Attributes */
    private Integer id;
    private String name;
    private Thread clockThread;
    private Thread multicastThread;

    /* Clock Attributes */
    private Double clockIncrement = new Random().nextDouble();
    private Double localClock = 0.0;

    /* Bully Algorithm */
    private Integer coordinatorId = -1;

    SDProcess(Integer id, String name) throws IOException {
        this.id = id;
        this.name = name;

        this.socket = new MulticastSocket(MulticastUtils.socketPort);
        this.group = InetAddress.getByName(MulticastUtils.groupAddress);
        this.socket.joinGroup(group);

        this.clockThread = new Thread(this, id.toString() + name);
        this.clockThread.start();

        this.multicastThread = new Thread() {
            @Override
            public void run() {
                super.run();
                do {
                    try {
                        DatagramPacket packet = new DatagramPacket(multicastMsgBuffer, multicastMsgBuffer.length);
                        socket.receive(packet);
                        String receivedCommand = new String(packet.getData(), 0 , packet.getLength());
                        String[] commandSplitted = receivedCommand.split("::");

                        if (commandSplitted.length <= 0) {
                            System.err.println("No command received via multicast.");
                            return;
                        }

                        /* Debug msg */
                        String readString = "SDProcess [" + SDProcess.this.toString() + "] received command=" + receivedCommand;
                        System.out.println(readString);

                        /* Calls the correct function for each command */
                        String command = commandSplitted[0];
                        switch (command) {

                            case MulticastUtils.COMMAND_ELECTION:
                                publishElectionAnswer();
                                break;

                            case MulticastUtils.ANSWER_ELECTION:
                                Integer candidateId = Integer.valueOf(commandSplitted[1]);
                                if (candidateId > SDProcess.this.id) { setCoordinatorId(candidateId); }
                                else { publishCoordinatorVictory(); }
                                break;

                            case MulticastUtils.COMMAND_UPDATE_CLOCK:
                                Double newClock = Double.valueOf(commandSplitted[1]);
                                setLocalClock(newClock);
                                break;

                            case MulticastUtils.COMMAND_COORDINATOR_VICTORY:
                                Integer coordinatorId = Integer.valueOf(commandSplitted[1]);
                                setCoordinatorId(coordinatorId);
                                break;

                            case MulticastUtils.COMMAND_KILL_PROCESS:
                                break;

                            default:
                                System.err.println("Command [" + command + "] received via multicast but not mapped yet.");
                                break;

                        }
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                while (!this.isInterrupted());
            }
        };
        this.multicastThread.start();
    }

    @Override
    public void run() {
        String readString = "SDProcess [" + this.toString() + "] initialized with clock="+ this.localClock.toString() +" clockIncrement=" + this.clockIncrement.toString();
        System.out.println(readString);
        doClockIncrement();
    }

    /**
     * Sends to every SDProcess on multicast that a new election has begun.
     */
    @Override
    public void publishStartElection() throws IOException {
        String readString = "SDProcess [" + this.toString() + "] ￿started election!";
        System.out.println(readString);

        /* Mounts msg to send */
        DatagramSocket datagramSocket = new DatagramSocket();
        String msg = MulticastUtils.generateCommandMsg(CommandType.ELECTION);

        /* Mounts datagram packet */
        DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.getBytes().length, group, MulticastUtils.socketPort);
        datagramSocket.send(packet);
        datagramSocket.close();
    }

    /**
     * Sends to every SDProcess on multicast my id.
     */
    @Override
    public void publishElectionAnswer() throws IOException {
        /* Mounts msg to send */
        DatagramSocket datagramSocket = new DatagramSocket();
        String msg = MulticastUtils.generateCommandMsg(CommandType.ANSWER_ELECTION, this.id.toString());

        /* Mounts datagram packet */
        DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.getBytes().length, group, MulticastUtils.socketPort);
        datagramSocket.send(packet);
        datagramSocket.close();
    }

    /**
     * Sends to every SDProcess on multicast that I'm the new coordinator.
     */
    @Override
    public void publishCoordinatorVictory() throws IOException {
        /* Mounts msg to send */
        DatagramSocket datagramSocket = new DatagramSocket();
        String msg = MulticastUtils.generateCommandMsg(CommandType.COORDINATOR_VICTORY, this.id.toString());

        /* Mounts datagram packet */
        DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.getBytes().length, group, MulticastUtils.socketPort);
        datagramSocket.send(packet);
        datagramSocket.close();
    }

    /**
     * Updates new clock value for every SDProcess on Multicast
     * @param newClockValue the new clock value
     */
    @Override
    public void publishUpdatedClock(Double newClockValue) throws IOException {
        /* Mounts msg to send */
        DatagramSocket datagramSocket = new DatagramSocket();
        String msg = MulticastUtils.generateCommandMsg(CommandType.UPDATE_CLOCK, newClockValue.toString());

        /* Mounts datagram packet */
        DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.getBytes().length, group, MulticastUtils.socketPort);
        datagramSocket.send(packet);
        datagramSocket.close();
    }

    /**
     * Updates local clock with current increment each 1 second.
     */
    private void doClockIncrement() {
        do {
            try {
                localClock += clockIncrement;
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while (!clockThread.isInterrupted());
    }

    /* Getters & Setters */
    @Override
    public String toString() {
        return this.name + " - ID: " + this.id;
    }

    public MulticastSocket getSocket() {
        return socket;
    }

    public InetAddress getGroup() {
        return group;
    }

    public byte[] getMulticastMsgBuffer() {
        return multicastMsgBuffer;
    }

    public String getName() {
        return name;
    }

    public Thread getClockThread() {
        return clockThread;
    }

    public Thread getMulticastThread() {
        return multicastThread;
    }

    private void setLocalClock(Double localClock) {
        this.localClock = localClock;

        String readString = "SDProcess [" + this.toString() + "] local clock updated to=" + this.localClock.toString();
        System.out.println(readString);
    }

    public Double getLocalClock() {
        return localClock;
    }

    public void setCoordinatorId(Integer coordinatorId) {
        this.coordinatorId = coordinatorId;

        String readString = "SDProcess [" + this.toString() + "] coordinator updated to=" + this.coordinatorId.toString();
        System.out.println(readString);
    }
}
