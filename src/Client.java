import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Client {
    public static void main(String[] args) {
        Client c = new Client();
    }

    Client() {
        Scanner scan = new Scanner(System.in);
        try {
            InetAddress ip = InetAddress.getLocalHost();

            // connect with server
            Socket socket = new Socket(ip, 5555);
            /// request
            DataInputStream response = new DataInputStream(socket.getInputStream());
            /// response
            DataOutputStream request = new DataOutputStream(socket.getOutputStream());
            String str = " ";

            ///// get Hello message from server
            str = response.readUTF();
            System.out.println(str);

            /// get server question
            str = response.readUTF();

            //// Enter client request as string
            System.out.print(str);
            str = scan.nextLine();
            //// send request to server
            request.writeUTF(str);
            //// recive response from server
            str = response.readUTF();
            System.out.println(str);

            ///// close all connections
            response.close();
            request.close();
            socket.close();

        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
}
