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
			if(mainController.menagePetriNetVis())
				//richiedi il nome della rete da simulare;
				if(requestNetToSimulateName())
					startSimulation();

				break;
		default:
			simView.mainMenu();
			break;
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
			simView.printToDisplay(ViewStringConstants.ERR_NET_NOT_PRESENT);
			return false;
		}
		
			
	}
	
	private void startSimulation() {
		Collection<Transition> activeTransitions = netToSimulate.getEnabledTransitions();
		
		if(activeTransitions.isEmpty()) {
			simView.printToDisplay(ViewStringConstants.MSG_CRITICAL_BLOCK);
		}else {

			List<String> listNames =
					activeTransitions.stream().
					map(e -> e.getName()).
					collect(Collectors.toList());

			simView.printActiveTransitions(listNames);
			String name = simView.readNotEmpyString(ViewStringConstants.INSERT_TRANSITION_MSG);

			if(activeTransitions.contains(new Transition(name))) {
				netToSimulate.fire(new Transition(name));
				printMarking(netToSimulate);
			}

		}
		
		
	}

	private void printMarking(SimulatableNet netToSimulate) {

	}

}
