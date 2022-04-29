package mkara.krypto.kryptoanalizer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;

public class Controller {
    static String path;
    static int key;
    static String textWarning;
    ObservableList<String> codeOrDecodeItem = FXCollections.observableArrayList("Code", "Decode");
    static int enteredKey;

    @FXML
    private Button button;

    @FXML
    private TextField key_field;

    @FXML
    private TextArea text_field1;

    @FXML
    private TextArea text_field;

    @FXML
    private TextField path_field;

    @FXML
    private ChoiceBox<String> cocdeDecodeChoiseBox;

    @FXML
    void initialize() {
        cocdeDecodeChoiseBox.setItems(codeOrDecodeItem);

        cocdeDecodeChoiseBox.paddingProperty().addListener((observable, oldText, newText)-> {
            if(conditionsOk()){
                setButtonEnable();
            }
            else{
                setButtonDisable();
            }
        });

        key_field.textProperty().addListener((observable, oldText, newText)-> {
            if(conditionsOk()){
                setButtonEnable();
            }
            else{
                setButtonDisable();
            }

        });

        path_field.textProperty().addListener((observable, oldText, newText)-> {
                if(conditionsOk()){
                    setButtonEnable();
                }
                else{
                    setButtonDisable();
                }
        });

        button.setOnAction(actionEvent -> {
            text_field.setText("");
            text_field1.setText("");
            if (cocdeDecodeChoiseBox.getValue().equals("Code")){
                KryptoConverter.CodeDecode(path_field.getText(), Integer.parseInt(key_field.getText()));
            }
            else{
                KryptoConverter.CodeDecode(path_field.getText(), (-1) * Integer.parseInt(key_field.getText()));
            }
            text_field.setText(KryptoConverter.currentSrcText);
            text_field1.setText(KryptoConverter.currentModText);

        });

    }

    private void warningWindowShow (){
        try {
            FXMLLoader fxmlLoaderWarning = new FXMLLoader(KryptoApplication.class.getResource("warning.fxml"));
            Stage stageWarning = new Stage();
            Scene scene = new Scene(fxmlLoaderWarning.load());
            stageWarning.setScene(scene);
            stageWarning.show();
        }catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public boolean conditionsOk(){
        try {
            if (path_field.getText()!=null && path_field.getText().contains(".txt") &&
                    Path.of(path_field.getText()).isAbsolute() &&
                    Integer.parseInt(key_field.getText())>0 &&
            cocdeDecodeChoiseBox.getValue() != null) {
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public void setButtonEnable(){
        button.setDisable(false);
    }

    public void setButtonDisable(){
        button.setDisable(true);
    }

//    private void fileChooser (){
//        FileChooser
//    }
}