/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package c2_2025_clase22_practica6;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.collections.FXCollections.observableArrayList;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author Usuario
 */
public class RegistroProductoController implements Initializable {

    // ******* variables locales ******* 
    private ObservableList<Producto> listaProductos; //observablelist hace que este sincronizada la lista visual con la lsita en memoria

    private Archivo archivo = new Archivo(".\\RegistroProducto.dat");

    // ******* importaciones ******* 
    @FXML
    private Label lblInformativo;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtPrecio;

    @FXML
    private ChoiceBox<Categoria> cbCategoria;

    @FXML
    private TableView<Producto> tvLista;

    @FXML
    private TableColumn<Producto, String> colNombre;

    @FXML
    private TableColumn<Producto, Double> colPrecio;

    @FXML
    private TableColumn<Producto, Categoria> colCategoria;

    // ******* inicializacion ******* 
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //configuracion de columnas
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));

        //cargar categorías en el ChoiceBox
        cbCategoria.getItems().setAll(Categoria.values());

        //cargar archivo
        try {
            Object cargado = archivo.cargar();

            if (cargado != null) {
                listaProductos = observableArrayList((List<Producto>) cargado);
            } else {
                listaProductos = observableArrayList();
            }

        } catch (Exception ex) {
            listaProductos = observableArrayList();
            // System.out.println("Error cargando archivo: " + ex.getMessage());
            lblInformativo.setText("Error cargando archivo: " + ex.getMessage());
        }

        // Conectar lista con TableView
        tvLista.setItems(listaProductos);
    }

    // ******* funciones ******* 
    @FXML
    private void agregar() {

        //******* validaciones
        if (!validacionesOK()) {
            return;

        }

        // crear objeto Producto
            Producto p = new Producto(txtNombre.getText(), Double.parseDouble(txtPrecio.getText()), cbCategoria.getValue());
        //agregar a la lista
        listaProductos.add(p);

        // Guardar
        try {
            archivo.guardar(new ArrayList<>(listaProductos));
            lblInformativo.setText("Producto agregado correctamente");
        } catch (Exception ex) {
            lblInformativo.setText("Error al guardar el archivo");
        }
    }

    @FXML
    private void eliminarSeleccion() {

        //logica de seleccion
        Producto seleccionado = tvLista.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            //System.out.println("No hay nada seleccionado");

            lblInformativo.setText("No hay nada seleccionado");
            return;
        }

        //remover 
        listaProductos.remove(seleccionado);
        lblInformativo.setText("Se elimino: " + seleccionado.toString());

        //guardar archivo
        try {
            archivo.guardar(new ArrayList<>(listaProductos));
        } catch (IOException ex) {
            Logger.getLogger(RegistroProductoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean validacionesOK() {
        String nombre = txtNombre.getText();
        String precioTexto = txtPrecio.getText();
        Categoria categoria = cbCategoria.getValue();

        // nombre
        if (nombre == null || nombre.isBlank()) {
            lblInformativo.setText("Nombre incorrecto");
            return false;
        }

        // precio vacio
        if (precioTexto == null || precioTexto.isBlank()) {
            lblInformativo.setText("El precio está vacío");
            return false;
        }

        // precio numerico
        double precio;
        try {
            precio = Double.parseDouble(precioTexto);
        } catch (NumberFormatException e) {
            lblInformativo.setText("El precio debe ser un número");
            return false;
        }

        // precio mayor que 0
        if (precio <= 0) {
            lblInformativo.setText("El precio debe ser mayor que 0");
            return false;
        }

        // categoria
        if (categoria == null) {
            lblInformativo.setText("Seleccione una categoría");
            return false;
        }

        return true;

    }
}
