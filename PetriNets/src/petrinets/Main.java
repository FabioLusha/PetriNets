package petrinets;

import java.io.BufferedOutputStream;
import java.io.PrintWriter;

import petrinets.MVC.controller.Controller;


public class Main {
	public static void main(String[] args) {
		
		Controller controller = new Controller(new PrintWriter(new BufferedOutputStream(System.out)));
		controller.startView();
	}
}
