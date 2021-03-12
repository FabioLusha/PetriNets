package analyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import net.Net;

public class Interpreter {

    enum State {

        Inizio {

            public State stepNext() {
                System.out.println("inserisci la tua scelta: 1)Crea Rete 2)Chiudi");

                if (scanint.nextInt() == 1) {
                    return Crea_rete;
                }
                return Fine;
            }

        },

        Crea_rete {

            public State stepNext() {
                System.out.println("Inserisci il nome della rete: ");
                semanalyzer.toDoInit_Stat(scanstring.nextLine());
                return Aggiunta;
            }

        },

        Aggiunta {
        	String place;
        	String transition;
            public State stepNext() {
                System.out.println("Quale tipo di relazione di flusso vuoi inserire: 1) posto->transizione 2)transizione->posto");
                switch (scanint.nextInt()) {
                    case 1:
                        {
                            System.out.println("Inserisci posto:");
                            place = scanstring.nextLine();
                            System.out.println("Inserisci transizione:");
                            transition = scanstring.nextLine();
                            break;
                        }
                    case 2:
                        {
                            System.out.println("Inserisci transizione:");
                            transition = scanstring.nextLine();
                            System.out.println("Inserisci posto:");
                            place = scanstring.nextLine();
                            break;
                        }
                    default:
                        {
                            System.out.println("valore inserito non corretto!");
                            break;
                        }
                }
                return AggiuntaF;
            }
        },

        AggiuntaF {

            public State stepNext() {

                System.out.println("1)Inserisci altre relazioni di flusso 2)Salva e torna al menu principale 3)Chiudi tutto senza salvare");
                switch ((scanint.nextInt())) {
                    case 1:
                        {
                            return Aggiunta;

                        }
                    case 2:
                        {
                            return Inizio;

                        }
                    case 3:
                        {
                            return Fine;
                        }
                    default:
                        {
                            return AggiuntaF;
                        }
                }
            }

        },

        Fine {
            public State stepNext() {
            	System.out.println("Chiusura programma");
                System.exit(0);
                return Fine;
            }



        };

        Scanner scanint = new Scanner(System.in);
        Scanner scanstring = new Scanner(System.in);
        SemanticAnalyzer semanalyzer = new SemanticAnalyzer();

        public abstract State stepNext();
      
    }

    public State state;

    public Interpreter() {
        state = State.Inizio;

    }

    public State nextStep() {
        state = state.stepNext();
        return state;
    }

    
    
}