package lexer;

import static lexer.TokenType.ID;
import static lexer.TokenType.INT;
import static lexer.TokenType.TRUE;
import static lexer.TokenType.FALSE;
import static lexer.TokenType.*;
import static lexer.TransitionOutput.GOTO_ACCEPT_ID;
import static lexer.TransitionOutput.GOTO_ACCEPT_BOOL;
import static lexer.TransitionOutput.GOTO_ACCEPT_INT;
import static lexer.TransitionOutput.GOTO_EOS;
import static lexer.TransitionOutput.GOTO_FAILED;
import static lexer.TransitionOutput.GOTO_MATCHED;
import static lexer.TransitionOutput.GOTO_SIGN;
import static lexer.TransitionOutput.GOTO_START;



enum State {
	// 시작
	START {
		@Override
		public TransitionOutput transit(ScanContext context) {
			Char ch = context.getCharStream().nextChar();
			char v = ch.value();
			switch ( ch.type() ) {
				case LETTER:
					context.append(v);
					return GOTO_ACCEPT_ID;
				case DIGIT:
					context.append(v);
					return GOTO_ACCEPT_INT;
				case SPECIAL_CHAR:	
					context.append(v);
					
					// #이 들어올 경우 T/F 판별을 위해 바로 ACCEPT_BOOL로 이동시킨다
					// 이 값이 SIGN으로 이동되면 
					if(v == '#') {
						return GOTO_ACCEPT_BOOL;
					}
					return GOTO_SIGN;
				case WS:
					return GOTO_START;
				case END_OF_STREAM:
					return GOTO_EOS;
				default:
					throw new AssertionError();
			}
		}
	},
	// ID를 받음
	ACCEPT_ID {
		@Override
		public TransitionOutput transit(ScanContext context) {
			Char ch = context.getCharStream().nextChar();
			char v = ch.value();
			switch ( ch.type() ) {
				case LETTER:
				case DIGIT:
					context.append(v);
					return GOTO_ACCEPT_ID;
				case SPECIAL_CHAR:
					return GOTO_FAILED;
				case WS:
				case END_OF_STREAM:
					//context.getLexime()를 여러번 사용하면 처음 이후에는 값이 없기 때문에 변수에 저장해준다.
					String contextvalue = context.getLexime();
					
					// 현재까지 들어온 값이 키워드와 같은지 확인해준다.
					// 1. 키워드와 같은 값이라면  => keyword
					// 2. 키워드가 아니고 ?가 마지막에 하나 있을때 => QUESTION
					// 3. 그 외의 경우에 ?를 포함 => invalid value
					// 4. 그 외에는 ID
					Token tok = new Token(null,null);
					// type에 contextvalue의 타입을 저장한다.
					TokenType type = tok.ofName(contextvalue).type();
					
					return GOTO_MATCHED(type, contextvalue);
				default:
					throw new AssertionError();
			}
		}
	},
	// #T/F를 받아서 처리하기 위한 부분이다.
	ACCEPT_BOOL {
		@Override
		public TransitionOutput transit(ScanContext context) {
			Char ch = context.getCharStream().nextChar();
			char v = ch.value();
			
			// context.getLexime() (지금까지 저장된 값)으로 다음 분기를 판명한다.
			// context.getLexime()은 한번 사용하면 안의 값이 없어진다.
			// 그래서 contextvalue에  context.getLexime() 값을 저장해준다.
			String contextvalue = context.getLexime();
						
			switch ( ch.type() ) {
				case LETTER:
					// LETTER의 경우 처음 입력값이 #일때 (처음에 BOOL_ACCEPT에 들어왔을 때 말고는  contextvalue가 "#"일 수 없음.)
					if(contextvalue.equals("#")) {
						// 현재 값이 T or F 이면 
						// 다시 #을 append 해주고 v값도 append 해준다.
						if(v == 'T' || v=='F') {
							context.append('#');
							context.append(v);
							return GOTO_ACCEPT_BOOL;
						}
					}
					
					return GOTO_FAILED;
					
				case DIGIT:
				case SPECIAL_CHAR:
					return GOTO_FAILED;
				case WS:
				case END_OF_STREAM:
					
					// #T, #F 일때 TRUE or FALSE 로 Match시킨다.
					if(contextvalue.equals("#T")) {
						return GOTO_MATCHED(TRUE, contextvalue);
					} else if (contextvalue.equals("#F")) {
						return GOTO_MATCHED(FALSE, contextvalue);
					}
					else {
						return GOTO_FAILED;
					}
				// 그 외의 경우는 모두 FAILED처리 해버린다.
					
				default:
					throw new AssertionError();
			}
		}
	},
	ACCEPT_INT {
		@Override
		public TransitionOutput transit(ScanContext context) {
			Char ch = context.getCharStream().nextChar();
			switch ( ch.type() ) {
				case LETTER:
					return GOTO_FAILED;
				case DIGIT:
					context.append(ch.value());
					return GOTO_ACCEPT_INT;
				case SPECIAL_CHAR:
					return GOTO_FAILED;
				case WS:
				case END_OF_STREAM:
					return GOTO_MATCHED(INT, context.getLexime());
				default:
					throw new AssertionError();
			}
		}
	},
	SIGN {
		@Override
		public TransitionOutput transit(ScanContext context) {
			Char ch = context.getCharStream().nextChar();
			char v = ch.value();
			
			// context.getLexime() (지금까지 저장된 값)으로 다음 분기를 판명한다.
			// context.getLexime()은 한번 사용하면 안의 값이 없어진다.
			// 그래서 contextvalue에  context.getLexime() 값을 저장해준다.
			String contextvalue = context.getLexime();
			
			switch ( ch.type() ) {
				case LETTER:
					return GOTO_FAILED;
					
				// '+', '-' 다음에 숫자가 나오면 이후에는 INT로 넘어가 양수, 정수로 처리된다.
				// contextvalue를 선언하는 부분에서 context.getLexime()를 통해
				// 기존에 있력되어 있던 특수 기호 값('+' or '-')가 사라지므로 append를 따로 해준다.
				case DIGIT:
					if(contextvalue.equals(String.valueOf('+'))){
						context.append('+');
						context.append(v);
						return GOTO_ACCEPT_INT;
						
					} else if(contextvalue.equals(String.valueOf('-'))){
						context.append('-');
						context.append(v);
						return GOTO_ACCEPT_INT;
					} else {
						return GOTO_FAILED;
					}
				// '+', '-' 다음에 공백이 나올시에는 독립적인 PLUS 와 MINUS 기호로 설정한다.
				case WS:				
				case END_OF_STREAM:		
				
					// 특수 기호값에 맞는 값들이 들어올 경우 뒤에 나오는 값이 없으므로 바로 처리한다.
					// fromSpecialCharactor(v)를 통해 미리 설정한 type으로 값을 불러온다.
					return GOTO_MATCHED(fromSpecialCharactor(contextvalue.charAt(0)),contextvalue);
					
				default:
					throw new AssertionError();
			}
		}
	},
	MATCHED {
		@Override
		public TransitionOutput transit(ScanContext context) {
			throw new IllegalStateException("at final state");
		}
	},
	FAILED{
		@Override
		public TransitionOutput transit(ScanContext context) {
			throw new IllegalStateException("at final state");
		}
	},
	// 끝
	EOS {
		@Override
		public TransitionOutput transit(ScanContext context) {
			return GOTO_EOS;
		}
	};
	
	
	abstract TransitionOutput transit(ScanContext context);

	}

