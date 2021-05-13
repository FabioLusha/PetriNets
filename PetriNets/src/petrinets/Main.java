package petrinets;

import java.io.BufferedOutputStream;
import java.io.PrintWriter;

import petrinets.MVC.Controller;
import petrinets.MVC.Model;

public class Main {
	public static void main(String[] args) {

		//Model model = new Model();
		Controller controller = new Controller();
		controller.startView();
	}
}
