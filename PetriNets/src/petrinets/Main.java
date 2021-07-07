package petrinets;

import java.io.BufferedOutputStream;
import java.io.PrintWriter;

import petrinets.UI.controller.Starter;


public class Main {
	public static void main(String[] args) {
		Starter starter = new Starter(new PrintWriter(new BufferedOutputStream(System.out)));
		starter.startView();
	}
}
