import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;




public class ThreadHanler implements Runnable {
    protected Socket clientSocket = null;
    protected int clientIndex = 0;

    public ThreadHanler(Socket clientSocket, int clientIndex) {
        this.clientSocket = clientSocket;
        this.clientIndex = clientIndex;
    }

    //// The search function for a file in subfolders
    public static File findFile(String dir, String fileName, String host) {
        File file = new File(dir);
        if (fileName.equalsIgnoreCase(file.getName())) return file;
        if (file.isDirectory()) {
            for (String aChild : file.list()) {
                File targetFile = findFile(dir + File.separator + aChild, fileName, host);
                if (targetFile != null){
                 System.out.println("\nRequest received\nGET/"+targetFile.getParent()+"/"+targetFile.getName()+" HTTP/1.1\nHost: " + host);    
                    return targetFile;
                }
            }
        }
        return null;
    }

    //// GET request Method
    static String getRequest(String host, String fileName) {
        
        String dir = "static/"+host+"/";
        
        try {
            File file = findFile(dir, fileName, host);
            
            if (file != null) {
        
                String res = "Request Accepted\nHTTP/1.1 200 OK\n";
                res += "Date: "+ new Date().toString() + "\n";
                res += "Content-type: text/html; charset=UTF-8\n";
                res += "Content-length: " + file.length() +"\n";
                res += "Content-file:\n";
            
                Scanner content = new Scanner(file);
                String str ;
                //// Read File Content 
                while(content.hasNextLine()){
                    str = content.nextLine();
                    res += str + "\n";
                }
        
                content.close();
            
                return res;
              
            } else {
                return "Request refuesd\nHTTP/1.1 404 NOt Found";
            }

        } catch (IOException e) {
            System.out.println("GET request Error");
            e.printStackTrace();
            return "Request refuesd\nHTTP/1.1 404 NOt Found";
        }
      
    }    

    /// POST request Method
    static Boolean postRequest(String host, String content) {

        /// GET File Name
        int newLineIndex = content.indexOf ("\n");
        String fileName = content.substring (0, newLineIndex);

        System.out.println("Request received\nPOST/"+fileName+" HTTP/1.1\nHost: " + host);
        
        try {
        
            File postedFile = new File("static/"+host+"/"+fileName);
            String fileContent = content.substring (newLineIndex + 1);
            if (!postedFile.exists()) {
                postedFile.getParentFile().mkdirs();
                if(postedFile.createNewFile()){
                    ///// write contetn file into file that is on server
                    FileWriter writer = new FileWriter(postedFile.getAbsoluteFile());
             
                    /// Write File content 
                    writer.write(fileContent);
            
                    writer.close();
                }

                System.out.println("File created Successfully");
                return true;
            } else {

                System.out.println("File already exists.");
                return false;
            }
        } catch (IOException e) {
            System.out.println("POST request Error");
            e.printStackTrace();
            return false;
        }
      
    }    

    @Override
    public void run() {
        try {

            DataInputStream request = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream response = new DataOutputStream(clientSocket.getOutputStream());

            ///// 
            String host;
            String dir;
            String fileContent = "";
            String str;

            ////// Hello message
            response.writeUTF("Hello Client you are connected now :)");
            ////// Login
            response.writeUTF("Enter HOST Name: ");
            host = request.readUTF();

            //// First Question to CLient
            response.writeUTF("Do you Want Do request!?\n Yes ==>> Enter Any value \n No ==> disconnect");
            str = request.readUTF();

            //// Create Request loop
            while(!str.equals("disconnect")){
                

                //// Write Request or return to Main Mnenu loop
                while (!str.equals("exit")) {

                    //// recive Client input
                    response.writeUTF("Chose Request Type:\n 1 - GET\n 2 - POST \n 3 - exit");
                    str = request.readUTF();
                    
                    if(str.equals("GET") || str.equals("POST")) {
                        switch (str) {
                            case "GET":
                                response.writeUTF("Please Enter the File Name: ");
                                dir = request.readUTF();
                                response.writeUTF(getRequest(host, dir) + "\n\nPress Enter to continue");
                                request.readUTF();
                                break;

                            case "POST":
                                response.writeUTF("Please Enter the Diraction of the required file: ");
                                fileContent = request.readUTF();
                                if(postRequest(host,fileContent)){
                                    response.writeUTF("Request Accepted\nHTTP/1.1 200 OK" + "\n\nPress Enter to continue");
                                    request.readUTF();
                                }else{
                                    response.writeUTF("Request refuesd\nHTTP/1.1 400 Bad Request" + "\n\nPress Enter to continue");
                                    request.readUTF();
                                }
                                break;
                           
                            default:
                                break;
                        }
                        str = ""; 
                    }else if(str.equals("exit")){
                        break;
                    }
                }
                response.writeUTF("Do you Want Do request \n Yes ==>> Enter Any value \n No ==> disconnect");
                str = request.readUTF();
            }
            response.writeUTF("You are disconnected");
            response.close();
            request.close();
            System.out.println("Cleint [" + this.clientIndex + "] disconnected");
            clientSocket.close();
        } catch (IOException e) {
            // report exception somewhere.
            System.out.println("run function Error");
            e.printStackTrace();
        }

    }
}
