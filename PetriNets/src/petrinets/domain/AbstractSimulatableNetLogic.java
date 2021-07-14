package petrinets.domain;

import petrinets.domain.petrinet.PetriNet;
import petrinets.domain.petrinet.SimulatableNet;
import systemservices.PropertiesInitializationException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractSimulatableNetLogic extends AbstractINetLogic{

    public AbstractSimulatableNetLogic() throws IOException, ReflectiveOperationException, PropertiesInitializationException {
        super();
    }

    public List<String> getMarc(SimulatableNet simulatableNet){

        return simulatableNet.getPlaces().stream().
                map(e -> Integer.toString(simulatableNet.getMarcValue(e))).
                collect(Collectors.toList());
    }

    public List<String> getValues(SimulatableNet simulatableNet){
        return simulatableNet.getFluxRelation().stream().
                map(e -> Integer.toString(simulatableNet.getFluxRelValue(e))).
                collect(Collectors.toList());
    }

    public List<String> getSavedPetriNetsNames(){
        return getSavedGenericNetsNames(PetriNet.class.getName());
    }






}
