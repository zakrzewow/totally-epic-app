package pl.zakrzewow.totallyepicapp.io;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyEvent;

public class ByteSequenceInputManager extends InputManager {
    boolean canBeEmpty;

    public ByteSequenceInputManager(TextField textField, Label label, boolean canBeEmpty) {
        super(textField, label);
        this.canBeEmpty = canBeEmpty;
    }

    @Override
    protected TextFormatter.Change filter(TextFormatter.Change change) {
        return change.getControlNewText().matches("[a-fA-F0-9 ]*") ? change : null;
    }

    @Override
    protected void onKeyTyped(KeyEvent keyEvent) {
        if (isTextFieldEmpty()) {
            if (canBeEmpty) {
                setLabelText("Ciąg bajtów jest pusty.", InfoTextType.NEUTRAL);
            } else {
                setLabelText("Ciąg bajtów nie może być pusty!", InfoTextType.NEGATIVE);
            }
        }
        else {
            clearLabelText();

            String text = getTextFieldText();
            text = text.replaceAll(" ", "");

            StringBuilder newText = new StringBuilder();
            int i = 0;
            for (; i < text.length() - 2; i += 2) {
                newText.append(text, i, i + 2);
                newText.append(" ");
            }
            newText.append(text.substring(i));
            textField.setText(newText.toString());
            textField.positionCaret(newText.length());
        }
    }

    public byte[] getByteSequence() {
        return hexStringToByteArray(getTextFieldText());
    }

    private static byte[] hexStringToByteArray(String s) {
        s = s.replaceAll(" ", "");
        if (s.length() % 2 == 1) {
            s = s + "0";
        }
        int len = s.length();

        byte[] bytes = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            bytes[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return bytes;
    }
}
