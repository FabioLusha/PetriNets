package net;

import java.util.Set;

public class Net {
	private String name;
	private Set<Transition> Transitions;
	private Set<Place> Places;

	public Net(String name){
		this.name = name;
	}
}
