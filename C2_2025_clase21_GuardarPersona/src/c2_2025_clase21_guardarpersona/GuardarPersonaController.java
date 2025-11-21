/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package c2_2025_clase21_guardarpersona;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author Usuario
 */
public class GuardarPersonaController {

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtApellido;

    @FXML
    private TextField txtDNI;
    @FXML
    private Label lblEstado;

    @FXML
    private RadioButton rbFemenino;

    @FXML
    private RadioButton rbMasculino;

    @FXML
    private void cerrar() {
        Stage stage = (Stage) rbMasculino.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void aceptar() {
        if (!validar()) {
            lblEstado.setText("Datos inválidos");
            return;
        }

        Persona persona = new Persona(
                txtNombre.getText(),
                txtApellido.getText(),
                Integer.parseInt(txtDNI.getText()),
                obtenerGenero()
        );
        System.out.println(persona);
        lblEstado.setText("Guardado correctamente");
    }


    private String obtenerGenero() {
        String retorno;

        if (rbMasculino.isSelected()) {
            retorno = "Masculino";
        } else if (rbFemenino.isSelected()) {
            retorno = "Femenino";
        } else {
            retorno = "";
        }

        return retorno;
    }

    private boolean validar() {
        if (txtNombre.getText().isEmpty()) {
            return false;
        }
        if (txtApellido.getText().isEmpty()) {
            return false;
        }

        try {
            int dni = Integer.parseInt(txtDNI.getText());
            if (dni <= 0) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }

        if (obtenerGenero().isEmpty()) {
            return false;
        }

        return true;
    }

}
