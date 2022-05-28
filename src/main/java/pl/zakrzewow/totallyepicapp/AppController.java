package pl.zakrzewow.totallyepicapp;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import pl.zakrzewow.totallyepicapp.io.*;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class AppController implements Initializable {

    public TextField directoryTextField;
    public TextField extensionTextField;
    public TextField firstByteSequenceTextField;
    public TextField secondByteSequenceTextField;
    public Label directoryInfoLabel;
    public Label extensionInfoLabel;
    public Label firstByteSequenceInfoLabel;
    public Label secondByteSequenceInfoLabel;
    public InputManager directoryInputManager;
    public InputManager extensionInputManager;
    public ByteSequenceInputManager firstByteSequenceInputManager;
    public ByteSequenceInputManager secondByteSequenceInputManager;
    public VBox logsBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        directoryInputManager = new DirectoryInputManager(directoryTextField, directoryInfoLabel);
        extensionInputManager = new ExtensionInputManager(extensionTextField, extensionInfoLabel);
        firstByteSequenceInputManager = new ByteSequenceInputManager(firstByteSequenceTextField, firstByteSequenceInfoLabel, false);
        secondByteSequenceInputManager = new ByteSequenceInputManager(secondByteSequenceTextField, secondByteSequenceInfoLabel, true);
        LogsManager.setLogsBox(logsBox);
        Platform.runLater(() -> directoryTextField.getParent().requestFocus());
    }

    private void updateIOManagers() {
        directoryInputManager.update();
        extensionInputManager.update();
        firstByteSequenceInputManager.update();
        secondByteSequenceInputManager.update();
    }

    private boolean isInputProperlyFilled() {
        return directoryInputManager.isProperlyFilled()
                && extensionInputManager.isProperlyFilled()
                && firstByteSequenceInputManager.isProperlyFilled()
                && secondByteSequenceInputManager.isProperlyFilled();
    }

    private void setTestValues() {
        directoryTextField.setText("C:\\Users\\User\\Desktop\\@#$%");
        extensionTextField.setText(".txt");
        firstByteSequenceTextField.setText("61 62");
        secondByteSequenceTextField.setText("62");
        updateIOManagers();
    }

    public void handleShowDirectoryChooseDialog() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(null);
        if (selectedDirectory != null) {
            directoryTextField.setText(selectedDirectory.getAbsolutePath());
            updateIOManagers();
        }
    }

    public void handleReplaceButton() {
        updateIOManagers();
        LogsManager.clearLogs();
        if (isInputProperlyFilled()) {
            byte[] firstByteSequence = firstByteSequenceInputManager.getByteSequence();
            byte[] secondByteSequence = secondByteSequenceInputManager.getByteSequence();

            ByteReplacer.Replacer replacer = new ByteReplacer.Replacer(firstByteSequence, secondByteSequence);
            new Thread(replacer).start();
        }
    }

}