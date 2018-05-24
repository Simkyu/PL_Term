package parser.ast;

import java.util.HashMap;

import java.util.Map;

import lexer.*;

//implements로 변경 : extends이면 오류 발생
public class FunctionNode implements Node{ //binaryOpNode클래스를 보고 참고해서 작성
	
	// FunctionNode 처리를 위한 enum
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
		
	
	// HashMap을 통해서 fromTokenType 을 생성한다.
	// key로는 TokenType, value로는 FunctionType을 받는다.
	// FunctionType에 있는 모든 value들을  put 해준다. => FunctionType의 값들이 저장되어 있는 HashMap
	private static Map<TokenType, FunctionType> fromTokenType = new HashMap<TokenType, FunctionType>();
	static {
		// enum.values() : enum의 요소들을 순서대로 enum타입의 배열로 리턴. 
		// enum FunctionType안의 FunctionType요소들을 모두 hashmap 안에 put(저장)한다.
		for (FunctionType fType : FunctionType.values()){
		// fType.tokenType()을 사용하기 위해 아래의 추상 메소드를 사용.
		fromTokenType.put ( fType.tokenType(), fType);
		}
	}
	
	// abstract 메소드는 abstract 클래스 안에서 정의해야 하지만 enum안에는 존재할 수 있다.
	
	// enum의 각 인스턴스는 고유 한 동작을 가질 수 있습니다.
	// 이를 위해 열거 형에 각각의 인스턴스가 자체 구현을 제공하는 추상 메소드를 정의 할 수 있습니다.
	// [출처] : https://www.ntu.edu.sg/home/ehchua/programming/java/JavaEnum.html#zz-2.2
	abstract TokenType tokenType();
	}

	public FunctionType value;
	
	@Override
	public String toString(){
		//내용 채우기
		// enum.name() : 호출된 값의 이름을 String으로 리턴한다.
		return value.name();
	}
	
	public void setValue(TokenType tType){
		// TokenType을 통해서  FunctionNode(FunctionType) 노드로  / type값 ( ex) CAR, COND ... )을 value로 저장.
		// ex) cond => FunctionNode / name : COND / value : [C, O, N, D]
		FunctionType fType = FunctionType.fromTokenType.get(tType);
		value = fType;
	}
}
