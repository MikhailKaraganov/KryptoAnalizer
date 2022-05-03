package mkara.krypto.kryptoanalizer;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Path;


public class Controller {
    static String textWarning;
    ObservableList<String> codeOrDecodeItem = FXCollections.observableArrayList("Code", "Decode");

    @FXML
    private Button button;

    @FXML
    private Button crackButton;

    @FXML
    private Text modFileName;

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
    private Text ModTextExample;

    @FXML
    private TextField path_field_bf;

    @FXML
    private Text SrcTextExample;

    @FXML
    private Text srcTextExample1;

    @FXML
    private TextArea text_fieldBF;

    @FXML
    private TextArea text_field1BF;

    @FXML
    private Text keyText;

    @FXML
    private Text crackedFilePath;

    @FXML
    void initialize() {
        cocdeDecodeChoiseBox.setItems(codeOrDecodeItem);
        keyText.setText("0");

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
//            Если передан отрицательный ключ (строка), тогда извлекаем интовое отрицательное значение
            int key = key_field.getText().charAt(0) == '-' ? -1 * Integer.parseInt(key_field.getText().substring(1)) : Integer.parseInt(key_field.getText()) ;
            if (cocdeDecodeChoiseBox.getValue().equals("Code")){
                KryptoConverter.CodeDecode(path_field.getText(), key);
            }
            else{
                KryptoConverter.CodeDecode(path_field.getText(), (-1) * key);
            }
            SrcTextExample.setText(path_field.getText() + " Example");
            ModTextExample.setText("Result example");
            modFileName.setText(KryptoConverter.resPas);
            text_field.setText(KryptoConverter.currentSrcText);
            text_field1.setText(KryptoConverter.currentModText);
        });

        path_field_bf.textProperty().addListener((observable, oldText, newText)->{
            if (Path.of(path_field_bf.getText()).isAbsolute()){
                setCrackButtonEnable();
            }
        });

        crackButton.setOnAction(actionEvent -> {
                int key = KryptoConverter.bruteForceCrack(path_field_bf.getText());
                keyText.setText(String.valueOf(key));
                KryptoConverter.CodeDecode(path_field_bf.getText(), -key);

                text_fieldBF.setText(KryptoConverter.currentSrcText);
                text_field1BF.setText(KryptoConverter.currentModText);
                srcTextExample1.setText((path_field_bf.getText() + " Example"));
                crackedFilePath.setText(KryptoConverter.resPas);
        });
    }

//    Окно предупреждения - не используется
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

//          Метод проверки значений полей ввода:
//          -Абсолютный путь к .txt файлу
//          -Что то выбрано в поле выбора
//          -Ключ не пустая строка, и обработка вариантов с минусом и без
    public boolean conditionsOk(){
        try {
            if (path_field.getText()!=null && path_field.getText().contains(".txt") &&
                Path.of(path_field.getText()).isAbsolute() && cocdeDecodeChoiseBox.getValue() != null) {
                    if(!key_field.getText().equals("")) {
                        if(key_field.getText().length() > 1){
                            if((key_field.getText().charAt(0) == '-' && Character.isDigit(key_field.getText().charAt(1))) ||
                                    Character.isDigit(key_field.getText().charAt(0))){
                                return true;
                            }
                            if (key_field.getText().length() == 1 && Character.isDigit(key_field.getText().charAt(0))) {
                                return true;
                            }
                        }
                    }
            }
        }
        catch (NumberFormatException numberFormatException) {
            System.out.println("Не введен ключ");
            conditionsOk();
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
    public void setCrackButtonEnable() {crackButton.setDisable(false);}
    public void setCrackButtonDisable() {crackButton.setDisable(true);}
}