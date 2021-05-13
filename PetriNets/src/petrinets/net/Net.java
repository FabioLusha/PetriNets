package petrinets.net;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;



public class Net implements INet {

	private String name;
	private Set<Transition> transitions;
	private Set<Place> places;
	private Set<OrderedPair> fluxRelation;

	// costrutture vuoto senza argomenti per XMLEncoder
	public Net() { }

	public Net(String pname) {
		assert pname != null;
		this.name = pname;
		this.transitions = new HashSet<>();
		this.places = new HashSet<>();
		this.fluxRelation = new HashSet<>();
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


/*
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
			output.append("\t" + couple.toString() + "\n");
		}

		return output.toString();

	}
	*/
	
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != this.getClass() || obj == null) {
			return false;
		}

		final Net other = (Net) obj;
		
		//Vengono rimossi gli elementi e si verifica che sia vuoto;
		Set<OrderedPair> fluxrelationcopy = new HashSet<OrderedPair>(fluxRelation);
		
		fluxrelationcopy.removeAll(new ArrayList<>(other.fluxRelation));
		other.fluxRelation.removeAll(new ArrayList<>(this.fluxRelation));
		
		return fluxrelationcopy.isEmpty() && other.fluxRelation.isEmpty();
		
	}
	
	public int hashCode(){
		return Objects.hash(fluxRelation);
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		assert name != null;
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
	
	public boolean containsTransition(Transition t) {
		return transitions.contains(t);
	}

}