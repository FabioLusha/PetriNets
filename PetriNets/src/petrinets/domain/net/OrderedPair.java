package petrinets.domain.net;


import java.io.Serializable;
import java.util.*;



public class OrderedPair implements Serializable {
	//Enum che stabilisce la direzione tra i due elementi
	public static enum Direction {
		pt,
		tp;
		
		//Metodo che converte un intero corrispondente alla posizione dell'elemento nell' elemento stesso
		public static Direction ordinalToType(int ordinal){
			//assert ordinal <= 1 && ordinal >= 0;
			if ( Direction.pt.ordinal() == ordinal ) return pt;
			else return tp;
		}
	
	}

	private Direction dir;
	private Place currentPlace;
	private Transition currentTransition;
	
	//Costruttuore vuoto poich� � richiesto dallo standard beans per utilizzare XMLSerializer
	public OrderedPair(){};

	public OrderedPair(Place pcurrentPlace, Transition pcurrentTransition) {
		this.currentPlace=pcurrentPlace;
		this.currentTransition=pcurrentTransition;
		this.dir= Direction.pt;
	}
	
	public OrderedPair(Transition pcurrentTransition,Place pcurrentPlace) {
		this.currentTransition=pcurrentTransition;
		this.currentPlace=pcurrentPlace;
		this.dir= Direction.tp;
	}

	public OrderedPair(Place currentPlace, Transition currentTransition, Direction type) {
		this.dir = type;
		this.currentPlace = currentPlace;
		this.currentTransition = currentTransition;
	}


	public Direction getDirection() {
		return dir;
	}

	public void setDirection(Direction type) {
		this.dir = type;
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
			if (!Objects.equals(this.dir.ordinal(), other.dir.ordinal()) || !Objects.equals(this.currentPlace, other.currentPlace)
					|| !Objects.equals(this.currentTransition, other.currentTransition)) {
				return false;
			}
			return true;
	}

	@Override
	public int hashCode(){
		return Objects.hash(this.currentPlace, this.currentTransition, this.dir.ordinal());
	}

	@Override
	public String toString(){
		String format = "( %s -> %s )";

		if(dir == Direction.pt) return String.format(format, currentPlace, currentTransition);
		else return String.format(format, currentTransition, currentPlace);
	}


}