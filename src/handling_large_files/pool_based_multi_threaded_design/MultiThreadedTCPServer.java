package handling_large_files.pool_based_multi_threaded_design;

import handling_large_files.pool_based_multi_threaded_design.prodcons.XMLParameters;
import handling_large_files.pool_based_multi_threaded_design.prodcons.v2.ProdConsBuffer;
import handling_large_files.pool_based_multi_threaded_design.prodcons.v2.Worker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

public class MultiThreadedTCPServer {

    int port;
    int backlog;
    ServerSocket listenSoc ;
    ProdConsBuffer clientsBuffer ;

    public MultiThreadedTCPServer(int port, int backlog) throws IOException {
        this.port = port;
        this.backlog = backlog;
        this.listenSoc = new ServerSocket( port , backlog );
    }

    public void listenToClients() throws IOException, InterruptedException {

        while( true ){
            System.out.println( "Listening For Another Connection ....");
            Socket clientSoc = listenSoc.accept();
            System.out.println(" Client was connected!!!");
            if( clientSoc != null ){
                this.clientsBuffer.put( clientSoc );
            }

        }
    }

    public void init_pool_workers() {

        //Loading paramters from XML file
        XMLParameters.getParameters();
        int nCons = XMLParameters.nCons;

        this.clientsBuffer = new ProdConsBuffer();

        ArrayList<Thread> threads = new ArrayList <Thread>();


        for(int i=1; i<= nCons; i++){
            Worker consThread = new Worker(this.clientsBuffer);
            threads.add(consThread);
            //consThread.start();
        }

        Collections.shuffle(threads);

        //long start = System.currentTimeMillis();

        for( Thread t : threads ){
            t.start();
        }

        /*for(Thread t : threads ) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long end = System.currentTimeMillis();

        System.out.println("\n**************************************************************");
        System.out.println("Programme terminé");
        System.out.println("Temps d'éxecution : " + (end-start) + " ms");*/

    }


    public static void main( String[] argv) throws InterruptedException {
        try {
            MultiThreadedTCPServer server = new MultiThreadedTCPServer( 4320, 3 );
            server.init_pool_workers();
            server.listenToClients();

        } catch (IOException e) {
            System.out.println( "Error Found : "+ e.getMessage() );
        }
    }
}
