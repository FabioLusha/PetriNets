package systemservices;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import petrinets.domain.net.INet;

public class Archive implements INetRepository{

    private XMLmanager<Map<String, INet>> mapXMLmanager;

    private static Archive instance;

    private Map<String, INet> inetMap;

    //per l'eccezione guarda il metodo open()
    public static Archive getInstance() throws IOException {
        if (instance == null) {
            instance = new Archive();
        }
        return instance;
    }

    public Archive(){
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

    public void add(String name, INet net) {
        inetMap.put(name, net);
    }

    public boolean contains(String name) {
        return inetMap.containsKey(name);
    }

    public boolean containsValue(INet inet) {
        return inetMap.containsValue(inet);
    }

    public void removeFromArchive(String key) {
        inetMap.keySet().remove(key);
    }

    public void readFromFile(String filePath) throws IOException {
        mapXMLmanager = new XMLmanager<>(filePath);
        if (!(mapXMLmanager.isEmpty())) {
            inetMap = (Map<String, INet>) mapXMLmanager.deserializeFromXML();
        }

    }

    public INet get(String netName){
        return inetMap.get(netName);
    }


    public Collection<INet> getAllElements() {
        return inetMap.values();
    }
}
