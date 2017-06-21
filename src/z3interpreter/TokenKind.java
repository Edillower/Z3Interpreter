package z3interpreter;

enum TokenKind {
	/**
	 * 0|Out_Signal|Variable|Capitalized words, space must be represented as _
	 */
	VARIABLE(0),
	
	/**
	 * 0|IN_FLOWRATE_NORMAL|Constant|All capitalized words, space must be represented as _
	 */
	CONSTANT(0),
	
	/**
	 * 0|2,2.0,2.4,0.4|Number|Treated as real number in Z3 .5 must be written as 0.5
	 */
	NUMBER(0),
	
	/**
	 * 1|=|Comparators, Logic Operators|Treated as == in Z3
	 */
	EQUALITY(1),
	
	/**
	 * 1|!=|Comparators, Logic Operators
	 */
	NOTEQUAL(1),
	
	/**
	 * 1|>|Comparators, Logic Operators|
	 */
	COMPGT(1),
	
	/**
	 * 1|<|Comparators, Logic Operators|
	 */
	COMPLT(1),
	
	/**
	 * 1|>=|Comparators, Logic Operators|
	 */
	COMPGE(1),
	
	/**
	 * 1|<=|Comparators, Logic Operators|
	 */
	COMPLE(1),
	
	/**
	 * 2|and|Logic Operators|Reserved lowercase words
	 */
	AND(2),
	
	/**
	 * 2|or|Logic Operators|Reserved lowercase words
	 */
	OR(2),
	
	/**
	 * 3|+|Arithmetic Operators|
	 */
	PLUS(3),
	
	/**
	 * 3|-0|Arithmetic Operators|
	 */
	MINUS(3),
	
	/**
	 * 3|*|Arithmetic Operators|
	 */
	MULTIPLY(3),
	
	/**
	 * 3|/|Arithmetic Operators|
	 */
	DIVISION(3),
	
	/**
	 * 4|G()|G Function|TODO
	 */
	G(4),
	
	/**
	 * 4|G()|G Function|TODO
	 */
	X(4),
	
	/**
	 * 4|G()|G Function|TODO
	 */
	F(4),
	
	/**
	 * 5|space|Separator|
	 */
	SPACE(5),
	
	/**
	 * 5|(|Separator|
	 */
	BRACKETL(5),
	
	/**
	 * 5|)|Separator|
	 */
	BRACKETR(5),
	
	/**
	 * 5|[|Separator|
	 */
	SQBRACKETL(5),
	
	/**
	 * 5|]|Separator|
	 */
	SQBRACKETR(5),
	
	/**
	 * 8|BEGIN
	 */
	BEGIN(8),
	
	/**
	 * 8|END
	 */
	END(8),
	
	/**
	 * 9|ERROR
	 */
	ERROR(9);

	private int tokenNum;

	/**
	 * Constructor.
	 */
	private TokenKind(int number) {
		this.tokenNum = number;
	}

	int getTokenNumber() {
		return this.tokenNum;
	}
}
