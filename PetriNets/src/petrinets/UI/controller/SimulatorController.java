package petrinets.UI.controller;

import java.io.IOException;
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
import systemservices.PropertiesInitializationException;

public class SimulatorController {
	private SimulatorView simView;
	private Starter mainStarter;
	private AbstractSimulatableNetLogic abstractSimulatableNetLogic;
	private SimulatableNet netToSimulate;

	public SimulatorController(View mainView, Starter pcontroller, AbstractSimulatableNetLogic pabstractSimulatableNetLogic) throws IOException, ReflectiveOperationException {
		simView = new SimulatorView(this, mainView);
		mainStarter = pcontroller;
		abstractSimulatableNetLogic = pabstractSimulatableNetLogic;
		simView.mainMenu();

	}
	
	public void mainMenuChoice(int choice) {
		try {
			switch (choice) {
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
		} catch (ReflectiveOperationException e) {
			simView.print(ViewStringConstants.ERR_INTERNAL);
		} catch (IOException e) {
			simView.print(ViewStringConstants.ERR_MSG_DESERIALIZATION_FAILED);
		}catch (PropertiesInitializationException e) {
			simView.print(e.getMessage());
		}
	}

	public void simulatePetriNet() throws IOException, ReflectiveOperationException, PropertiesInitializationException {
		PetriNetConfigurationController petriNetConfigurationController = new PetriNetConfigurationController(simView.getMainView());
		if (petriNetConfigurationController.managePetriNetVis()) {
			//richiedi il nome della rete da simulare;
			String netname = simView.readFromList(abstractSimulatableNetLogic.getSavedGenericNetsNames(PetriNet.class.getName()),ViewStringConstants.INSERT_PETRI_NET_NAME_MSG);
			SimulatableNet net = (SimulatableNet) abstractSimulatableNetLogic.getINet(netname);
			netToSimulate = (SimulatableNet) SerializationUtils.clone(net);
			simulate();
		} else {
			simView.mainMenu();

		}
	}
	
	public void simulatePriorityPetriNet() throws IOException, ReflectiveOperationException, PropertiesInitializationException {
		PriorityPetriNetConfigurationController priorityPetriNetConfigurationController = new PriorityPetriNetConfigurationController(simView.getMainView());
		if (priorityPetriNetConfigurationController.managePriorityPetriNetVis()) {
			//richiedi il nome della rete da simulare;
			    String netname = simView.readFromList(abstractSimulatableNetLogic.getSavedGenericNetsNames(PriorityPetriNet.class.getName()),ViewStringConstants.INSERT_PRIORITY_PETRI_NET_NAME_MSG);
				SimulatableNet net = (SimulatableNet) abstractSimulatableNetLogic.getINet(netname);
				netToSimulate = (SimulatableNet) SerializationUtils.clone(net);
				simulate();

		} else
			simView.mainMenu();
	}

	public void exitWithoutSaving() {
		mainStarter.startView();
	}


	public void simulate() throws IOException, ReflectiveOperationException {
		Collection<Transition> activeTransitions = netToSimulate.getEnabledTransitions();
		 {
		List<String> namesList =
				activeTransitions.stream().
				map(e -> e.getName()).
				collect(Collectors.toList());

		if(activeTransitions.size() != 1){

			String name = simView.readFromList(namesList, ViewStringConstants.INSERT_TRANSITION_MSG);
			netToSimulate.fire(new Transition(name));

		}else{
			Transition toBeFired = activeTransitions.iterator().next();
			simView.print(String.format(ViewStringConstants.MSG_AUTOMATIC_FIRE_TRANSITION, toBeFired ));
			netToSimulate.fire(toBeFired);
		}

		simView.print(ViewStringConstants.MSG_NEW_MARC);
		simView.printMarking(abstractSimulatableNetLogic.getPlaces(netToSimulate), abstractSimulatableNetLogic.getMarc(netToSimulate));

		 if(netToSimulate.getEnabledTransitions().isEmpty()) {
			 simView.print(ViewStringConstants.ERR_CRITICAL_BLOCK);
			 simView.mainMenu();
		 } else if(simView.userInputContinueAdding(ViewStringConstants.ASK_CONTINUE_SIMULATION))
				simulate();
		 	else
				simView.mainMenu();
		}
	}
	

}
