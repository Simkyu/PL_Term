package parser.parse;
import java.io.PrintStream;
import java.util.StringTokenizer;

import parser.ast.*;
public class NodePrinter {
PrintStream ps;

	public static NodePrinter getPrinter(PrintStream ps) {
		return new NodePrinter(ps);
	} 
	
	private NodePrinter(PrintStream ps) {
		this.ps = ps;
	}
	
	// ListNode, QuoteNode, Node에 대한 printNode 함수를 각각 overload 형식으로 작성
	
	// ListNode
	// ListNode를 통해서 재귀적으로 모든 노드를 탐색한다
	private void printNode(ListNode listNode) {
		
		if (listNode == ListNode.EMPTYLIST) {
			ps.print("( ) ");
			return;
		}
		
		if (listNode == ListNode.ENDLIST) {
			return;
		}
			
			printNode(listNode.car());
						
			if(listNode.cdr().equals(ListNode.EMPTYLIST)) {
				ps.print(" ");
			}
			 
			printNode(listNode.cdr());
		}
	
	// QuotedNode
	// QuotedNode를 처리하기 위한 메소드이다.
	// printNode(Node node)와 같이 값을 출력해 주는 메소드이다.
	// 다만 QuotedNode의 출력을 위해 " ' "를 출력하고, QuotedNode에 저장된 노드값을 출력해준다.
	private void printNode(QuoteNode quoteNode) {
		if (quoteNode.nodeInside() == null) {
			return;
		}
		// 이후 부분을 주어진 출력 형식에 맞게 코드를 작성하시오.
		
		// 'QUOTE', " ' " 는 둘다 " ' "를 출력한 후 ,
		// QuoteNode의 quoted 변수에 저장된 node를 출력한다.
		// ( ex) 'a => quoted : a (IdNode) )
		ps.print("'");
		printNode(quoteNode.nodeInside());
			
		}
	
	// ValueNode 
	// PrettyPrint에서 Node타입의 root를 주기때문에 맨처음에는 무조건 이 메소드로 온다.
	// 따라서 ListNode로 분기하기 위해서 node가 ListNode일 경우에는 형변환을 하여 
	// printNode((ListNode) node);를 통해 분기한다.
	// 그리고 QuotedNode와 ValueNode일 경우에는 값을 출력해준다.(QuotedNode는 메소드 호출한 후 출력)
	private void printNode(Node node) {
		if (node == null) {
			return;
		}
		
		if(node instanceof ListNode) {
			ps.print(" (");
			 printNode((ListNode)node);
			 ps.print(" )");
		} else if(node instanceof QuoteNode){
			// 일정한 출력을 위해 빈공간 출력 추가
			ps.print(" ");
			printNode((QuoteNode) node);
				
		// nextToken을 반환한다. 따라서 #T,#F의 경우 toString에 토큰값을 하나 넣어주거나 
		// 따로 처리를 해줘야한다. 안그러면 nosuchelementexception 발생함.
		} else if (node instanceof BooleanNode){
			String temp = node.toString();
			 StringTokenizer st = new StringTokenizer(temp, " ");
			 ps.print(" " +  st.nextToken());
		
		// IntNode의 경우는 'INT: x' 의 양식이 토큰화 되기 때문에 nextToken 두번 
		}	else {
			String temp = node.toString();
			 StringTokenizer st = new StringTokenizer(temp, " ");
			 st.nextToken();
			 ps.print(" " + st.nextToken());
		}
	}
	
	// root부터 printNode를 수행.
	public void prettyPrint(Node root){
		printNode(root);
	}
}