package petrinets.domain;

import petrinets.domain.net.*;

import java.util.*;



public class PetriNet implements SimulatableNet {
	private Map<Place,Integer> marcmap;
	private Map<OrderedPair, Integer>  valuemap;
	private Net basedNet;
	private String name;
	
	public PetriNet(String pname, Net pbasedNet) {

		name = pname;
		basedNet = pbasedNet;
		
		marcmap = new LinkedHashMap<>();
		valuemap = new LinkedHashMap<>();
		
		basedNet.getPlaces().forEach(e -> marcmap.put(e, 0));
		basedNet.getFluxRelation().forEach(e -> valuemap.put(e, 1));

	}
	
	public PetriNet() {}

	public void fire(Transition toFire){
		var pred = this.getBasedNet().getPreviousPlaces(toFire);
		var succ = this.getBasedNet().getSuccessorPlaces(toFire);
		
		for(var placeMark : marcmap.entrySet()) {

			if(pred.contains(placeMark.getKey())) {
				var tmpOrderedPair = new OrderedPair(placeMark.getKey(), toFire);
				placeMark.setValue(placeMark.getValue() - valuemap.get(tmpOrderedPair));

			}else if(succ.contains(placeMark.getKey())) {
				var tmpOrderedPair = new OrderedPair(toFire,placeMark.getKey());
				placeMark.setValue(placeMark.getValue() + valuemap.get(tmpOrderedPair));
			}
		}

		assert markingIsCorrect();

	}

	public Set<Transition> getEnabledTransitions(){
		Set<Transition> transCollection = new HashSet<>();

		for(Transition transition : basedNet.getTransitions()) {
			boolean isEnabled = true;
			for(Place place : basedNet.getPreviousPlaces(transition)) {
				if(  !(marcmap.get(place) >= valuemap.get(new OrderedPair(place,transition))) ) {
					isEnabled = false;
					break;
				}
			}
			if(isEnabled)
				transCollection.add(transition);
		}
		
		return transCollection;
	}

	public String toString() {
		StringBuilder output = new StringBuilder();
		output.append("Nome: " + this.getName()+ "\n");
		output.append("Posti: \n");
		getMarcmap().forEach((p, v) -> output.append("-" + p.getName() + "    marcatura: " + v + "\n"));
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
				|| !valuemap.equals(other.valuemap)
				) return false;

		return true;
		
	}
	
	public int hashCode(){
		return Objects.hash(name, basedNet, marcmap, valuemap);
	}

	private boolean markingIsCorrect() {
		return marcmap.values().stream()
				.reduce(true,
						(aBoolean, anInt) -> aBoolean && (anInt >= 0),
						Boolean::logicalAnd);
	}

	private boolean valueMapIsCorrect() {
		return valuemap.values().stream()
				.reduce(true,
						(aBoolean, anInt) -> aBoolean && (anInt >= 1),
						Boolean::logicalAnd);
	}

	public Map<Place, Integer> getMarcmap() {
		return marcmap;
	}

	public void setMarcmap(Map<Place, Integer> marcmap) {
		this.marcmap = marcmap;
		assert markingIsCorrect();
	}

	public Map<OrderedPair, Integer> getValuemap() {
		return valuemap;
	}

	public void setValuemap(Map<OrderedPair, Integer> valuemap) {
		this.valuemap = valuemap;
		assert valueMapIsCorrect();
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
}
