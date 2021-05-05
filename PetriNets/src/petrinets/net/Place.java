package petrinets.net;

import java.util.Objects;

public class Place {
	private String name;
	
	//Costruttuore vuoto poichè è richiesto dallo standard beans per utilizzare XMLSerializer
	public Place(){}
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		//assert name != null;
		this.name = name;
	}


	public Place(String pname) {
		//assert pname != null;
		this.name = pname;
	}
	
	
	@Override
	public boolean equals(Object place) {
		if (place == this) {
			return true;
		}
		if (place.getClass() != this.getClass()) {
			return false;
		}

		final Place other = (Place) place;
		if (!Objects.equals(this.name, other.name)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode(){
		return Objects.hash(name);
	}
	
	public String toString() {
		return name;
	}
}