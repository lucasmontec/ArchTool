package com.carvalab.archtool;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

import com.carvalab.archtool.csharp.CSharpGraph;

public class Frame extends JFrame {
	private static final long	serialVersionUID	= -5113165141038015617L;

	private JLabel				lblProject;
	private Font				fntRoboto			= font("Roboto-BoldCondensed", 18);

	private Graph				mainGraph;
	private Viewer				graphViewer;

	private String[]			primitiveTypes		=
	{ "float", "int", "string", "byte", "float[]", "byte[]", "char", "uint", "uint[]", "string[]", "short",
			"long", "double", "single", "bool", "decimal", "object", "var" };

	//@formatter:off
    protected static String styleSheet =
            "node {" +
            "	size: 16px;"+
            "	fill-mode: gradient-diagonal2;"+
            "	fill-color: #6489c4, #304463;" +
            "	stroke-mode: none;"+
            "	stroke-width: 2px;"+
            "	stroke-color: #121c2d;"+
            "	text-background-mode: rounded-box;"+
            "	text-padding: 3px, 2px;"+
            "	text-alignment: under;"+
            "	text-background-color: #a6ddd0;"+
            "	text-font: arial;"+
            "}" +
            "node.primitive {" +
            "	size: 7px;"+
            "	stroke-mode: none;"+
            "	fill-color: #6999e5;" +
            "}"+
            "edge {" +
            "	shape: blob;"+
            "	fill-color: #121c2d;" +
            "}"+
            "graph { fill-color: #71767f; }"
            ;
    //@formatter:on

	public Frame() {
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		// Create main graph
		mainGraph = new MultiGraph("Main graph");
		mainGraph.addAttribute("ui.stylesheet", styleSheet);

		setSize(800, 600);
		setTitle("ArchTool");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		ImageIcon icon = null;
		try {
			icon = new ImageIcon(ImageIO.read(imageStream("archtool_icon.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (icon != null) {
			setIconImage(icon.getImage());
		}

		setLayout(new BorderLayout());
		buildFrame();
	}

	private void buildFrame() {
		getContentPane().setBackground(new Color(56, 56, 56));

		// Create top label
		lblProject = new JLabel("Project: none");
		lblProject.setToolTipText("Drag some C# files or a C# project folder to the panel bellow.");
		lblProject.setFont(fntRoboto);
		lblProject.setBorder(new EmptyBorder(10, 10, 10, 10));
		lblProject.setForeground(new Color(198, 203, 211));
		add(lblProject, BorderLayout.NORTH);

		// Viewer
		graphViewer = new Viewer(mainGraph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
		View view = graphViewer.addDefaultView(false);   // false indicates "no JFrame".
		add((JPanel) view, BorderLayout.CENTER);
		graphViewer.enableXYZfeedback(true);

		// Add the graph builder to the viewer
		((JPanel) view).setDropTarget(new GraphLoaderDropTarget());
	}

	private class GraphLoaderDropTarget extends DropTarget {
		private static final long serialVersionUID = -7268334714021568236L;

		@Override
		public synchronized void drop(DropTargetDropEvent evt) {
			try {
				evt.acceptDrop(DnDConstants.ACTION_COPY);
				@SuppressWarnings("unchecked")
				List<File> droppedFiles = (List<File>) evt.getTransferable()
						.getTransferData(DataFlavor.javaFileListFlavor);

				graphViewer.disableAutoLayout();
				buildGraph(droppedFiles);
				graphViewer.enableAutoLayout();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	private void buildGraph(List<File> files) {
		lblProject.setText("Building graph...");
		new Thread(() -> {
			mainGraph.clear();
			mainGraph.addAttribute("ui.stylesheet", styleSheet);
			mainGraph.addAttribute("ui.quality");
			mainGraph.addAttribute("ui.antialias");

			if (files.size() >= 1) {
				String projName = "";
				for (File f : files) {
					projName += f.getName().replaceAll(".cs", "") + " ";
				}
				lblProject.setText("Project: " + projName);
			}

			if (files.size() == 1) {
				CSharpGraph.parseFileTree(files.get(0).getAbsolutePath(), mainGraph);
			} else if (files.size() > 1) {
				CSharpGraph.parseFiles((File[]) files.toArray(), mainGraph);
			}
			// System.out.println("Done");
			updateGraph();
		}).start();
	}

	private void updateGraph() {
		for (Node node : mainGraph) {
			if (Arrays.asList(primitiveTypes).contains(node.getId())) {
				// System.out.println(node.getId());
				node.setAttribute("ui.class", "primitive");
			}
		}
	}

	public static InputStream imageStream(String imageFile) {
		return System.class.getResourceAsStream("/assets/images/" + imageFile);
	}

	public static InputStream fontStream(String fontFile) {
		return System.class.getResourceAsStream("/assets/fonts/" + fontFile);
	}

	public static Font font(String name, float size) {
		Font font;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, fontStream(name + ".ttf"));

			return font.deriveFont(size);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}

		return null;
	}
}
