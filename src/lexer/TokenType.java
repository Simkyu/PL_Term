package lexer;


public enum TokenType {
	// ����ó�� (?ó�� : Ű���� ���� �ƴ���)
	ID, ATOM_Q, NULL_Q, EQ_Q, QUESTION,
	
	// ���� ó��( '+','-' ��ȣ�� ���ڿ� ��ȣ ����ó��)
	INT, PLUS, MINUS,
	
	// ��ȣ, ��ȣ
	TIMES, DIV, 
	LT, GT, EQ, APOSTROPHE,
	L_PAREN, R_PAREN,
	
	// Ű����
	DEFINE, LAMBDA, COND, QUOTE,
	CAR, CDR, CONS, NOT,
	
	// #T, #F
	TRUE, FALSE;
	
	
	// Ư�������� Ű���带 ��ȯ�Ѵ�.
	static TokenType fromSpecialCharactor(char ch) {
		switch(ch) {
		// ���� ǥ������ �����Ͽ� ch�� ��Ī�Ǵ� Ű���带 ��ȯ�ϴ� case�� �ۼ�
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
		// quote�� ���� �߰� case'`'
		case '\'': case '`':
			return APOSTROPHE;
		default :
			throw new IllegalArgumentException("unregistered char : " + ch);
		
		
		}
		
	}
}
