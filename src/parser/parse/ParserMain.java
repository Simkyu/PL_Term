package parser.parse;
import java.io.*;
import java.util.Scanner;

import interpreter.CuteInterpreter;
import parser.ast.Node;

public class ParserMain {
	 public static void main(String[] args) throws IOException {
		 
		 while(true) {
		
		// " > " 이후에 입력된 값을 command.txt라는 파일 안에 저장한 후 저장된 값을 판별하는 방법을 사용했다.
		System.out.print("> ");
		Scanner scan = new Scanner(System.in);
		
		
		// ParserMain클래스가 있는 디렉토리를 기준으로 삼았다.
		 String path = ParserMain.class.getResource("").getPath();
		 
		// ParserMain클래스가 있는 디렉토리(bin/parser/parse/)를 기준으로 삼았다.
		 File file = new File(path + "command.txt");
		 
		 // 파일을 생성
		 file.createNewFile();
		 
		 // 파일의 내용에 입력한 값을 저장한다.
		 Writer objWriter = new BufferedWriter(new FileWriter(file));
		 objWriter.write(scan.nextLine());
		 objWriter.flush();
		 objWriter.close();
	
		    
		 CuteParser cuteParser = new CuteParser(file);
		 
		 Node parseTree = cuteParser.parseExpr();
		 
		 CuteInterpreter i = new CuteInterpreter();
		 Node resultNode = i.runExpr(parseTree);
		 
		 // 값 출력 전에 ...을 출력하여 가독성을 높인다.
		 System.out.print("...");
		 NodePrinter.getPrinter(System.out).prettyPrint(resultNode);
		 
		 // 줄바꿈해준다.
		 System.out.println("");
		 }
	 }
}

