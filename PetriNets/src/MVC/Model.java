package MVC;

import analyzer.XMLmanager;
import net.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;



public class Model {
	
	//Messo NetArchive come parametro della classe generica, ora funziona salvataggio
    private static XMLmanager netxmlmanager = new XMLmanager<NetArchive>("nets.xml");

    private NetArchive netArchive;
    private Net controlNet;

    public Model(NetArchive netArchive){
        this.netArchive = netArchive;
    }

    public Model(){
        if (!(netxmlmanager.isEmpty())) {
            try {
               // netArchive.setNetMap((Map<>) netxmlmanager.deserializeFromXML());
                netArchive = (NetArchive) netxmlmanager.deserializeFromXML();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else
            netArchive = new NetArchive();
    }


    //return true se in Archive non è presente alcun elemento con chiave name
    public boolean createNet(String name){

        if(netArchive.contains(name))
            return false;
        else{
            controlNet = new Net(name);
        }
        return true;
    }

    //Trasizione non puntata de nessun posto
    public boolean transitionIsNotPointed(String posto, String trans, int direction){
        return (OrderedPair.typePair.ordinalToType(direction) == OrderedPair.typePair.tp
                && !controlNet.containsTransition(new Transition(trans)));
      
       
    }

    public boolean giaPresente(String posto, String trans, int direction){
        return controlNet.addFluxRelElement(
                new OrderedPair(
                        new Place(posto), new Transition(trans), OrderedPair.typePair.ordinalToType(direction))
        );
    }

    public boolean containsPlace(String name) {
    	return controlNet.isPlace(new Place(name));
    }
    
    public boolean containsTransition(String name) {
    	return controlNet.isTransition(new Transition(name));
    }
    
   public boolean addFluxElem(String placename,String transitionname, int direction) {
	    OrderedPair.typePair type = OrderedPair.typePair.ordinalToType(direction);
	    
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
