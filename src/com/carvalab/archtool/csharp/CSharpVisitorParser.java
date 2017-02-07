package com.carvalab.archtool.csharp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;

import com.carvalab.archtool.parsers.CSharpLexer;
import com.carvalab.archtool.parsers.CSharpParser;
import com.carvalab.archtool.parsers.CSharpParser.Base_typeContext;
import com.carvalab.archtool.parsers.CSharpParser.Class_definitionContext;
import com.carvalab.archtool.parsers.CSharpParserBaseVisitor;

public class CSharpVisitorParser {

	public static class CSClassRelationships {
		public String		className	= "from";
		public Set<String>	relations	= new HashSet<>();

		public CSClassRelationships(String cls) {
			className = cls;
		}

		@Override
		public String toString() {
			return "CSClassRelationships [className=" + className + ", relations=" + relations + "]";
		}
	}

	public CSClassRelationships parseClass(String classDefFile) {
		CharStream charStream = new ANTLRInputStream(classDefFile);
		CSharpLexer lexer = new CSharpLexer(charStream);
		TokenStream tokens = new CommonTokenStream(lexer);
		CSharpParser parser = new CSharpParser(tokens);

		ClassDeclarationVisitor classVisitor = new ClassDeclarationVisitor();
		classVisitor.visit(parser.compilation_unit());
		return classVisitor.relationship;
	}

	private class ClassDeclarationVisitor extends CSharpParserBaseVisitor<CSClassRelationships> {

		private CSClassRelationships relationship;

		@Override
		public CSClassRelationships visitClass_definition(Class_definitionContext ctx) {
			// System.out.println(ctx.identifier().getText());
			ClassBaseTypesVisitor baseTypesVisitor = new ClassBaseTypesVisitor();
			baseTypesVisitor.visit(ctx);
			CSClassRelationships relationship = new CSClassRelationships(ctx.identifier().getText());
			relationship.relations.addAll(baseTypesVisitor.types());
			this.relationship = relationship;
			return relationship;
		}

		/*
		 * @Override
		 * public CSClassRelationship visitClass_base(Class_baseContext ctx) {
		 * // Gets base class (from current class file)
		 * // System.out.println(ctx.getText());
		 * return super.visitClass_base(ctx);
		 * }
		 */

	}

	private class ClassBaseTypesVisitor extends CSharpParserBaseVisitor<String> {
		private ArrayList<String> types = new ArrayList<>();

		public ArrayList<String> types() {
			return types;
		}

		@Override
		public String visitBase_type(Base_typeContext ctx) {
			types.add(ctx.getText());
			return ctx.getText();
		}
	}
}
