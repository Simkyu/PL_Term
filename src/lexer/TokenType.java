package lexer;


public enum TokenType {
	// 문자처리 (?처리 : 키워드 인지 아닌지)
	ID, ATOM_Q, NULL_Q, EQ_Q, QUESTION,
	
	// 숫자 처리( '+','-' 기호는 숫자와 기호 이중처리)
	INT, PLUS, MINUS,
	
	// 기호, 괄호
	TIMES, DIV, 
	LT, GT, EQ, APOSTROPHE,
	L_PAREN, R_PAREN,
	
	// 키워드
	DEFINE, LAMBDA, COND, QUOTE,
	CAR, CDR, CONS, NOT,
	
	// #T, #F
	TRUE, FALSE;
	
	
	// 특수문자의 키워드를 반환한다.
	static TokenType fromSpecialCharactor(char ch) {
		switch(ch) {
		// 정규 표현식을 참고하여 ch와 매칭되는 키워드를 반환하는 case문 작성
		case '-':
			return MINUS;
		case '+':
			return PLUS;
		case '(': 
			return L_PAREN;
		case ')':  
			return R_PAREN;
		case '*':
			return TIMES;
		case '/': 
			return DIV;
		case '<': 
			return LT;
		case '=': 
			return EQ;
		case '>':
			return GT;
		// quote를 위해 추가 case'`'
		case '\'': case '`':
			return APOSTROPHE;
		default :
			throw new IllegalArgumentException("unregistered char : " + ch);
		
		
		}
		
	}
}
