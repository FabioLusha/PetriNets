package net;

import java.beans.ExceptionListener;
import java.beans.XMLEncoder;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Net {

	private String name;
	private Set<Transition> transitions;
	private Set<Place> places;
	private Set<OrderedPair> fluxRelation = new HashSet<OrderedPair>();

	// costrutture vuoto senza argomenti per XMLEncoder
	public Net() {
	};

	public Net(String name) {
		this.name = name;
		transitions = new HashSet<>();
		places = new HashSet<>();
	}

	public boolean isTransition(Transition transition) {
		return transitions.contains(transition);
	}

	public boolean isPlace(Place place) {
		return places.contains(place);
	}

	public boolean addPlace(Place p) {
		return places.add(p);
	}

	public boolean addTransition(Transition t) {
		return transitions.add(t);
	}

	public boolean addFluxRelElement(OrderedPair pair) {

		if (isTransition(new Transition(pair.getCurrentPlace().getName()))
				|| isPlace(new Place(pair.getCurrentTransition().getName()))) {
			return false;
		} else {
			places.add(pair.getCurrentPlace());
			transitions.add(pair.getCurrentTransition());

			return fluxRelation.add(pair);
		}
	}



	public String toString() {
		StringBuilder output = new StringBuilder();
		output.append("Rete: " + name + " \n");
		output.append("Posti: \n");
		for (Place p : places) {
			output.append("\t" + p.toString() + "\n");
		}
		output.append("Transizioni: \n");
		for (Transition t : transitions) {
			output.append("\t" + t.toString() + "\n");
		}
		output.append("Relazioni di flusso presenti: \n");
		for(OrderedPair couple : fluxRelation) {
			output.append(couple.toString() + "\n");
		}

		return output.toString();

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Transition> getTransitions() {
		return transitions;
	}

	public void setTransitions(Set<Transition> transitions) {
		this.transitions = transitions;
	}

	public Set<Place> getPlaces() {
		return places;
	}

	public void setPlaces(Set<Place> places) {
		this.places = places;
	}

	public Set<OrderedPair> getFluxRelation() {
		return fluxRelation;
	}

	public void setFluxRelation(Set<OrderedPair> fluxRelation) {
		this.fluxRelation = fluxRelation;
	}

}