import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FreeDiskSpaceServer {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter outputWriter;
    private BufferedReader inputReader;

    static void main() {
        FreeDiskSpaceServer server = new FreeDiskSpaceServer();
        server.start(4711);
    }

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            clientSocket = serverSocket.accept();
            outputWriter = new PrintWriter(clientSocket.getOutputStream(), true);
            inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println("received connection");

            String path = inputReader.readLine();
            System.out.println(path);
            System.out.println(Files.getFileStore(Paths.get(path)));

            outputWriter.printf("Info for path \"%s\": %s of %s bytes free", path, Files.getFileStore(Paths.get(path)).getUnallocatedSpace(), Files.getFileStore(Paths.get(path)).getTotalSpace());
        } catch (Exception _) {/* Exception handeling is not needed right now */}
    }
}
