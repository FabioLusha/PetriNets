package petrinets.UI.view;

import java.io.IOException;
import java.util.List;

import it.unibs.fp.mylib.MyMenu2;
import petrinets.UI.controller.SimulatorController;

public class SimulatorView {
	SimulatorController simController;
	View mainView;
	
	public SimulatorView(SimulatorController pcontroller,View pmainView) {
		simController = pcontroller;
		mainView = pmainView;
	}
	
	public void mainMenu() {
		MyMenu2 mainMenu = new MyMenu2(mainView.getBufferedInputDati(), ViewStringConstants.SIMULATOR_WELCOME_TITLE, ViewStringConstants.SIMULATOR_WELCOME_OPTIONS);
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

	public String readFromList(List<String> listOfOptions, String message){
		return mainView.readFromList(listOfOptions,message);
	}

	public View getMainView(){
		return mainView;
	}

}
