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

	public SimulatorController(View mainView, Controller pcontroller, Model pmodel) {
		simView = new SimulatorView(this, mainView);
		mainController = pcontroller;
		model = pmodel;
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
		if (mainController.menagePetriNetVis()) {
			//richiedi il nome della rete da simulare;
			String netname = simView.readNotEmpyString(ViewStringConstants.INSERT_PETRI_NET_NAME_MSG);
			if (model.containsPetriNet(netname)) {
				mainController.requestPrintPetriNet(netname);
				SimulatableNet net = (SimulatableNet) model.getNet(netname);
				netToSimulate = (SimulatableNet) SerializationUtils.clone(net);
				simulate();
			} else {
				simView.print(ViewStringConstants.ERR_NET_NOT_PRESENT);


			}
		}
	}

	private void exitWithoutSaving() {
		System.exit(0);
	}


	private void simulate() {
		Collection<Transition> activeTransitions = netToSimulate.getEnabledTransitions();
		
		if(activeTransitions.isEmpty()) {
			simView.print(ViewStringConstants.ERR_CRITICAL_BLOCK);
			simView.mainMenu();
		}else {
			List<String> listNames =
					activeTransitions.stream().
					map(e -> e.getName()).
					collect(Collectors.toList());


			simView.printActiveTransitions(listNames);

			while(true) {
				String name = simView.readNotEmpyString(ViewStringConstants.INSERT_TRANSITION_MSG);

				if (activeTransitions.contains(new Transition(name))) {
					netToSimulate.fire(new Transition(name));
					simView.printMarking(model.getPlaces(netToSimulate), model.getMarc(netToSimulate));
					break;
				} else {
					simView.print(ViewStringConstants.ERR_ELEMENT_NAME_DOES_NOT_EXSIST);
					continue;
				}
			}

			if(simView.userInputContinueAdding(ViewStringConstants.ASK_CONTINUE_SIMULATION))
				simulate();
			else
				simView.mainMenu();
		}
	}

}
