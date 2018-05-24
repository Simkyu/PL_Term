package parser.ast;

import java.util.HashMap;

import java.util.Map;

import lexer.*;

//implements�� ���� : extends�̸� ���� �߻�
public class FunctionNode implements Node{ //binaryOpNodeŬ������ ���� �����ؼ� �ۼ�
	
	// FunctionNode ó���� ���� enum
	public enum FunctionType {
		ATOM_Q { TokenType tokenType() {return TokenType.ATOM_Q;} },
		CAR { TokenType tokenType() {return TokenType.CAR;} },
		CDR { TokenType tokenType() {return TokenType.CDR;} },
		COND { TokenType tokenType() {return TokenType.COND;} },
		CONS { TokenType tokenType() {return TokenType.CONS;} },
		DEFINE { TokenType tokenType() {return TokenType.DEFINE;} },
		EQ_Q { TokenType tokenType() {return TokenType.EQ_Q;} },
		LAMBDA { TokenType tokenType() {return TokenType.LAMBDA;} },
		NOT { TokenType tokenType() {return TokenType.NOT;} },
		NULL_Q { TokenType tokenType() {return TokenType.NULL_Q;} };
		
	
	// HashMap�� ���ؼ� fromTokenType �� �����Ѵ�.
	// key�δ� TokenType, value�δ� FunctionType�� �޴´�.
	// FunctionType�� �ִ� ��� value����  put ���ش�. => FunctionType�� ������ ����Ǿ� �ִ� HashMap
	private static Map<TokenType, FunctionType> fromTokenType = new HashMap<TokenType, FunctionType>();
	static {
		// enum.values() : enum�� ��ҵ��� ������� enumŸ���� �迭�� ����. 
		// enum FunctionType���� FunctionType��ҵ��� ��� hashmap �ȿ� put(����)�Ѵ�.
		for (FunctionType fType : FunctionType.values()){
		// fType.tokenType()�� ����ϱ� ���� �Ʒ��� �߻� �޼ҵ带 ���.
		fromTokenType.put ( fType.tokenType(), fType);
		}
	}
	
	// abstract �޼ҵ�� abstract Ŭ���� �ȿ��� �����ؾ� ������ enum�ȿ��� ������ �� �ִ�.
	
	// enum�� �� �ν��Ͻ��� ���� �� ������ ���� �� �ֽ��ϴ�.
	// �̸� ���� ���� ���� ������ �ν��Ͻ��� ��ü ������ �����ϴ� �߻� �޼ҵ带 ���� �� �� �ֽ��ϴ�.
	// [��ó] : https://www.ntu.edu.sg/home/ehchua/programming/java/JavaEnum.html#zz-2.2
	abstract TokenType tokenType();
	}

	public FunctionType value;
	
	@Override
	public String toString(){
		//���� ä���
		// enum.name() : ȣ��� ���� �̸��� String���� �����Ѵ�.
		return value.name();
	}
	
	public void setValue(TokenType tType){
		// TokenType�� ���ؼ�  FunctionNode(FunctionType) ����  / type�� ( ex) CAR, COND ... )�� value�� ����.
		// ex) cond => FunctionNode / name : COND / value : [C, O, N, D]
		FunctionType fType = FunctionType.fromTokenType.get(tType);
		value = fType;
	}
}
