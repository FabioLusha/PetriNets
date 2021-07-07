package petrinets.domain;

import java.io.IOException;

import petrinets.domain.net.Net;
import petrinets.domain.net.OrderedPair;
import petrinets.domain.net.Place;
import petrinets.domain.net.Transition;

public class NetLogic extends AbstractINetLogic{
	private Net controlNet;
	
	
	public NetLogic() throws IOException,ReflectiveOperationException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean saveCurrentNet() {
		if(netArchive.containsValue(controlNet)) {
	           return false;
	       }
	        netArchive.add(controlNet.getName(), controlNet);
	        //pulisco la variabile di comodo per evidenziare eventuali errori
	        controlNet = null;

	        return true;
	}
	
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

	public boolean containsTransition(String name) {
		return controlNet.isTransition(new Transition(name));
	}

	public boolean addFluxElem(String placename,String transitionname, int direction) {
		OrderedPair.Direction type = OrderedPair.Direction.ordinalToType(direction);

		return controlNet.addFluxRelElement(new OrderedPair(new Place(placename) , new Transition(transitionname) , type));
	}

	public boolean containsPlace(String name) {
		return controlNet.isPlace(new Place(name));
	}




}
