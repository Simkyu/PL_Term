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
	// ����
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
					
					// #�� ���� ��� T/F �Ǻ��� ���� �ٷ� ACCEPT_BOOL�� �̵���Ų��
					// �� ���� SIGN���� �̵��Ǹ� 
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
	// ID�� ����
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
					//context.getLexime()�� ������ ����ϸ� ó�� ���Ŀ��� ���� ���� ������ ������ �������ش�.
					String contextvalue = context.getLexime();
					
					// ������� ���� ���� Ű����� ������ Ȯ�����ش�.
					// 1. Ű����� ���� ���̶��  => keyword
					// 2. Ű���尡 �ƴϰ� ?�� �������� �ϳ� ������ => QUESTION
					// 3. �� ���� ��쿡 ?�� ���� => invalid value
					// 4. �� �ܿ��� ID
					Token tok = new Token(null,null);
					// type�� contextvalue�� Ÿ���� �����Ѵ�.
					TokenType type = tok.ofName(contextvalue).type();
					
					return GOTO_MATCHED(type, contextvalue);
				default:
					throw new AssertionError();
			}
		}
	},
	// #T/F�� �޾Ƽ� ó���ϱ� ���� �κ��̴�.
	ACCEPT_BOOL {
		@Override
		public TransitionOutput transit(ScanContext context) {
			Char ch = context.getCharStream().nextChar();
			char v = ch.value();
			
			// context.getLexime() (���ݱ��� ����� ��)���� ���� �б⸦ �Ǹ��Ѵ�.
			// context.getLexime()�� �ѹ� ����ϸ� ���� ���� ��������.
			// �׷��� contextvalue��  context.getLexime() ���� �������ش�.
			String contextvalue = context.getLexime();
						
			switch ( ch.type() ) {
				case LETTER:
					// LETTER�� ��� ó�� �Է°��� #�϶� (ó���� BOOL_ACCEPT�� ������ �� �����  contextvalue�� "#"�� �� ����.)
					if(contextvalue.equals("#")) {
						// ���� ���� T or F �̸� 
						// �ٽ� #�� append ���ְ� v���� append ���ش�.
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
					
					// #T, #F �϶� TRUE or FALSE �� Match��Ų��.
					if(contextvalue.equals("#T")) {
						return GOTO_MATCHED(TRUE, contextvalue);
					} else if (contextvalue.equals("#F")) {
						return GOTO_MATCHED(FALSE, contextvalue);
					}
					else {
						return GOTO_FAILED;
					}
				// �� ���� ���� ��� FAILEDó�� �ع�����.
					
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
			
			// context.getLexime() (���ݱ��� ����� ��)���� ���� �б⸦ �Ǹ��Ѵ�.
			// context.getLexime()�� �ѹ� ����ϸ� ���� ���� ��������.
			// �׷��� contextvalue��  context.getLexime() ���� �������ش�.
			String contextvalue = context.getLexime();
			
			switch ( ch.type() ) {
				case LETTER:
					return GOTO_FAILED;
					
				// '+', '-' ������ ���ڰ� ������ ���Ŀ��� INT�� �Ѿ ���, ������ ó���ȴ�.
				// contextvalue�� �����ϴ� �κп��� context.getLexime()�� ����
				// ������ �ַµǾ� �ִ� Ư�� ��ȣ ��('+' or '-')�� ������Ƿ� append�� ���� ���ش�.
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
				// '+', '-' ������ ������ ���ýÿ��� �������� PLUS �� MINUS ��ȣ�� �����Ѵ�.
				case WS:				
				case END_OF_STREAM:		
				
					// Ư�� ��ȣ���� �´� ������ ���� ��� �ڿ� ������ ���� �����Ƿ� �ٷ� ó���Ѵ�.
					// fromSpecialCharactor(v)�� ���� �̸� ������ type���� ���� �ҷ��´�.
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
	// ��
	EOS {
		@Override
		public TransitionOutput transit(ScanContext context) {
			return GOTO_EOS;
		}
	};
	
	
	abstract TransitionOutput transit(ScanContext context);

	}

