package petrinets.domain;

import petrinets.domain.net.*;
import petrinets.domain.petrinet.PetriNet;
import petrinets.domain.petrinet.PriorityPetriNet;
import petrinets.domain.petrinet.SimulatableNet;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public class Model extends  AbstractINetLogic{
	private NetLogic logicOfNet;
	private PetriNetLogic logicOfPetriNet;
	private PriorityPetriNetLogic logicOfPriorityPetriNet;

    //private static XMLmanager<NetArchive> netxmlmanager = new XMLmanager<NetArchive>("nets.xml");


    public Model() throws IOException,ReflectiveOperationException{
        logicOfNet = new NetLogic();
        logicOfPetriNet = new PetriNetLogic();
        logicOfPriorityPetriNet = new PriorityPetriNetLogic();
    }


    //Ritorna true se in Archive non e' presente alcun elemento con chiave name
    public boolean createNet(String name){
       return logicOfNet.createNet(name);
    }

    public boolean containsPlace(String name) {
    	return logicOfNet.containsPlace(name);
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
        return logicOfNet.containsNet(netname);
    }

    public boolean containsFluxRel(String placeName, String transName, int direction) {
       return logicOfPetriNet.containsFluxRel(placeName,transName,direction);
    }
    
    public List<String> getSavedNetsNames(){
        return getSavedGenericNetsNames(Net.class.getName());
      }


    //PARTE RETE PETRI
    public boolean saveCurrentPetriNet() {
    	return logicOfPetriNet.saveCurrentNet();
    }
    
    public boolean containsPetriNet(String petriNetName) {
    return logicOfPetriNet.containsPetriNet(petriNetName);
    }
    

    public boolean createPetriNet(String name, String net){
       return logicOfPetriNet.createPetriNet(name,net);
    }
    


    public List<String> getSavedPetriNetsNames(){
        return getSavedGenericNetsNames(PetriNet.class.getName());
    }

    public List<String> getMarc(SimulatableNet petrinet){
        return logicOfPetriNet.getMarc(petrinet);
    }

    public List<String> getValues(SimulatableNet petrinet){
        return logicOfPetriNet.getValues(petrinet);
    }

    public PetriNet getCurrentPetriNet(){
        return logicOfPetriNet.getCurrentPetriNet();
    }

    
    public void changeMarc(String name, int newValue) {
    	logicOfPetriNet.changeMarc(name, newValue);
    }

    public void changeFluxRelVal(String placeName, String transName, int direction, int newValue) {
        logicOfPetriNet.changeFluxRelVal(placeName,transName,direction,newValue);
    }


    //PRIORITY PETRI NET
    public boolean createPriorityPetriNet(String priorityPetriNetName, PetriNet petriNet) {
           return logicOfPriorityPetriNet.createPriorityPetriNet(priorityPetriNetName,petriNet);
    }

    public boolean saveCurrentPriorityPetriNet() {
            return  logicOfPriorityPetriNet.saveCurrentNet();
    }


    public PriorityPetriNet getCurrentPriorityPetriNet() {
        return logicOfPriorityPetriNet.getCurrentPriorityPetriNet();
    }

    public Map<String, Integer> getPriorityMapInString(PriorityPetriNet priorityPetriNet){
        return logicOfPriorityPetriNet.getPriorityMapInString(priorityPetriNet);
    }

    public void changePriority(String transName, int newValue) {
        logicOfPriorityPetriNet.changePriority(transName,newValue);
    }

    public List<String> getSavedPriorityPetriNetsNames() {
        return logicOfPriorityPetriNet.getSavedPriorityPetriNetsNames();
    }

    public boolean containsPriorityPetriNet(String netname) {
        return logicOfPriorityPetriNet.containsPriorityPetriNet(netname);
    }
}
