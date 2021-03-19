package analyzer;

import it.unibs.fp.mylib.*;

import net.Net;
import net.OrderedPair;
import net.PetriNet;
import net.Place;
import net.Transition;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public enum UserMenuFSA {

	START {

		public UserMenuFSA stepNext() {

			if (!(netxmlmanager.isEmpty())) {
				try {
					netMap = (Map<String, Net>) netxmlmanager.deserializeFromXML();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (!(petrinetxmlmanager.isEmpty())) {
				try {
					petrinetMap = (Map<String, PetriNet>) petrinetxmlmanager.deserializeFromXML();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			return MAIN_MENU;
		}
	},

	MAIN_MENU {

		public UserMenuFSA stepNext() {
			int choice = principalMenu.scegli();

			switch (choice) {
			case 1:
				return CREATE_NET;
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
	},

	CREATE_NET {

		String netName;

		public UserMenuFSA stepNext() {

			netName = InputDati.leggiStringaNonVuota(INSERT_NET_NAME_MSG).trim();
			if (netMap.containsKey(netName)) {
				System.out.println("Nome della rete già presente!");
				return CREATE_NET;
			}

			temporaryNet = new Net(netName);
			return ADD_ELEMENT;
		}
	},

	ADD_ELEMENT {

		public UserMenuFSA stepNext() {
			String place;
			String trans;
			int direction;

			do {
				System.out.println("Inserimento relazione di fusso".toUpperCase());
				place = InputDati.leggiStringaNonVuota(INSERT_PLACE_MSG).trim();
				trans = InputDati.leggiStringaNonVuota(INSERT_TRANSITION_MSG).trim();
				direction = InputDati.leggiIntero(INSERT_DIRECTION_MSG
						+ String.format(FLUX_DIRECTION_MSG, OrderedPair.typePair.pt.ordinal(), place, trans)
						+ String.format(FLUX_DIRECTION_MSG, OrderedPair.typePair.tp.ordinal(), trans, place) + "\n > ",
						OrderedPair.typePair.pt.ordinal(), OrderedPair.typePair.tp.ordinal());

				// TO-DO messaggio in base al perchè torna false
				if (OrderedPair.typePair.ordinalToType(direction) == OrderedPair.typePair.tp
						&& !temporaryNet.containsTransition(new Transition(trans))) {
					System.out.println("Transizione non puntata da alcun posto");
					return ADD_ELEMENT;
				}

				OrderedPair pair = new OrderedPair(new Place(place), new Transition(trans),
						OrderedPair.typePair.ordinalToType(direction));
				boolean result = temporaryNet.addFluxRelElement(pair);

				if (result)
					System.out.println(String.format(FLUX_ELEM_ADD_TRUE, pair.toString()));
				else
					System.out.println(String.format(FLUX_ELEM_ADD_FALSE, pair.toString()));

			} while (InputDati.yesOrNo("Aggiungere nuova relazione di flusso?"));
			return SAVE;
		}
	},

	CREATE_PETRI_NET {
		public UserMenuFSA stepNext() {

			if (netMap.isEmpty()) {
				System.out.println("Non vi è alcuna rete presente per poter definire una rete di Petri!");
				return MAIN_MENU;
			} else {
				System.out.println("Reti presenti da cui poter definire una rete di Petri: ");
				netMap.keySet().forEach(e -> System.out.println("-" + e));
				String basedNetName = InputDati
						.leggiStringa("Inserisci il nome della rete su cui si basera' la rete di Petri: ");
				String petriname = InputDati.leggiStringa("Inserisci il nome della rete di Petri:  ");

				temporaryPetriNet = new PetriNet(petriname, netMap.get(basedNetName));
				return ADD_VALUES_TO_PETRI_NET;

			}

		}
	},

	ADD_VALUES_TO_PETRI_NET {

		public UserMenuFSA stepNext() {
			MyMenu petriNetValue = new MyMenu(PETRI_NET_CHANGE_VALUE, UserMenuFSA.PETRI_NET_CHANGE_VALUE_OPTIONS);

			switch (petriNetValue.scegli()) {
			case 1: {
				System.out.println("Posti presenti:");
				temporaryPetriNet.getMarcmap().keySet().forEach(
						e -> System.out.println("-" + e + "     marcatura:" + temporaryPetriNet.getMarcmap().get(e)));
				return CHANGE_PLACE_VALUE;
			}
			case 2: {
				System.out.println("Relazioni di flusso presenti:");
				temporaryPetriNet.getValuemap().keySet().forEach(
						e -> System.out.println("- " + e + "    valore:" + temporaryPetriNet.getValuemap().get(e)));
				return CHANGE_FLUX_VALUE;
			}
			case 3: {
				return SAVE_PETRI_NET;
			}
			default:
				return MAIN_MENU;
			}
		}
	},

	CHANGE_PLACE_VALUE {
		public UserMenuFSA stepNext() {

			String choice = InputDati.leggiStringa("Inserisci il nome del posto di cui vuoi modificare la marcatura: ")
					.trim();
			if (temporaryPetriNet.getMarcmap().keySet().contains(new Place(choice))) {

				int newValue;
				do {
					newValue = InputDati.leggiIntero("Inserisci la nuova marcatura per " + choice + ": ");
					if (newValue < 0) {
						System.out.println("Il valore della marcatura inserito deve essere un intero non negativo");
					}
				} while (newValue < 0);
				temporaryPetriNet.getMarcmap().replace(new Place(choice), newValue);
				return ADD_VALUES_TO_PETRI_NET;

			} else {
				System.out.println("Non è presente alcun posto con questo nome!");
				return ADD_VALUES_TO_PETRI_NET;
			}
		}
	},

	CHANGE_FLUX_VALUE {
		public UserMenuFSA stepNext() {
			String choiceplace = InputDati
					.leggiStringa("Inserisci il posto della relazione di flusso di cui vuoi modificare la marcatura: ")
					.trim();
			String choicetransition = InputDati
					.leggiStringa(
							"Inserisci la transizione della relazione di flusso di cui vuoi modificare la marcatura: ")
					.trim();
			if (temporaryPetriNet.getValuemap().keySet()
					.contains(new OrderedPair(new Place(choiceplace), new Transition(choicetransition)))) {

				int newValue;
				do {
					newValue = InputDati.leggiIntero("Inserisci il nuovo valore per la transizione scelta:  ");
					if (newValue <= 0) {
						System.out.println("Il valore inserito deve essere un intero positivo");
					}
				} while (newValue <= 0);
				temporaryPetriNet.getValuemap()
						.replace(new OrderedPair(new Place(choiceplace), new Transition(choicetransition)), newValue);
				return ADD_VALUES_TO_PETRI_NET;

			} else {
				System.out.println("Transizione non presente!");
				return ADD_VALUES_TO_PETRI_NET;
			}
		}
	},

	SAVE {

		public UserMenuFSA stepNext() {
			MyMenu netSaving = new MyMenu(NET_SAVING_MENU, NET_SAVING_MENU_OPTIONS);
			switch (netSaving.scegli()) {
			case 0:
				return END;
			case 1: {
				if (!(netMap.containsValue(temporaryNet))) {
					netMap.put(temporaryNet.getName(), temporaryNet);
				} else {
					System.out.println("La rete che hai inserito esiste gia' ");
				}

				try {
					netxmlmanager.serializeToXML(netMap);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace(System.out);
				}
				return MAIN_MENU;
			}
			// case 2: //Ritorna al menu senza salvare
			default: {
				return MAIN_MENU;
			}
			}
		}
	},

	SAVE_PETRI_NET {
		public UserMenuFSA stepNext() {
			petrinetMap.put(temporaryPetriNet.getName(), temporaryPetriNet);
			
			try {
				petrinetxmlmanager.serializeToXML(petrinetMap);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace(System.out);
			}
			

			return MAIN_MENU;
		}
	},

	PRINT_NET {
		public UserMenuFSA stepNext() {
			if (netMap.isEmpty()) {
				System.out.println("Nessuna rete presente da visualizzare!");
			} else {
				System.out.println("Reti disponibili alla visualizzazione:");
				netMap.keySet().forEach(e -> System.out.println("-" + e));
				try {
					System.out.println(netMap.get(InputDati.leggiStringaNonVuota(INSERT_NET_TO_VIEW)).toString());
				} catch (NullPointerException e) {
					System.out.println("Nome della rete inserito non presente!");

				}
			}

			return MAIN_MENU;
		}
	},

	PRINT_PETRI_NET {
		public UserMenuFSA stepNext() {
			if (petrinetMap.isEmpty()) {
				System.out.println("Nessuna rete di petri presente da visualizzare!");
			} else {
				System.out.println("Reti disponibili alla visualizzazione:");
				petrinetMap.keySet().forEach(e -> System.out.println("-" + e));
				try {
					System.out.println(petrinetMap.get(InputDati.leggiStringaNonVuota(INSERT_NET_TO_VIEW)).toString());
				} catch (NullPointerException e) {
					System.out.println("Nome della rete inserito non presente!");

				}
			}

			return MAIN_MENU;
		}
	},

	END {
		public UserMenuFSA stepNext() {
			System.out.println("Chiusura programma");
			System.exit(0);
			return END;
		}

	};

	public static final String WELCOME_MESSAGE = "BENVENUTO";
	public static final String[] STARTING_OPTIONS = { "Aggiungi una nuova rete", "Visualizza reti salvate",
			"Aggiungi una nuova rete di petri", "Visualizza rete di petri" };

	static final MyMenu principalMenu = new MyMenu(WELCOME_MESSAGE, STARTING_OPTIONS);

	private static XMLmanager netxmlmanager = new XMLmanager<Map<String, Net>>("nets.xml");
	private static XMLmanager petrinetxmlmanager = new XMLmanager<Map<String, PetriNet>>("petrinets.xml");

	private static Net temporaryNet = new Net();
	public static Map<String, Net> netMap = new HashMap<>();

	private static PetriNet temporaryPetriNet = new PetriNet();
	public static Map<String, PetriNet> petrinetMap = new HashMap<>();

	public abstract UserMenuFSA stepNext();

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

	public static final String FLUX_DIRECTION_MSG = " %d \t %s -> %s\n";
	public static final String FLUX_ELEM_ADD_TRUE = "Elemento %s inserito correttamente";
	public static final String FLUX_ELEM_ADD_FALSE = "Elemento %s gia' presente";

	public static final String INSERT_NET_TO_VIEW = "Inserisci il nome della rete che vuoi visualizzare:\n";

}
