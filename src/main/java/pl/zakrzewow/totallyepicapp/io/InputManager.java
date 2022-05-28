package pl.zakrzewow.totallyepicapp.io;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyEvent;

public abstract class InputManager {
    protected TextField textField;
    protected Label label;
    private boolean properlyFilled = false;

    public InputManager(TextField textField, Label label) {
        this.textField = textField;
        this.label = label;
        setTextFilter();
        setOnKeyTyped();
    }

    private void setTextFilter() {
        textField.setTextFormatter(new TextFormatter<>(this::filter));
    }

    private void setOnKeyTyped() {
        textField.setOnKeyTyped(this::onKeyTyped);
    }

    protected abstract TextFormatter.Change filter(TextFormatter.Change change);

    protected abstract void onKeyTyped(KeyEvent keyEvent);

    public void update() {
        onKeyTyped(null);
    }

    protected boolean isTextFieldEmpty() {
        return getTextFieldText().isEmpty();
    }

    protected String getTextFieldText() {
        return textField.getText();
    }

    protected enum InfoTextType {
        POSITIVE, NEGATIVE, NEUTRAL
    }

    protected void clearLabelText() {
        label.setText("");
        setProperlyFilled(true);
    }

    protected void setLabelText(String text, InfoTextType infoTextType) {
        label.setText(text);
        if (infoTextType.equals(InfoTextType.POSITIVE)) {
            setProperlyFilled(true);
            setLabelTextFill("green");
        }
        else if (infoTextType.equals(InfoTextType.NEGATIVE)) {
            setProperlyFilled(false);
            setLabelTextFill("red");
        }
        else if (infoTextType.equals(InfoTextType.NEUTRAL)) {
            setProperlyFilled(true);
            setLabelTextFill("black");
        }
    }

    private void setLabelTextFill(String color) {
        label.setStyle("-fx-text-fill: " + color + ";");
    }

    public boolean isProperlyFilled() {
        return properlyFilled;
    }

    private void setProperlyFilled(boolean properlyFilled) {
        this.properlyFilled = properlyFilled;
    }
}
