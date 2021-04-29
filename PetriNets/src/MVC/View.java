package MVC;

import it.unibs.fp.mylib.*;
import net.OrderedPair;
import net.Place;
import net.Transition;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class View {
    public static final PrintWriter OUTPUT_STREAM = new PrintWriter(new BufferedOutputStream(System.out));

    private Controller controller;

    //private String currentTransition;
    //private String currentNet;

    public View(Controller controller){
        this.controller = controller;
    }

    public void mainMenu() {
        MyMenu mainMenu = new MyMenu(Message.WELCOME_MESSAGE, Message.MAIN_OPTIONS);
        controller.menuChoice(mainMenu.scegli());
        
        

    }


    public void initializeNet() {
        String netName = InputDati.leggiStringaNonVuota(Message.INSERT_NET_NAME_MSG).trim();
        controller.manageNetCreation(netName);
    }
    
    public void visualizeNets(List<String> names) {
    	printToDisplay(Message.AVAILABLE_NETS);
    	names.forEach(e -> printToDisplay(" - " + e));
    	controller.requestPrintNet(InputDati.leggiStringaNonVuota(Message.INSERT_NET_TO_VIEW).toString().trim());
 
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
    	String placename = InputDati.leggiStringaNonVuota(Message.INSERT_PLACE_MSG).trim();
    	String transitionname = InputDati.leggiStringaNonVuota(Message.INSERT_TRANSITION_MSG).trim();
    	int direction = InputDati.leggiIntero(Message.INSERT_DIRECTION_MSG
				+ String.format(Message.FLUX_DIRECTION_MSG, 0, placename, transitionname)
				+ String.format(Message.FLUX_DIRECTION_MSG, 1, transitionname, placename) + "\n > ",
				0, 1);
    	controller.addFluxRel(placename, transitionname, direction);
    }
    
    public void userInputContinueAdding() {
    	controller.choice(InputDati.yesOrNo(Message.CONTINUE_ADDING_QUESTION));
    }
    
    public void saveMenu() {
    	MyMenu netSaving = new MyMenu(Message.NET_SAVING_MENU,Message.NET_SAVING_MENU_OPTIONS);
    	
    	controller.userSavingChoice(netSaving.scegli());
  
	}
    
    public void printToDisplay(String ErrMsg){
        OUTPUT_STREAM.println(ErrMsg);
        OUTPUT_STREAM.flush();
    }

}

