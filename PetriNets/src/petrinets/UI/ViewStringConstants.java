package petrinets.UI;

public class ViewStringConstants {

    //Messaggi di interazione
    public static final String WELCOME_MESSAGE = "BENVENUTO";
    public static final String[] MAIN_OPTIONS = { "Aggiungi una nuova rete", "Visualizza reti salvate",
            "Aggiungi una nuova rete di petri", "Visualizza rete di petri", "Rimuovi una rete" };

    public static final String NET_SAVING_MENU = "INSERIMENTO RETE COMPLETATO";
    public static final String[] NET_SAVING_MENU_OPTIONS = { "Salva e torna al menu principale",
            "Ritorna al menu principale senza salvare" };

    public static final String PETRI_NET_CHANGE_VALUE = "RETE DI PETRI CREATA CON VALORI DI DEFAULT";
    public static final String[] PETRI_NET_CHANGE_VALUE_OPTIONS = { "Modifica valori delle marcature dei posti",
            "Modifica valori delle relazioni di flusso", "Salva la rete di petri" };

    public static final String INSERT_ELEM_MSG = "Inserisci il nome dell'elemento %s:\n > ";

    public static final String INSERT_NET_NAME_MSG = String.format(INSERT_ELEM_MSG, "rete");
    public static final String INSERT_PETRI_NET_NAME_MSG = String.format(INSERT_ELEM_MSG, "rete di petri");
    public static final String INSERT_PLACE_MSG = String.format(INSERT_ELEM_MSG, "posto");
    public static final String INSERT_TRANSITION_MSG = String.format(INSERT_ELEM_MSG, "transizione");
    public static final String INSERT_NET_NAME_TO_REMOVE = "Inserisci il nome della rete da eliminare: \n > ";

    public static final String INSERT_DIRECTION_MSG = "Inserisci la direzione della relazione di flusso:\n";
    public static final String CONTINUE_ADDING_QUESTION = "Aggiungere nuova relazione di flusso?";

    public static final String PETRI_NET_INITIALIZED_DEFAULT = "La rete di Petri e' stata creata e inizializzata con i valori di default" +
            " (marcatura 0, peso 1)\n";
    
    public static final String FLUX_DIRECTION_MSG = " %d \t %s -> %s\n";

    //Messaggi di risposta in funzione dell'azione compiuta

    public static final String FLUX_ELEM_ADD_TRUE = "Elemento inserito correttamente";
    public static final String ERR_MSG_FLUX_ELEM_ALREADY_EXSISTS = "Elemento gia' presente";
    public static final String ERR_PLACE_AS_TRANSITION = "%s e' gia presente come transizione";
    public static final String ERR_TRANSITION_AS_PLACE = "%s e' gia presente come posto";
    
    public static final String INSERT_NET_TO_VIEW = "Inserisci il nome della rete che vuoi visualizzare:\n>";
    public static final String ERR_MSG_NOT_POINTED_TRANSITION = "Transizione non puntata da alcun posto";
    public static final String ERR_MSG_NET_NAME_ALREADY_EXIST = "Nome della rete già presente!";
    

    public static final String ERR_NET_ALREADY_PRESENT = "Esiste gia' una rete con la topologia che hai inserito!";
    public static final String ERR_NET_NOT_PRESENT = "Non ci sono reti salvate con questo nome";
    
    public static final String ERR_SAVED_NET_NOT_PRESENT = "Non ci sono reti salvate";
    public static final String ERR_MSG_DESERIALIZATION_FAILED = "Atenzione! Non è stato posibile caricare le reti salvate. E' stato inizializzato un nuvo archivio.";

    
    public static final String AVAILABLE_NETS = "Reti disponibili: ";
    public static final String INSERT_BASE_NET_NAME_FOR_PETRI = String.format(INSERT_ELEM_MSG, "Inserisci il nome della rete da utilizzare come base:\n> ");

    public static final String PETRI_NET_MENU_TITLE = "OPZIONI SULLE RETI DI PETRI";
    public static final String[] CHANGE_PETRI_NET_OPTIONS = { "Modifica valori delle marcature dei posti", "Modifica valori delle relazioni di flusso",
            "Visualizza la rete di petri che si sta configurando", "Salva la rete di Petri", "Ritorna al menu principale senza salvare" };
	public static final String INSERT_PLACE_NAME_TO_MODIFY = "Inserisci il nome del posto di cui vuoi modificare la marcatura: \n > ";
	public static final String ERR_PLACE_NOT_PRESENT = "Il nome del posto inserito non e' presente!";
	public static final String ERR_FLUX_REL_NOT_PRESENT = "Relazione di flusso non presente!";
	public static final String INSERT_NEW_MARC = "Inserisci la nuova marcatura: \n > ";
	public static final String INSERT_NEW_VAL = "Inserisci il nuovo valore: \n > ";
}