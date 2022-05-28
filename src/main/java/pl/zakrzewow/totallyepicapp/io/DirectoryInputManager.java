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
            setLabelText("Znaleziono katalog oraz " + getNoun(count - 1), InfoTextType.POSITIVE);
        } else {
            setLabelText("Katalog nie istnieje!", InfoTextType.NEGATIVE);
        }
    }

    private String getNoun(int count) {
        if (count == 1) return "1 podkatalog!";
        else if (count >= 2 && count <= 4) return count + " podkatalogi!";
        else if (count == FileManager.COUNT_LIMIT - 1) return "co najmniej " + count + " podkatalogów!";
        else return count + " podkatalogów!";
    }
}
