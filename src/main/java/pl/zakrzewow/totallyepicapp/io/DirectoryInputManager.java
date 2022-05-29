package pl.zakrzewow.totallyepicapp.io;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyEvent;
import pl.zakrzewow.totallyepicapp.FileManager;

public class DirectoryInputManager extends CountingInputManager {
    public DirectoryInputManager(TextField textField, Label label) {
        super(textField, label, FileManager::countDirectories);
    }

    @Override
    protected TextFormatter.Change filter(TextFormatter.Change change) {
        return change.getControlNewText().matches("[^\"*?<>|/]*") ? change : null;
    }

    @Override
    protected void onKeyTyped(KeyEvent keyEvent) {
        if (isTextFieldEmpty()) {
            setLabelText("Nie wybrano katalogu!", InfoTextType.NEGATIVE);
        } else {
            FileManager.setDirectoryPath(getTextFieldText());
            count();
        }
    }

    @Override
    protected void setCount(int count) {
        if (count == 1) {
            setLabelText("Znaleziono katalog!", InfoTextType.POSITIVE);
        } else if (count > 1) {
            setLabelText("Znaleziono katalog oraz " + getNounForm(count - 1), InfoTextType.POSITIVE);
        } else {
            setLabelText("Katalog nie istnieje!", InfoTextType.NEGATIVE);
        }
    }

    private String getNounForm(int count) {
        String form = Noun.getForm(count, "podkatalog", "podkatalogi", "podkatalogów");
        if (count > FileManager.COUNT_LIMIT) return "co najmniej " + form;
        else return form;
    }
}
