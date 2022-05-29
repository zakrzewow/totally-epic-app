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
    private static final int DIRECTORY_VISITED_LIMIT = 100;
    private static final int FILES_CHECKED_LIMIT = 1000;

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

    public static class Count {
        public int directoryCount = 0;
        public int fileWithExtCount = 0;
        public int fileCount = 0;
        public boolean isLimited = false;
    }

    private static class CountingFileVisitor extends SkippingFileVisitor {
        private final Count count = new Count();

        public Count getCount() {
            return count;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            count.directoryCount++;
            if (count.directoryCount >= DIRECTORY_VISITED_LIMIT) {
                count.isLimited = true;
                return FileVisitResult.TERMINATE;
            }
            return super.preVisitDirectory(dir, attrs);
        }

        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            if (isMatchingFileName(file)) {
                count.fileWithExtCount++;
            }
            count.fileCount++;
            if (count.fileCount >= FILES_CHECKED_LIMIT) {
                count.isLimited = true;
                return FileVisitResult.TERMINATE;
            }
            return super.visitFile(file, attrs);
        }
    }

    private static class CollectingFileVisitor extends SkippingFileVisitor {
        private final List<Path> filePaths = new ArrayList<>();

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

    public static Count count() {
        if (directoryExists()) {
            try {
                CountingFileVisitor countingFileVisitor = new CountingFileVisitor();
                Files.walkFileTree(directoryPath, countingFileVisitor);
                return countingFileVisitor.getCount();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new Count();
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
