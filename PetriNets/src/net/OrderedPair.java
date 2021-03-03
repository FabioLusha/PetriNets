package net;


import java.util.*;

enum typePair{
	pt,
	tp};

public class OrderedPair implements Map.Entry<Place,Transition>{
	private typePair type;
	private Place currentPlace;
	private Transition currentTransition;
	

	public OrderedPair(Place pcurrentPlace, Transition pcurrentTransition) {
		this.currentPlace=pcurrentPlace;
		this.currentTransition=pcurrentTransition;
		this.type=typePair.pt;
	}
	
	public OrderedPair(Transition pcurrentTransition,Place pcurrentPlace) {
		this.currentTransition=pcurrentTransition;
		this.currentPlace=pcurrentPlace;
		this.type=typePair.tp;
	}
	
	
	
	
	
	//Metodi non implementati

	@Override
	public Place getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Transition getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Transition setValue(Transition value) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
