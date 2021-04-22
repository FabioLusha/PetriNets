package MVC;

import it.unibs.fp.mylib.*;

import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.Map;

public class View {
    public static final PrintWriter OUTPUT_STREAM_PRINTER = new PrintWriter(new BufferedOutputStream(System.out));

    private Controller controller;

    //private String currentTransition;
    //private String currentNet;

    public View(Controller controller){
        this.controller = controller;
    }

    public void mainMenu() {
        MyMenu mainMenu = new MyMenu(Message.WELCOME_MESSAGE, Message.MAIN_OPTIONS);

        int choice = mainMenu.scegli();
        switch (choice) {
            case 1:
                initializeNet();
                /*
            case 2:
                return PRINT_NET;
            case 3:
                return CREATE_PETRI_NET;
            case 4:
                return PRINT_PETRI_NET;
            default:
                return END;

                 */

        }

    }


    public void initializeNet() {
        String netName = InputDati.leggiStringaNonVuota(Message.INSERT_NET_NAME_MSG).trim();
        controller.manageNetCreation(netName);
    }

    public void printNet(String name, Map<String, String> fluxRelation){

      StringBuilder strBuilder = new StringBuilder(name + "\n");
      for(var elem : fluxRelation.entrySet()) {
          //Se lasciamo il print così non si capsice chi è il posto e chi la transizione
          strBuilder.append("\t (" + elem.getKey() + " , " + elem.getValue());
      }
      OUTPUT_STREAM_PRINTER.println();
    }


    public void notifyError(String ErrMsg){
        OUTPUT_STREAM_PRINTER.println(ErrMsg);
    }

}

