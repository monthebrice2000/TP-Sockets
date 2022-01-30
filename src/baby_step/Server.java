package baby_step;

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
            System.out.println(" Client was connected!!!");
            if( clientSoc != null ){
                receiveRequest( clientSoc );
            }

        }
    }

    public void receiveRequest( Socket clientSoc ) throws IOException {
        InputStream is = clientSoc.getInputStream();
        DataInputStream dis = new DataInputStream( is );
        String file_name = dis.readUTF();

        System.out.println( "Downloading file "+ file_name + " ...");

        File file = new File("D:\\Projects\\JavaProjects\\TP-Sockets\\src\\basic_file_server\\"+ file_name);
        FileInputStream fis = new FileInputStream( file );
        byte[] data = new byte[ fis.available() ];
        fis.read( data );
        fis.close();
        sendReply( clientSoc, new String( data ) );
    }

    public void sendReply( Socket clientSoc, String body ) throws IOException {
        OutputStream os = clientSoc.getOutputStream();
        DataOutputStream dos = new DataOutputStream( os );
        dos.writeUTF( body );
        System.out.println( "Download Finished");
        System.out.println( "File was send to client");
        dos.close();
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
