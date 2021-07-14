package petrinets.domain.petrinet;

import petrinets.domain.net.OrderedPair;
import petrinets.domain.net.Place;
import petrinets.domain.net.Transition;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class PriorityPetriNet implements SimulatableNet {
    private PetriNet basedPetriNet;
    private Map<Transition, Integer> priorityMap;
    private String name;

    //Costruttore vuoto per essere conforme allo standard Beans e poter utilizzare
    //XMLEncoder correttamente
    public PriorityPetriNet(){};

    public PriorityPetriNet(String pname, PetriNet pPetriNet){

        name = pname;
        basedPetriNet = pPetriNet;

        priorityMap = new LinkedHashMap<>();
        basedPetriNet.getBasedNet().getTransitions().forEach(e -> priorityMap.put(e,1));

    }

    public PetriNet getBasedPetriNet() {
        return basedPetriNet;
    }

    public void setBasedPetriNet(PetriNet basedPetriNet) {
        this.basedPetriNet = basedPetriNet;
    }

    public Map<Transition, Integer> getPriorityMap() {
        return priorityMap;
    }

    public void setPriorityMap(Map<Transition, Integer> priorityMap) {
        this.priorityMap = priorityMap;
        
        assert priorityMapIsCorrect();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Set<Place> getPlaces() {
        return basedPetriNet.getPlaces();
    }

    @Override
    public Set<Transition> getTransitions() {
        return basedPetriNet.getTransitions();
    }

    @Override
    public Set<OrderedPair> getFluxRelation() {
        return basedPetriNet.getFluxRelation();
    }

    @Override
    public void fire(Transition toFire) {
        //il comportamento dello scatto della transizione è identico a quella di una rete di petri normale
       basedPetriNet.fire(toFire);
    }
    @Override
    public Set<Transition> getEnabledTransitions() {
        Set<Transition> petriNetEnabled = basedPetriNet.getEnabledTransitions();
        int maxPriority = priorityMap.entrySet().stream()
                .filter(e -> petriNetEnabled.contains(e.getKey()))
                .map(Map.Entry::getValue)
                .max(Integer::compare).orElse(-1); //-1 nel caso la lista fosse vuota

        petriNetEnabled.removeIf(e -> priorityMap.get(e) != maxPriority);
        return petriNetEnabled;
    }


    @Override
    public int getFluxRelValue(OrderedPair pair) {
        return basedPetriNet.getFluxRelValue(pair);
    }

    @Override
    public int getMarcValue(Place n1) {
        return basedPetriNet.getMarcValue(n1);
    }

    @Override
    public void changeMarc(Place n1, int newValue) {
        basedPetriNet.changeMarc(n1,newValue);
    }

    @Override
    public void changeFluxRel(OrderedPair pair, int newFluxRelValue) {
        basedPetriNet.changeFluxRel(pair,newFluxRelValue);
    }


    public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != this.getClass() || obj == null) {
			return false;
		}

		final PriorityPetriNet other = (PriorityPetriNet) obj;
		
		
		
		if(!basedPetriNet.equals(other.basedPetriNet)
				|| !priorityMap.equals(other.priorityMap))
		   return false;

		return true;
		
	}
    
	public int hashCode(){
		return Objects.hash(name, basedPetriNet, priorityMap);
	}
	
	private boolean priorityMapIsCorrect() {
		return priorityMap.values().stream()
				.reduce(true,
						(aBoolean, anInt) -> aBoolean && (anInt >= 1),
						Boolean::logicalAnd);
	}

	public void changePriorityTransition(Transition t1, int newPriorityValue) {
		assert priorityMap.containsKey(t1);
		assert newPriorityValue >= 1;
		
		priorityMap.replace(t1, newPriorityValue);
	}

	public int getPriority(Transition t1) {
		assert priorityMap.containsKey(t1);
		return priorityMap.get(t1);
	}
}
