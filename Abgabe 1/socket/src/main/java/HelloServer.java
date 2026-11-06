import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpCookie;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;

//mit http://localhost:8080/?name=Angi

public class HelloServer {
    private ServerSocket serverSocket;
    private static Socket clientSocket;
    private static PrintWriter outputWriter;
    private static BufferedReader inputReader;
    private static String name;

    public static void main(String[] args) {
        HelloServer server = new HelloServer();
        server.start(8080);
    }

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            while (true){
                clientSocket = serverSocket.accept();
                outputWriter = new PrintWriter(clientSocket.getOutputStream(), true);
                inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                System.out.println("received connection");

                String path = inputReader.readLine();
                if(path.contains("name")){
                    String[] requestTeile = path.split(" ");

                    if (requestTeile.length > 1) {
                        String url = requestTeile[1];
                        int startPos = url.indexOf("?name=") + 6;
                        name = url.substring(startPos);
                    }
                }

                String line;
                while ((line = inputReader.readLine()) != null && !line.isEmpty()) {
                    System.out.println(line);
                    if (line.startsWith("Cookie: ") && line.contains("nutzername=") && name == null) {

                        int startPos = line.indexOf("nutzername=") + 11;
                        String nameCookie = line.substring(startPos);

                        if (nameCookie.contains(";")) {
                            nameCookie = nameCookie.substring(0, nameCookie.indexOf(";"));
                        }

                        name = nameCookie;
                    }
                }

                outputWriter.print("HTTP/1.1 200 OK\r\n");
                outputWriter.print("Content-Type: text/html\r\n");
                if(name != null){
                    outputWriter.print("Set-Cookie: nutzername=" + name + "\r\n");
                }
                outputWriter.print("\r\n");

                outputWriter.println(Calendar.getInstance().getTime());
                if(name != null){
                    outputWriter.println("Hallo " + name);
                }

                outputWriter.flush();
                clientSocket.close();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
