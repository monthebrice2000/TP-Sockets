package multi_threaded_file_server.pool_based_multi_threaded_design.prodcons;

import multi_threaded_file_server.pool_based_multi_threaded_design.prodcons.v2.ProdConsBuffer;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class XMLParameters {
    
    public static int nProd;
    public static int nCons;
    public static int bufSz;
    public static int prodTime;
    public static int consTime;
    public static int minProd;
    public static int maxProd;
    
    public static void getParameters(){
        try {
            Properties prop = new Properties();
            prop.loadFromXML(new FileInputStream("options.xml"));
            nProd = Integer.parseInt(prop.getProperty("nProd"));
            nCons = Integer.parseInt(prop.getProperty("nCons"));
            bufSz = Integer.parseInt(prop.getProperty("bufSz"));
            prodTime = Integer.parseInt(prop.getProperty("prodTime"));
            consTime = Integer.parseInt(prop.getProperty("consTime"));
            minProd = Integer.parseInt(prop.getProperty("minProd"));
            maxProd = Integer.parseInt(prop.getProperty("maxProd"));
        } catch (IOException ex) {
            Logger.getLogger(ProdConsBuffer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    
    
}
