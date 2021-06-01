package petrinets.domain;

import petrinets.domain.net.OrderedPair;
import petrinets.domain.net.Place;
import petrinets.domain.net.Transition;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class PriorityPetriNet implements SimulatableNet {
    private PetriNet basePatriNet;
    private Map<Transition, Integer> priorityMap;
    private String name;

    public PriorityPetriNet(String pname, PetriNet pPetriNet){

        name = pname;
        basePatriNet = pPetriNet;

        priorityMap = new LinkedHashMap<>();
        basePatriNet.getBasedNet().getTransitions().forEach(e -> priorityMap.put(e,1));

    }

    public PetriNet getBasePatriNet() {
        return basePatriNet;
    }

    public void setBasePatriNet(PetriNet basePatriNet) {
        this.basePatriNet = basePatriNet;
    }

    public Map<Transition, Integer> getPriorityMap() {
        return priorityMap;
    }

    public void setPriorityMap(Map<Transition, Integer> priorityMap) {
        this.priorityMap = priorityMap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Set<Place> getPlaces() {
        return basePatriNet.getPlaces();
    }

    @Override
    public Set<Transition> getTransitions() {
        return basePatriNet.getTransitions();
    }

    @Override
    public Set<OrderedPair> getFluxRelation() {
        return basePatriNet.getFluxRelation();
    }

    @Override
    public void fire(Transition toFire) {
        //TODO

    }
    @Override
    public Set<Transition> getEnabledTransitions() {
        //TODO
        return null;
    }

    @Override
    public Map<Place, Integer> getMarcmap() {
        return basePatriNet.getMarcmap();
    }
}
