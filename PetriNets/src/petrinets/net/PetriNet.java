package petrinets.net;

import java.util.*;

public class PetriNet implements INet,Simulatable{
	private Map<Place,Integer> marcmap;
	private Map<OrderedPair, Integer>  valuemap;
	private Net basedNet;
	private String name;
	
	public PetriNet(String pname, Net pbasedNet) {
		name = pname;
		basedNet = pbasedNet;
		
		marcmap = new HashMap<>();
		valuemap = new HashMap<>();
		
		basedNet.getPlaces().forEach(e -> marcmap.put(e, 1));
		basedNet.getFluxRelation().forEach(e -> valuemap.put(e, 0));
	}
	
	public PetriNet() {
		
	}

	public Map<Place, Integer> getMarcmap() {
		return marcmap;
	}

	public void setMarcmap(Map<Place, Integer> marcmap) {
		this.marcmap = marcmap;
	}

	public Map<OrderedPair, Integer> getValuemap() {
		return valuemap;
	}

	public void setValuemap(Map<OrderedPair, Integer> valuemap) {
		this.valuemap = valuemap;
	}
	
	public Net getBasedNet() {
		return basedNet;
	}

	public void setBasedNet(Net basedNet) {
		this.basedNet = basedNet;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Transition> getTransitions(){
		return basedNet.getTransitions();
	}

	public Set<Place> getPlaces(){
		return basedNet.getPlaces();
	}

	public Set<OrderedPair> getFluxRelation(){
		return basedNet.getFluxRelation();
	}
	
	public void simulate(Transition toExecute){
		var pred = this.getBasedNet().getPreviousPlaces(toExecute);
		var succ = this.getBasedNet().getSuccessorPlaces(toExecute);
		
		for(var placeMark : marcmap.entrySet()) {
			if(pred.contains(placeMark.getKey())) {
				var tmpOrderedPair = new OrderedPair(placeMark.getKey(),toExecute);
				int cost = valuemap.get(tmpOrderedPair);
				
				assert placeMark.getValue() >= cost;
				
				placeMark.setValue(placeMark.getValue() - cost);
			}else if(succ.contains(placeMark.getKey())) {
				var tmpOrderedPair = new OrderedPair(toExecute,placeMark.getKey());
				placeMark.setValue(placeMark.getValue() + valuemap.get(tmpOrderedPair));
			}
		}

	}

	public String toString() {
		StringBuilder output = new StringBuilder();
		output.append("Nome: " + this.getName()+ "\n");
		output.append("Posti: \n");
		getMarcmap().forEach((p,v) -> output.append("-" + p.getName() + "    marcatura: " + v + "\n"));
		output.append("Transizioni: \n");
		getValuemap().forEach((p,v) -> output.append("-" + p.getCurrentPlace().getName() + "->" + p.getCurrentTransition().getName() + "    valore: " + v + "\n"));
		return output.toString();
		
	}
	
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != this.getClass() || obj == null) {
			return false;
		}

		final PetriNet other = (PetriNet) obj;
		
		if(!basedNet.equals(other.basedNet)
				|| !marcmap.equals(other.marcmap)
				|| !valuemap.equals(other.valuemap)) return false;
		
		
		return true;
		
	}
	
	public int hashCode(){
		return Objects.hash(name, basedNet, marcmap, valuemap);
	}
	
}
