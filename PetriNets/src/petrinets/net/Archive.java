package petrinets.net;

import java.io.IOException;
import java.util.*;

import systemservices.XMLmanager;

public class Archive {
	
	private static XMLmanager<Map<String, INet>> mapXMLmanager = new XMLmanager<>("nets.xml");
	
	private static Archive instance;

	private static  Map<String, INet> inetMap;
    
    //per l'eccezione guarda il metodo open()
    public static Archive getInstance() throws IOException{
    	if(instance == null) {
    		open();
    		instance = new Archive(inetMap);
    	}
    	return instance;
    }

    private Archive(Map<String, INet> pinetMap){
        inetMap = pinetMap;
    }
    
    private Archive() {
        inetMap = new HashMap<>();
    }
    
 
    public static void open() throws IOException{
    
        	if (!(mapXMLmanager.isEmpty())) {
        	    try {
                    inetMap = (Map<String, INet>) mapXMLmanager.deserializeFromXML();
                }catch(IOException e){
        	        inetMap = new HashMap<>();
        	        throw e;
                }
        	}else
                inetMap = new HashMap<>();
    }
    
    public void permanentSave() throws IOException {
		mapXMLmanager.serializeToXML(inetMap);
    }

    public Map<String, INet> getInetMap() {
        return inetMap;
    }

    public void setInetMap(Map<String, INet> inetMap) {
        Archive.inetMap = inetMap;
    }

    public void add(String name, INet net){
        inetMap.put(name, net);
    }

    public boolean contains(String name){
        return inetMap.containsKey(name);
    }
    
    public boolean containsValue(INet inet){
        return inetMap.containsValue(inet);
    }

    public void removeFromArchive(String key){
        inetMap.keySet().remove(key);
    }
    
 
}
