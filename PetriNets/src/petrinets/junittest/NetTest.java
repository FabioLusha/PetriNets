package petrinets.junittest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import petrinets.net.Net;
import petrinets.net.OrderedPair;
import petrinets.net.Place;
import petrinets.net.Transition;

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
	
	@Test
	void testEqualsObject3() {

		Map<String, Net> mappa = new HashMap<>();
		Set<OrderedPair> fluxrelation = new HashSet<OrderedPair>();
		
		
		Net rete = new Net("reteprova1");
		Net rete2 = new Net("reteprova2");
		
		
		OrderedPair coppia1 = new OrderedPair(new Place("n1"), new Transition("t1"));
		OrderedPair coppia2 = new OrderedPair(new Transition("t1"), new Place("n2"));
		
		fluxrelation.add(coppia1);
		fluxrelation.add(coppia2);
		
		Set<OrderedPair> fluxrelationcopy = new HashSet<OrderedPair>(fluxrelation);
		
		fluxrelationcopy.removeAll(new ArrayList<OrderedPair>(Arrays.asList(coppia1)));
		
		assert !fluxrelationcopy.isEmpty();

	}
	
	@Test
	void mapEqualsTest() {
		Map<Integer, String> a = new HashMap<>();
		Map<Integer, String> b = new HashMap<>();
		
		a.put(1, "a");
		b.put(1, "a");
		
		assert a.equals(b);
	}
	
	@Test
	void mapNotEqualsTest() {
		Map<Integer, String> a = new HashMap<>();
		Map<Integer, String> b = new HashMap<>();
		
		a.put(1, "a");
		b.put(1, "b");
		
		assert !a.equals(b);
	}

}
