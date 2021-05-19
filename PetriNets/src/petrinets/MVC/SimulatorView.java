package petrinets.MVC;

import it.unibs.fp.mylib.MyMenu;
import petrinets.MVC.controller.SimulatorController;

public class SimulatorView {
	SimulatorController simController;
	View mainView;
	
	public SimulatorView(SimulatorController pcontroller) {
		simController = pcontroller;
	}
	
	public void mainMenu() {
		MyMenu mainMenu = new MyMenu(ViewStringConstants.SIMULATOR_WELCOME_TITLE, ViewStringConstants.SIMULATOR_WELCOME_OPTIONS);
		simController.mainMenuChoice(mainMenu.scegli());
	}
	
	

}
