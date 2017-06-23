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
		this.cfg.put("model", "true");
		this.ctx = new Context(this.cfg);
		this.exe = new Executer(this.ctx);
		this.solver = this.ctx.mkSolver();
	}
	
	public void config(HashMap<String,String> newCfg){
		this.cfg = newCfg;
		this.ctx = new Context(this.cfg);
		this.exe = new Executer(this.ctx);
		this.solver = this.ctx.mkSolver();
	}
	
	public List<Set<String>> add(String rules){
		List<Set<String>> varCon = new ArrayList<Set<String>>();
		Tokenizer tk = new Tokenizer(rules);
		tk.skipToken();
		ParseTree pt = Parser.parseCond(tk);
		this.solver.add(this.exe.execCond(pt));
		varCon.add(tk.getConstants());
		varCon.add(tk.getVariables());
		return varCon;
	}
	
	public void pop(){
		this.solver.pop();
	}
	
	public void pop(int i){
		this.solver.pop(i);
	}
	
	public void push(){
		this.solver.push();
	}
	
	public String getSolver(){
		return this.solver.toString();
	}
	
	public String solve(){
		return this.solver.check().toString();
	}
}

