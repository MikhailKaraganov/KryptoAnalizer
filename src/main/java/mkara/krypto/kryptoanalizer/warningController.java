package mkara.krypto.kryptoanalizer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class warningController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Text warning_text;

    @FXML
    private Button ok_button;

    @FXML
    void initialize() {
        warning_text.setText(Controller.textWarning);
        ok_button.setOnAction(actionEvent -> {
            Stage stage = (Stage)ok_button.getScene().getWindow();
            stage.close();
        });
    }


}
