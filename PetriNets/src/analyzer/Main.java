package analyzer;

import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args){
        System.out.println( LexicalAnalyzer.isID("{thisx"));
        String[] a = "aaa c0".split("->");
        System.out.println("\\\\");

        bnfHolder bnf = new bnfHolder(SyntaxAnalyzer.GRAMMAR);
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

        //System.out.println( bnf.matchGeneric(" { this}-  > {t1 }", "{this } ->{ t1    \n}"));
        /*
        String sentence = "net   rete1   begin {n1}->n2 -> m3; end Re";
        sentence = sentence.replaceAll("net","graph")
        .replaceAll("\\{"," { " )
        .replaceAll("}"," } " )
        .replaceAll(",", " , " )
        .replaceAll(";", " ; " )
                .replaceAll("->", " -> ")
        .replaceAll("[ \\t]+", " ");
        System.out.println(sentence);
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
         System.out.println(bnf.matchNonTerminal("#jgg\n hj", "<stat>"));
            System.out.println("yay");
        }catch(bnfHolder.NoMatchFoundException e){
            System.out.println("Err");
        }
*/

        System.out.println(SyntaxAnalyzer.belongsTo("net rete1 begin {n1, n3} -> {n2, n4} -> m3 ; n3 -> n2 -> n1; end Re", SyntaxAnalyzer.GRAMMAR));

    }
}
