package analyzer;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BnfHolder {

    public static final String NOMATCH_REGEX_MSG = "La Regex non ha trovato alcun match";

    class NoMatchFoundException extends Exception{
        public NoMatchFoundException() {super();}
        public NoMatchFoundException(String s) {super(s);}

    }

    class SentenceTooShortException extends Exception{
        private String errorMsg;
        private String errorPoint;

        public SentenceTooShortException(String msg, String point){
            errorMsg = msg;
            errorPoint = point;
        }

        public String getErrorMsg() {
            return errorMsg;
        }

        public String getErrorPoint() {
            return errorPoint;
        }
    }

    //public static final char SPACES = '\n' | '\t' | ' ';
        /*
        axiom = astrazione di più alto livello, non-terminale radice
        grammar = grammatica del linguaggio, ovvero l'insieme delle regole di produzione
         */
    private Map<String, List<String>> bnf;
    private Set<String> nonTerminals;
    private Set<String> regularDefinitions;
    private String axiom;

    public BnfHolder(String grammar){
        String[] tmp = grammar.split("\\n");
        bnf = new LinkedHashMap<>();
        nonTerminals = new LinkedHashSet<>();
        regularDefinitions = new LinkedHashSet<>();
        axiom = tmp[0].split("::=")[0].replaceAll("[ \\t]+","");

        for(String str : tmp){
            String[] strList = str.split("::=");
            //elimino i spazi
            String lhs = strList[0].replaceAll("[ \\t]+","");

            //se split non trova la regex in input ritorna l'intera stringa
            String[] rhs = strList[1].split("\\|");
            for(int i = 0; i < rhs.length; i++)
                rhs[i] = rhs[i].replaceAll("[ \\t]+"," ").trim();

            bnf.put(lhs, Arrays.asList(rhs));
            if(lhs.startsWith("<\\")) regularDefinitions.add(lhs);
        }

        nonTerminals = bnf.keySet();


    }

    public String toString(){
        StringBuffer output = new StringBuffer();

        for(Map.Entry<String, List<String>> entry : bnf.entrySet()){
            output.append(entry.getKey() + " ->");
            for(int i = 0; i < entry.getValue().size(); i++){
                if(i > 0 ) output.append("|");
                output.append(entry.getValue().get(i));
            }
            output.append("\n");
        }

        return output.toString();
    }

    public boolean isTerminal(String word){
        return !nonTerminals.contains(word);
    }

    public boolean isRegex(String word){
        return regularDefinitions.contains(word);
    }

    public List<String> production(String lhs){
        return bnf.get(lhs);
    }

    public String getRoot(){
        return axiom;
    }

    public Set<String> getNonTerminals(){
        return nonTerminals;
    }

    public boolean belongsTo(String sentence) throws SentenceTooShortException {
        //formatto il testo in una forma ideale per essere trattato
        sentence = sentence.replaceAll("\\{","\\{ " ).
                            replaceAll("}"," } " ).
                            replaceAll(",", " , " ).
                            replaceAll(";", " ; " ).
                            replaceAll("->", " -> ").
                            replaceAll("[ \\t]+", " ");
        //System.out.println(sentence);

        try {
            if(matchGeneric(sentence, axiom).isEmpty()) return true;
        }catch (NoMatchFoundException  e){
           e.getMessage();
        }

        return false;
    }

    /**
     * @ assert( true, nonTerminals.contains(nonTerminal) )
     */
    public String matchNonTerminal(String sentence, String nonTerminal) throws NoMatchFoundException, SentenceTooShortException {

        //se una variante della bnf è vera allora la frase appartiene al linguaggio
        if(regularDefinitions.contains(nonTerminal)){
           return matchRegex(sentence, production(nonTerminal).get(0));
        }

        for(String option : production(nonTerminal)){
            try{
                return matchGeneric(sentence, option);
            } catch (NoMatchFoundException e) {
                continue;
            }
        }

        throw new NoMatchFoundException();
    }

    public String matchRegex(String sentence, String regexString) throws NoMatchFoundException {
        Pattern regex = Pattern.compile(regexString);
        Matcher matcher = regex.matcher(sentence);

        /*
            Se la regex è verificata per una certa sequenza di caratteri e questa sequenza inizia
            al'inzio di sentence. La sentence inviata deve iniziare con la parola che deve essere
            confrontata con la regex, le parti precedenti di sentence sono già stati eliminati con
            i precedenti confronti esati.
         */
        if(matcher.find() && matcher.start() == 0)
            return sentence.substring(matcher.end());
        throw new NoMatchFoundException(NOMATCH_REGEX_MSG);
    }


    public String matchGeneric(String sentence, String option) throws NoMatchFoundException, SentenceTooShortException{

        sentence = sentence.replaceAll("\\{","\\{ " ).
                replaceAll("}"," } " ).
                replaceAll(",", " , " ).
                replaceAll(";", " ; " ).
                replaceAll("->", " -> ").
                replaceAll("[ \\t]+", " ");

        String originalSentence = sentence;
        sentence = sentence.trim();
        option = option.trim();

        int i = 0, j = 0;
        while( i < sentence.length() && j < option.length()){

            //se c'è un newline lo salto
            if(sentence.charAt(i) == '\n') i++;


            if(option.charAt(j) == '<') {
                //tolgo la parte che ha già fatto match per evitare errori con ->
                option = option.substring(j);
                //resetto j a 0 per la parte che ho eliminato
                j = 0;
                int endOfNonTerminal = option.indexOf(">") + 1;
                String nonTerminal = option.substring(j, option.indexOf(">") + 1);
                sentence = matchNonTerminal(sentence.substring(i), nonTerminal);

                option = option.substring(endOfNonTerminal);
                j = 0;

                i = 0;
                sentence = sentence.trim();
                option = option.trim();
                continue;
            }

            char sentenceChar = sentence.charAt(i);
            char optionChar = option.charAt(j);

            if(sentenceChar != optionChar) break;

            i++; j++;
        }
        if( j == option.length())
            return sentence.substring(i);
        else if (sentence.isEmpty())
            throw new SentenceTooShortException("frase vuota, option no", option);
        else
            throw new NoMatchFoundException("Errore nel Match");
    }

}
