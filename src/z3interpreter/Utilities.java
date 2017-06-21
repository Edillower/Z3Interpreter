package z3interpreter;

import java.util.*;

class Utilities {

	private static List<String> warnings = new ArrayList<String>();
	private static List<String> errors = new ArrayList<String>();

	static void showMsg(String m) {
		String msg = "[MSG] " + m;
		System.out.println(msg);
	}

	static void showError(String e) {
		String msg = "[ERROR] " + e;
		errors.add(msg);
		System.out.println(msg);
		System.exit(0);
	}

	static void showWarning(String e) {
		String msg = "[WARNING] " + e;
		warnings.add(msg);
		System.out.println(msg);
	}

	static void showExpectedError(String treeName, String expected) {
		showError(treeName + " - " + expected + " expected");
	}
	
	static void showExpectedError(String treeName, String expected, String expectedFor) {
		showError(treeName + " - " + expected + " expected for " + expectedFor);
	}

	static void showTreeKindError(String treeName) {
		showError(treeName + " - tree kind error");
	}

	static void showOpKindError(String opName) {
		showError(opName + " - operator kind error");
	}
}
