package systemservices;

import java.io.IOException;
import java.util.*;

import petrinets.domain.net.INet;

public class Archive implements INetRepository{

    private Serializer<Map<String, INet>> mapSerializer;
    private Map<String, INet> inetMap;

    public Archive(){
        inetMap = new HashMap<>();
    }

    public void permanentSave() throws IOException {
        mapSerializer.serialize(inetMap);
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
        mapSerializer = new XMLmanager<>(filePath);
        if (!(mapSerializer.isEmpty())) {
            inetMap = (Map<String, INet>) mapSerializer.deserialize();
        }

    }

    public INet get(String netName){
        return inetMap.get(netName);
    }


    public Collection<INet> getAllElements() {
        return inetMap.values();
    }
}
