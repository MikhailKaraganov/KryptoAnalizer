module mkara.krypto.kryptoanalizer {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens mkara.krypto.kryptoanalizer to javafx.fxml;
    exports mkara.krypto.kryptoanalizer;
}