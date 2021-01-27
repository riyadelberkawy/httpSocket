import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    static int num = 0;
    public static void main(String[] args) {
        
        try {

            new Server();

        } catch (Exception ex) {

            System.out.println("[Server] Starting Error ===>>> \n" + ex);

        }
    }

    Server() throws IOException {
        ServerSocket server = new ServerSocket(5555);
        System.out.println("Server is Online...........");
        while (true) {
            try {

                Socket client = server.accept(); /// Server is waiting for Cliet Connection

                System.out.println("Cleint [" + (++num) + "] is connected");
                
                Thread thread = new Thread(new ThreadHanler(client, num));
                thread.start();

            } catch (IOException ex) {
                System.out.println("Error ==> " + ex);
            }
        }

    }
}
