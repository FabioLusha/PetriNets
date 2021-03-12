package analyzer;

import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Interpreter interpreter = new Interpreter();
		
		while(true) {
		interpreter.nextStep();
		}
	}
}
