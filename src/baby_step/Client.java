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
        receiveReply( body );
        dos.close();
    }

    public void receiveReply( String fileName ) throws IOException {
        File file = new File( fileName );
        FileOutputStream fos = new FileOutputStream( file );
        InputStream is = serverSoc.getInputStream();
        DataInputStream dis = new DataInputStream( is );
        String data = dis.readUTF();
        fos.write( data.getBytes() );
        System.out.println( "Download File Success" );
        fos.close();
        dis.close();
    }


    public static void main( String[] argv ) throws IOException {
        Client client = new Client("10.188.207.110", 4320);
        try {
            client.connectToServer();
            client.sendRequest( "index.txt");
        } catch (IOException e) {
            throw new IOException( e.getMessage() );
        }
    }

}
