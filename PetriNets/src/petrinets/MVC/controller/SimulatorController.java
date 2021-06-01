package petrinets.MVC.controller;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.SerializationUtils;

import petrinets.MVC.*;
import petrinets.net.SimulatableNet;
import petrinets.net.Transition;

public class SimulatorController {
	private SimulatorView simView;
	private Controller mainController;
	private Model model;

	private SimulatableNet netToSimulate;

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
			//Simula rete di Petri
			simulatePetriNet();

			break;
		default:
			simView.mainMenu();
			break;
		}
	}

	private void simulatePetriNet() {
		if(mainController.menagePetriNetVis())
			//richiedi il nome della rete da simulare;
			if(mainController.managePetriNetCreation();) {
				simulate();

			}
	}

	private void exitWithoutSaving() {
		System.exit(0);
	}
	
	private boolean requestNetToSimulateName() {

		String input = simView.readNotEmpyString(ViewStringConstants.INSERT_NET_NAME_MSG);

		if(model.containsSimulatableNet(input)) {
			SimulatableNet net = (SimulatableNet) model.getNet(input);
			netToSimulate = (SimulatableNet) SerializationUtils.clone(net);
			return true;
		}else {
			simView.print(ViewStringConstants.ERR_NET_NOT_PRESENT);
			return false;
		}
		
			
	}
	
	private void simulate() {
		Collection<Transition> activeTransitions = netToSimulate.getEnabledTransitions();
		
		if(activeTransitions.isEmpty()) {
			simView.print(ViewStringConstants.ERR_CRITICAL_BLOCK);
		}else {
			List<String> listNames =
					activeTransitions.stream().
					map(e -> e.getName()).
					collect(Collectors.toList());

			simView.printActiveTransitions(listNames);
			String name = simView.readNotEmpyString(ViewStringConstants.INSERT_TRANSITION_MSG);

			if(activeTransitions.contains(new Transition(name))) {
				netToSimulate.fire(new Transition(name));
			} else{
				simView.print(ViewStringConstants.ERR_ELEMENT_NAME_DOES_NOT_EXSIST);
			}
		}
	}

	private void printMarking(SimulatableNet netToSimulate) {

	}

}
