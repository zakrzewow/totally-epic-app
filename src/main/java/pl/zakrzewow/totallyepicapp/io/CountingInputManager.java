package pl.zakrzewow.totallyepicapp.io;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import pl.zakrzewow.totallyepicapp.FileManager;

public abstract class CountingInputManager extends InputManager {
    private Thread thread;

    public CountingInputManager(TextField textField, Label label) {
        super(textField, label);
    }

    protected void count() {
        if (thread != null) {
            thread.interrupt();
        }
        thread = new Thread(() -> {
            FileManager.Count count = FileManager.count();
            Platform.runLater(() -> setCount(count));
        });
        thread.start();
    }

    protected abstract void setCount(FileManager.Count count);
}
