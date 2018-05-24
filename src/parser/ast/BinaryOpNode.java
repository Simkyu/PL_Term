package parser.ast;

import java.util.HashMap;
import java.util.Map;

import lexer.*;


//implements로 변경
public class BinaryOpNode implements Node {
	
	// BinaryOpNode 처리를 위한 enum
	// type BinType is not visibled 라는 패키지 오류 때문에 public으로 변경
	public enum BinType {
		MINUS { TokenType tokenType() {return TokenType.MINUS;} },
		PLUS { TokenType tokenType() {return TokenType.PLUS;} },
		TIMES { TokenType tokenType() {return TokenType.TIMES;} },
		DIV { TokenType tokenType() {return TokenType.DIV;} },
		LT { TokenType tokenType() {return TokenType.LT;} },
		GT { TokenType tokenType() {return TokenType.GT;} },
		EQ { TokenType tokenType() {return TokenType.EQ;} };
	
	
	// HashMap을 통해서 fromTokenType 을 생성한다.
	// key로는 TokenType, value로는 BinType을 받는다.
	// BinType에 있는 모든 value들을  put 해준다. => BinType의 값들이 저장되어 있는 HashMap
	private static Map<TokenType, BinType> fromTokenType = new HashMap<TokenType, BinType>();
	static {
		// enum.values() : enum의 요소들을 순서대로 enum타입의 배열로 리턴. 
		// enum BinType안의 BinType요소들을 모두 hashmap 안에 put(저장)한다.
		for (BinType bType : BinType.values()){
		// bType.tokenType()을 사용하기 위해 아래의 추상 메소드를 사용.
		fromTokenType.put(bType.tokenType(), bType);
		}
	}
	
	// HashMap을 통해서 TokenType에 맞는 BinType을 반환한다.
	static BinType getBinType(TokenType tType){
		return fromTokenType.get(tType);
	}
	
	// abstract 메소드는 abstract 클래스 안에서 정의해야 하지만 enum안에는 존재할 수 있다.
	
	// enum의 각 인스턴스는 고유 한 동작을 가질 수 있습니다.
	// 이를 위해 열거 형에 각각의 인스턴스가 자체 구현을 제공하는 추상 메소드를 정의 할 수 있습니다.
	// [출처] : https://www.ntu.edu.sg/home/ehchua/programming/java/JavaEnum.html#zz-2.2
	abstract TokenType tokenType();
	}
	
	public BinType value;
	
	public void setValue(TokenType tType){
		// TokenType을 통해서  BinaryOpNode(BinType) 노드로  / type값 ( ex) MINUS, PLUS ... )을 value로 저장.
		// ex) - => BinaryOpNode / name : MINUS / value : [M, I, N, U, S]
		BinType bType = BinType.getBinType(tType);
		value = bType;
	}
	
	@Override
	public String toString(){
		// enum.name() : 호출된 값의 이름을 String으로 리턴한다.
		return value.name();
	}
}