import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

public class OwnClient_v1_1 {
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
        System.out.print("Bitte geben Sie die Ziel IP an [127.0.0.1]: ");
        String enteredHost = consoleScanner.nextLine();
        hostIP = enteredHost.isEmpty() || enteredHost.equals("localhost") ? "127.0.0.1" : enteredHost;

        System.out.print("Bitte geben Sie den Ziel Port ein [8000]: ");
        String enteredPort = consoleScanner.nextLine();
        hostPort = enteredPort.isEmpty() ? 8000 : Integer.parseInt(enteredPort);
    }

    public static void sendRequest() {
        try {
            System.out.print("Bitte geben Sie den Pfad an: ");
            String path = consoleScanner.nextLine();
            URL url = new URI(String.format("http://%s:%s/%s", hostIP, hostPort, path)).toURL();
            String requestTarget = url.getFile().isEmpty() ? "/" : url.getFile();

            outputWriter.printf("GET %s HTTP/1.0\r\n", requestTarget);
            outputWriter.printf("Host: %s:%d\r\n", url.getHost(), url.getPort());
            outputWriter.print("\r\n");
            outputWriter.flush();
            String line;
            while ((line = inputReader.readLine()) != null) {
                System.out.println(line);
            }
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
