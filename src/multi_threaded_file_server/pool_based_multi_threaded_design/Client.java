package multi_threaded_file_server.pool_based_multi_threaded_design;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;


/**
 * Ici on effectue une connexion pour un ensemble de requete
 */
public class Client {

    String serverHost ;
    int serverPort;
    Socket serverSoc ;
    InputStream is;
    OutputStream os;
    DataOutputStream dos;
    DataInputStream dis;

    public Client(String serverHost, int serverPort ) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    public void connectToServer() throws IOException {
        try {
            this.serverSoc = new Socket( serverHost, serverPort );
            this.is = this.serverSoc.getInputStream();
            this.os = this.serverSoc.getOutputStream();
            this.dos = new DataOutputStream( this. os );
            this.dis = new DataInputStream( this.is );
        } catch (IOException e) {
            throw new IOException("Connection Failed");
        }
        System.out.println( "Connection Success");
    }

    public void sendRequest(String body) throws IOException {

        this.dos.writeUTF( body );
        //dos.close();
        receiveReply();
    }

    public void receiveReply() throws IOException {

        String replyServer = this.dis.readUTF();
        System.out.println( replyServer );
        //dis.close();
    }

    public void sendRequestFile(String body) throws IOException {

        this.dos.writeUTF( body );
        receiveReplyFile( body );
        //dos.close();
    }

    public void receiveReplyFile( String fileName ) throws IOException {
        String data = this.dis.readUTF();

        if( fileName.equals( "-1") ){
            System.out.println( data );
            return;
        }
        File file = new File( fileName );
        FileOutputStream fos = new FileOutputStream( file );
        fos.write( data.getBytes() );
        System.out.println( "Download File Success" );
        //fos.close();
        //dis.close();
    }

    public static void main( String[] argv ) throws IOException {
        Client client = new Client("localhost", 4320);
        try {
            client.connectToServer();
            String request = null;
            Scanner sc = new Scanner( System.in );
            do {
                System.out.println( "Press -1 to abandon or index.txt to download the file ");
                System.out.println( "Enter A Request : ");
                request = sc.nextLine();
                client.sendRequestFile( request.trim() );
            }while (!request.equals("-1"));
            client.serverSoc.close();
        } catch (IOException e) {
            throw new IOException( e.getMessage() );
        }
    }

}
