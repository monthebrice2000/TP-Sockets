package multi_threaded_file_server.basic_multi_threaded_design;

import java.io.*;
import java.net.Socket;


/***
 * Ici je fais un thread par requete  et
 * le thread meurt lorsqu'il a finit de traiter la requête'
 */
public class Worker_2 extends Thread {

    Socket socClient;
    InputStream is;
    OutputStream os;
    DataInputStream dis;
    DataOutputStream dos;

    public Worker_2(Socket socClient) throws IOException {
        this.socClient = socClient;
        this.is = socClient.getInputStream();
        this.os = socClient.getOutputStream();
        this.dis = new DataInputStream( this.is );
        this.dos = new DataOutputStream( this.os );
    }

    @Override
    public void run() {
        try {
            receiveRequest();
            System.out.println( "The request was terminated by Worker "+ this.socClient.getPort() );
            socClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveRequest( ) throws IOException {

        String request = this.dis.readUTF();
        //dis.close();
        System.out.println( "Hello " + request );
        sendReply(  "Hello "+ request );
    }

    public void sendReply(  String body ) throws IOException {
        this.dos.writeUTF( body );
        //dos.close();
    }
}
