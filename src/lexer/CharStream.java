package lexer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

class CharStream {
	private final Reader reader;
	private Character cache;
	
	static CharStream from(File file) throws FileNotFoundException {
		return new CharStream(new FileReader(file));
	}
	
	// charstream을 생성.
	CharStream(Reader reader) {
		this.reader = reader;
		this.cache = null;
	}
	
	// 문자를 확인해서 타입을 결정.
	Char nextChar() {
		if ( cache != null ) {
			char ch = cache;
			cache = null;
			
			return Char.of(ch);
		}
		else {
			try {
				int ch = reader.read();
				if ( ch == -1 ) {
					return Char.end();
				}
				else {
					return Char.of((char)ch);
				}
			}
			catch ( IOException e ) {
				throw new ScannerException("" + e);
			}
		}
	}
	
	void pushBack(char ch) {
		cache = ch;
	}
}
