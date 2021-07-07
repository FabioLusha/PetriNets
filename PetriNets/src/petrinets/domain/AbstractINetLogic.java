package petrinets.domain;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

import petrinets.UI.Pair;
import petrinets.domain.net.INet;
import petrinets.domain.net.Net;
import petrinets.domain.net.OrderedPair;
import petrinets.domain.net.Place;
import petrinets.domain.net.Transition;
import systemservices.INetRepository;
import systemservices.RepositoryFactory;

public abstract class AbstractINetLogic {
	protected final INetRepository netArchive;

	public AbstractINetLogic()
            throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
		    netArchive = RepositoryFactory.getInstance().getRepo();
	}

	public boolean inetContainsPlace(INet inet, String name) {
		return inet.getPlaces().contains(new Place(name));
	}

	public boolean inetContainsTransition(INet inet, String name) {
		return inet.getTransitions().contains(new Transition(name));
	}

	public abstract boolean saveCurrentNet();
	
    public void permanentSave() throws IOException {
        netArchive.permanentSave();
    }
    
    public boolean containsINet(String inetname) {
       return netArchive.contains(inetname);
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


     
     public List<String> getSavedGenericNetsNames(String className){
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
     
     public void remove(String name){
         netArchive.removeFromArchive(name);
     }
     

}
