package petrinets.domain.net;


import java.util.*;
import java.util.stream.Collectors;



public class Net implements INet {

	private String name;
	private Set<Transition> transitions;
	private Set<Place> places;
	private Set<OrderedPair> fluxRelations;

	// costrutture vuoto senza argomenti per XMLEncoder
	public Net() { }

	public Net(String pname) {
		assert pname != null;
		this.name = pname;
		this.transitions = new LinkedHashSet<>();
		this.places = new LinkedHashSet<>();
		this.fluxRelations = new LinkedHashSet<>();
	}
	
	public List<Place> getPreviousPlaces(Transition trans){
		return fluxRelations.stream().
				filter(e -> e.getCurrentTransition().equals(trans)  && e.getDirection() == OrderedPair.Direction.pt).
				map(e -> e.getCurrentPlace()).
				collect(Collectors.toList());
	}
	
	public List<Place> getSuccessorPlaces(Transition trans){
		return fluxRelations.stream().
				filter(e -> e.getCurrentTransition().equals(trans)  && e.getDirection() == OrderedPair.Direction.tp).
				map(e -> e.getCurrentPlace()).
				collect(Collectors.toList());
	}
	
	public List<Transition> getPreviousTransitions(Place place){
		return fluxRelations.stream().
				filter(e -> e.getCurrentPlace() == place  && e.getDirection() == OrderedPair.Direction.tp).
				map(e -> e.getCurrentTransition()).
				collect(Collectors.toList());
	}
	
	public List<Transition> getSuccessorTransitions(Place place){
		return fluxRelations.stream().
				filter(e -> e.getCurrentPlace() == place  && e.getDirection() == OrderedPair.Direction.pt).
				map(e -> e.getCurrentTransition()).
				collect(Collectors.toList());
	}
	
	
	public boolean isTransition(Transition transition) {
		return transitions.contains(transition);
	}

	public boolean isPlace(Place place) {
		return places.contains(place);
	}

	public boolean addFluxRelElement(OrderedPair pair) {
		if (isTransition(new Transition(pair.getCurrentPlace().getName()))
				|| isPlace(new Place(pair.getCurrentTransition().getName()))) {
			return false;
		} else {
			places.add(pair.getCurrentPlace());
			transitions.add(pair.getCurrentTransition());

			return fluxRelations.add(pair);
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
		
		return fluxRelations.containsAll(other.fluxRelations) && other.fluxRelations.containsAll(fluxRelations);
		
	}
	
	public int hashCode(){
		return Objects.hash(fluxRelations);
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		assert name != null;
		this.name = name;
	}
	
	//I setter servono per essere conformi allo standard Beans
	// per poter usare XMLManager
	public void setTransitions(Set<Transition> transitions) {
		this.transitions = transitions;
	}

	public void setPlaces(Set<Place> places) {
		this.places = places;
	}

	public Set<Transition> getTransitions() {
		return transitions;
	}

	public Set<Place> getPlaces() {
		return places;
	}

	public Set<OrderedPair> getFluxRelation() {
		return fluxRelations;
	}

	public void setFluxRelation(Set<OrderedPair> fluxRelation) {
		this.fluxRelations = fluxRelation;
	}
	
	public boolean invariant() {
		return
				fluxRelations.stream()
				.map(e -> e.getCurrentPlace())
				.collect(Collectors.toSet())
				.equals(places)
				&&
				fluxRelations.stream()
				.map(e -> e.getCurrentTransition())
				.collect(Collectors.toSet())
				.equals(transitions);
	}
}