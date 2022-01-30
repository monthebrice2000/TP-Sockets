package multi_threaded_file_server.basic_multi_threaded_design;

import java.io.*;
import java.net.Socket;


/***
 * Ici je fais un thread par client et
 * le thread meurt lorsque le client seul coupe la connexion
 */
public class Worker extends Thread {

    Socket socClient;
    InputStream is;
    OutputStream os;
    DataInputStream dis;
    DataOutputStream dos;

    public Worker(Socket socClient) throws IOException {
        this.socClient = socClient;
        this.is = socClient.getInputStream();
        this.os = socClient.getOutputStream();
        this.dis = new DataInputStream( this.is );
        this.dos = new DataOutputStream( this.os );
    }

    @Override
    public void run() {
        try {
            boolean client_go ;
            while( true ){
                client_go = receiveRequest();
                if( client_go )
                    break;
            }
            System.out.println( "One Client is going ");
            socClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public boolean receiveRequest( ) throws IOException {

        String request = this.dis.readUTF();
        if( request.equals("-1") ){
            sendReply(  "Good Bye" );
            return true;
        }
        //dis.close();
        System.out.println( "Hello " + request );
        sendReply(  "Hello "+ request );
        return false;
    }

    public void sendReply(  String body ) throws IOException {
        this.dos.writeUTF( body );
        //dos.close();
    }
}
