package petrinets.domain;

import petrinets.domain.net.OrderedPair;
import petrinets.domain.net.Place;
import petrinets.domain.net.Transition;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class PriorityPetriNet extends PetriNet implements SimulatableNet {
    private PetriNet basePatriNet;
    private Map<Transition, Integer> priorityMap;
    private String name;

    //Costruttore vuoto per essere conforme allo standard Beans e poter utilizzare
    //XMLEncoder correttamente
    public PriorityPetriNet(){};

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
        //il comportamento dello scatto della transizione è identico a quella di una rete di petri normale
       basePatriNet.fire(toFire);
    }
    @Override
    public Set<Transition> getEnabledTransitions() {
        Set<Transition> petriNetEnabled = basePatriNet.getEnabledTransitions();
        int maxPriority = priorityMap.entrySet().stream()
                .filter(e -> petriNetEnabled.contains(e.getKey()))
                .map(Map.Entry::getValue)
                .max(Integer::compare).orElse(-1); //-1 nel caso la lista fosse vuota

        petriNetEnabled.removeIf(e -> priorityMap.get(e) != maxPriority);
        return petriNetEnabled;
    }

    @Override
    public Map<Place, Integer> getMarcmap() {
        return basePatriNet.getMarcmap();
    }

    @Override
    public Map<OrderedPair, Integer> getValuemap() {
        return basePatriNet.getValuemap();
    }
}
