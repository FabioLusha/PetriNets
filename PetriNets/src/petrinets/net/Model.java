package petrinets.net;

import systemservices.Archive;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import petrinets.UI.Pair;




public class Model {
	
    //private static XMLmanager<NetArchive> netxmlmanager = new XMLmanager<NetArchive>("nets.xml");

    private Archive netArchive;

    private Net controlNet;
    private PetriNet controlPetriNet;
    

    public Model(Archive netArchive){
        this.netArchive = netArchive;
    }

    public Model() throws IOException{
    	netArchive = Archive.getInstance();
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
    public boolean transitionIsNotPointed(String placeName, String transitionName, int direction){
    	
        return (OrderedPair.Direction.ordinalToType(direction) == OrderedPair.Direction.tp
                && !controlNet.isTransition(new Transition(transitionName)));
       
    }

    public boolean containsPlace(String name) {
    	return controlNet.isPlace(new Place(name));
    }
    public boolean petriNetContainsPlace(String name) {
    	return controlPetriNet.getBasedNet().isPlace(new Place(name));
    }
    
    public boolean containsTransition(String name) {
    	return controlNet.isTransition(new Transition(name));
    }
    
    public boolean petriNetContainsTransition(String name){
    	return controlPetriNet.getBasedNet().isTransition(new Transition(name));
    }
    
   public boolean addFluxElem(String placename,String transitionname, int direction) {
	    OrderedPair.Direction type = OrderedPair.Direction.ordinalToType(direction);
	    
	    return controlNet.addFluxRelElement(new OrderedPair(new Place(placename) , new Transition(transitionname) , type));
   }
   
   public boolean saveCurrentNet() {
	   
	   if(netArchive.containsValue(controlNet)) {
		   return false;
	   }
	    netArchive.add(controlNet.getName(), controlNet);
	    //pulisco la variabile di comodo per evidenziare eventuali errori
	    controlNet = null;

	    return true;
   }

   public void permanentSave() throws IOException {
	   
			netArchive.permanentSave();
		}
   
   public boolean containsINet(String inetname) {
	   return netArchive.contains(inetname);
   }
   
   public boolean containsNet(String netname) {
	   if(netArchive.contains(netname))
   		if(netArchive.getInetMap().get(netname) instanceof Net)
   			return true;
   	
   	return false;
   }
   
   public boolean containsFluxRel(String placeName, String transName, int direction) {
	   return controlPetriNet.getFluxRelation().contains(new OrderedPair(new Place(placeName), new Transition(transName), OrderedPair.Direction.ordinalToType(direction)));
   }
   
   public List<String> getPlaces(INet net) {
	   return net.getPlaces().stream().map(e -> e.getName()).collect(Collectors.toList());
   }
   
   public List<String> getTransitions(INet net) {
	   return net.getTransitions().stream().
			   map(e -> e.getName()).
			   collect(Collectors.toList());
   }
   
   public List<Pair<String,String>> getFluxRelation(INet net){
	   return net.getFluxRelation().
			   stream()
			   .map(e -> e.getDirection() == OrderedPair.Direction.pt ?
                       new Pair<String,String>(e.getCurrentPlace().getName(), e.getCurrentTransition().getName()) :
                        new Pair<String,String>(e.getCurrentTransition().getName(), e.getCurrentPlace().getName()))
			   .collect(Collectors.toList());
	   
   }
   
  public List<String> getSavedNetsNames(){
	  return getSavedGenericNetsNames(Net.class.toString());
  }

    private List<String> getSavedGenericNetsNames(String className){
        return netArchive.getInetMap().entrySet().stream()
                .filter(e -> e.getValue().getClass().toString().equals(className))
                .map(e -> e.getKey())
                .collect(Collectors.toList());
    }
   
  public INet getNet(String netName) {
	  assert netArchive.contains(netName);
	  INet tmpNet = netArchive.getInetMap().get(netName);
	 

	  return  tmpNet;
  }


    //PARTE RETE PETRI
    public boolean saveCurrentPetriNet() {
    	
    	if(netArchive.containsValue(controlPetriNet))
    		return false;
  
        netArchive.add(controlPetriNet.getName(), controlPetriNet);
        controlPetriNet = null;
        return true;
    }
    
    public boolean containsPetriNet(String petriNetName) {
    	if(netArchive.contains(petriNetName))
    		if(netArchive.getInetMap().get(petriNetName) instanceof PetriNet)
    			return true;
    	
    	return false;
    }
    

    public boolean createPetriNet(String name, Net net){
        if(netArchive.contains(name))
            return false;
        else{
            controlPetriNet = new PetriNet(name,net);
        }
        return true;
    }

    public List<String> getSavedPetriNetsNames(){
        return getSavedGenericNetsNames(PetriNet.class.toString());
    }

    public List<String> getMarc(PetriNet petrinet){
      
        return petrinet.getMarcmap().values().stream().
                map(e -> Integer.toString(e)).
                collect(Collectors.toList());
    }

    public List<String> getValues(PetriNet petrinet){
        return petrinet.getValuemap().values().stream().
                map(e -> Integer.toString(e)).
                collect(Collectors.toList());
    }

    public PetriNet getCurrentPetriNet(){
        return controlPetriNet;
    }

    public void remove(String name){
        netArchive.removeFromArchive(name);
    }
    
    public void changeMarc(String name, int newValue) {
    	controlPetriNet.getMarcmap().replace(new Place(name), newValue);
    	
    }

    public void changeFluxRelVal(String placeName, String transName, int direction, int newValue) {
    	controlPetriNet.getValuemap().replace(new OrderedPair(new Place(placeName), new Transition(transName), OrderedPair.Direction.ordinalToType(direction)), newValue);
    
    }
}