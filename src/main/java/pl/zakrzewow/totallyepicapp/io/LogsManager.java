package pl.zakrzewow.totallyepicapp.io;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import pl.zakrzewow.totallyepicapp.FileManager;

import java.nio.file.Path;

public class LogsManager {
    private static VBox logsBox;

    public static void setLogsBox(VBox logsBox) {
        LogsManager.logsBox = logsBox;
    }

    public static void clearLogs() {
        logsBox.getChildren().clear();
    }

    public static void addTotalInfoLog(int numberOfFiles, int totalCount) {
        Label label = new Label("Sumarycznie: " + getReplacementNoun(totalCount) +
                " w " + getFileNoun(numberOfFiles));

        label.getStyleClass().add("first-log-label");
        logsBox.getChildren().add(0, label);
    }

    public static void addReplacementLog(Path path, int count) {
        path = FileManager.relativize(path);
        Label label = new Label("• " + getReplacementNoun(count) + " w pliku " + path);
        label.getStyleClass().add("log-label");
        logsBox.getChildren().add(label);
    }

    public static void addTooLargeFileLog(Path path) {
        path = FileManager.relativize(path);
        Label label = new Label("• Plik " + path + " pominięty - rozmiar większy niż 100MB.");
        label.getStyleClass().add("log-label");
        logsBox.getChildren().add(label);
    }

    private static String getFileNoun(int numberOfFiles) {
        if (numberOfFiles == 1) return "1 pliku.";
        else return numberOfFiles + " plikach.";
    }

    private static String getReplacementNoun(int count) {
        if (count == 1) return "1 zamiana";
        else if (count >= 2 && count <= 4) return count + " zamiany";
        else return count + " zamian";
    }
}
