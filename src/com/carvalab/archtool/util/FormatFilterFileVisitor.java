package com.carvalab.archtool.util;

import java.io.File;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.Consumer;

public class FormatFilterFileVisitor extends SimpleFileVisitor<Path> {

	private String			formatFilter	= ".*";
	private Consumer<File>	fileOperationConsumer;

	public FormatFilterFileVisitor(String filter, Consumer<File> fileOperationConsumer) {
		formatFilter = filter;
		this.fileOperationConsumer = fileOperationConsumer;
	}

	@Override
	public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) {
		if (basicFileAttributes.isRegularFile()) {
			File file = path.toFile();
			if (file.getName().endsWith(formatFilter)) {
				fileOperationConsumer.accept(file);
			}
			// System.out.println(path + " is a regular file with size " + basicFileAttributes.size());
		} else if (basicFileAttributes.isSymbolicLink()) {
			// System.out.println(path + " is a symbolic link.");
		} else {
			// System.out.println(path + " is not a regular file or symbolic link.");
		}
		return FileVisitResult.CONTINUE;
	}

}
