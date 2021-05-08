package petrinets.MVC;

import it.unibs.fp.mylib.*;

import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.List;

public class View {
    private static final PrintWriter outputStream = new PrintWriter(new BufferedOutputStream(System.out));
    private Controller controller;
    
    public View(Controller controller){
        this.controller = controller;
        //this.outputStream = out;
    }

    public void mainMenu() {
        MyMenu mainMenu = new MyMenu(ViewStringConstants.WELCOME_MESSAGE, ViewStringConstants.MAIN_OPTIONS);
        controller.menuChoice(mainMenu.scegli());
    }


    public void initializeNet() {
        String netName = InputDati.leggiStringaNonVuota(ViewStringConstants.INSERT_NET_NAME_MSG).trim();
        controller.manageNetCreation(netName);
    }
    
    public void visualizeNets(List<String> names) {
    	printToDisplay(ViewStringConstants.AVAILABLE_NETS);
    	names.forEach(e -> printToDisplay(" - " + e));
    	controller.requestPrintNet(InputDati.leggiStringaNonVuota(ViewStringConstants.INSERT_NET_TO_VIEW).toString().trim());
 
    }

    public void printNet(String name, List<String> places, List<String> transitions, List<Pair<String,String>> fluxRelations){
      StringBuilder output = new StringBuilder();
		output.append("Rete: " + name + " \n");
		output.append("Posti: \n");
		for (String p : places) {
			output.append("\t" + p.toString() + "\n");
		}
		output.append("Transizioni: \n");
		for (String t : transitions) {
			output.append("\t" + t.toString() + "\n");
		}
      output.append("Relazioni di flusso: \n");
      for(Pair<String,String> elem : fluxRelations) {
          output.append("\t (" + elem.getFirst() + " , " + elem.getSecond() + ") \n");
      }
      printToDisplay(output.toString());
    }
    
    public void addFluxElement() {
    	String placename = InputDati.leggiStringaNonVuota(ViewStringConstants.INSERT_PLACE_MSG).trim();
    	String transitionname = InputDati.leggiStringaNonVuota(ViewStringConstants.INSERT_TRANSITION_MSG).trim();
    	int direction = InputDati.leggiIntero(ViewStringConstants.INSERT_DIRECTION_MSG
				+ String.format(ViewStringConstants.FLUX_DIRECTION_MSG, 0, placename, transitionname)
				+ String.format(ViewStringConstants.FLUX_DIRECTION_MSG, 1, transitionname, placename) + "\n > ",
				0, 1);
    	controller.addFluxRel(placename, transitionname, direction);
    }
    
    public void userInputContinueAdding() {
    	controller.continueAddingElement(InputDati.yesOrNo(ViewStringConstants.CONTINUE_ADDING_QUESTION));
    }
    
    public void saveMenu() {
    	MyMenu netSaving = new MyMenu(ViewStringConstants.NET_SAVING_MENU, ViewStringConstants.NET_SAVING_MENU_OPTIONS);
    	controller.userSavingChoice(netSaving.scegli());
	}
    
    public void printToDisplay(String ErrMsg){
        outputStream.println(ErrMsg);
        outputStream.flush();
    }
    
    public String getInput(String message) {
    	return InputDati.leggiStringaNonVuota(message);
    }


}

