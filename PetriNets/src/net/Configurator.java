package net;

import it.unibs.fp.mylib.*;
import java.util.Scanner;

public class Configurator {

    public static final String WELCOME_MESSAGE = "BENVENUTO";

    public static final String[] OPTIONS = { "Aggiungi una nuova rete", "Visualizza reti salvate" };

    public static final String INSERT_ELEM_MSG =
            "Inserisci il nome nel %s:\n";

    public static final String INSERT_NET_NAME_MSG = String.format(INSERT_ELEM_MSG, "rete");
    public static final String INSERT_PLACE_MSG = String.format(INSERT_ELEM_MSG, "posto");
    public static final String INSERT_TRANSITION_MSG = String.format(INSERT_ELEM_MSG, "trensizione");

    public static final String INSERT_DIRECTION_MSG =
            "Inserisci la direzione della relzione di flusso:\n";

    public final static Net temporaryNet = new Net();
    static Scanner scanint = new Scanner(System.in);
    static Scanner scanstring = new Scanner(System.in);

    public static void startConfigurator() {

        MyMenu menu = new MyMenu(WELCOME_MESSAGE, OPTIONS);
        menu.scegli();
    }


    private static void addNet() {

    }

}
