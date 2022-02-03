package multi_threaded_file_server.basic_multi_threaded_design.thread_per_request;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadedTCPServer {

    int port;
    int backlog;
    ServerSocket listenSoc ;

    public MultiThreadedTCPServer(int port, int backlog) throws IOException {
        this.port = port;
        this.backlog = backlog;
        this.listenSoc = new ServerSocket( port , backlog );
    }

    public void listenToClients() throws IOException {

        while( true ){
            System.out.println( "Listening For Another Connection ....");
            Socket clientSoc = listenSoc.accept();
            System.out.println(" Client was connected!!!");
            if( clientSoc != null ){
                Worker worker = new Worker( clientSoc );
                worker.start();
            }

        }
    }


    public static void main( String[] argv){
        try {
            MultiThreadedTCPServer server = new MultiThreadedTCPServer( 4320, 3 );
            server.listenToClients();
        } catch (IOException e) {
            System.out.println( "Error Found : "+ e.getMessage() );
        }
    }
}
