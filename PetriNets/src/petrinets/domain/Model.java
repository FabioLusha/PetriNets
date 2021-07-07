package petrinets.domain;

import petrinets.domain.net.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class Model extends  AbstractINetLogic{
	public NetLogic logicOfNet;
    //private static XMLmanager<NetArchive> netxmlmanager = new XMLmanager<NetArchive>("nets.xml");


    private Net controlNet;
    private PetriNet controlPetriNet;
    private PriorityPetriNet controlPriorityPetriNet;
    

    public Model() throws IOException,ReflectiveOperationException{
        logicOfNet = new NetLogic();
    }


    //Ritorna true se in Archive non e' presente alcun elemento con chiave name
    public boolean createNet(String name){
       return logicOfNet.createNet(name);
    }

    public boolean containsPlace(String name) {
    	return controlNet.isPlace(new Place(name));
    }

    public boolean transitionIsNotPointed(String placeName, String transitionName, int direction){

        return logicOfNet.transitionIsNotPointed(placeName,transitionName,direction);

    }


    public boolean containsTransition(String name) {
    	return logicOfNet.containsTransition(name);
    }

    public boolean addFluxElem(String placename,String transitionname, int direction) {
        return logicOfNet.addFluxElem(placename ,transitionname,direction);
    }

    public boolean saveCurrentNet() {
      return logicOfNet.saveCurrentNet();
    }



    public boolean containsNet(String netname) {
       if(netArchive.contains(netname))
           return netArchive.get(netname) instanceof Net;

    return false;
    }

    public boolean containsFluxRel(String placeName, String transName, int direction) {
       return controlPetriNet.getFluxRelation().contains(new OrderedPair(new Place(placeName), new Transition(transName), OrderedPair.Direction.ordinalToType(direction)));
    }
    
    public List<String> getSavedNetsNames(){
        return getSavedGenericNetsNames(Net.class.getName());
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
            return netArchive.get(petriNetName) instanceof PetriNet;
    	
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
        return getSavedGenericNetsNames(PetriNet.class.getName());
    }

    public List<String> getMarc(SimulatableNet petrinet){
      
        return petrinet.getMarcmap().values().stream().
                map(e -> Integer.toString(e)).
                collect(Collectors.toList());
    }

    public List<String> getValues(SimulatableNet petrinet){
        return petrinet.getValuemap().values().stream().
                map(e -> Integer.toString(e)).
                collect(Collectors.toList());
    }

    public PetriNet getCurrentPetriNet(){
        return controlPetriNet;
    }

    
    public void changeMarc(String name, int newValue) {
    	controlPetriNet.changeMarc(new Place(name), newValue);
    }

    public void changeFluxRelVal(String placeName, String transName, int direction, int newValue) {
    	controlPetriNet.changeFluxRel(new OrderedPair(new Place(placeName), new Transition(transName), OrderedPair.Direction.ordinalToType(direction)), newValue);
    }


    //PRIORITY PETRI NET
    public boolean createPriorityPetriNet(String priorityPetriNetName, PetriNet petriNet) {
           if(netArchive.contains(priorityPetriNetName))
               return false;
           else
               controlPriorityPetriNet = new PriorityPetriNet(priorityPetriNetName,petriNet);

           return true;
    }

    public boolean saveCurrentPriorityPetriNet() {

        if(netArchive.containsValue(controlPriorityPetriNet))
            return false;

        netArchive.add(controlPriorityPetriNet.getName(), controlPriorityPetriNet);
        controlPriorityPetriNet = null;
        return true;
    }


    public PriorityPetriNet getCurrentPriorityPetriNet() {
        return controlPriorityPetriNet;
    }

    public Map<String, Integer> getPriorityMapInString(PriorityPetriNet priorityPetriNet){
        return priorityPetriNet.getPriorityMap().entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey().getName(), e ->  e.getValue()));
    }

    public void changePriority(String transName, int newValue) {
        controlPriorityPetriNet.getPriorityMap().replace(new Transition(transName),newValue);
    }

    public List<String> getSavedPriorityPetriNetsNames() {
        return getSavedGenericNetsNames(PriorityPetriNet.class.getName());
    }

    public boolean containsPriorityPetriNet(String netname) {
        if(netArchive.contains(netname))
            return netArchive.get(netname) instanceof PriorityPetriNet;

        return false;
    }
}
