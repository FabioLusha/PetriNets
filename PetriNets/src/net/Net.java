package net;

import java.util.HashSet;
import java.util.Set;

public class Net {
	private String name;
	private Set<Transition> Transitions;
	private Set<Place> Places;

	public Net(String name) {
		this.name = name;
		Transitions = new HashSet<>();
		Places = new HashSet<>();
	}

	public boolean isTransition(Transition transition) {
		return Transitions.contains(transition);
	}

	public String getName() {
		return name;
	}

	public Set<Transition> getTransitions() {
		return Transitions;
	}

	public Set<Place> getPlaces() {
		return Places;
	}

	public boolean isPlace(Place place) {
		return Places.contains(place);
	}

	public boolean addPlace(Place p) {
		return Places.add(p);
	}

	public boolean addTransition(Transition t) {
		return Transitions.add(t);
	}
	
	public String toString() {
		StringBuilder output = new StringBuilder();
		output.append("Rete: " + name +" \n");
		output.append("Posti: \n");
		for(Place p : Places) {
			output.append("\t" + p.toString() + "\n");
		}
		output.append("Transizioni: \n");
		for(Transition t : Transitions) {
			output.append("\t" + t.toString() +"\n");
		}
		
		return output.toString();
		
	}

}