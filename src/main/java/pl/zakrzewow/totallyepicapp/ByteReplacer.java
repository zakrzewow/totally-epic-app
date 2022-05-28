package pl.zakrzewow.totallyepicapp;

import javafx.application.Platform;
import pl.zakrzewow.totallyepicapp.io.LogsManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ByteReplacer {
    public static int replaceBytes(Path path, byte[] firstByteSequence, byte[] secondByteSequence) throws IOException {
        byte[] fileBytes = Files.readAllBytes(path);

        Files.deleteIfExists(path);
        Files.createFile(path);

        int counter = 0;
        int i = 0;
        int pi = 0;

        try (FileOutputStream stream = new FileOutputStream(path.toFile())) {
            while (i <= fileBytes.length - firstByteSequence.length) {
                int j = 0;
                for (; j < firstByteSequence.length; j++) {
                    if (firstByteSequence[j] != fileBytes[i + j]) {
                        break;
                    }
                }

                if (j == firstByteSequence.length) {
                    stream.write(fileBytes, pi, i - pi);
                    stream.write(secondByteSequence);
                    i += j;
                    pi = i;
                    counter++;
                } else {
                    i++;
                }
            }
            stream.write(fileBytes, pi, fileBytes.length - pi);
        }

        return counter;
    }

    record Replacer(byte[] firstByteSequence, byte[] secondByteSequence) implements Runnable {

        @Override
        public void run() {
            List<Path> filePaths = FileManager.getAllMatchingFiles();
            int totalCount = 0;
            for (Path filePath : filePaths) {
                try {

                    if (FileManager.isTooLargeFile(filePath)) {
                        Platform.runLater(() -> LogsManager.addTooLargeFileLog(filePath));
                    } else {
                        int replacementCount = replaceBytes(filePath, firstByteSequence, secondByteSequence);
                        totalCount += replacementCount;
                        Platform.runLater(() -> LogsManager.addReplacementLog(filePath, replacementCount));
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            int finalTotalCount = totalCount;
            Platform.runLater(() -> LogsManager.addTotalInfoLog(filePaths.size(), finalTotalCount));
        }
    }
}
