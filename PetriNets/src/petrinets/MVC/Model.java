package petrinets.MVC;

import petrinets.net.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;




public class Model {
	
    //private static XMLmanager<NetArchive> netxmlmanager = new XMLmanager<NetArchive>("nets.xml");

    private Archive netArchive;

    private Net controlNet;
    private PetriNet controlPetriNet;
    //Flag, default a false
    private boolean petriNetModified = false;
    

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
	   		if(controlPetriNet != null) {
	   			netArchive.removeFromArchive(getCurrentPetriNet().getName());
	   		}else
			netArchive.permanentSave();
		}
   
   public boolean containsNet(String netname) {
	   return netArchive.contains(netname);
   }
   
   public boolean containsFluxRel(String placeName, String transName, int direction) {
	   return controlPetriNet.getFluxRelation().contains(new OrderedPair(new Place(placeName), new Transition(transName), OrderedPair.Direction.ordinalToType(direction)));
   }
   
   public List<String> getPlaces(String netname) {
	   return netArchive.getInetMap().get(netname).getPlaces().stream().map(e -> e.getName()).collect(Collectors.toList());
   }
   
   public List<String> getTransitions(String netname) {
	   return netArchive.getInetMap().get(netname).getTransitions().stream().
			   map(e -> e.getName()).
			   collect(Collectors.toList());
   }
   
   public List<Pair<String,String>> getFluxRelation(String netname){
	   return netArchive.getInetMap().get(netname).getFluxRelation().
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
   
  public Net getNet(String netName) {
	  assert netArchive.contains(netName);
	  INet tmpNet = netArchive.getInetMap().get(netName);
	  assert tmpNet.getClass() == controlNet.getClass();

	  return (Net) tmpNet;
  }


    //PARTE RETE PETRI
    public boolean saveCurrentPetriNet() {
    	
    	if(netArchive.containsValue(controlPetriNet) && petriNetModified)
    		return false;
  
        netArchive.add(controlPetriNet.getName(), controlPetriNet);
        controlPetriNet = null;
        return true;
    }
    
    public void temporarySave() {
        netArchive.add(controlPetriNet.getName(), controlPetriNet);
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

    public List<String> getMarc(String netname){
        assert netArchive.getInetMap().get(netname).getClass() == PetriNet.class;
        
        PetriNet tmp = (PetriNet) netArchive.getInetMap().get(netname);
        return tmp.getMarcmap().values().stream().
                map(e -> Integer.toString(e)).
                collect(Collectors.toList());
    }

    public List<String> getValues(String netname){
        assert netArchive.getInetMap().get(netname).getClass() == PetriNet.class;
        
        PetriNet tmp = (PetriNet) netArchive.getInetMap().get(netname);
        return tmp.getValuemap().values().stream().
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
    	
    	petriNetModified = true;
    	controlPetriNet.getMarcmap().replace(new Place(name), newValue);
    	temporarySave();
    }

    public void changeFluxRelVal(String placeName, String transName, int direction, int newValue) {
    	
    	petriNetModified = true;
    	controlPetriNet.getValuemap().replace(new OrderedPair(new Place(placeName), new Transition(transName), OrderedPair.Direction.ordinalToType(direction)), newValue);
    	temporarySave();
    }
}
