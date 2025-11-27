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
    private ObservableList<Producto> listaProductos;

    private Archivo archivo = new Archivo(".\\RegistroProducto.dat");

    // ******* importaciones ******* 
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

        // configuracion de columnas
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));

        // 2. Cargar categorías en el ChoiceBox
        cbCategoria.getItems().setAll(Categoria.values());

        // 3. Cargar archivo
        try {
            Object cargado = archivo.cargar();

            if (cargado != null) {
                listaProductos = javafx.collections.FXCollections.observableArrayList(
                        (List<Producto>) cargado
                );
            } else {
                listaProductos = javafx.collections.FXCollections.observableArrayList();
            }

        } catch (Exception ex) {
            listaProductos = javafx.collections.FXCollections.observableArrayList();
            System.out.println("Error cargando archivo: " + ex.getMessage());
        }

        // 4. Conectar lista con TableView
        tvLista.setItems(listaProductos);
    }

    // ******* funciones ******* 
    @FXML
    private void agregar() {

        String nombre = txtNombre.getText();
        double precio = Double.parseDouble(txtPrecio.getText());
        Categoria categoria = cbCategoria.getValue();

        if (nombre == null || nombre.isBlank()) {
            return;
        }
        if (precio <= 0) {
            return;
        }
        if (categoria == null) {
            return;
        }

        Producto p = new Producto(nombre, precio, categoria);

        // agregar a la lista 
        listaProductos.add(p);

        // guardar archivo
        try {
            archivo.guardar(new ArrayList<>(listaProductos));
        } catch (IOException ex) {
            Logger.getLogger(RegistroProductoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void eliminarSeleccion() {
        
        //logica de seleccion
        Producto seleccionado = tvLista.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            System.out.println("No hay nada seleccionado");
            return;
        }

        //remover 
        listaProductos.remove(seleccionado);

        //guardar archivo
        try {
            archivo.guardar(new ArrayList<>(listaProductos));
        } catch (IOException ex) {
            Logger.getLogger(RegistroProductoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
