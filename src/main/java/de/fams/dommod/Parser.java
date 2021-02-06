package de.fams.dommod;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.google.common.collect.Lists;

import de.fams.dommod.Argument.Type;

public class Parser {

	public class ParsingException extends Exception {
		private static final long serialVersionUID = 1L;

		public ParsingException(String message) {
			super(String.format("Line %d on %s: %s", lineNo, fileName.getName(), message));
		}
		
		public ParsingException(Exception e) {
			super(String.format("Line %d on %s: %s", lineNo, fileName.getName(), e.getMessage()));
		}
		
	}
	
	private final File fileName;
	private final BufferedReader reader;
	private int lineNo = 1;
	private char lastChar;
	private String lastComment;

	private boolean isEof() {
		return lastChar == '\0';
	}
	
	public Parser(String fName) throws ParsingException {
		this.fileName = new File(fName);
		try {
			this.reader = new BufferedReader(new FileReader(fName));
		} catch (FileNotFoundException e) {
			throw new ParsingException("File not found");
		}
	}

	public void readChar() throws ParsingException {
		int c;
		try {
			c = (char) reader.read();
		} catch (IOException e) {
			throw new ParsingException(e);
		}
		if (c == -1 || c == 65535) {
			lastChar = '\0';
		} else {
			lastChar = (char) c;
			if (lastChar == '\r') {
				readChar();
			}
		}
	}
	
	public String readPrefixComment() throws ParsingException {
		StringBuffer comment = new StringBuffer();
		while (!isEof() && (Character.isWhitespace(lastChar) || lastChar == '-')) {
			comment.append(lastChar);
			if (lastChar == '\n') lineNo++;
			readChar();
			if (lastChar == '-') {
				comment.append(lastChar);
				while (!isEof() && (lastChar != '\n')) {
					comment.append(lastChar);
					readChar();
				}
			}	
		}
		return comment.toString();
	}
	
	public boolean lastIsAlphaNum() {
		return Character.isAlphabetic(lastChar) || Character.isDigit(lastChar) || lastChar == '_';
	}
	
	public String readCommand() throws ParsingException {
		if (lastChar != '#') {
			Print.info("%d", Character.getNumericValue(lastChar));
			throw new ParsingException("# expected");
		}
		readChar();
		if (!lastIsAlphaNum()) {
			throw new ParsingException("Missing command after #");
		}
		StringBuffer name = new StringBuffer();
		while (!isEof() && lastIsAlphaNum()) {
			name.append(lastChar);
			readChar();
		}
		return name.toString();
	}
	
	public Command readLine() throws ParsingException {
		String prefixWhitespace = readPrefixComment();
		if (isEof()) {
			lastComment = prefixWhitespace;
			return null;
		}
		String command = readCommand();
		List<Argument> arguments = readArguments();
		return new Command(prefixWhitespace, command, arguments, lastComment);
	}
	
	private List<Argument> readArguments() throws ParsingException {
		lastComment = null;
		List<Argument> result = Lists.newArrayList();
		skipWhitespace();
		while(!isEof() && lastChar != '\n') {
			StringBuffer buf = new StringBuffer();
			switch (lastChar) {
			case '-':
				buf.append('-');
				readChar();
				if (lastChar == '-') {
					while (!isEof() && lastChar != '\n') {
						buf.append(lastChar);
						readChar();
					}
					lastComment = buf.toString();
				} else if (Character.isDigit(lastChar)) {
					while (Character.isDigit(lastChar) || lastChar == '.') {
						buf.append(lastChar);
						readChar();
					}					
					result.add(new Argument(Type.NUMBER, buf.toString()));					
				} else {
					throw new ParsingException("Error parsing arguments");					
				}
				break;
			case '"':
				readChar();
				while (!isEof() && lastChar != '"') {
					if (lastChar == '\n') lineNo++;
					buf.append(lastChar);
					readChar();
				}
				if (isEof()) {
					throw new ParsingException("Unclosed String");
				}
				result.add(new Argument(Type.STRING, buf.toString()));
				readChar();
				break;
			default:
				if (Character.isDigit(lastChar)) {
					while (Character.isDigit(lastChar) || lastChar == '.') {
						buf.append(lastChar);
						readChar();
					}					
					result.add(new Argument(Type.NUMBER, buf.toString()));
				} else {
					throw new ParsingException("Error parsing arguments");					
				}
			}
			skipWhitespace();
		}	
		return result;
	}

	private void skipWhitespace() throws ParsingException {
		while (!isEof() && (lastChar == ' ' || lastChar == '\t')) {
			readChar();
		}
	}

	public DmFile parse() throws ParsingException {
		readChar(); // Preload 'lastCharacter'
		List<Command> commands = Lists.newArrayList();
		while (!isEof()) {
			Command cmd = readLine();
			if (cmd != null) {
				commands.add(cmd);
			}
		}
		return new DmFile(commands, lastComment);
	}
	
}
