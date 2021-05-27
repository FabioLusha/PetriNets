package petrinets.MVC.controller;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.SerializationUtils;

import petrinets.MVC.Model;
import petrinets.MVC.SimulatorView;
import petrinets.MVC.View;
import petrinets.MVC.ViewStringConstants;
import petrinets.net.Simulatable;
import petrinets.net.Transition;

public class SimulatorController {
	private SimulatorView simView;
	private Controller mainController;
	private Model model;
	private Simulatable netSimulating;

	public SimulatorController(View mainView) {
		simView = new SimulatorView(this, mainView);
		simView.mainMenu();
	}
	
	public void mainMenuChoice(int choice) {
		switch(choice) {
		case 0:
			exitWithoutSaving();
			break;
		case 1:
			if(mainController.menagePetriNetVis())
				//richiedi il nome della rete da simulare;
				requestNetToSimulateName();
				break;
		default:
			simView.mainMenu();
			break;
		}
	}
	
	public void netToSimulate() {
		
	}

	private void exitWithoutSaving() {
		System.exit(0);
	}
	
	private void requestNetToSimulateName() {
		var input = simView.getInput(ViewStringConstants.INSERT_NET_NAME_MSG);
		if(model.containsSimulatableNet(input)) {
			Simulatable net = (Simulatable) model.getNet(input);
			netSimulating = (Simulatable) SerializationUtils.clone(net);
			startSimulation();
		}else {
			simView.printToDisplay(ViewStringConstants.ERR_NET_NOT_PRESENT);
		}
		
			
	}
	
	private void startSimulation() {
		Collection<Transition> activeTransitions = netSimulating.getActiveTransitions();
		
		if(activeTransitions.isEmpty()) {
			simView.printToDisplay(ViewStringConstants.MSG_CRITICAL_BLOCK);
		}else {
			List<String> listNames = activeTransitions.stream().
					map(e-> e.getName()).
					collect(Collectors.toList());
			simView.printActiveTransitions(listNames);
			String name = simView.getInput(ViewStringConstants.INSERT_TRANSITION_MSG);
			if(activeTransitions.contains(new Transition(name))) {
				netSimulating.simulate(new Transition(name));
				mainController.requestPrintPetriNet(netSimulating.getName());
			}
		}
		
		
	}
	
	public boolean menageSimulatableNetVis() {
		if (model.getSavedPetriNetsNames().isEmpty()) {
			simView.printToDisplay(ViewStringConstants.ERR_SAVED_NET_NOT_PRESENT);
			return false;
		} else {
			simView.visualizeNets(model.getSavedPetriNetsNames());
			return true;
		
		}
	}
	
	
}
