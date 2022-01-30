package multi_threaded_file_server.basic_multi_threaded_design;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadedTCPServer_2 {

    int port;
    int backlog;
    ServerSocket listenSoc ;

    public MultiThreadedTCPServer_2(int port, int backlog) throws IOException {
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
                Worker_2 worker = new Worker_2( clientSoc );
                worker.start();
            }

        }
    }


    public static void main( String[] argv){
        try {
            MultiThreadedTCPServer_2 server = new MultiThreadedTCPServer_2( 4320, 3 );
            server.listenToClients();
        } catch (IOException e) {
            System.out.println( "Error Found : "+ e.getMessage() );
        }
    }
}
