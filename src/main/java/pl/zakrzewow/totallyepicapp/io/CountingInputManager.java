package pl.zakrzewow.totallyepicapp.io;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.function.IntSupplier;

public abstract class CountingInputManager extends InputManager {
    private Thread thread;
    private final IntSupplier countingMethod;

    public CountingInputManager(TextField textField, Label label, IntSupplier countingMethod) {
        super(textField, label);
        this.countingMethod = countingMethod;
    }

    protected void count() {
        if (thread != null) {
            thread.interrupt();
        }
        thread = new Thread(() -> {
            int count = countingMethod.getAsInt();
            Platform.runLater(() -> setCount(count));
        });
        thread.start();
    }

    protected abstract void setCount(int count);
}
