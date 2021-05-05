package petrinets.MVC;

public class ViewStringConstants {

    //Messaggi di interazione
    public static final String WELCOME_MESSAGE = "BENVENUTO";
    public static final String[] MAIN_OPTIONS = { "Aggiungi una nuova rete", "Visualizza reti salvate",
            "Aggiungi una nuova rete di petri", "Visualizza rete di petri" };

    public static final String NET_SAVING_MENU = "INSERIMENTO RETE COMPLETATO";
    public static final String[] NET_SAVING_MENU_OPTIONS = { "Salva e torna al menu principale",
            "Ritorna al menu principale senza salvare" };

    public static final String PETRI_NET_CHANGE_VALUE = "RETE DI PETRI CREATA CON VALORI DI DEFAULT";
    public static final String[] PETRI_NET_CHANGE_VALUE_OPTIONS = { "Modifica valori delle marcature dei posti",
            "Modifica valori delle relazioni di flusso", "Salva la rete di petri" };

    public static final String INSERT_ELEM_MSG = "Inserisci il nome dell'elemento %s:\n > ";

    public static final String INSERT_NET_NAME_MSG = String.format(INSERT_ELEM_MSG, "rete");
    public static final String INSERT_PLACE_MSG = String.format(INSERT_ELEM_MSG, "posto");
    public static final String INSERT_TRANSITION_MSG = String.format(INSERT_ELEM_MSG, "transizione");

    public static final String INSERT_DIRECTION_MSG = "Inserisci la direzione della relazione di flusso:\n";
    public static final String CONTINUE_ADDING_QUESTION = "Aggiungere nuova relazione di flusso?";
    
    public static final String FLUX_DIRECTION_MSG = " %d \t %s -> %s\n";

    //Messaggi di risposta in funzione dell'azione compiuta

    //TO-DO decidere se eliminare elementi di formattazione (%s) oppure modificare messaggi errore nella view
    public static final String FLUX_ELEM_ADD_TRUE = "Elemento %s inserito correttamente";
    public static final String ERR_MSG_FLUX_ELEM_ALREADY_EXSISTS = "Elemento %s gia' presente";
    public static final String ERR_PLACE_AS_TRANSITION = "%s � gia presente come transizione";
    public static final String ERR_TRANSITION_AS_PLACE = "%s � gia presente come posto";
    
    public static final String INSERT_NET_TO_VIEW = "Inserisci il nome della rete che vuoi visualizzare:\n";
    public static final String ERR_MSG_NOT_POINTED_TRANSITION = "Transizione non puntata da alcun posto";
    public static final String ERR_MSG_NET_NAME_ALREADY_EXIST = "Nome della rete già presente!";
    
    public static final String ERR_NET_ALREADY_PRESENT = "La rete che hai inserito esiste gia'";
    public static final String ERR_NET_NOT_PRESENT = "Non ci sono reti salvate con questo nome";
    
    public static final String ERR_SAVED_NET_NOT_PRESENT = "Non ci sono reti salvate";


    
    public static final String AVAILABLE_NETS = "Reti disponibili alla visualizzazione: ";
}
