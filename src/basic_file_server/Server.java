package basic_file_server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    int port;
    int backlog;
    ServerSocket listenSoc ;

    public Server( int port, int backlog) throws IOException {
        this.port = port;
        this.backlog = backlog;
        this.listenSoc = new ServerSocket( port , backlog );
    }

    public void listenToClients() throws IOException {

        while( true ){
            System.out.println( "Listening ....");
            Socket clientSoc = listenSoc.accept();
            System.out.println("One baby_step.Client was connected!!!");
            if( clientSoc != null ){
                receiveRequest( clientSoc );
            }

        }
    }

    public void receiveRequest( Socket clientSoc ) throws IOException {
        InputStream is = clientSoc.getInputStream();
        DataInputStream dis = new DataInputStream( is );
        String request = dis.readUTF();
        System.out.println( "Hello "+ request );
        sendReply( clientSoc, request );
    }

    public void sendReply( Socket clientSoc, String body ) throws IOException {
        OutputStream os = clientSoc.getOutputStream();
        DataOutputStream dos = new DataOutputStream( os );
        dos.writeUTF( "Hello "+ body);
    }




    public static void main( String[] argv){
        try {
            Server server = new Server( 4320, 3 );
            server.listenToClients();
        } catch (IOException e) {
            System.out.println( "Error Found : "+ e.getMessage() );
        }
    }
}
