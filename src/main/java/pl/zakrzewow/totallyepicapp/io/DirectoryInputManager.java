package pl.zakrzewow.totallyepicapp.io;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyEvent;
import pl.zakrzewow.totallyepicapp.FileManager;

public class DirectoryInputManager extends CountingInputManager {
    public DirectoryInputManager(TextField textField, Label label) {
        super(textField, label);
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
    protected void setCount(FileManager.Count count) {
        if (count.directoryCount == 1) {
            setLabelText("Znaleziono katalog!", InfoTextType.POSITIVE);
        } else if (count.directoryCount > 1) {
            setLabelText("Znaleziono katalog oraz " + getNounForm(count.directoryCount - 1, count.isLimited), InfoTextType.POSITIVE);
        } else {
            setLabelText("Katalog nie istnieje!", InfoTextType.NEGATIVE);
        }
    }

    private String getNounForm(int count, boolean isLimited) {
        String form = Noun.getForm(count, "podkatalog!", "podkatalogi!", "podkatalogów!");
        if (isLimited) return "co najmniej " + form;
        else return form;
    }
}
