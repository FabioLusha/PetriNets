package petrinets.UI.view;

import it.unibs.fp.mylib.*;
import petrinets.UI.controller.Starter;
import petrinets.UI.Pair;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

public class View {
    private PrintWriter outputStream;
    private BufferedInputDati bufferedInputDati;
    private Starter starter;
    
    public View(Starter starter, PrintWriter out){
        this.starter = starter;
        this.outputStream = out;
    }

    public View(Starter starter, InputStream in, OutputStream out){
        this.starter = starter;
        this.bufferedInputDati = new BufferedInputDati(in,out);
    }

    
    public void loginMenu() {
    	MyMenu2 logMenu = new MyMenu2(bufferedInputDati, ViewStringConstants.WELCOME_MESSAGE, ViewStringConstants.LOGIN_MENU_OPTIONS);
    	starter.logMenuChoice(logMenu.scegli());
    }

    public void mainMenu() {
        MyMenu2 mainMenu = new MyMenu2(bufferedInputDati, ViewStringConstants.WELCOME_MESSAGE, ViewStringConstants.MAIN_OPTIONS);
        starter.mainMenuChoice(mainMenu.scegli());
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

    public boolean userInputContinueAdding(String message) {
    	return bufferedInputDati.yesOrNo(message);
    }
    
    public int saveMenu() {
    	MyMenu2 netSaving = new MyMenu2(bufferedInputDati, ViewStringConstants.NET_SAVING_MENU, ViewStringConstants.NET_SAVING_MENU_OPTIONS);
    	return netSaving.scegli();
	}

    public int petriNetMenu(){
        MyMenu2 mymenu = new MyMenu2(bufferedInputDati, ViewStringConstants.PETRI_NET_MENU_TITLE, ViewStringConstants.CHANGE_PETRI_NET_OPTIONS);
        return mymenu.scegli();
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
        output.append(fluxRelFormatter(fluxRelations, cost));
        printToDisplay(output.toString());
    }

    public String marcFormatter(List<String> places, List<String> marcs) {
    	StringBuilder output =  new StringBuilder();
    	output.append("Posti: \n");
        for(int i = 0; i < places.size(); i++){
            output.append("\t" + places.get(i))
                    .append("\t" + "- marcatura: " + marcs.get(i) + "\n");
        }
        return output.toString();
    }
    
    public String fluxRelFormatter(List<Pair<String,String>> fluxRelations, List<String> cost) {
    	  StringBuilder output =  new StringBuilder();
    	  output.append("Relazioni di flusso: \n");
          for(int i = 0; i < fluxRelations.size(); i++) {
              output.append("\t (" +  fluxRelations.get(i).getFirst() + " , " + fluxRelations.get(i).getSecond() + ")")
                      .append("\t" + "- costo: " + cost.get(i) + "\n");
          }
          return output.toString();
    }

    public void printToDisplay(String ErrMsg){
        bufferedInputDati.println(ErrMsg);
    }

    public String readNotEmptyString(String message) {
        return bufferedInputDati.leggiStringaNonVuota(message).trim();
    }

    public int getIntInput(String message,int min ,int max) {
        return bufferedInputDati.leggiIntero(message, min, max);
    }

    public int getNotNegativeInt(String message) {
        return bufferedInputDati.leggiInteroNonNegativo(message);
    }


    //RETI DI PETRI CON PRIORITA'
    public int priorityPetriNetMenu() {
        MyMenu2 mymenu = new MyMenu2(bufferedInputDati, ViewStringConstants.PRIORITY_PETRI_NET_MENU_TITLE, ViewStringConstants.CHANGE_PRIORITY_PETRI_NET_OPTIONS);
        return mymenu.scegli();
    }

    public void printPriorityPetriNet(String netname, List<String> placesNames, List<String> marc, Map<String, Integer> transAndPriorities, List<Pair<String, String>> fluxrelations, List<String> values) {
        StringBuilder output = new StringBuilder();
        output.append(String.format("Nome rete con priorità: %s\n ",netname));
        output.append(marcFormatter(placesNames, marc));
        output.append(prioritiesFormatter(transAndPriorities));
        output.append(fluxRelFormatter(fluxrelations,values));

        printToDisplay(output.toString());
    }

    public String prioritiesFormatter(Map<String, Integer> transWithPriorities){
        StringBuilder strb = new StringBuilder();
        strb.append("Transizioni con priorità:\n");
        transWithPriorities.entrySet().forEach(e -> strb.append(String.format("\t%s\t - priorità: %2d\n", e.getKey(), e.getValue())));

        return strb.toString();
    }

    public String readFromList(List<String> listOfOptions, String message){
        String element;
        while(true) {
            element = readNotEmptyString(message);

            if (listOfOptions.contains(element))
                return element;

            printToDisplay(ViewStringConstants.ERR_ELEMENT_NAME_DOES_NOT_EXSIST);
        }
    }

    public BufferedInputDati getBufferedInputDati() {
        return bufferedInputDati;
    }
}

