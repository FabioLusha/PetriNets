package net;

public class Transition {
	private String name;
	
	public Transition(String pname) {
		this.name = pname;
	}
	
	public String toString() {
		return name;
	}
	
	public String getName() {
		return this.name;
	}
	
	@Override
	public boolean equals(Object t) {
		if (t == this) { 
            return true; 
        } 
		if (t.getClass() != this.getClass()) {
            return false;
        }
		
		final Transition other = (Transition) t;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return true;
	}
}
