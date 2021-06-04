package petrinets.junittest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import petrinets.domain.PetriNet;
import petrinets.domain.PriorityPetriNet;
import petrinets.domain.net.Net;
import petrinets.domain.net.OrderedPair;
import petrinets.domain.net.Place;
import petrinets.domain.net.Transition;

class PriorityTest {

	@Test
	void checkEnabledTransitions() {
		
		Net rete = new Net("net1");
		rete.addFluxRelElement(new OrderedPair(new Place("n1"), new Transition("t1")));
		Transition t2 = new Transition("t2");
		rete.addFluxRelElement(new OrderedPair(new Place("n2"), t2));
		
		PetriNet reteDiPetri = new PetriNet("pn1", rete);
		
		PriorityPetriNet reteDiPetriWP = new PriorityPetriNet("pnp1", reteDiPetri);
		reteDiPetriWP.getPriorityMap().replace(t2, 10);

		assert reteDiPetriWP.getEnabledTransitions().iterator().next().equals(t2);
	}

}
