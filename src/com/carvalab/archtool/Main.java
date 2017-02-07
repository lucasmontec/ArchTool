package com.carvalab.archtool;

import java.awt.EventQueue;
import java.util.Iterator;

import org.graphstream.graph.Node;

public class Main {

	//@formatter:off
    protected static String styleSheet =
            "node {" +
            "	fill-color: black;" +
            "}" +
            "node.marked {" +
            "	fill-color: blue;" +
            "}";
    //@formatter:on

	/*
	 *
	 * Graph graph = new MultiGraph("Bazinga!");
	 * // Populate the graph.
	 * Viewer viewer = graph.display();
	 * // Let the layout work ...
	 * viewer.disableAutoLayout();
	 * // Do some work ...
	 * viewer.enableAutoLayout();
	 *
	 * node.setAttribute("x", 1);
	 * node.setAttribute("y", 3);
	 *
	 */

	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Frame frame = new Frame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		/*
		 * Graph graph = CSharpGraph
		 * .parseFileTree("G:\\UnityProjects\\Sawce\\TrashMan\\Assets\\Scripts\\Player");
		 *
		 * if (graph != null) {
		 * graph.addAttribute("ui.quality");
		 * graph.addAttribute("ui.antialias");
		 * graph.display();
		 * } else {
		 * System.err.println("Failed to generate graph!");
		 * }
		 */
	}

	public static void explore(Node source) {
		Iterator<? extends Node> k = source.getBreadthFirstIterator();

		while (k.hasNext()) {
			Node next = k.next();
			next.setAttribute("ui.class", "marked");
			sleep();
		}
	}

	protected static void sleep() {
		try {
			Thread.sleep(1000);
		} catch (Exception e) {}
	}

}
