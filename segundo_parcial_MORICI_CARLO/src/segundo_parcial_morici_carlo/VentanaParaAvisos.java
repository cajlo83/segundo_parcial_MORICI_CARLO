package segundo_parcial_morici_carlo;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class VentanaParaAvisos {

    public static void mostrar(String mensaje, int ancho, int alto) {

        Stage ventana = new Stage();
        ventana.initModality(Modality.APPLICATION_MODAL); // bloquea la ventana principal
        ventana.setTitle("Aviso");
        ventana.setMinWidth(ancho);
        ventana.setMinHeight(alto);

        Label lbl = new Label(mensaje);
        Button btnCerrar = new Button("Cerrar");
        btnCerrar.setOnAction(e -> ventana.close());

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(lbl, btnCerrar);
        layout.setStyle("-fx-alignment: center;");

        Scene scene = new Scene(layout);
        ventana.setScene(scene);
        ventana.showAndWait(); // pausa hasta que se cierre
    }
}
