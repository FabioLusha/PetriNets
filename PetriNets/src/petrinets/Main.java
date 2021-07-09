package petrinets;

import java.io.BufferedOutputStream;
import java.io.PrintWriter;

import petrinets.UI.controller.Starter;


public class Main {
	public static void main(String[] args) {
		Starter starter = new Starter(System.in, System.out);
		starter.startView();
	}
}
