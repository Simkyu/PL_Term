package parser.ast;

import java.util.HashMap;
import java.util.Map;

import lexer.*;


//implements�� ����
public class BinaryOpNode implements Node {
	
	// BinaryOpNode ó���� ���� enum
	// type BinType is not visibled ��� ��Ű�� ���� ������ public���� ����
	public enum BinType {
		MINUS { TokenType tokenType() {return TokenType.MINUS;} },
		PLUS { TokenType tokenType() {return TokenType.PLUS;} },
		TIMES { TokenType tokenType() {return TokenType.TIMES;} },
		DIV { TokenType tokenType() {return TokenType.DIV;} },
		LT { TokenType tokenType() {return TokenType.LT;} },
		GT { TokenType tokenType() {return TokenType.GT;} },
		EQ { TokenType tokenType() {return TokenType.EQ;} };
	
	
	// HashMap�� ���ؼ� fromTokenType �� �����Ѵ�.
	// key�δ� TokenType, value�δ� BinType�� �޴´�.
	// BinType�� �ִ� ��� value����  put ���ش�. => BinType�� ������ ����Ǿ� �ִ� HashMap
	private static Map<TokenType, BinType> fromTokenType = new HashMap<TokenType, BinType>();
	static {
		// enum.values() : enum�� ��ҵ��� ������� enumŸ���� �迭�� ����. 
		// enum BinType���� BinType��ҵ��� ��� hashmap �ȿ� put(����)�Ѵ�.
		for (BinType bType : BinType.values()){
		// bType.tokenType()�� ����ϱ� ���� �Ʒ��� �߻� �޼ҵ带 ���.
		fromTokenType.put(bType.tokenType(), bType);
		}
	}
	
	// HashMap�� ���ؼ� TokenType�� �´� BinType�� ��ȯ�Ѵ�.
	static BinType getBinType(TokenType tType){
		return fromTokenType.get(tType);
	}
	
	// abstract �޼ҵ�� abstract Ŭ���� �ȿ��� �����ؾ� ������ enum�ȿ��� ������ �� �ִ�.
	
	// enum�� �� �ν��Ͻ��� ���� �� ������ ���� �� �ֽ��ϴ�.
	// �̸� ���� ���� ���� ������ �ν��Ͻ��� ��ü ������ �����ϴ� �߻� �޼ҵ带 ���� �� �� �ֽ��ϴ�.
	// [��ó] : https://www.ntu.edu.sg/home/ehchua/programming/java/JavaEnum.html#zz-2.2
	abstract TokenType tokenType();
	}
	
	public BinType value;
	
	public void setValue(TokenType tType){
		// TokenType�� ���ؼ�  BinaryOpNode(BinType) ����  / type�� ( ex) MINUS, PLUS ... )�� value�� ����.
		// ex) - => BinaryOpNode / name : MINUS / value : [M, I, N, U, S]
		BinType bType = BinType.getBinType(tType);
		value = bType;
	}
	
	@Override
	public String toString(){
		// enum.name() : ȣ��� ���� �̸��� String���� �����Ѵ�.
		return value.name();
	}
}