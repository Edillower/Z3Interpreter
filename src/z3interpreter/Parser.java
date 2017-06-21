package z3interpreter;

import static z3interpreter.TokenKind.*;

class Parser {
	static ParseTree parseCond(Tokenizer tk) {
		ParseTree p = new ParseTree("cond", "");
		if (tk.getTokenVal() == 4) {
			p.attributes = tk.getTokenName();
			tk.skipToken();
			if (tk.getTokenKind() == BRACKETL) {
				tk.skipToken();
				p.addChild(parseCond(tk));
				if (tk.getTokenKind() == BRACKETR) {
					tk.skipToken();
				} else {
					Utilities.showExpectedError("cond", "right bracket", "G/F/X");
				}
			} else {
				Utilities.showExpectedError("cond", "left bracket", "G/F/X");
			}
		} else {
			p.addChild(parseCondS(tk));
			if (tk.getTokenKind() == OR) {
				p.attributes = tk.getTokenName();
				tk.skipToken();
				p.addChild(parseCond(tk));
			}
		}
		return p;
	}

	static ParseTree parseCondS(Tokenizer tk) {
		ParseTree p = new ParseTree("condS", "");
		p.addChild(parseComp(tk));
		if (tk.getTokenKind() == AND) {
			p.attributes = tk.getTokenName();
			tk.skipToken();
			p.addChild(parseCondS(tk));
		}
		return p;
	}

	static ParseTree parseComp(Tokenizer tk) {
		ParseTree p = new ParseTree("comp", "");
		if (tk.getTokenKind() == SQBRACKETL) {
			tk.skipToken();
			p.attributes = "cond";
			p.addChild(parseCond(tk));
			if (tk.getTokenKind() == SQBRACKETR) {
				tk.skipToken();
			} else {
				Utilities.showExpectedError("cond", "right square bracket");
			}
		} else {
			p.addChild(parseExp(tk));
			p.addChild(parseCompOp(tk));
			p.addChild(parseExp(tk));
		}
		return p;
	}

	static ParseTree parseCompOp(Tokenizer tk) {
		ParseTree p = new ParseTree("compop", "");
		if (tk.getTokenVal() == 1) {
			p.attributes = tk.getTokenName();
		} else {
			Utilities.showExpectedError("compop", "symbol =/!=/>/>=/</<=");
		}
		tk.skipToken();
		return p;
	}

	static ParseTree parseOp(Tokenizer tk) {
		ParseTree p = new ParseTree("op", "");
		if (tk.getTokenKind() == BRACKETL) {
			tk.skipToken();
			p.attributes = "exp";
			p.addChild(parseExp(tk));
			if (tk.getTokenKind() == BRACKETR) {
				tk.skipToken();
			} else {
				Utilities.showExpectedError("op", "right bracket", "exp");
			}
		} else if (tk.getTokenKind() == NUMBER) {
			p.attributes = "number";
			p.addChild(parseNumber(tk));
		} else if (tk.getTokenKind() == CONSTANT) {
			p.attributes = "constant";
			p.addChild(parseConstant(tk));
		} else if (tk.getTokenKind() == VARIABLE) {
			p.attributes = "variable";
			p.addChild(parseVariable(tk));
		} else {
			Utilities.showExpectedError("op", "expected number/variable/constand/(experession)");
		}
		return p;
	}

	static ParseTree parseNumber(Tokenizer tk) {
		ParseTree p = new ParseTree("number", tk.getTokenName());
		tk.skipToken();
		return p;
	}

	static ParseTree parseVariable(Tokenizer tk) {
		ParseTree p = new ParseTree("variable", tk.getTokenName());
		tk.skipToken();
		return p;
	}

	static ParseTree parseConstant(Tokenizer tk) {
		ParseTree p = new ParseTree("constant", tk.getTokenName());
		tk.skipToken();
		return p;
	}

	static ParseTree parseExp(Tokenizer tk) {
		ParseTree p = new ParseTree("exp", "");
		p.addChild(parseTrm(tk));
		p.addChild(parseExpS(tk));
		return p;
	}

	private static ParseTree parseExpS(Tokenizer tk) {
		ParseTree p = new ParseTree("expS", "");
		if (tk.getTokenKind() == PLUS || tk.getTokenKind() == MINUS) {
			p.attributes = tk.getTokenName();
			tk.skipToken();
			p.addChild(parseTrm(tk));
			p.addChild(parseExpS(tk));
		}
		return p;
	}

	static ParseTree parseTrm(Tokenizer tk) {
		ParseTree p = new ParseTree("trm", "");
		p.addChild(parseOp(tk));
		p.addChild(parseTrmS(tk));
		return p;
	}

	static ParseTree parseTrmS(Tokenizer tk) {
		ParseTree p = new ParseTree("trmS", "");
		if (tk.getTokenKind() == MULTIPLY || tk.getTokenKind() == DIVISION) {
			p.attributes = tk.getTokenName();
			tk.skipToken();
			p.addChild(parseOp(tk));
			p.addChild(parseTrmS(tk));
		}
		return p;
	}

}
