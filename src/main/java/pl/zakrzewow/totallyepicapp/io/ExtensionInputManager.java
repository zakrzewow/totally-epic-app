package pl.zakrzewow.totallyepicapp.io;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyEvent;
import pl.zakrzewow.totallyepicapp.FileManager;

public class ExtensionInputManager extends CountingInputManager {
    public ExtensionInputManager(TextField textField, Label label) {
        super(textField, label, FileManager::countFilesWithExt);
    }

    @Override
    protected TextFormatter.Change filter(TextFormatter.Change change) {
        return change.getControlNewText().matches("^(\\.)?\\w*") ? change : null;
    }

    @Override
    protected void onKeyTyped(KeyEvent keyEvent) {
        if (isTextFieldEmpty()) {
            setLabelText("Nie wpisano rozszerzenia!", InfoTextType.NEGATIVE);
        } else {
            FileManager.setExtension(getTextFieldText());
            count();
        }
    }

    @Override
    protected void setCount(int count) {
        if (count > 0) {
            setLabelText("Znaleziono " + getNoun(count), InfoTextType.POSITIVE);
        } else {
            setLabelText("Brak plików o podanym rozszerzeniu!", InfoTextType.NEGATIVE);
        }
    }

    private String getNoun(int count) {
        String form = Noun.getForm(count, "pliki", "pliki", "plików");
        if (count > FileManager.COUNT_LIMIT) return "co najmniej " + form;
        else return form;
    }
}
