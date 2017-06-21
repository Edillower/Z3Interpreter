package z3interpreter;

import java.util.*;

import com.microsoft.z3.*;

class Executer {
	private Context ctx;

	Executer(Context execContext) {
		ctx = execContext;
	}

	static final Set<String> func = new HashSet<String>(Arrays.asList("G", "F", "X"));

	BoolExpr execCond(ParseTree p) {
		BoolExpr result = null;
		if (p.kind.equals("cond")) {
			if (func.contains(p.attributes)) {
				// TODO: G F X function operation
			} else {
				if (p.attributes.equals("or")) {
					result = ctx.mkOr(execCondS(p.children.get(0)), execCond(p.children.get(1)));
				} else {
					result = execCondS(p.children.get(0));
				}
			}
		} else {
			Utilities.showTreeKindError("cond");
		}
		return result;
	}

	private BoolExpr execCondS(ParseTree p) {
		BoolExpr result = null;
		if (p.kind.equals("condS")) {
			if (p.attributes.equals("and")) {
				result = ctx.mkAnd(execComp(p.children.get(0)), execCondS(p.children.get(1)));
			} else {
				result = execComp(p.children.get(0));
			}
		} else {
			Utilities.showTreeKindError("condS");
		}
		return result;
	}

	private BoolExpr execComp(ParseTree p) {
		BoolExpr result = null;
		if (p.kind.equals("comp")) {
			if (p.attributes.equals("cond")) {
				result = execCond(p.children.get(0));
			} else {
				ArithExpr left = execExp(p.children.get(0));
				ArithExpr right = execExp(p.children.get(2));
				String operator = p.children.get(1).attributes;
				if (operator.equals("=")) {
					result = ctx.mkEq(left, right);
				} else if (operator.equals("!=")) {
					result = ctx.mkNot(ctx.mkEq(left, right));
				} else if (operator.equals(">")) {
					result = ctx.mkGt(left, right);
				} else if (operator.equals(">=")) {
					result = ctx.mkGe(left, right);
				} else if (operator.equals("<")) {
					result = ctx.mkLt(left, right);
				} else if (operator.equals("<=")) {
					result = ctx.mkLe(left, right);
				} else {
					Utilities.showOpKindError("comp");
				}
			}
		} else {
			Utilities.showTreeKindError("comp");
		}
		return result;
	}

	// private Map<String, ArithExpr> constants = new HashMap<String,
	// ArithExpr>();
	// private Map<String, ArithExpr> variables = new HashMap<String,
	// ArithExpr>();

	private ArithExpr execOp(ParseTree p) {
		ArithExpr result = null;
		if (p.kind.equals("op")) {
			if (p.attributes.equals("exp")) {
				result = execExp(p.children.get(0));
			} else if (p.attributes.equals("number")) {
				result = ctx.mkReal(p.children.get(0).attributes);
			} else if (p.attributes.equals("constant")) {
				// String temp = p.children.get(0).attributes;
				// if (constants.containsKey(temp)) {
				// result = constants.get(temp);
				// } else {
				result = ctx.mkRealConst(p.children.get(0).attributes);
				// constants.put(temp, result);
				// }
			} else if (p.attributes.equals("variable")) {
				// String temp = p.children.get(0).attributes;
				// if (variables.containsKey(temp)) {
				// result = variables.get(temp);
				// } else {
				result = ctx.mkRealConst(p.children.get(0).attributes);
				// variables.put(temp, result);
				// }
			} else {
				Utilities.showOpKindError("op");
			}
		} else {
			Utilities.showTreeKindError("op");
		}
		return result;
	}

	private ArithExpr execExp(ParseTree p) {
		ArithExpr result = null;
		if (p.kind.equals("exp")) {
			ArithExpr current = execTrm(p.children.get(0));
			result = execExpS(p.children.get(1), current);
		} else {
			Utilities.showTreeKindError("exp");
		}
		return result;
	}

	private ArithExpr execExpS(ParseTree p, ArithExpr prev) {
		ArithExpr result = null;
		if (p.kind.equals("expS")) {
			ArithExpr current = null;
			if (p.attributes.equals("+")) {
				current = ctx.mkAdd(prev, execTrm(p.children.get(0)));
				result = execExpS(p.children.get(1), current);
			} else if (p.attributes.equals("-")) {
				current = ctx.mkSub(prev, execTrm(p.children.get(0)));
				result = execExpS(p.children.get(1), current);
			} else {
				result = prev;
			}
		} else {
			Utilities.showTreeKindError("expS");
		}
		return result;
	}

	private ArithExpr execTrm(ParseTree p) {
		ArithExpr result = null;
		if (p.kind.equals("trm")) {
			ArithExpr current = execOp(p.children.get(0));
			result = execTrmS(p.children.get(1), current);
		} else {
			Utilities.showTreeKindError("trm");
		}
		return result;
	}

	private ArithExpr execTrmS(ParseTree p, ArithExpr prev) {
		ArithExpr result = null;
		if (p.kind.equals("trmS")) {
			ArithExpr current = null;
			if (p.attributes.equals("*")) {
				current = ctx.mkMul(prev, execOp(p.children.get(0)));
				result = execTrmS(p.children.get(1), current);
			} else if (p.attributes.equals("/")) {
				current = ctx.mkDiv(prev, execOp(p.children.get(0)));
				result = execTrmS(p.children.get(1), current);
			} else {
				result = prev;
			}
		} else {
			Utilities.showTreeKindError("trmS");
		}
		return result;
	}

}
