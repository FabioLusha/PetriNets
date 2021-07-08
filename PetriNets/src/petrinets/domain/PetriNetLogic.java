package petrinets.domain;

import petrinets.domain.net.Net;
import petrinets.domain.net.OrderedPair;
import petrinets.domain.net.Place;
import petrinets.domain.net.Transition;
import petrinets.domain.petrinet.PetriNet;

import java.io.IOException;

public class PetriNetLogic extends AbstractSimulatableNetLogic{
    private PetriNet controlPetriNet;


    public PetriNetLogic() throws IOException,ReflectiveOperationException {
        super();
        // TODO Auto-generated constructor stub
    }

        public boolean containsFluxRel(String placeName, String transName, int direction) {
        return controlPetriNet.getFluxRelation().contains(new OrderedPair(new Place(placeName), new Transition(transName), OrderedPair.Direction.ordinalToType(direction)));
    }

    public boolean saveCurrentNet() {

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

    public boolean createPetriNet(String petriNetName, String netName){
        Net net = (Net) getINet(netName);
        if(netArchive.contains(petriNetName))
            return false;
        else{
            controlPetriNet = new PetriNet(petriNetName,net);
        }
        return true;
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


}
