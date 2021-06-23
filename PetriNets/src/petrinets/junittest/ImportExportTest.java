package petrinets.junittest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import petrinets.domain.PetriNet;
import petrinets.domain.PriorityPetriNet;
import petrinets.domain.net.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.*;

class ImportExportTest {

	@Test
	void Importtest() throws IOException {
		Net testNet = new Net("net1");
		testNet.addFluxRelElement(new OrderedPair(new Place("p1") , new Transition("t1")));
		PetriNet testPetriNet = new PetriNet("petrinet1", testNet);
		PriorityPetriNet testPriorityPetriNet = new PriorityPetriNet("prioritypetrinet1" , testPetriNet);
		
		File file = new File("filename.txt");
		file.getParentFile().mkdirs();
		FileWriter writer = new FileWriter(file);
	}

}
