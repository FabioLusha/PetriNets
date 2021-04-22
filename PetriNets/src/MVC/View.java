package MVC;

import it.unibs.fp.mylib.MyMenu;

import javax.swing.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class View {

    public static final String[] NET_SAVING_MENU_OPTIONS = { "Salva e torna al menu principale",
            "Ritorna al menu principale senza salvare" };
    public static final Map<String, Integer> MAP_OPTIONS
    private Controller controller;

    //private String currentTransition;
    //private String currentNet;

    public void openView() {
        MyMenu mainMenu = new MyMenu(Message.WELCOME_MESSAGE, Message.MAIN_OPTIONS);

        int choice = mainMenu.scegli();
        switch (choice) {
            case 1:
                initializeNet();
            case 2:
                return PRINT_NET;
            case 3:
                return CREATE_PETRI_NET;
            case 4:
                return PRINT_PETRI_NET;
            default:
                return END;
        }
    }


    public void initializeNet() {
       // TODO gestire l'input

        String netName= null;
        controller.manageNetCreatrion(netName);
    }


    public void notifyError(String ErrMsg){
        System.out.println(ErrMsg);
    }

}

