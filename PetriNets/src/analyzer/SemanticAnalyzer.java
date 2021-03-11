package analyzer;

import net.Net;
import net.Place;
import net.Transition;

import java.util.*;
import java.util.function.Function;

public class SemanticAnalyzer {

	private static BnfHolder bnf;
	private Map<String, Function<String, Boolean>> nonTerminalToSemanticCheck;
	private Net net;

	public Net returnNet() {
		return net;
	}
	
	public void toDoInit_Stat(String sentence) {
		sentence = sentence.replaceAll("begin", "").replaceAll("net", "").trim();
		net = new Net(sentence);
	}

	public void toDoStat_List(String sentence) {
		String[] stats = sentence.split(";");

		for (int i = 0; i < stats.length; i++) {
			toDoStat(stats[i]);
		}
	}

	public void toDoStat(String sentence) {

		if (sentence.startsWith("place")) {
			sentence = sentence.replaceAll("place", "");
			String[] parts = sentence.split("->");
			
			

			for (int i = 0; i < parts.length; i++) {
				if (parts[i].contains("{")) {
					parts[i] = parts[i].replaceAll("[{}]", "");
					List<String> id = Arrays.asList(parts[i].split(","));
					id.forEach(e->e.trim());
					if (i % 2 == 0) {
						for (int j = 0; j < id.size(); j++) {
							if (!net.isTransition(new Transition(id.get(j)))) {
								net.addPlace(new Place(id.get(j)));
							}
						}
					} else {
						for (int j = 0; j < id.size(); j++) {
							if (!net.isPlace(new Place(id.get(j)))) {
								net.addTransition(new Transition(id.get(j)));
							}
						}
					}

				} else {
					parts[i] = parts[i].trim();
					if (i % 2 == 0) {
						if (!(net.isTransition(new Transition(parts[i])))) {
							net.addPlace(new Place(parts[i]));
						}
					} else {
						if (!(net.isPlace(new Place(parts[i])))) {
							net.addTransition(new Transition(parts[i]));
						}
					}
				}

			}
		}
	}

}
