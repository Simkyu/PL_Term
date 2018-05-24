package lexer;

import java.util.HashMap;
import java.util.Map;

public class Token {
	private final TokenType type;
	private final String lexme;
	
	// 현재까지 들어온 값이 키워드와 같은지 확인해준다.
		// 1. 키워드와 같은 값이라면  => keyword
		// 2. 키워드가 아니고 ?가 마지막에 하나 있을때 => QUESTION
		// 3. 그 외의 경우에 ?를 포함 => invalid value
		// 4. 그 외에는 ID
	static Token ofName(String lexme) {
		TokenType type = KEYWORDS.get(lexme);
		if(type != null) {
			return new Token(type,lexme);
		}
		// '?'로 끝날 때 : 
		else if (lexme.endsWith("?")) {
			// 마지막 '?'를 제외하고 '?'를 포함할 떄
			if (lexme.substring(0, lexme.length()-1).contains("?")) {
				throw new ScannerException("invalid ID = " +lexme);
			}
			// 키워드가 아니고 xxxx?로 끝나면 QUESTION
			return new Token(TokenType.QUESTION, lexme);
		}
		// 그 외에 '?'를 포함할 때
		else if (lexme.contains("?")) {
			throw new ScannerException("invalid ID =" + lexme);
		// 그 외에 
		} else {
			return new Token(TokenType.ID, lexme);
		}
	}
	
	Token(TokenType type, String lexme) {
		this.type = type;
		this.lexme = lexme;
	}
	
	public TokenType type() {
		return this.type;
	}
	
	public String lexme() {
		return this.lexme;
	}
	
	@Override
	public String toString() {
		return String.format("%s(%s)", type, lexme);
	}
	
	// KEYWORDS를 미리 설정해놓는 것을 통해서 위의 ofName()메소드에서 타입을 keyword로 결정할 수 있다.
	public static final Map<String, TokenType> KEYWORDS = new HashMap<>();
	static {
		KEYWORDS.put("define", TokenType.DEFINE);
		KEYWORDS.put("lambda", TokenType.LAMBDA);
		KEYWORDS.put("cond", TokenType.COND);
		KEYWORDS.put("quote", TokenType.QUOTE);
		KEYWORDS.put("not", TokenType.NOT);
		KEYWORDS.put("cdr", TokenType.CDR);
		KEYWORDS.put("car", TokenType.CAR);
		KEYWORDS.put("cons", TokenType.CONS);
		KEYWORDS.put("eq?", TokenType.EQ_Q);
		KEYWORDS.put("null?", TokenType.NULL_Q);
		KEYWORDS.put("atom?", TokenType.ATOM_Q);
		
	}
}
