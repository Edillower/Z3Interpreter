package z3interpreter;

import static z3interpreter.TokenKind.*;

import java.util.*;

class Tokenizer {

	private String front;
	private TokenKind frontKind;
	private Set<String> constants = new HashSet<String>();
	private Set<String> variables = new HashSet<String>();
	private String reminder;

	Tokenizer(String src) {
		this.front = "BEGIN";
		this.frontKind = BEGIN;
		this.reminder = src;
	}

	TokenKind getTokenKind() {
		return this.frontKind;
	}

	String getTokenName() {
		return this.front;
	}

	int getTokenVal() {
		return this.frontKind.getTokenNumber();
	}
	
	Set<String> getConstants(){
		return this.constants;
	}
	
	Set<String> getVariables(){
		return this.variables;
	}

	private static final Set<Character> func = new HashSet<Character>(Arrays.asList('G', 'F', 'X'));
	private static final Set<Character> separator = new HashSet<Character>(Arrays.asList('[', ']', '(', ')', ' '));
	private static final Set<Character> operator = new HashSet<Character>(
			Arrays.asList('=', '>', '<', '+', '-', '*', '/', '!'));

	void skipToken() {
		if (this.reminder.length() > 0) {
			char c = reminder.charAt(0);
			this.front = "" + c;
			this.frontKind = ERROR;
			this.reminder = this.reminder.substring(1);
			if (c == ' ') {
				this.skipToken();
			} else if (separator.contains(c)) {
				switch (c) {
				case '[':
					this.frontKind = SQBRACKETL;
					break;
				case ']':
					this.frontKind = SQBRACKETR;
					break;
				case '(':
					this.frontKind = BRACKETL;
					break;
				case ')':
					this.frontKind = BRACKETR;
					break;
				default:
					this.frontKind = ERROR;
					break;
				}
			} else if (operator.contains(c)) {
				switch (c) {
				case '=':
					this.frontKind = EQUALITY;
					break;
				case '!':
					if (this.reminder.length() > 0 && this.reminder.charAt(0) == '=') {
						this.front += '=';
						this.frontKind = NOTEQUAL;
						this.reminder = this.reminder.substring(1);
					} else {
						this.frontKind = ERROR;
					}
					break;
				case '>':
					if (this.reminder.length() > 0 && this.reminder.charAt(0) == '=') {
						this.front += '=';
						this.frontKind = COMPGE;
						this.reminder = this.reminder.substring(1);
					} else {
						this.frontKind = COMPGT;
					}
					break;
				case '<':
					if (this.reminder.length() > 0 && this.reminder.charAt(0) == '=') {
						this.front += '=';
						this.frontKind = COMPLE;
						this.reminder = this.reminder.substring(1);
					} else {
						this.frontKind = COMPLT;
					}
					break;
				case '+':
					this.frontKind = PLUS;
					break;
				case '-':
					if (this.reminder.length() > 0 && this.reminder.charAt(0) == ' ') {
						this.frontKind = MINUS;
					} else {
						while (this.reminder.length() > 0 && !separator.contains(this.reminder.charAt(0))
								&& !operator.contains(this.reminder.charAt(0))) {
							this.front += this.reminder.charAt(0);
							this.reminder = this.reminder.substring(1);
						}
						this.frontKind = NUMBER;
					}
					break;
				case '*':
					this.frontKind = MULTIPLY;
					break;
				case '/':
					this.frontKind = DIVISION;
					break;
				default:
					break;
				}
			} else {
				while (this.reminder.length() > 0 && !separator.contains(this.reminder.charAt(0))
						&& !operator.contains(this.reminder.charAt(0))) {
					this.front += this.reminder.charAt(0);
					this.reminder = this.reminder.substring(1);
				}
				if (this.front.length() == 1 && func.contains(c)) {
					switch (c) {
					case 'G':
						this.frontKind = G;
						break;
					case 'F':
						this.frontKind = F;
						break;
					case 'X':
						this.frontKind = X;
						break;
					default:
						break;
					}
				} else {
					if (this.front == this.front.toLowerCase()) {
						if (this.front.equals("and")) {
							this.frontKind = AND;
						} else if (this.front.equals("or")) {
							this.frontKind = OR;
						} else {
							if (isNumeric(this.front)) {
								this.frontKind = NUMBER;
							} else {
								this.frontKind = VARIABLE;
								this.variables.add(this.front);
							}
						}
					} else {
						if (this.front == this.front.toUpperCase()) {
							this.frontKind = CONSTANT;
							this.constants.add(this.front);
						} else {
							this.frontKind = VARIABLE;
							this.variables.add(this.front);
						}
					}
				}
			}
		} else {
			this.front = "END";
			this.frontKind = END;
		}
	}

	private static boolean isNumeric(String str) {
		try {
			Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

}
