package net;


import java.util.*;



public class OrderedPair implements Map.Entry<Place,Transition>{

	public enum typePair{
		pt,
		tp;

		public static typePair ordinalToType(int ordinal){
			if ( typePair.pt.ordinal() == ordinal ) return pt;
			else return tp;
		}
	}

	private typePair type;
	private Place currentPlace;
	private Transition currentTransition;
	
	public OrderedPair(){};

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

	public OrderedPair(Place currentPlace, Transition currentTransition, typePair type) {
		this.type = type;
		this.currentPlace = currentPlace;
		this.currentTransition = currentTransition;
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

	public typePair getType() {
		return type;
	}

	public void setType(typePair type) {
		this.type = type;
	}

	public Place getCurrentPlace() {
		return currentPlace;
	}

	public void setCurrentPlace(Place currentPlace) {
		this.currentPlace = currentPlace;
	}

	public Transition getCurrentTransition() {
		return currentTransition;
	}

	public void setCurrentTransition(Transition currentTransition) {
		this.currentTransition = currentTransition;
	}

	@Override
	public boolean equals(Object obj){
			if (obj == this) {
				return true;
			}
			if (obj.getClass() != this.getClass()) {
				return false;
			}

			final OrderedPair other = (OrderedPair) obj;
			if (!Objects.equals(this.type.ordinal(), other.type.ordinal()) || !Objects.equals(this.currentPlace, other.currentPlace)
					|| !Objects.equals(this.currentTransition, other.currentTransition)) {
				return false;
			}
			return true;
	}

	@Override
	public int hashCode(){
		return Objects.hash(this.currentPlace, this.currentTransition, this.type.ordinal());
	}

	@Override
	public String toString(){
		String format = "( %s -> %s )";

		if(type == typePair.pt) return String.format(format, currentPlace, currentTransition);
		else return String.format(format, currentTransition, currentPlace);
	}


}