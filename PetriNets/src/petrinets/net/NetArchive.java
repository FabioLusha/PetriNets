package petrinets.net;

import java.io.IOException;
import java.util.*;

import systemservices.XMLmanager;

public class NetArchive {
	
	private static XMLmanager<Map<String, INet>> netxmlmanager = new XMLmanager<>("nets.xml");
	
	private static NetArchive instance;

	private static  Map<String, INet> inetMap;
    
    //per l'eccezione guarda il metodo open()
    public static NetArchive getInstance() throws Exception{
    	if(instance == null) {
    		open();
    		instance = new NetArchive(inetMap);
    	}
    	return instance;
    }

    public NetArchive(Map<String, INet> pinetMap){
        inetMap = pinetMap;
    }
    
    public NetArchive() {
        inetMap = new HashMap<>();
    }
    
    /*TODO
     * Creare un tipo di eccezione apposito
     */
    public static void open() throws Exception {
    
        	if (!(netxmlmanager.isEmpty())) {
        	    try {
                    inetMap = (Map<String, INet>) netxmlmanager.deserializeFromXML();
                }catch(Exception e){
        	        inetMap = new HashMap();
        	        throw e;
                }
        	}else
                inetMap = new HashMap<>();
    }
    
    public void permanentSave() throws IOException {
		netxmlmanager.serializeToXML(inetMap);
    }

    public static Map<String, INet> getInetMap() {
        return inetMap;
    }

    public static void setInetMap(Map<String, INet> inetMap) {
        NetArchive.inetMap = inetMap;
    }

    public static void add(String name, INet net){
        inetMap.put(name, net);
    }

    public static boolean contains(String name){
        return inetMap.containsKey(name);
    }
    
    
 
}
