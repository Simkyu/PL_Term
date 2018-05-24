package lexer;

class Char {
	private final char value;
	private final CharacterType type;

	// 타입 : 문자, 숫자, 기호, 공백, 마지막 스트림
	enum CharacterType {
		LETTER, DIGIT, SPECIAL_CHAR, WS, END_OF_STREAM,
	}
	
	// ch의 타입 확인
	static Char of(char ch) {
		return new Char(ch, getType(ch));
	}
	
	// 마지막 
	static Char end() {
		return new Char(Character.MIN_VALUE, CharacterType.END_OF_STREAM);
	}
	
	// Char 생성자 : 값, 타입 설정.
	private Char(char ch, CharacterType type) {
		this.value = ch;
		this.type = type;
	}
	
	// Char 값 반환
	char value() {
		return this.value;
	}
	
	// Char 타입 반환
	CharacterType type() {
		return this.type;
	}
	
	
	private static CharacterType getType(char ch) {
		int code = (int)ch;
		
		// 문자는 타입 LETTER		
		if ( (code >= (int)'A' && code <= (int)'Z')
			|| (code >= (int)'a' && code <= (int)'z')) {
			return CharacterType.LETTER;
		}
		
		// 숫자는 타입 DIGIT
		if ( Character.isDigit(ch) ) {
			return CharacterType.DIGIT;
		}
		

		// 모든 부호들을  SPECIAL_CHAR의 상태 부여.
		// keyword와 QUESTION을 위해서 '?' 조건 추가해주었다.
		// QUESTION과 keywords의 판별은 동시에 Token클래스의 ofName의 메소드에서 실행된다.
		// 메소드에서의 판별할 때 분기점에 ID, Keywords, QUESTION이므 '?'만 LETTER타입으로 결정해 주었다(ID타입들과 어울림).
		// '?'를 추가하지 않을 경우 '?'는 적당한 타입을 반환하지 않으므로 맨아래의 execption으로 처리된다.
		// quote를 위해 추가 case'`'
		switch ( ch ) {
			case '?' :
				return CharacterType.LETTER;
			case '-': case '+':
			case '(': case ')':  
			case '*': case '/': case '<': case '=': 
			case '>': case '\'': case '#': case'`':
				return CharacterType.SPECIAL_CHAR;
		}

		
		// 공백은 타입 WS
		if ( Character.isWhitespace(ch) ) {
			return CharacterType.WS;
		}
		
		// 올바르지 않은 값(설정되지 않은 값이 들어왔을 때)
		throw new IllegalArgumentException("input=" + ch);
	}
}
