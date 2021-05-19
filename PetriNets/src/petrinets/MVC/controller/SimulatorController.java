package petrinets.MVC.controller;

import petrinets.MVC.SimulatorView;

public class SimulatorController {
	private SimulatorView simView;
	private Controller mainController;

	public SimulatorController() {
		simView = new SimulatorView(this);
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
				break;
		default:
			simView.mainMenu();
			break;
		}
	}

	private void exitWithoutSaving() {
		System.exit(0);
	}
}
