package net;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class NetTest {

	@Test
	void testEqualsObject() {

		Map<String, Net> mappa = new HashMap<>();

		Net rete = new Net("reteprova1");
		Net rete2 = new Net("reteprova2");

		OrderedPair coppia1 = new OrderedPair(new Place("n1"), new Transition("t1"));

		rete.addFluxRelElement(coppia1);
		rete2.addFluxRelElement(coppia1);

		mappa.put(rete2.getName(), rete2);

		assert mappa.containsValue(rete);
	}

	@Test
	void testEqualsObject2() {

		Map<String, Net> mappa = new HashMap<>();

		Net rete = new Net("reteprova1");
		Net rete2 = new Net("reteprova2");

		OrderedPair coppia1 = new OrderedPair(new Place("n1"), new Transition("t1"));
		OrderedPair coppia2 = new OrderedPair(new Transition("t1"), new Place("n2"));

		rete.addFluxRelElement(coppia1);
		rete2.addFluxRelElement(coppia2);

		mappa.put(rete2.getName(), rete2);

		assert !mappa.containsValue(rete);
	}

}
