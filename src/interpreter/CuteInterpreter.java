package interpreter;

import parser.ast.*;
import parser.parse.CuteParser;
import parser.parse.NodePrinter;

import java.io.File;

public class CuteInterpreter {
	
	private void errorLog(String err) {
		System.out.println(err);
	 }
	
	 public Node runExpr(Node rootExpr) {
		 if (rootExpr == null)
			 return null;
		 if (rootExpr instanceof IdNode)
			 return rootExpr;
		 else if (rootExpr instanceof IntNode)
			 return rootExpr;
		 else if (rootExpr instanceof BooleanNode)
			 return rootExpr;
		 else if (rootExpr instanceof ListNode)
			 return runList((ListNode) rootExpr);
		 else
			 errorLog("run Expr error");
		 
		 return null;
	 }
	 
	 private Node runList(ListNode list) {
		 if(list.equals(ListNode.EMPTYLIST))
			 return list;
		 
		 if(list.car() instanceof FunctionNode){
			 return runFunction((FunctionNode)list.car(), list.cdr());
		 }
		 
		 if(list.car() instanceof BinaryOpNode){
			 return runBinary(list);
		 }
		 
		 return list;
	 }
	 
	 
	 private Node runFunction(FunctionNode operator, ListNode operand) {
	
	
		 switch (operator.value){
	
			case CAR:
				// QuoteNode의 값 불러오기
				if(operand.car() instanceof QuoteNode ) {
					return runFunction(operator, (ListNode) runQuote(operand));
				}
				
				// head가 ListNode일 경우
				if (operand.car() instanceof ListNode) {
					
					// operand의 head가 ListNode이고, head의 head가 ListNode일 경우
					// ex) ( ( ( c ) ) b )
					if( (((ListNode) operand.car()).car() instanceof ListNode)){
						return operand.car();
					}
					// operand의 head가 ListNode이고, head의 head가 ListNode가 아닐경우
					// ex) ( ( a b ) d )
					if( !(((ListNode) operand.car()).car() instanceof ListNode)){
						return operand.car();
					}
					
					// head가 ListNode이고 비어있지 않을 경우 runFunction을 통해서 head를 탐색
					// (ListNode의 안으로 이동)
					if(((ListNode)operand.car()).car() != null){
						return runFunction(operator, (ListNode) operand.car());
					}
					
				} else {
					// operand의 head가 ListNode이고, head의 head가 ListNode가 아닐경우, ListNode인 경우
					// head가 ListNode고 비어있을 때 를 제외한 경우
					return operand.car();
				}
				break;
				
				// CAR과 동일하지만, tail을 반환하고 QuoteNode안에 포함된 상태로 반환한다는 차이점만 존재한다.
			case CDR:
				// QuoteNode의 값 불러오기
				if(operand.car() instanceof QuoteNode ) {
					return runFunction(operator, (ListNode) runQuote(operand));
				}
				
				
				if (operand.car() instanceof ListNode) {
					// operand의 head가 ListNode이고, head의 head가 ListNode일 경우
					// ex) ( ( ( c ) ) b )
					if( (((ListNode) operand.car()).car() instanceof ListNode)){
						// operand.car()이 head 이므로 operand.cdr()이 tail이 된다.
						// QuoteNode안에 저장한 상태로 반환한다.
						QuoteNode qnode = new QuoteNode(operand.cdr());
						return qnode;
					}
					
					// operand의 head가 ListNode이고, head의 head가 ListNode가 아닐경우
					// ex) ( ( a b ) d )
					if( !(((ListNode) operand.car()).car() instanceof ListNode)){
						QuoteNode qnode = new QuoteNode(operand.cdr());
						return qnode;
					}
					
					// head가 ListNode이고 비어있지 않을 경우 runFunction을 통해서 head를 탐색
					// (ListNode의 안으로 이동)
					if(((ListNode)operand.car()).car() != null){
						return runFunction(operator, (ListNode) operand.car());
					}
					
				} else {
					QuoteNode qnode = new QuoteNode(operand.cdr());
					return qnode;
				}
				break;
				
			// 완성
			// 두개의 노드를 연결
			case CONS:
				QuoteNode qnode;
				
				// 한 개의 원소가 List일 경우 (QuoteNode)
				// operand head의 QuoteNode 내부의 값과 tail의 QuoteNode에 저장된 값을 연결
				if(operand.car() instanceof QuoteNode) {
					qnode = new QuoteNode(ListNode.cons(runQuote(operand), (ListNode) runQuote(operand.cdr())));
				// 그 외의 경우 operand의 head와 tail의 QuoteNode에 저장된 값을 연결
				} else {
					qnode = new QuoteNode(ListNode.cons(operand.car(), (ListNode) runQuote(operand.cdr())));	
				}
				
				return qnode;
				
			// 완성
			// 가장 처음에 나오는 #T의 조건인 값을 반환한다.
			case COND:
				
				// 처음 주어지는 노드는 무조건 ListNode
				// ex ) ( cond ( #T 4 ) ( #F 3 ) )
				if (operand.car() instanceof ListNode) {
					
					// 내부에 BinaryOpNode가 있을 경우에는 계산 실행
					// 값이 #T 이면 조건의 값을 반환 , #F이면 null 반환
					if(((ListNode)operand.car()).car() instanceof BinaryOpNode) {
						// runBinary를 통해서 nested된 구조도 계산된 상태로 반환된다.
						if (runBinary((ListNode)operand.car()).equals(BooleanNode.TRUE_NODE)){
							return ((ListNode)operand.cdr()).car();
						} else {
							return null;
						}
					}
					
					// BooleanNode일 경우 계산
					if (((ListNode)operand.car()).car() instanceof BooleanNode) {
						if(((ListNode)operand.car()).car().equals(BooleanNode.TRUE_NODE)) {
							return ((ListNode)operand.car()).cdr().car();
						} else {
							// 첫번째 노드가 #F 이면 다음 노드로 이동해서 실행
							return runFunction(operator, operand.cdr());
						}
					}
					
					// ListNode가 중첩된 구조일 경우 ListNode안으로 이동
					// 중첩된 구조에서 반환된 n의 값이 null이 아니라면 해당 값 반환
					if (((ListNode)operand.car()).car() instanceof ListNode) {
						// Node n 을 통해서 값을 확인 #F 이면 null
						Node n = runFunction(operator, (ListNode)operand.car());
						if(n!=null){
							return n;
						}
						else {
							// 첫번째 노드가 #F 이면 다음 노드로 이동해서 실행
							return runFunction(operator, operand.cdr());
						}
					}
				}
				break;
		
			// 완성
			// #T or #F의 값을 반대로 반환한다.
			case NOT:
				// BooleanNode일 경우
				// #T -> #F , #F -> #T
				if(operand.car() instanceof BooleanNode) {
					if(((BooleanNode)operand.car()).equals(BooleanNode.TRUE_NODE)) {
						return BooleanNode.FALSE_NODE;
					} else if(((BooleanNode)operand.car()).equals(BooleanNode.FALSE_NODE)) {
						return BooleanNode.TRUE_NODE;
					} 
				}
				
				// ListNode일 경우 runBinary를 통해 계산결과에 반환된 BooleanNode(n)의 반대값을 반환시킨다.
				// #T -> #F, #F -> #T
				if(operand.car() instanceof ListNode) {
					// runBinary
					Node n = runBinary((ListNode)operand.car());
					if(((BooleanNode)n).equals(BooleanNode.TRUE_NODE)) {
						return BooleanNode.FALSE_NODE;
					} else if(((BooleanNode)n).equals(BooleanNode.FALSE_NODE)) {
						return BooleanNode.TRUE_NODE;
					}
				}
				
				break;
				
			//완성
			// Quote에 저장된 노드가 ListNode인지 확인한다.
			case ATOM_Q:
				// Quote에 저장된 노드가 ListNode이면 False #F
				if(runQuote(operand) instanceof ListNode ) {
					return BooleanNode.FALSE_NODE;
				// 그 외에는 #T를 반환한다.
				} else {
					return BooleanNode.TRUE_NODE;
				}
			
			// 완성
			// List가 null인지 확인한다.
			case NULL_Q:
				
				if(runQuote(operand) instanceof ListNode) {
					// QuoteNode에 ListNode의 내부가 null이면 (비어있으면) #T를 반환한다.
					if (((ListNode) runQuote(operand)).car()== null &&((ListNode) runQuote(operand)).cdr()== null) {
						return BooleanNode.TRUE_NODE;
					}
				}
				// ListNode에 그 외의 노드가 존재하면 #F
				return BooleanNode.FALSE_NODE;
		
				
			// 완성
			// 두 List의 값이 같은지 확인한다.
			case EQ_Q:	
				// QuoteNode의 경우 내부 노드가 ListNode이면 두 노드를 연결한 새로운 노드를 인자로 함수를 다시 호출
				// QuoteNode에 대한 처리는 결국 맨처음에 Quote(')가 나오는 부분에서 한번의 처리를 위한 것이다.
				if(operand.car() instanceof QuoteNode && operand.cdr().car() instanceof QuoteNode) {
					if (runQuote(operand) instanceof ListNode && runQuote(operand.cdr()) instanceof ListNode) {
						
						// ListNode의 tail부분은 무조건 ListNode가 와야되기 때문에 tail가 ListNode로 한겹 더 감싸게 한다.
						return runFunction(operator,ListNode.cons(runQuote(operand), 
								ListNode.cons((ListNode) runQuote(operand.cdr()) , ListNode.EMPTYLIST)) );
						//runFunction(operator, operand.cdr());
						
					// list내의 노드가 ListNode가 아니면 값을 비교
					// ex) ( eq? ' a ' b )
					} else if (!(runQuote(operand) instanceof ListNode) && !(runQuote(operand.cdr()) instanceof ListNode)) {
						if(runQuote(operand).toString().equals(runQuote(operand.cdr()).toString())){
							return BooleanNode.TRUE_NODE;
						}
						else {
							return BooleanNode.FALSE_NODE;
						}
					// 노드가 ListNode, 그외의 노드가 각각 1개씩 있을 때 
					} else if(((runQuote(operand) instanceof ListNode) && !(runQuote(operand.cdr()) instanceof ListNode)) ||
							!((runQuote(operand) instanceof ListNode) && (runQuote(operand.cdr()) instanceof ListNode))){
						return BooleanNode.FALSE_NODE;
					}
				}
				
				
				
				// operand의 head
				Node hhead = operand.car();
				// operand 의 tail
				Node thead = operand.cdr().car();
				
				
				// thead와 hhead의 타입이 같지 않다면 #F 반환
		 		// ex) ' ( a ( b ) c ) ' ( a b ) : thead와 hhead의 타입이 같지 않게 읽힐 경우 #F를 반환해버린다.
		 		// => 따라서 형식이 같지 않다면 #F를 반환해 버린다.
		 		if(!hhead.getClass().equals(thead.getClass())){
		 			return BooleanNode.FALSE_NODE;
		 		}
				
				
				// list를 비교하기 위해서
				// 둘다 ListNode일때

				// hhead와 thead가 List일 경우에만 실행한다.
				if(hhead instanceof ListNode && thead instanceof ListNode ) {
						
					// hhead와 thead의 head값을 연결한 노드를 생성한다.	
					// ( tail은 무조건 ListNode이어야 하기 때문에 ListNode.cons를 통해서 ListNode로 한겹을 감싸준다. )
					Node conshead = ListNode.cons(((ListNode)hhead).car(),ListNode.cons(((ListNode)thead).car(), ListNode.EMPTYLIST));
						
					// conshead를 통해서 노드를 탐색한다.
					// => ListNode 안으로 들어가 탐색한다.
					Node n  = runFunction(operator, (ListNode)conshead);
					
					// 탐색한 결과가 #F일 경우에는 #F를 반환해준다.
					if(n.equals(BooleanNode.FALSE_NODE)) {
		 				return BooleanNode.FALSE_NODE;
		 			}		
				}
							
				// head가 ListNode가 아닐때 : 두 노드의 값을 비교
		 		if (!(hhead instanceof ListNode) && !(thead instanceof ListNode)) {
					// 값이 다르다면 #F를 반환한다.
					if(!hhead.toString().equals((thead).toString())){
						return BooleanNode.FALSE_NODE;
					}
					// 값이 같다면 #T를 반환한다.
					// (어차피 탐색으로 인해 반환된 노드가 #F인지 확인하기 때문에 #F로만 반환하지 않기만 하면 될 것으로 보인다. : 확인 필요)
					// (어쨋든 탐색이 끝났다는 것이기 때문에 어떠한 값이든 값을 반환해야 된다.)
					else if (hhead.toString().equals((thead).toString())) {
						return BooleanNode.TRUE_NODE;
						
					}
				}
		 		
		 		// tail을 탐색하기 위한 노드처리 부분이다.
		 		// 두개의 노드의 cdr부분이 ListNode일 때만 작동하도록 한다.
		 		if(((ListNode)hhead).cdr() instanceof ListNode && ((ListNode)thead).cdr() instanceof ListNode) {
		 			// 두개 노드의 tail부분을 연결한 constail 노드를 생성해 준다.
		 			// ( tail은 무조건 ListNode이어야 하기 때문에 ListNode.cons를 통해서 ListNode로 한겹을 감싸준다. )
		 			Node constail = ListNode.cons(((ListNode)hhead).cdr(), ListNode.cons(((ListNode)thead).cdr(), ListNode.EMPTYLIST));
					
		 			// 두개의 노드 각각 tail이 존재하지 않거나, tail 비어있는 리스트일 경우에는 tail을 탐색하지 않는다. 
		 			if(((ListNode)hhead).cdr() != null && ((ListNode)thead).cdr()!=null 
		 					&& (((ListNode)hhead).cdr()).car() !=null && (((ListNode)hhead).cdr()).car() !=null) {
		 				// 앞에서 생성한 constail을 runFunction을 통해서 탐색한다.
		 				Node t = runFunction(operator, (ListNode)constail);
		 				
		 				// 탐색한 결과가 #F일 경우에는 #F를 반환해준다.
		 				if(t.equals(BooleanNode.FALSE_NODE)) {
		 					return BooleanNode.FALSE_NODE;
		 				}
		 			}
					
		 		}
		 		
		 		// #F를 반환하지 않고 끝까지 실행될 경우 #T가 반환된다.
		 		return BooleanNode.TRUE_NODE;

			
		default:
				break;
		 }
		 
		return null;
	 }
	 
