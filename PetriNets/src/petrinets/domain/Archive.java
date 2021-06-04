package petrinets.domain;

import java.io.IOException;
import java.util.*;

import petrinets.domain.net.INet;
import systemservices.XMLmanager;

public class Archive {
	
	private  XMLmanager<Map<String, INet>> mapXMLmanager;
	
	private static Archive instance;

	private  Map<String, INet> inetMap;
    
    //per l'eccezione guarda il metodo open()
    public static Archive getInstance() throws IOException{
    	if(instance == null) {
    		instance = new Archive();
    	}
    	return instance;
    }
 
    private Archive() throws IOException{
    		mapXMLmanager = new XMLmanager<>("nets.xml");
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
        this.inetMap = inetMap;
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
