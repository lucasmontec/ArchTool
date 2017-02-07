package com.carvalab.archtool.csharp;

import java.lang.reflect.InvocationTargetException;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;

public class AntlrUtil {

	public static CommonTokenStream getTokenStream(String code, Class<? extends Lexer> lexClass)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		//The antlr reader
		ANTLRInputStream inputStream = new ANTLRInputStream(code);
		// Create the lexer
		Lexer lex = lexClass.getConstructor(CharStream.class).newInstance(inputStream);
		// Create the token stream
		return new CommonTokenStream(lex);
	}

	public static CommonTokenStream getTokenStream(String code, Class<? extends Lexer> lexClass,
			BaseErrorListener lexerErrorReport) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		// The antlr reader
		ANTLRInputStream inputStream = new ANTLRInputStream(code);
		// Create the lexer
		Lexer lex = lexClass.getConstructor(CharStream.class).newInstance(inputStream);
		// Install the error report
		lex.addErrorListener(lexerErrorReport);
		// Create the token stream
		return new CommonTokenStream(lex);
	}

}
