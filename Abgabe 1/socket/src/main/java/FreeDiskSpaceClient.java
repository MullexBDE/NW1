import java.net.*;
import java.io.*;
import java.util.Scanner;

public class FreeDiskSpaceClient {
    private static Socket clientSocket;
    private static PrintWriter outputWriter;
    private static BufferedReader inputReader;
    private static String hostIP;
    private static int hostPort;
    private static final Scanner consoleScanner = new Scanner(System.in);

    static void main() {
        getConnectionDetails();
        start(hostIP, hostPort);
        sendRequest();
        stop();
    }

    public static void start(String host, int port) {
        try {
            clientSocket = new Socket(host, port);
            outputWriter = new PrintWriter(clientSocket.getOutputStream(), true);
            inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (Exception _) {/* Exception handeling is not needed right now */}
    }

    public static void getConnectionDetails() {
        System.out.print("Bitte geben Sie die Zeil IP an [127.0.0.1]: ");
        String enteredHost = consoleScanner.nextLine();
        hostIP = enteredHost.isEmpty() || enteredHost.equals("localhost") ? "127.0.0.1" : enteredHost;

        System.out.print("Bitte geben Sie den Ziel Port ein [4711]: ");
        String enteredPort = consoleScanner.nextLine();
        hostPort = enteredPort.isEmpty() ? 4711 : Integer.parseInt(enteredPort);
    }

    public static void sendRequest() {
        try {
            System.out.print("Bitte geben Sie den Pfad an: ");
            String path = consoleScanner.nextLine();

            outputWriter.println(path);
            System.out.println(inputReader.readLine());
        } catch (Exception _) {/* Exception handeling is not needed right now */}
    }

    public static void stop() {
        try {
            outputWriter.close();
            inputReader.close();
            clientSocket.close();
        } catch (Exception _) {/* Exception handeling is not needed right now */}
    }

}
