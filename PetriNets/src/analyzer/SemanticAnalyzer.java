package analyzer;

import net.Net;

import java.util.*;
import java.util.function.Function;

public class SemanticAnalyzer {

    private static BnfHolder bnf;
    private Map<String, Function<String, Boolean>> nonTerminalToSemanticCheck;
    private Net net;

}
