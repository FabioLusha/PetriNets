package net;

import java.util.Objects;

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
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
	}

	@Override
	public int hashCode(){
		return Objects.hash(name);
	}
}
