package pl.zakrzewow.totallyepicapp;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    public static final int COUNT_LIMIT = 100;

    private static Path directoryPath;
    private static String extension;

    public static void setDirectoryPath(String directoryPathString) {
        directoryPath = Path.of(directoryPathString);
    }

    public static boolean directoryExists() {
        return isSetDirectoryPath() && Files.isDirectory(directoryPath);
    }

    public static boolean isSetDirectoryPath() {
        return directoryPath != null;
    }

    public static void setExtension(String extension) {
        if (extension != null && !extension.startsWith(".")) {
            extension = "." + extension;
        }
        FileManager.extension = extension;
    }

    public static boolean isSetExtension() {
        return extension != null;
    }

    private static class SkippingFileVisitor extends SimpleFileVisitor<Path> {
        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) {
            return FileVisitResult.SKIP_SUBTREE;
        }
    }

    private static class CountingFileVisitor extends SkippingFileVisitor {
        private int counter = 0;

        protected void incrementCounter() {
            counter++;
        }

        public int getCounter() {
            return Math.min(counter, COUNT_LIMIT);
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            if (counter > COUNT_LIMIT) {
                return FileVisitResult.TERMINATE;
            }
            return super.preVisitDirectory(dir, attrs);
        }
    }

    private static class directoryCountingFileVisitor extends CountingFileVisitor {
        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            incrementCounter();
            return super.preVisitDirectory(dir, attrs);
        }
    }

    private static class fileWithExtCountingFileVisitor extends CountingFileVisitor {
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            if (isMatchingFileName(file)) {
                incrementCounter();
            }
            return super.visitFile(file, attrs);
        }
    }

    private static class CollectingFileVisitor extends SkippingFileVisitor {
        private List<Path> filePaths = new ArrayList<>();

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            if (isMatchingFileName(file)) {
                filePaths.add(file);
            }
            return super.visitFile(file, attrs);
        }

        public List<Path> getFilePaths() {
            return filePaths;
        }
    }

    private static boolean isMatchingFileName(Path file) {
        String fileName = file.toString();
        return fileName.contains(".") && fileName.substring(fileName.lastIndexOf(".")).equals(extension);
    }

    public static boolean isTooLargeFile(Path path) throws IOException {
        return (long) Files.getAttribute(path, "size") > 100000000;
    }

    private static int count(CountingFileVisitor countingFileVisitor) {
        if (directoryExists()) {
            try {
                Files.walkFileTree(directoryPath, countingFileVisitor);
                return countingFileVisitor.getCounter();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public static int countDirectories() {
        return count(new directoryCountingFileVisitor());
    }

    public static int countFilesWithExt() {
        return isSetExtension() ? count(new fileWithExtCountingFileVisitor()) : -1;
    }

    public static List<Path> getAllMatchingFiles() {
        try {
            CollectingFileVisitor fileVisitor = new CollectingFileVisitor();
            Files.walkFileTree(directoryPath, fileVisitor);
            return fileVisitor.getFilePaths();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static Path relativize(Path path) {
        return directoryPath.relativize(path);
    }
}
