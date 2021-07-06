package petrinets.domain;

import petrinets.domain.net.*;
import systemservices.Archive;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import petrinets.UI.Pair;
import systemservices.INetRepository;
import systemservices.RepositoryFactory;


public class Model {
	
    //private static XMLmanager<NetArchive> netxmlmanager = new XMLmanager<NetArchive>("nets.xml");

    private final INetRepository netArchive;

    private Net controlNet;
    private PetriNet controlPetriNet;
    private PriorityPetriNet controlPriorityPetriNet;
    

    public Model(INetRepository netArchive){
        this.netArchive = netArchive;
    }

    public Model() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
    	netArchive = RepositoryFactory.getInstance().   getRepo();
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

    public boolean inetContainsPlace(INet inet, String  name) {
    	return inet.getPlaces().contains(new Place(name));
    }

    public boolean inetContainsTransition(INet inet, String name){
        return inet.getTransitions().contains(new Transition(name));
    }
    
    public boolean inetContainsTransition(String name) {
    	return controlNet.isTransition(new Transition(name));
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
           return netArchive.get(netname) instanceof Net;

    return false;
    }

    public boolean containsFluxRel(String placeName, String transName, int direction) {
       return controlPetriNet.getFluxRelation().contains(new OrderedPair(new Place(placeName), new Transition(transName), OrderedPair.Direction.ordinalToType(direction)));
    }

    public List<String> getPlaces(INet net) {
       return net.getPlaces().stream().map(Place::getName).collect(Collectors.toList());
    }

    public List<String> getTransitions(INet net) {
       return net.getTransitions().stream().
               map(Transition::getName).
               collect(Collectors.toList());
    }

    public List<Pair<String,String>> getFluxRelation(INet net){
       return net.getFluxRelation().
               stream().
               map(e -> e.getDirection() == OrderedPair.Direction.pt ?
                       new Pair<>(e.getCurrentPlace().getName(), e.getCurrentTransition().getName()) :
                       new Pair<>(e.getCurrentTransition().getName(), e.getCurrentPlace().getName())
               ).
               collect(Collectors.toList());

    }

    public List<String> getSavedNetsNames(){
      return getSavedGenericNetsNames(Net.class.getName());
    }
    

    private List<String> getSavedGenericNetsNames(String className){
        return netArchive.getAllElements().stream()
                .filter(e -> e.getClass().getName().equals(className))
                .map(e -> e.getName())
                .collect(Collectors.toList());
    }

    public INet getINet(String netName) {
	  assert netArchive.contains(netName);

      return netArchive.get(netName);
  }

    public void saveINet(INet inet) {
    	netArchive.add(inet.getName(), inet);
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

    public void remove(String name){
        netArchive.removeFromArchive(name);
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
