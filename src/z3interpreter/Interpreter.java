package z3interpreter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;

public class Interpreter {
	private HashMap<String, String> cfg = new HashMap<String, String>();
	private Context ctx;
	private Executer exe;
	private Solver solver;

	public Interpreter() {
		this.cfg.put("model", "true");
		this.ctx = new Context(this.cfg);
		this.exe = new Executer(this.ctx);
		this.solver = this.ctx.mkSolver();
	}

	public Interpreter(Context mCtx) {
		this.ctx = mCtx;
		this.exe = new Executer(this.ctx);
		this.solver = this.ctx.mkSolver();
	}

	public void config(HashMap<String, String> newCfg) {
		this.cfg = newCfg;
		this.ctx = new Context(this.cfg);
		this.exe = new Executer(this.ctx);
		this.solver = this.ctx.mkSolver();
	}

	public List<Set<String>> add(String rules) {
		List<Set<String>> varCon = new ArrayList<Set<String>>();
		Tokenizer tk = new Tokenizer(rules);
		tk.skipToken();
		ParseTree pt = Parser.parseCond(tk);
		this.solver.add(this.exe.execCond(pt));
		varCon.add(tk.getConstants());
		varCon.add(tk.getVariables());
		return varCon;
	}

	public BoolExpr getBoolExpr(String rules) {
		Tokenizer tk = new Tokenizer(rules);
		tk.skipToken();
		ParseTree pt = Parser.parseCond(tk);
		return this.exe.execCond(pt);
	}

	public void add(BoolExpr rules) {
		this.solver.add(rules);
	}

	public void pop() {
		this.solver.pop();
	}

	public void pop(int i) {
		this.solver.pop(i);
	}

	public void push() {
		this.solver.push();
	}

	public String getSolver() {
		return this.solver.toString();
	}

	public String solve() {
		return this.solver.check().toString();
	}

	public String checkValidity(String rules) {
		this.solver.push();
		Tokenizer tk = new Tokenizer(rules);
		tk.skipToken();
		ParseTree pt = Parser.parseCond(tk);
		BoolExpr rulesBE = this.exe.execCond(pt);
		this.solver.add(rulesBE);
		String r1 = this.solver.check().toString();
		this.solver.pop();
		this.solver.push();
		this.solver.add(this.ctx.mkNot(rulesBE));
		String r2 = this.solver.check().toString();
		this.solver.pop();
		String validity = r1;
		if (r1.equals("SATISFIABLE") && r2.equals("UNSATISFIABLE")) {
			validity = "VALID";
		}
		return validity;
	}

	public String checkValidity(BoolExpr rules) {
		this.solver.push();
		this.solver.add(rules);
		String r1 = this.solver.check().toString();
		this.solver.pop();
		this.solver.push();
		this.solver.add(this.ctx.mkNot(rules));
		String r2 = this.solver.check().toString();
		this.solver.pop();
		String validity = r1;
		if (r1.equals("SATISFIABLE") && r2.equals("UNSATISFIABLE")) {
			validity = "VALID";
		}
		return validity;
	}

	public void closeContext(){
		this.ctx.close();
	}
}
