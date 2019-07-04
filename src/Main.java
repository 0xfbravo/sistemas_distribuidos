import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try {
            /* Creates process */
            SDProcess p0 = new SDProcess(MulticastUtils.nextAvailableProcessId(), "a");
            SDProcess p1 = new SDProcess(MulticastUtils.nextAvailableProcessId(), "b");
            SDProcess p2 = new SDProcess(MulticastUtils.nextAvailableProcessId(), "c");

            /* Reads user input */
            Scanner scanner = new Scanner(System.in);
            int option = scanner.nextInt();
            if (option == 0) {
                p0.publishStartElection();
            } else {
                System.out.println("Opção inválida.");
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
