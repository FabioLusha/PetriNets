package net;

import java.util.*;

public class NetArchive {
    private Map<String, Net> netMap;

    public NetArchive(Map<String, Net> netMap){
        this.netMap = netMap;
    }
    
    public NetArchive() {
        netMap = new HashMap<>();
    }

    public Map<String, Net> getNetMap() {
        return netMap;
    }

    public void setNetMap(Map<String, Net> netMap) {
        this.netMap = netMap;
    }

    public void add(String name, Net net){
        netMap.put(name, net);
    }

    public boolean contains(String name){
        return netMap.containsKey(name);
    }
    
    
 
}
