package lexer;

import java.util.Optional;

class TransitionOutput {
	private final State nextState;
	private final Optional<Token> token;

	// 다음의 실행(상태) 설정
	// 시작, ID값 읽기, INT값 읽기, 특수기호값 읽기, 실패(오류), 끝
	static TransitionOutput GOTO_START = new TransitionOutput(State.START);
	static TransitionOutput GOTO_ACCEPT_ID = new TransitionOutput(State.ACCEPT_ID);
	static TransitionOutput GOTO_ACCEPT_INT = new TransitionOutput(State.ACCEPT_INT);
	static TransitionOutput GOTO_SIGN = new TransitionOutput(State.SIGN);
	static TransitionOutput GOTO_FAILED = new TransitionOutput(State.FAILED);
	static TransitionOutput GOTO_EOS = new TransitionOutput(State.EOS);
	
	// #T/F의 처리를 위해 ACCEPT_BOOL로 가는 길(GOTO_ACCEPT_BOOL)을 만들어준다.
	static TransitionOutput GOTO_ACCEPT_BOOL = new TransitionOutput(State.ACCEPT_BOOL);
	
	
	
	static TransitionOutput GOTO_MATCHED(TokenType type, String lexime) {
		return new TransitionOutput(State.MATCHED, new Token(type, lexime));
	}
	
	static TransitionOutput GOTO_MATCHED(Token token) {
		return new TransitionOutput(State.MATCHED, token);
	}
	
	TransitionOutput(State nextState, Token token) {
		this.nextState = nextState;
		this.token = Optional.of(token);
	}
	
	TransitionOutput(State nextState) {
		this.nextState = nextState;
		this.token = Optional.empty();
	}
	
	State nextState() {
		return this.nextState;
	}
	
	Optional<Token> token() {
		return this.token;
	}
}