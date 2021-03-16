package analyzer;

import it.unibs.fp.mylib.*;

import net.Net;
import net.OrderedPair;
import net.Place;
import net.Transition;

import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Interpreter {

    public static final String WELCOME_MESSAGE = "BENVENUTO";
    public static final String[] STARTING_OPTIONS = { "Aggiungi una nuova rete", "Visualizza reti salvate" };

    public static final String NET_SAVING_MENU = "INSERIMENTO RETE COMPLETATO";
    public static final String[] NET_SAVING_MENU_OPTIONS = { "Salva e torna al menu principale", "Ritorna al menu principale senza salvare"};


    public static final String INSERT_ELEM_MSG =
            "Inserisci il nome del %s:\n > ";

    public static final String INSERT_NET_NAME_MSG = String.format(INSERT_ELEM_MSG, "rete");
    public static final String INSERT_PLACE_MSG = String.format(INSERT_ELEM_MSG, "posto");
    public static final String INSERT_TRANSITION_MSG = String.format(INSERT_ELEM_MSG, "transizione");

    public static final String INSERT_DIRECTION_MSG =
            "Inserisci la direzione della relazione di flusso:\n";

    public static final String FLUX_DIRECTION_MSG = " %d \t %s -> %s\n";
    public static final String FLUX_ELEM_ADD_TRUE = "Elemento %s inserito correttamente";
    public static final String FLUX_ELEM_ADD_FALSE = "Elemento %s gia'  presesente";
    
    public static final String INSERT_NET_TO_VIEW=
            "Inserisci il nome della rete che vuoi visualizzare:\n";


    enum State {

        INIZIO {
      
            public State stepNext() {

            	File newFile = new File("nets.xml");
            	 if(newFile.length() != 0) {
            		 try {
						netList = deserializeFromXML();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	 }
            	 return MENU_PRINCIPALE;
            }
        },

        MENU_PRINCIPALE {

            public State stepNext() {

                int choice = principalMenu.scegli();

                switch (choice) {
                    case 1:
                        return CREA_RETE;
                    case 2:
                        return VISUALIZZA;
                    default:
                        return FINE;
                }
            }
        },

        CREA_RETE {

            Net tmpNet;
            String netName;
            String place;
            String trans;
            int direction;

            public State stepNext() {

                netName =  InputDati.leggiStringaNonVuota(INSERT_NET_NAME_MSG).trim();
                tmpNet = new Net(netName);

                do {
                    System.out.println( "Inserimento relazione di fusso".toUpperCase());
                    place = InputDati.leggiStringaNonVuota(INSERT_PLACE_MSG).trim();
                    trans = InputDati.leggiStringaNonVuota(INSERT_TRANSITION_MSG).trim();
                    direction = InputDati.leggiIntero(INSERT_DIRECTION_MSG
                            + String.format(FLUX_DIRECTION_MSG, OrderedPair.typePair.pt.ordinal(), place, trans)
                            + String.format(FLUX_DIRECTION_MSG, OrderedPair.typePair.tp.ordinal(), trans, place) + "\n > ",
                            OrderedPair.typePair.pt.ordinal(),OrderedPair.typePair.tp.ordinal());


                    /**
                     * TO-DO
                     * Controllare che trans non sia un posto e che place non sia una transizione.
                     * Dove fare il controllo? qui o nel metodo addFluxRelElement(arg) ?
                     */
                    OrderedPair pair = new OrderedPair(new Place(place), new Transition(trans), OrderedPair.typePair.ordinalToType(direction));
                    boolean result = tmpNet.addFluxRelElement(pair);

                    if(result) System.out.println(String.format(FLUX_ELEM_ADD_TRUE, pair.toString()));
                    else System.out.println(String.format(FLUX_ELEM_ADD_FALSE, pair.toString()));


                }while(InputDati.yesOrNo("Aggiungere nuova relazione di flusso?"));
                
               rete=tmpNet;

                return AGGIUNTA_F;
            }
        },
/*
        Aggiunta {
        	String place;
        	String transition;
            public State stepNext() {


            }
        },
*/
        AGGIUNTA_F {

            public State stepNext() {
                MyMenu netSaving = new MyMenu(NET_SAVING_MENU,NET_SAVING_MENU_OPTIONS);
                switch (netSaving.scegli()) {
                    case 0: return FINE;
                    case 1: {
                        /**
                         * TO-DO
                         * Aggiungere la rete nella lista di reti salvate
                         */
                    	
                    	netList.put(rete.getName(), rete);
                        return MENU_PRINCIPALE; }
                    case 2: //Ritorna al menu senza salvare
                    default: { return MENU_PRINCIPALE; }
                }
            }
        },
        
        VISUALIZZA{
        	public State stepNext() {
        		if(netList.isEmpty()) {
        			System.out.println("Nussuna rete presente da visualizzare!");
        		}else {
        			System.out.println("Reti disponibili alla visualizzazione:");
        		for(String s : netList.keySet()) {
        			System.out.println("-" + s);
        		}
        		try {
        		System.out.println(netList.get(InputDati.leggiStringaNonVuota(INSERT_NET_TO_VIEW)).toString());
        		}catch(NullPointerException e) {
        			System.out.println("Nome della rete inserito non presente!");
        		}
        		}
        		
        		return MENU_PRINCIPALE;
        	}
        },

        FINE {
            public State stepNext() {
            	
            	try {
					serializeToXML(netList);
				} catch (IOException e) {
					// TODO Auto-generated catch block
                    e.printStackTrace(System.out);
				}
            	System.out.println("Chiusura programma");
                System.exit(0);
                return FINE;
            }



        };

        static final MyMenu principalMenu = new MyMenu(WELCOME_MESSAGE, STARTING_OPTIONS);

        private static Net rete = new Net();
        public static Map<String, Net> netList = new HashMap<>();

        public abstract State stepNext();
        
    	private static void serializeToXML (Map<String,Net> netlist) throws IOException
    	{
    		FileOutputStream fos = new FileOutputStream("nets.xml");
    		XMLEncoder encoder = new XMLEncoder(fos);
    		encoder.setExceptionListener(new ExceptionListener() {
    			public void exceptionThrown(Exception e) {
    				System.out.println("Exception! :"+ e.toString());
    			}
    		});


    		encoder.writeObject(netlist);
    		encoder.close();
    		fos.close();
    	}
    	
    	
    	private static Map<String,Net> deserializeFromXML() throws IOException {
    	    FileInputStream fis = new FileInputStream("nets.xml");
    	    XMLDecoder decoder = new XMLDecoder(fis);
    	    Map<String,Net> savednets = (Map<String,Net>) decoder.readObject();
    	    decoder.close();
    	    fis.close();
    	    return savednets;
    	}
    }

    private State state;

    public Interpreter() {
        state = State.INIZIO;
    }

    public State start() {
        state = state.stepNext();
        return state;
    }
    
}