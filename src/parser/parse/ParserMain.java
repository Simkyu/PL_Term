package parser.parse;
import java.io.*;
import java.util.Scanner;

import interpreter.CuteInterpreter;
import parser.ast.Node;

public class ParserMain {
	 public static void main(String[] args) throws IOException {
		 
		 while(true) {
		
		// " > " ���Ŀ� �Էµ� ���� command.txt��� ���� �ȿ� ������ �� ����� ���� �Ǻ��ϴ� ����� ����ߴ�.
		System.out.print("> ");
		Scanner scan = new Scanner(System.in);
		
		
		// ParserMainŬ������ �ִ� ���丮�� �������� ��Ҵ�.
		 String path = ParserMain.class.getResource("").getPath();
		 
		// ParserMainŬ������ �ִ� ���丮(bin/parser/parse/)�� �������� ��Ҵ�.
		 File file = new File(path + "command.txt");
		 
		 // ������ ����
		 file.createNewFile();
		 
		 // ������ ���뿡 �Է��� ���� �����Ѵ�.
		 Writer objWriter = new BufferedWriter(new FileWriter(file));
		 objWriter.write(scan.nextLine());
		 objWriter.flush();
		 objWriter.close();
	
		    
		 CuteParser cuteParser = new CuteParser(file);
		 
		 Node parseTree = cuteParser.parseExpr();
		 
		 CuteInterpreter i = new CuteInterpreter();
		 Node resultNode = i.runExpr(parseTree);
		 
		 // �� ��� ���� ...�� ����Ͽ� �������� ���δ�.
		 System.out.print("...");
		 NodePrinter.getPrinter(System.out).prettyPrint(resultNode);
		 
		 // �ٹٲ����ش�.
		 System.out.println("");
		 }
	 }
}

