package petrinets.net;

import java.util.Objects;

public class Transition {
	private String name;

	public Transition(){}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		//assert name != null;
		this.name = name;
	}

	public Transition(String pname) {
		//assert pname != null;
		this.name = pname;
	}
	
	public String toString() {
		return name;
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
