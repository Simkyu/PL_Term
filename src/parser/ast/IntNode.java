package parser.ast;

public class IntNode implements ValueNode {

	// 새로 수정된 IntNode
	private Integer value;
	
	@Override
	public String toString(){
		return "INT: " + value;
	}
	
	public IntNode(String text) {
		this.value = new Integer(text);
	}
	
	// interpreter 계산을 위해서 추가
	public int getValue() {
		return value;
	}
} 