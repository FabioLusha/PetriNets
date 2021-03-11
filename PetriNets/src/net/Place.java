package net;

import java.util.Objects;

public class Place {
	private String name;
	
	public Place(String pname) {
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