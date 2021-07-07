package petrinets.domain;

import petrinets.domain.net.Transition;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PriorityPetriNetLogic extends  AbstractSimulatableNetLogic{
    private PriorityPetriNet controlPriorityPetriNet;

    public PriorityPetriNetLogic() throws IOException, ReflectiveOperationException {
    }

    public boolean createPriorityPetriNet(String priorityPetriNetName, PetriNet petriNet) {
        if(netArchive.contains(priorityPetriNetName))
            return false;
        else
            controlPriorityPetriNet = new PriorityPetriNet(priorityPetriNetName,petriNet);

        return true;
    }

    public boolean saveCurrentNet() {

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

    public boolean containsPriorityPetriNet(String netname) {
        if(netArchive.contains(netname))
            return netArchive.get(netname) instanceof PriorityPetriNet;

        return false;
    }

    public List<String> getSavedPriorityPetriNetsNames() {
        return getSavedGenericNetsNames(PriorityPetriNet.class.getName());
    }


}
