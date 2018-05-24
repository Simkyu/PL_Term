package parser.parse;
import java.io.File;
import java.util.StringTokenizer;

import java.io.FileNotFoundException;
import java.util.Iterator;

import parser.ast.*;
import lexer.*;


public class CuteParser {
	
	private Iterator<Token> tokens;
	private static Node END_OF_LIST = new Node(){}; // 새로 추가된 부분
	
	// file을 token화 함.
	public CuteParser(File file) {
		try {
			tokens = Scanner.scan(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		}


	// 다음 토큰을 반환.
	private Token getNextToken() {
		if (!tokens.hasNext()) {
			return null;
		}
		return tokens.next();
	}
	
	
	// 토큰에 따라서 각 토큰의 type과 value를 설정한 노드를 반환한다.
	public Node parseExpr() {
		// 다음 토큰을 확인. 없다면 null
		Token t = getNextToken();
		if (t == null) {
			System.out.println("No more token");
			return null;
		}
		
		// 토큰의 타입과 값
		TokenType tType = t.type();
		String tLexeme = t.lexme();
		
		// type에 따른 idNode, intNode 등 노드를 반환.
		
		// 생성자 변경으로 값 변경 해줘야댐
		// ID와 INT 노드의 생성자가 매개변수를 받은 후 그 값을 value로 삼게 변경됨.
		switch (tType) {
			case ID:
				IdNode idNode = new IdNode(tLexeme);
				return idNode;
				
			case INT:
				IntNode intNode = new IntNode(tLexeme);
				
				if (tLexeme == null) {
					System.out.println("???");
				}
				
				return intNode;
			// BinaryOpNode +, -, /, *가 해당
				
			case DIV:
			case EQ:
			case MINUS:
			case GT:
			case PLUS:
			case TIMES:
			case LT:
				BinaryOpNode binaryNode = new BinaryOpNode();
				binaryNode.setValue(tType);
				return binaryNode;
				
				
			// FunctionNode 키워드가 FunctionNode에 해당
			// 위의 특수문자와 유사하게 처리해 준다. 
			// 위의 경우는  BinaryOpNode로 처리해서 type을 설정하고 반환해주었지만
			// 키워드의 경우는 FunctionNode로 type을 설정하고 반환해 준다.
			case ATOM_Q:
			case CAR:
			case CDR:
			case COND:
			case CONS:
			case DEFINE:
			case EQ_Q:
			case LAMBDA:
			case NOT:
			case NULL_Q:
				FunctionNode functionNode = new FunctionNode();
				functionNode.setValue(tType);
				return functionNode;
					// 내용 채우기 (BinaryOp참고)
				
				
				//새로 구현된 BooleanNode Case
			case FALSE:
				return BooleanNode.FALSE_NODE; 
					
			case TRUE:
				return BooleanNode.TRUE_NODE; 
				//새로 구현된 L_PAREN, R_PAREN Case
				
			case L_PAREN:
				return parseExprList();
			case R_PAREN:
				return END_OF_LIST ;
			//새로 추가된 APOSTROPHE, QUOTE
				
				
			// " ' " 혹은 'QUOTE'가 나올경우 다음 토큰에 있는 값(노드)을
			// parseExpr()을 통해 값(노드)를 반환하고,
			// QuoteNode의 생성자를 통해 값(노드)을 quoted에 저장한다.
			case APOSTROPHE:
				return new QuoteNode(parseExpr());
			case QUOTE:
				return new QuoteNode(parseExpr());
					
			default:
				// head의 next를 만들고 head를 반환하도록 작성
				System.out.println("Parsing Error!");
				return null;
		}
	}

	// List의 value를 생성하는 메소드
	// head와 tail로 구조를 나누게 된다.
	private ListNode parseExprList() {
		
		// parseExpr()을 통해서 기본적으로 다음 토큰으로 이동한 후 판별을 실시
		// L_Paren 이라면 반복해서 순차적으로 실행
		// head 부분이 됨.
		Node head = parseExpr();
		
		if (head == null)
			return null;
		
		if (head == END_OF_LIST) // if next token is RPAREN
			return ListNode.ENDLIST;
		
		// 위의 Node head = parseExpr();다음에 실행되므로 기본적으로 head의 다음부분이 된다.
		// tail에서 parseExprList();가 다시 호출되므로 재귀적으로 처리된다.
		// tail 안에 head와 tail이 있는(또는 head 안에 head와 tail이 있는) 계층구조가 형성된다. 
		ListNode tail = parseExprList();
		
		if (tail == null)
			return null;
		
		// parseExprList의 반복수행을 통해 여러 계층으로 구성된 Node들의 가장 밖의 head 와 tail을 반환해주는 것으로
		// NodePrinter클래스를 통해서 모든 노드들을 탐색할 수 있도록한다.
		return ListNode.cons(head, tail);
		}
}