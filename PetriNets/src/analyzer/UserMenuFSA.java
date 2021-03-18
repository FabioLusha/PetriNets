package analyzer;

import it.unibs.fp.mylib.*;

import net.Net;
import net.OrderedPair;
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
					netMap = netxmlmanager.deserializeFromXML();
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

	PRINT_NET {
		public UserMenuFSA stepNext() {
			if (netMap.isEmpty()) {
				System.out.println("Nussuna rete presente da visualizzare!");
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

	END {
		public UserMenuFSA stepNext() {
			System.out.println("Chiusura programma");
			System.exit(0);
			return END;
		}

	};

	public static final String WELCOME_MESSAGE = "BENVENUTO";
	public static final String[] STARTING_OPTIONS = { "Aggiungi una nuova rete", "Visualizza reti salvate" };

	static final MyMenu principalMenu = new MyMenu(WELCOME_MESSAGE, STARTING_OPTIONS);

	private static XMLmanager netxmlmanager = new XMLmanager("nets.xml");

	private static Net temporaryNet = new Net();
	public static Map<String, Net> netMap = new HashMap<>();

	public abstract UserMenuFSA stepNext();

	public static final String NET_SAVING_MENU = "INSERIMENTO RETE COMPLETATO";
	public static final String[] NET_SAVING_MENU_OPTIONS = { "Salva e torna al menu principale",
			"Ritorna al menu principale senza salvare" };

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
