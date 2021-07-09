package petrinets.UI.controller;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.SerializationUtils;
import petrinets.UI.view.SimulatorView;
import petrinets.UI.view.View;
import petrinets.UI.view.ViewStringConstants;
import petrinets.domain.AbstractSimulatableNetLogic;
import petrinets.domain.Model;
import petrinets.domain.petrinet.PetriNet;
import petrinets.domain.petrinet.PriorityPetriNet;
import petrinets.domain.petrinet.SimulatableNet;
import petrinets.domain.net.Transition;

public class SimulatorController {
	private SimulatorView simView;
	private Starter mainStarter;
	private AbstractSimulatableNetLogic abstractSimulatableNetLogic;
	private SimulatableNet netToSimulate;

	public SimulatorController(View mainView, Starter pcontroller, AbstractSimulatableNetLogic pabstractSimulatableNetLogic) {
		simView = new SimulatorView(this, mainView);
		mainStarter = pcontroller;
		abstractSimulatableNetLogic = pabstractSimulatableNetLogic;
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
		case 2:
			simulatePriorityPetriNet();
			
			break;
		default:
			simView.mainMenu();
			break;
		}
	}

	public void simulatePetriNet() {
		if (mainStarter.managePetriNetVis()) {
			//richiedi il nome della rete da simulare;
			String netname = simView.readFromList(abstractSimulatableNetLogic.getSavedGenericNetsNames(PetriNet.class.getName()),ViewStringConstants.INSERT_PETRI_NET_NAME_MSG);

			mainStarter.requestPrintPetriNet(netname);
			SimulatableNet net = (SimulatableNet) abstractSimulatableNetLogic.getINet(netname);
			netToSimulate = (SimulatableNet) SerializationUtils.clone(net);
			simulate();
		} else {
			simView.mainMenu();

		}
	}
	
	public void simulatePriorityPetriNet() {
		if (mainStarter.managePriorityPetriNetVis()) {
			//richiedi il nome della rete da simulare;
			String netname = simView.readFromList(abstractSimulatableNetLogic.getSavedGenericNetsNames(PriorityPetriNet.class.getName()),ViewStringConstants.INSERT_PRIORITY_PETRI_NET_NAME_MSG);

				mainStarter.requestPrintPriorityPetriNet(netname);
				SimulatableNet net = (SimulatableNet) abstractSimulatableNetLogic.getINet(netname);
				netToSimulate = (SimulatableNet) SerializationUtils.clone(net);
				simulate();

		} else
			simView.mainMenu();
	}

	public void exitWithoutSaving() {
		mainStarter.startView();
	}


	public void simulate() {
		Collection<Transition> activeTransitions = netToSimulate.getEnabledTransitions();
		
		if(activeTransitions.isEmpty()) {
			simView.print(ViewStringConstants.ERR_CRITICAL_BLOCK);
			simView.mainMenu();
		}else {
			List<String> listNames =
					activeTransitions.stream().
					map(e -> e.getName()).
					collect(Collectors.toList());

			if(activeTransitions.size() != 1){
				//TODO Utilizzare il metodo readFromList della classe view
				while(true) {
					simView.printActiveTransitions(listNames);
					String name = simView.readNotEmpyString(ViewStringConstants.INSERT_TRANSITION_MSG);

					if (activeTransitions.contains(new Transition(name))) {
						netToSimulate.fire(new Transition(name));
						break;
					} else {

						simView.print(ViewStringConstants.ERR_ELEMENT_NAME_DOES_NOT_EXSIST);
						continue;
					}
				}
			}else{
				Transition toBeFired = activeTransitions.iterator().next();
				simView.print(String.format(ViewStringConstants.MSG_AUTOMATIC_FIRE_TRANSITION, toBeFired ));
				netToSimulate.fire(toBeFired);
			}

			simView.print(ViewStringConstants.MSG_NEW_MARC);
			simView.printMarking(abstractSimulatableNetLogic.getPlaces(netToSimulate), abstractSimulatableNetLogic.getMarc(netToSimulate));
			if(simView.userInputContinueAdding(ViewStringConstants.ASK_CONTINUE_SIMULATION))
				simulate();
			else
				simView.mainMenu();
		}
	}
	

}
