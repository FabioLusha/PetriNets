package petrinets.MVC;

import petrinets.net.*;
import systemservices.XMLmanager;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;




public class Model {
	
    private static XMLmanager<NetArchive> netxmlmanager = new XMLmanager<NetArchive>("nets.xml");

    private NetArchive netArchive;
    private Net controlNet;

    public Model(NetArchive netArchive){
        this.netArchive = netArchive;
    }

    public Model(){
        
            try {
            	if (!(netxmlmanager.isEmpty())) {
               // netArchive.setNetMap((Map<>) netxmlmanager.deserializeFromXML());
                netArchive = (NetArchive) netxmlmanager.deserializeFromXML();
            	}else
                    netArchive = new NetArchive();

            } catch (IOException e) {
                // TODO Auto-generated catch block
            	netArchive = new NetArchive();
                e.printStackTrace();
            }
        
    }


    //Ritorna true se in Archive non e' presente alcun elemento con chiave name
    public boolean createNet(String name){
        if(netArchive.contains(name))
            return false;
        else{
            controlNet = new Net(name);
        }
        return true;
    }

    //Trasizione non puntata da nessun posto
    public boolean transitionIsNotPointed(String placename, String transitionname, int direction){
    	
        return (OrderedPair.Direction.ordinalToType(direction) == OrderedPair.Direction.tp
                && !controlNet.containsTransition(new Transition(transitionname)));
       
    }

    public boolean containsPlace(String name) {
    	return controlNet.isPlace(new Place(name));
    }
    
    public boolean containsTransition(String name) {
    	return controlNet.isTransition(new Transition(name));
    }
    
   public boolean addFluxElem(String placename,String transitionname, int direction) {
	    OrderedPair.Direction type = OrderedPair.Direction.ordinalToType(direction);
	    
	    return controlNet.addFluxRelElement(new OrderedPair(new Place(placename) , new Transition(transitionname) , type));
   }
   
   public boolean addNet() {
	   if(netArchive.contains(controlNet.getName())) {
		   return false;
	   }
	    netArchive.add(controlNet.getName(), controlNet);
	    
	    return true;
   }

   public void permanentSave() throws IOException {
			netxmlmanager.serializeToXML(netArchive);
   }
   
   public boolean containsNet(String netname) {
	   return netArchive.contains(netname);
   }
   
   public List<String> getPlaces(String netname) {
	   return netArchive.getNetMap().get(netname).getPlaces().stream().map(e -> e.getName()).collect(Collectors.toList());
   }
   
   public List<String> getTransitions(String netname) {
	   return netArchive.getNetMap().get(netname).getTransitions().stream().
			   map(e -> e.getName()).
			   collect(Collectors.toList());
   }
   
   public List<Pair<String,String>> getFluxRelation(String netname){
	   return netArchive.getNetMap().get(netname).getFluxRelation().
			   stream().
			   map(e -> new Pair<String,String>(e.getCurrentPlace().getName(), e.getCurrentTransition().getName())).
			   collect(Collectors.toList());
	   
   }
   
  public List<String> getSavedNetsNames(){
	  return netArchive.getNetMap().keySet().stream().collect(Collectors.toList());
  }
   
}
