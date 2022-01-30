package multi_threaded_file_server.pool_based_multi_threaded_design.prodcons.v2;


import multi_threaded_file_server.pool_based_multi_threaded_design.prodcons.IProdConsBuffer;
import multi_threaded_file_server.pool_based_multi_threaded_design.prodcons.Message;
import multi_threaded_file_server.pool_based_multi_threaded_design.prodcons.XMLParameters;

import java.net.Socket;

public class ProdConsBuffer {
    
    //Load the parameters from the XML file; 
    int bufSz = XMLParameters.bufSz;
    int prodTime = XMLParameters.prodTime; 
    int consTime = XMLParameters.consTime;
    int nbProd = XMLParameters.nProd;
    int nbCons = XMLParameters.nCons;
    
    private Socket[] buffer = new Socket[bufSz];
    private int notEmpty = 0;
    private int notFull = bufSz;
    private int in = 0;
    private int out = 0;
    

    //Total messages produits
    private int totmsg = 0;
    //Nombre de messages consommés
    private int msgcons = 0;
    //Nombre de producteurs ayant terminés
    private int nbProdDone = 0;
    //Nombre de threads ayant consommé un message
    private int nbConsDone = 0;
    

    public synchronized void put(Socket socket) throws InterruptedException {
    	
        while ( nbConsDone<nbCons && notFull == 0 ){
            try{
                wait();
            }
            catch(InterruptedException e){
                e.getMessage();
            }
        }

        if(nbConsDone == nbCons) {
            notifyAll();
            Thread.currentThread().interrupt();
            throw new InterruptedException( "Un message produit par le Thread : " + socket.getPort() + " est interrompu par inssufisance de consommateur");
        }

        
        //Production du message 
        buffer[in] = socket;
        Thread.sleep(prodTime);
        in = (in + 1) % bufSz;
        totmsg++;
        notFull--;
        notEmpty++;
        System.out.println("Un message produit par le Thread : " + socket.getPort() );
         
        notifyAll();
    }

    public synchronized Socket get() throws InterruptedException {
        
        while(notEmpty == 0){
            try{
                if(nbProdDone == nbProd){
                	notifyAll();
                    Thread.currentThread().interrupt();
                    throw new InterruptedException("Le consommateur ne peut pas consommer par inssufisance de message");
                }
                wait();
            }
            catch(InterruptedException e){
                e.getMessage();
            }
        }
        //Consommation du message 
        Socket socket = buffer[out];
        Thread.sleep(consTime);
        out = (out+1) % bufSz;
        msgcons++;
        notEmpty--;
        nbConsDone++;
        notFull++;
        
        System.out.println("Un message consommé, messages disponibles : " + nmsg() + ", total produit : "+totmsg);
        
        notifyAll();
       
        return socket;
    }


    public int nmsg() {
        return (totmsg - msgcons);
    }


    public int totmsg() {
        return totmsg;
    } 
    
    public synchronized void producerDone(){
        nbProdDone++;
        notifyAll();
    }
}
