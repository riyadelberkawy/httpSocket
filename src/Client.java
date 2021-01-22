import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Client {
    public static void main(String[] args) {
        new Client();
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

            str = response.readUTF();
            System.out.println(str);
            
            str = scan.nextLine();
            request.writeUTF(str);

            str = response.readUTF();
            System.out.println(str);
            str = scan.nextLine();
            request.writeUTF(str);

            //// Create Request or disconnect Client loop
            while (!str.equals("disconnect")) {
                //// send input
                str = response.readUTF();
                System.out.println(str);
                str = scan.nextLine();
                request.writeUTF(str);

         
            }

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
