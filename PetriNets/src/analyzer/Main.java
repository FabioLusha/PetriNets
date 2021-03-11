package analyzer;

import net.Net;
import net.Transition;

import java.util.Scanner;

public class Main {
    public static void main(String[] args){

        /*
        BnfHolder bnf = new BnfHolder(Grammar.NET_GRAMMAR);
        System.out.println(bnf.toString());
        String[] str = " a b  c".split(" ");
        for(String s : str){
            System.out.println(s);
        }

        for(String s : bnf.getNonTerminals()){
            System.out.println(s);
            for(String ss : bnf.production(s)){
                System.out.println("\t" + ss);
            }
        }
*/

       /*
        System.out.println( Pattern.matches(LexicalAnalyzer.ID_PATTERN, " rete1 "));
        Pattern ptn = Pattern.compile(LexicalAnalyzer.ID_PATTERN);
        Matcher matcher = ptn.matcher(" rete1 45");
        System.out.println(matcher.matches());
        System.out.println(matcher.find());
        System.out.println(matcher.end());
        System.out.println(matcher.start());

        */
        /*
        Pattern ptn = Pattern.compile("#.*\\n");
        Matcher matcher = ptn.matcher("#jgg\n");
        //System.out.println(matcher.matches());
        System.out.println(matcher.find());
        //System.out.println(matcher.end());
        //System.out.println(matcher.start());

*/
        /*
        try {
         System.out.println(bnf.matchNonTerminal("n1 -> n3 -> n4 ; ", "<stat-list>"));
            System.out.println("yay");
        }catch(bnfHolder.NoMatchFoundException e){
            System.out.println("Err");
        }

         */
       //System.out.println(Grammar.belongsTo("net rete1 begin { n1, n3} -> {n2, n4, n4} -> m3 -> n2 -> {n4, n5}; n3 -> {n2  -> n1 ; end Re", Grammar.NET_GRAMMAR));
/*
        Scanner scan = new Scanner(System.in);
        BnfHolder bnf = new BnfHolder(Grammar.INSTRUCTION_GRAMMAR);
        String toMatch = bnf.getRoot();
        do{
            System.out.print("> ");
            String str = scan.nextLine();
            try{
                bnf.matchGeneric(str, toMatch);
            }catch(BnfHolder.SentenceTooShortException e ){
                toMatch =  e.getErrorPoint() + " " + toMatch;
                System.out.println("arrivato");
                System.out.println(toMatch);
            }catch (BnfHolder.NoMatchFoundException e){
                e.getMessage();
            };
        }while(true);
*/
        Interpreter interpreter = new Interpreter();
        System.out.println(interpreter.state.ordinal());
        Scanner scan = new Scanner(System.in);
        BnfHolder bnf = new BnfHolder(Grammar.INSTRUCTION_GRAMMAR);
        String toMatch = bnf.getRoot();
        StringBuilder quelloCheScrivi = new StringBuilder();
        

        do{
            System.out.print("> ");
            String str = scan.nextLine();
            for(String command : interpreter.state.commands){
                try{
                    if( bnf.matchGeneric(str, command).isEmpty()) {
                        interpreter.nextStep(command);

                        interpreter.toDoCommand(command, str);
                        System.out.println(interpreter.returnNet().toString());
                        quelloCheScrivi.append(str + "\n");
                        System.out.println(interpreter.state.ordinal());
                        System.out.println(quelloCheScrivi);
                        break;
                    }
                }catch(BnfHolder.SentenceTooShortException e ){
                    toMatch =  e.getErrorPoint() + " " + toMatch;
                    System.out.println("arrivato");
                    System.out.println(toMatch);
                    continue;
                }catch (BnfHolder.NoMatchFoundException e){
                    System.out.println(e.getMessage());;
                    continue;
                };
            }
        }while(true);
    }
}
