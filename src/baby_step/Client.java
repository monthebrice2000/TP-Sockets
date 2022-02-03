package baby_step;

import java.io.*;
import java.net.Socket;

public class Client {

    String serverHost ;
    int serverPort;
    Socket serverSoc ;

    public Client( String serverHost, int serverPort ) {
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
        receiveReply();
    }

    public void receiveReply() throws IOException {
        InputStream is = serverSoc.getInputStream();
        DataInputStream dis = new DataInputStream( is );
        String replyServer = dis.readUTF();
        System.out.println( replyServer );
    }

    public static void main( String[] argv ) throws IOException {
        Client client = new Client("130.190.74.208", 4320);
        try {
            client.connectToServer();
            client.sendRequest( "MONTHE");
        } catch (IOException e) {
            throw new IOException( e.getMessage() );
        }
    }

}
