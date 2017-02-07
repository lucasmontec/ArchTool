package com.carvalab.archtool.csharp;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import com.carvalab.archtool.csharp.CSharpVisitorParser.CSClassRelationships;
import com.carvalab.archtool.util.FormatFilterFileVisitor;

public class CSharpGraph {

	private static String filterLines(String original, String removeWith) {
		String finalString = "";

		for (String line : original.split("\n")) {
			if (!line.trim().startsWith("#")) {
				finalString += line + "\n";
			}
		}

		return finalString;
	}

	public static Graph parseFiles(File[] files) {
		return parseFiles(files, new SingleGraph("Class graph"));
	}

	public static Graph parseFiles(File[] files, Graph graph) {
		// Prepare a list of relationships
		ArrayList<CSClassRelationships> relationships = new ArrayList<>();

		// Create a parser to get class relationships
		CSharpVisitorParser parser = new CSharpVisitorParser();
		for (File f : files) {
			getRelationships(f, parser, relationships);
		}

		if (!relationships.isEmpty()) {
			graph.setStrict(false);
			graph.setAutoCreate(true);

			for (CSClassRelationships relationship : relationships) {
				if (relationship != null) {
					for (String toClass : relationship.relations) {
						graph.addEdge(relationship.className + toClass, relationship.className, toClass);
					}
				}
			}

			for (Node node : graph) {
				node.addAttribute("ui.label", node.getId());
			}
		}

		return graph;
	}

	public static Graph parseFileTree(String origin) {
		return parseFileTree(origin, new SingleGraph("Class graph"));
	}

	public static Graph parseFileTree(String origin, Graph graph) {
		// Prepare a list of relationships
		ArrayList<CSClassRelationships> relationships = new ArrayList<>();

		// Get the path to the project
		Path path = Paths.get(origin);
		// Create a parser to get class relationships
		CSharpVisitorParser parser = new CSharpVisitorParser();
		// Walk the file tree of the directory parsing each file that has the .cs extension
		FormatFilterFileVisitor csharpParserVisitor = new FormatFilterFileVisitor(".cs", f -> {
			getRelationships(f, parser, relationships);
		});

		// Walk the file tree
		try {
			Files.walkFileTree(path, csharpParserVisitor);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (!relationships.isEmpty()) {
			graph.setStrict(false);
			graph.setAutoCreate(true);

			for (CSClassRelationships relationship : relationships) {
				if (relationship != null) {
					for (String toClass : relationship.relations) {
						graph.addEdge(relationship.className + toClass, relationship.className, toClass);
					}
				}
			}

			for (Node node : graph) {
				node.addAttribute("ui.label", node.getId());
			}
		}

		return graph;
	}

	private static void getRelationships(File f, CSharpVisitorParser parser,
			ArrayList<CSClassRelationships> relationships) {
		String code = null;
		try {
			code = new String(Files.readAllBytes(f.toPath()), "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Remove preprocessor lines
		code = filterLines(code.substring(1), "#");
		// System.out.println("Code: " + code.substring(0, 150));

		// If we have code, parse it
		if (code != null && code.length() > 0 && code.contains("class")) {
			CSClassRelationships relationship = parser.parseClass(code);
			// System.out.println(relationship);
			if (relationship != null) {
				relationships.add(relationship);
			}
		}
	}
}
