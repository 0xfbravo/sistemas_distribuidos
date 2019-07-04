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
            System.out.println("\t== Sistemas Distribuídos ==");
            System.out.println("----");
            System.out.println(":: 0 -> Executar algoritmos");
            System.out.println("----");
            System.out.print("Digite a sua opção: ");

            int option = scanner.nextInt();
            switch (option) {
                case 0:
                    p0.publishStartElection();
                    break;

                default:
                    System.out.println("Opção inválida.");
                    break;
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
