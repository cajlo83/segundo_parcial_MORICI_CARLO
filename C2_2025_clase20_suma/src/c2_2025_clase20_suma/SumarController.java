
package c2_2025_clase20_suma;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class SumarController {
    
     @FXML
     private TextField txtNumeroA;
     @FXML
     private TextField txtNumeroB;
     @FXML
     private Label lblResultado;
     @FXML
     private void sumar(){
         try {
             double a = Double.parseDouble(txtNumeroA.getText());
             double b = Double.parseDouble(txtNumeroB.getText());
             double suma = a + b;
             lblResultado.setText("Resultado: "+ suma);
         } catch (NumberFormatException e){
             lblResultado.setText("ingresar solo numeros");
         }
     }
    
    
}
