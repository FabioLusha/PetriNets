package petrinets.net;

import java.io.IOException;
import java.util.*;

import systemservices.XMLmanager;

public class Archive {
	
	private static XMLmanager<Map<String, INet>> netxmlmanager = new XMLmanager<>("nets.xml");
	
	private static Archive instance;

	private static  Map<String, INet> inetMap;
    
    //per l'eccezione guarda il metodo open()
    public static Archive getInstance() throws Exception{
    	if(instance == null) {
    		open();
    		instance = new Archive(inetMap);
    	}
    	return instance;
    }

    public Archive(Map<String, INet> pinetMap){
        inetMap = pinetMap;
    }
    
    public Archive() {
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
        Archive.inetMap = inetMap;
    }

    public static void add(String name, INet net){
        inetMap.put(name, net);
    }

    public static boolean contains(String name){
        return inetMap.containsKey(name);
    }

    public void removeFromArchive(String key){
        inetMap.keySet().remove(key);
    }
    
 
}
