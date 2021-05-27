package petrinets.MVC;

import it.unibs.fp.mylib.*;
import petrinets.MVC.controller.Controller;

import java.io.PrintWriter;
import java.util.List;

public class View {
    private PrintWriter outputStream;
    private Controller controller;
    
    public View(Controller controller, PrintWriter out){
        this.controller = controller;
        this.outputStream = out;
    }
    
    public void loginMenu() {
    	MyMenu logMenu = new MyMenu(ViewStringConstants.WELCOME_MESSAGE, ViewStringConstants.LOGIN_MENU_OPTIONS);
    	controller.logMenuChoice(logMenu.scegli());
    }

    public void mainMenu() {
        MyMenu mainMenu = new MyMenu(ViewStringConstants.WELCOME_MESSAGE, ViewStringConstants.MAIN_OPTIONS);
        controller.mainMenuChoice(mainMenu.scegli());
    }


    public void initializeNet() {
        String netName = InputDati.leggiStringaNonVuota(ViewStringConstants.INSERT_NET_NAME_MSG).trim();
        controller.manageNetCreation(netName);
    }
    
    public void visualizeNets(List<String> names) {
    	printToDisplay(ViewStringConstants.AVAILABLE_NETS);
    	names.forEach(e -> printToDisplay(" - " + e));
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

    public void petriNetMenu(){
        MyMenu mymenu = new MyMenu(ViewStringConstants.PETRI_NET_MENU_TITLE, ViewStringConstants.CHANGE_PETRI_NET_OPTIONS);
        controller.petriNetMenuChoice(mymenu.scegli());
    }

    public void printPetriNet(String name, List<String> places, List<String> marc, List<String> transitions,
                              List<Pair<String,String>> fluxRelations, List<String> cost){
        StringBuilder output = new StringBuilder();
        output.append("Rete: " + name + " \n");
        output.append(marcFormatter(places, marc));
        output.append("Transizioni: \n");
        for (String t : transitions) {
            output.append("\t" + t.toString() + "\n");
        }
        output.append(fluxrelFormatter(fluxRelations, cost));
        printToDisplay(output.toString());
    }

    public String marcFormatter(List<String> places, List<String> marcs) {
    	StringBuilder output =  new StringBuilder();
    	output.append("Posti: \n");
        for(int i = 0; i < places.size(); i++){
            output.append("\t" + places.get(i).toString())
                    .append("\t" + "marcatura: " + marcs.get(i) + "\n");
        }
        return output.toString();
    }
    
    public String fluxrelFormatter(List<Pair<String,String>> fluxRelations, List<String> cost) {
    	  StringBuilder output =  new StringBuilder();
    	  output.append("Relazioni di flusso: \n");
          for(int i = 0; i < fluxRelations.size(); i++) {
              output.append("\t (" +  fluxRelations.get(i).getFirst() + " , " + fluxRelations.get(i).getSecond() + ")")
                      .append("\t" + "costo: " + cost.get(i) + "\n");
          }
          return output.toString();
    }

    public void printToDisplay(String ErrMsg){
        outputStream.println(ErrMsg);
        outputStream.flush();
    }

    public String readNotEmptyString(String message) {
        return InputDati.leggiStringaNonVuota(message).trim();
    }

    public int getIntInput(String message,int min ,int max) {
        return InputDati.leggiIntero(message, min, max);
    }

    public int getNotNegativeInt(String message) {
        return InputDati.leggiInteroNonNegativo(message);
    }
    
    
}

