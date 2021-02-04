import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        new Client();
    }



    static String postRequest(String dir) {

        
        try {
            File file = new File(dir);
    
            Scanner content = new Scanner(file);
            String str;
            String req = "";

            req += file.getName() + "\n";
            //// Read File Content 
            
            while(content.hasNextLine()){
                str = content.nextLine();
                req += str + "\n";
            }
            content.close();
            return req;
            
        } catch (IOException e) {
            System.out.println("POST request Error");
            e.printStackTrace();
            return "error";
        }
      
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

            //// Login
            str = response.readUTF();
            System.out.println(str);

            str = scan.nextLine();
            request.writeUTF(str);

            //// First Question From Server
            str = response.readUTF();
            System.out.println(str);

            str = scan.nextLine();
            request.writeUTF(str);

            //// Create Request Client loop
            while (!str.equals("disconnect")) {

                if(str.equals("POST")){
                    /// recive Response
                    str = response.readUTF();
                    System.out.println(str);
                    
                    /// dirction of file
                    str = scan.nextLine();
                    /// Send file content to server
                    request.writeUTF(postRequest(str));

                    
                }else{
                /// recive Response
                str = response.readUTF();
                System.out.println(str);
                
                /// Send Request
                str = scan.nextLine();
                request.writeUTF(str);

                }
               
            }

            //// recive response from server
            str = response.readUTF();
            System.out.println(str);

            scan.close();
            ///// close all connections
            response.close();
            request.close();
            socket.close();

        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
}