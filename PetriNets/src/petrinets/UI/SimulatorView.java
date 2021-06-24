package petrinets.UI;

import java.util.List;

import it.unibs.fp.mylib.InputDati;
import it.unibs.fp.mylib.MyMenu;
import petrinets.UI.controller.SimulatorController;

public class SimulatorView {
	SimulatorController simController;
	View mainView;
	
	public SimulatorView(SimulatorController pcontroller,View pmainView) {
		simController = pcontroller;
		mainView = pmainView;
	}
	
	public void mainMenu() {
		MyMenu mainMenu = new MyMenu(ViewStringConstants.SIMULATOR_WELCOME_TITLE, ViewStringConstants.SIMULATOR_WELCOME_OPTIONS);
		simController.mainMenuChoice(mainMenu.scegli());
	}

    public boolean userInputContinueAdding(String message) {
        return mainView.userInputContinueAdding(message);
    }
    public void print(String ErrMsg){
        mainView.printToDisplay(ErrMsg);;
    }
    
    public String readNotEmpyString(String message) {
    	return mainView.readNotEmptyString(message);
    }
    
    public int getIntInput(String message,int min ,int max) {
    	return mainView.getIntInput(message, min, max);
    }

    public int getNotNegativeInt(String message) {
    	return mainView.getNotNegativeInt(message);
    }

	public void printActiveTransitions(List<String> listNames) {
		StringBuilder output = new StringBuilder();
		output.append("Transizioni abilitate: \n");
		for (String t : listNames) {
			output.append("\t" + t.toString() + "\n");
		}
		
		print(output.toString());
	}


	public void printMarking(List<String> places, List<String> marc) {
		String mark = mainView.marcFormatter(places, marc);
		print(mark);
	}
}
