package multi_threaded_file_server.pool_based_multi_threaded_design.prodcons.v2;


import multi_threaded_file_server.pool_based_multi_threaded_design.prodcons.Message;

import java.io.*;
import java.net.Socket;


/***
 * Ce worker meurt sur la demande de terminaison de connexion par le client
 */
public class Worker extends Thread{
    
    private ProdConsBuffer buffer;
    Message m;
    InputStream is;
    OutputStream os;
    DataInputStream dis;
    DataOutputStream dos;
    
    public Worker(ProdConsBuffer buffer){
        this.buffer = buffer;
    }
    
    public void run(){
        try {
            while( true ){
                Socket socClient = buffer.get();
                initStreams( socClient );
                boolean client_go ;
                while( true ){
                    client_go = receiveRequest();
                    if( client_go )
                        break;
                }
                System.out.println( "One Client is going ");
                socClient.close();
            }
        } catch (InterruptedException | IOException ex) {
            System.out.println( ex.getMessage() );
        } 
    }

    public void initStreams(Socket socClient) throws IOException {
        this.is = socClient.getInputStream();
        this.os = socClient.getOutputStream();
        this.dis = new DataInputStream( this.is );
        this.dos = new DataOutputStream( this.os );
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
