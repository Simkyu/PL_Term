package parser.parse;
import java.io.File;
import java.util.StringTokenizer;

import java.io.FileNotFoundException;
import java.util.Iterator;

import parser.ast.*;
import lexer.*;


public class CuteParser {
	
	private Iterator<Token> tokens;
	private static Node END_OF_LIST = new Node(){}; // ���� �߰��� �κ�
	
	// file�� tokenȭ ��.
	public CuteParser(File file) {
		try {
			tokens = Scanner.scan(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		}


	// ���� ��ū�� ��ȯ.
	private Token getNextToken() {
		if (!tokens.hasNext()) {
			return null;
		}
		return tokens.next();
	}
	
	
	// ��ū�� ���� �� ��ū�� type�� value�� ������ ��带 ��ȯ�Ѵ�.
	public Node parseExpr() {
		// ���� ��ū�� Ȯ��. ���ٸ� null
		Token t = getNextToken();
		if (t == null) {
			System.out.println("No more token");
			return null;
		}
		
		// ��ū�� Ÿ�԰� ��
		TokenType tType = t.type();
		String tLexeme = t.lexme();
		
		// type�� ���� idNode, intNode �� ��带 ��ȯ.
		
		// ������ �������� �� ���� ����ߴ�
		// ID�� INT ����� �����ڰ� �Ű������� ���� �� �� ���� value�� ��� �����.
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
			// BinaryOpNode +, -, /, *�� �ش�
				
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
				
				
			// FunctionNode Ű���尡 FunctionNode�� �ش�
			// ���� Ư�����ڿ� �����ϰ� ó���� �ش�. 
			// ���� ����  BinaryOpNode�� ó���ؼ� type�� �����ϰ� ��ȯ���־�����
			// Ű������ ���� FunctionNode�� type�� �����ϰ� ��ȯ�� �ش�.
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
					// ���� ä��� (BinaryOp����)
				
				
				//���� ������ BooleanNode Case
			case FALSE:
				return BooleanNode.FALSE_NODE; 
					
			case TRUE:
				return BooleanNode.TRUE_NODE; 
				//���� ������ L_PAREN, R_PAREN Case
				
			case L_PAREN:
				return parseExprList();
			case R_PAREN:
				return END_OF_LIST ;
			//���� �߰��� APOSTROPHE, QUOTE
				
				
			// " ' " Ȥ�� 'QUOTE'�� ���ð�� ���� ��ū�� �ִ� ��(���)��
			// parseExpr()�� ���� ��(���)�� ��ȯ�ϰ�,
			// QuoteNode�� �����ڸ� ���� ��(���)�� quoted�� �����Ѵ�.
			case APOSTROPHE:
				return new QuoteNode(parseExpr());
			case QUOTE:
				return new QuoteNode(parseExpr());
					
			default:
				// head�� next�� ����� head�� ��ȯ�ϵ��� �ۼ�
				System.out.println("Parsing Error!");
				return null;
		}
	}

	// List�� value�� �����ϴ� �޼ҵ�
	// head�� tail�� ������ ������ �ȴ�.
	private ListNode parseExprList() {
		
		// parseExpr()�� ���ؼ� �⺻������ ���� ��ū���� �̵��� �� �Ǻ��� �ǽ�
		// L_Paren �̶�� �ݺ��ؼ� ���������� ����
		// head �κ��� ��.
		Node head = parseExpr();
		
		if (head == null)
			return null;
		
		if (head == END_OF_LIST) // if next token is RPAREN
			return ListNode.ENDLIST;
		
		// ���� Node head = parseExpr();������ ����ǹǷ� �⺻������ head�� �����κ��� �ȴ�.
		// tail���� parseExprList();�� �ٽ� ȣ��ǹǷ� ��������� ó���ȴ�.
		// tail �ȿ� head�� tail�� �ִ�(�Ǵ� head �ȿ� head�� tail�� �ִ�) ���������� �����ȴ�. 
		ListNode tail = parseExprList();
		
		if (tail == null)
			return null;
		
		// parseExprList�� �ݺ������� ���� ���� �������� ������ Node���� ���� ���� head �� tail�� ��ȯ���ִ� ������
		// NodePrinterŬ������ ���ؼ� ��� ������ Ž���� �� �ֵ����Ѵ�.
		return ListNode.cons(head, tail);
		}
}