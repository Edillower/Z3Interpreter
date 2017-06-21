package z3interpreter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;

public class Interpreter {
	private HashMap<String, String> cfg = new HashMap<String, String>();
	private Context ctx;
	private Executer exe;
	private Solver solver;
	
	public Interpreter(){
		cfg.put("model", "true");
		ctx = new Context(cfg);
		exe = new Executer(ctx);
		solver = ctx.mkSolver();
	}
	
	public void config(HashMap<String,String> newCfg){
		cfg = newCfg;
		ctx = new Context(cfg);
		exe = new Executer(ctx);
		solver = ctx.mkSolver();
	}
	
	public List<Set<String>> add(String rules){
		List<Set<String>> varCon = new ArrayList<Set<String>>();
		Tokenizer tk = new Tokenizer(rules);
		tk.skipToken();
		ParseTree pt = Parser.parseCond(tk);
		solver.add(exe.execCond(pt));
		varCon.add(tk.getConstants());
		varCon.add(tk.getVariables());
		return varCon;
	}
	
	public void pop(){
		solver.pop();
	}
	
	public void pop(int i){
		solver.pop(i);
	}
	
	public void push(){
		solver.push();
	}
	
	public String getSolver(){
		return solver.toString();
	}
	
	public String solve(){
		return solver.check().toString();
	}
}

