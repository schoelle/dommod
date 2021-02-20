package de.fams.dommod;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import de.fams.dommod.Argument.Type;

import java.io.*;
import java.util.List;

public class Parser {

	public static final ObjectMapper MAPPER = new ObjectMapper();

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
	private boolean parsingJson;

	private boolean isEof() {
		return lastChar == '\0';
	}

	public Parser(String fName) throws ParsingException {
		parsingJson = fName.toLowerCase().endsWith(".json");
		this.fileName = new File(fName);
		try {
			this.reader = new BufferedReader(new FileReader(fName));
		} catch (FileNotFoundException e) {
			throw new ParsingException("File not found");
		}
	}

	public void readChar() throws ParsingException {
		int c;
		if (lastChar == '\n') {
			lineNo++;
		}
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
			readChar();
			if (lastChar == '-') {
				while (!isEof() && (lastChar != '\n')) {
					shift(comment);
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
			throw new ParsingException("# expected");
		}
		readChar();
		if (!lastIsAlphaNum()) {
			throw new ParsingException("Missing command after #");
		}
		StringBuffer name = new StringBuffer();
		while (!isEof() && lastIsAlphaNum()) {
			shift(name);
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
		int cmdLine = lineNo;
		List<Argument> arguments = readArguments();
		return new Command(cmdLine, prefixWhitespace, command, arguments, lastComment);
	}

	private List<Argument> readArguments() throws ParsingException {
		lastComment = null;
		List<Argument> result = Lists.newArrayList();
		skipWhitespace();
		while (!isEof() && lastChar != '\n') {
			StringBuffer buf = new StringBuffer();
			switch (lastChar) {
			case '-':
				shift(buf);
				if (lastChar == '-') {
					while (!isEof() && lastChar != '\n') {
						shift(buf);
					}
					lastComment = buf.toString();
				} else {
					while (!isEof() && lastChar != '-' && lastChar != ' ' && lastChar != '\t' && lastChar != '\n') {
						shift(buf);
					}
					result.add(new Argument(Type.NUMBER, buf.toString()));
				}
				break;
			case '"':
				readChar(); // Skipping opening "
				while (!isEof() && lastChar != '"') {
					shift(buf);
				}
				if (isEof()) {
					throw new ParsingException("Unclosed String");
				}
				result.add(new Argument(Type.STRING, buf.toString()));
				readChar(); // Skipping closing "
				break;
			default:
				while (!isEof() && lastChar != '-' && lastChar != ' ' && lastChar != '\t' && lastChar != '\n') {
					shift(buf);
				}
				result.add(new Argument(Type.NUMBER, buf.toString()));
			}
			skipWhitespace();
		}
		if (!isEof() && lastChar == '\n') {
			readChar();
		}
		return result;
	}

	private void shift(StringBuffer buf) throws ParsingException {
		buf.append(lastChar);
		readChar();
	}

	private void skipWhitespace() throws ParsingException {
		while (!isEof() && (lastChar == ' ' || lastChar == '\t')) {
			readChar();
		}
	}

	public DmFile parse() throws ParsingException {
		DmFile result;
		if (parsingJson) {
			result = parseJson();
		} else {
			result = parseDm();
		}
		result.rebuildDefinitions();
		return result;
	}

	public DmFile parseDm() throws ParsingException {
		readChar(); // "Preload" first character into lastChar
		List<Command> commands = Lists.newArrayList();
		while (!isEof()) {
			Command cmd = readLine();
			if (cmd != null) {
				commands.add(cmd);
			}
		}
		DmFile dmFile = new DmFile(commands, lastComment);
		commands.forEach(c -> c.setDmFile(dmFile));
		return dmFile;
	}

	private Command parseJsonCommand(JsonNode node) {
		String name = node.get("name").textValue();
		JsonNode prefixNode = node.get("prefix");
		String prefix = prefixNode == null ? null : prefixNode.textValue();
		JsonNode commentNode = node.get("comment");
		String comment = commentNode == null ? null : commentNode.textValue();
		List<Argument> arguments = Lists.newArrayList();
		int argCount = 1;
		JsonNode numNode = node.get(String.format("arg%dnum", argCount));
		JsonNode strNode = node.get(String.format("arg%dstr", argCount));
		while (numNode != null || strNode != null) {
			if (numNode != null) {
				arguments.add(new Argument(Argument.Type.NUMBER, numNode.textValue()));
			} else {
				arguments.add(new Argument(Argument.Type.STRING, strNode.textValue()));
			}
			argCount++;
			numNode = node.get(String.format("arg%dnum", argCount));
			strNode = node.get(String.format("arg%dstr", argCount));
		}
		return new Command(1, prefix, name, arguments, comment); // we lose the line numbers
	}

	private DmFile parseJson() throws ParsingException {
		List<Command> commands = Lists.newArrayList();
		String lastComment = "";
		try {
			ArrayNode data = (ArrayNode) MAPPER.readTree(reader);
			for (JsonNode node: data) {
				ObjectNode oNode = (ObjectNode) node;
				if (oNode.has("tailComment")) {
					lastComment = oNode.get("tailComment").textValue();
					continue;
				}
				Command cmd = parseJsonCommand(oNode);
				commands.add(cmd);
				if (oNode.has("content")) {
					ArrayNode content = (ArrayNode) oNode.get("content");
					for (JsonNode cNode: content) {
						Command ccmd = parseJsonCommand(cNode);
						commands.add(ccmd);
					}
				}
			}
		} catch (IOException e) {
			throw new ParsingException(e);
		}
		DmFile result = new DmFile(commands, lastComment);
		commands.forEach(c -> c.setDmFile(result));
		return result;
	}
}
