
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ThreadHanler implements Runnable {
    protected Socket clientSocket = null;
    protected int clientIndex = 0;

    public ThreadHanler(Socket clientSocket, int clientIndex) {
        this.clientSocket = clientSocket;
        this.clientIndex = clientIndex;
    }

    static int num = 0;

    @Override
    public void run() {
        try {
            DataInputStream request = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream response = new DataOutputStream(clientSocket.getOutputStream());

            response.writeUTF("Hello Client your connected now :)");
            response.writeUTF("Enter your request: ");

            String str = request.readUTF();
            System.out.println("Cleint [" + this.clientIndex + "] Request ===>> " + str);

            response.writeUTF("Pleas wait until file got prepared");
            response.close();
            request.close();
            System.out.println("Cleint [" + this.clientIndex + "] disconnected");
            clientSocket.close();
        } catch (IOException e) {
            // report exception somewhere.
            e.printStackTrace();
        }

    }
}
