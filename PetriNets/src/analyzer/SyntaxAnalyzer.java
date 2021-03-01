package analyzer;

import com.sun.nio.sctp.SendFailedNotification;

import java.util.*;

public class SyntaxAnalyzer {
    public static final String[] KEYWORDS = {"net", "begin", "end"};
    public static final String GRAMMAR = "<net-description> ::= net <\\ID> begin <stat-list> end <\\ID>\n" +
                                     "<stat-list> ::= <stat> <stat-list> | <stat>\n" +
                                     "<stat> ::= <flux-stat> ; | <\\comment>\n" +
                                     "<flux-stat> ::= <id> -> <id> -> <flux-stat> | <id> -> <id> -> <id>\n" +
                                     "<id> ::= { <list-id> } | <\\ID>\n" +
                                     "<list-id> ::= <\\ID> , <list-id> | <\\ID>\n" +
                                     "<\\comment> ::=#.*(\\n)?\n" +
                                     "<\\ID> ::=\\b[A-Za-z][0-9A-Za-z_]*\\b";




    public static boolean belongsTo(String sentence, String grammar){
        bnfHolder bnf = new bnfHolder(grammar);

        return bnf.belongsTo(sentence);
    }



}
