package multi_threaded_file_server.basic_multi_threaded_design;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Ici on on effectue une requete par connexion
 */
public class Client_2 {

    String serverHost ;
    int serverPort;
    Socket serverSoc ;
    InputStream is;
    OutputStream os;
    DataOutputStream dos;
    DataInputStream dis;

    public Client_2(String serverHost, int serverPort ) {
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
        Client_2 client = new Client_2("10.188.207.110", 4320);
        try {
            String request = null;
            Scanner sc = new Scanner( System.in );
            do {
                client.connectToServer();
                System.out.println( "Press -1 to abandon or another to continue");
                System.out.println( "Enter A Request : ");
                request = sc.nextLine();
                client.sendRequestFile( request );
                client.serverSoc.close();
            }while (!request.equals("-1"));
        } catch (IOException e) {
            throw new IOException( e.getMessage() );
        }
    }

}