	private Node runBinary(ListNode list) {
		
		BinaryOpNode operator = (BinaryOpNode) list.car();
		
		int sum;
		
		IntNode inode;
		
		// 첫번째 노드와 두번째 노드 저장
		Node a = list.cdr().car();
		Node b = list.cdr().cdr().car();
		
		// ListNode일 경우 재귀로 runBinary를 수행한 결과노드를 저장
		if(a instanceof ListNode) {
			a = runBinary((ListNode) a);
		}
		if(b instanceof ListNode) {
			b = runBinary((ListNode) b);
		}
		
		switch (operator.value){
			case PLUS:
				// sum에 값의 합 저장
				sum = (((IntNode)a).getValue() + ((IntNode)b).getValue());
				
				// IntNode생성
				inode = new IntNode(String.valueOf(sum));
				
				return inode;
				
			case MINUS:
				// sum에 값의 차 저장
				sum = (((IntNode)a).getValue() - ((IntNode)b).getValue());
				
				// IntNode생성
				inode = new IntNode(String.valueOf(sum));
				
				return inode;
				
			case DIV:
				// sum에 값의 나눔 저장
				sum = (((IntNode)a).getValue() / ((IntNode)b).getValue());
				
				// IntNode생성
				inode = new IntNode(String.valueOf(sum));
				
				return inode;
			case TIMES:
				// sum에 값의 곱 저장
				sum = (((IntNode)a).getValue() * ((IntNode)b).getValue());
				
				// IntNode생성
				inode = new IntNode(String.valueOf(sum));
				
				return inode;
				
				// 값의 비교
			case LT:
				if(((IntNode)a).getValue() < ((IntNode)b).getValue()) {
					return BooleanNode.TRUE_NODE;
				} else {
					return BooleanNode.FALSE_NODE;
				}
			case GT:
				if(((IntNode)a).getValue() > ((IntNode)b).getValue()) {
					return BooleanNode.TRUE_NODE;
				} else {
					return BooleanNode.FALSE_NODE;
				}
			case EQ:
				if(((IntNode)a).getValue() == ((IntNode)b).getValue()) {
					return BooleanNode.TRUE_NODE;
				} else {
					return BooleanNode.FALSE_NODE;
				}
			 default:
			 break;
		}
		 return null;
	 }
	
	
	 private Node runQuote(ListNode node) {
		 return ((QuoteNode)node.car()).nodeInside();
	 }
	 
}