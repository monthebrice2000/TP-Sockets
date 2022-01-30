package multi_threaded_file_server.pool_based_multi_threaded_design;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    String serverHost ;
    int serverPort;
    Socket serverSoc ;

    public Client(String serverHost, int serverPort ) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    public void connectToServer() throws IOException {
        try {
            this.serverSoc = new Socket( serverHost, serverPort );
        } catch (IOException e) {
            throw new IOException("Connection Failed");
        }
        System.out.println( "Connection Success");
    }

    public void sendRequest(String body) throws IOException {
        OutputStream os = serverSoc.getOutputStream();
        DataOutputStream dos = new DataOutputStream( os );
        dos.writeUTF( body );
        //dos.close();
        receiveReply();
    }

    public void receiveReply() throws IOException {
        InputStream is = serverSoc.getInputStream();
        DataInputStream dis = new DataInputStream( is );
        String replyServer = dis.readUTF();
        System.out.println( replyServer );
        //dis.close();
    }

    public static void main( String[] argv ) throws IOException {
        Client client = new Client("10.188.207.110", 4320);
        try {
            client.connectToServer();
            String want_to_send_request = "0";
            String request = null;
            Scanner sc = new Scanner( System.in );
            do {
                System.out.println( "Press -1 to abandon or 0 to continue");
                System.out.println( "Enter A Request : ");
                request = sc.nextLine();
                client.sendRequest( request );
            }while (!request.equals("-1"));
            client.serverSoc.close();
        } catch (IOException e) {
            throw new IOException( e.getMessage() );
        }
    }

}
