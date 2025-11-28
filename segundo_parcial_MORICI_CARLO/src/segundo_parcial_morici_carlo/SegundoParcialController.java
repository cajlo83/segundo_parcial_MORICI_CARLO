package segundo_parcial_morici_carlo;

import modelo.Producto;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
public class SegundoParcialController implements Initializable {

    // ******* variables locales ******* 
    private ObservableList<Producto> listaProductos; //observablelist hace que este sincronizada la lista visual con la lista en memoria
    private ObservableList<Producto> listaCarrito; //observablelist hace que este sincronizada la lista visual con la lista en memoria

    private ArchivoDat archivoProductosDat = new ArchivoDat(".\\productos.dat");

    // ******* importaciones ******* 
    @FXML
    private Label lblInformativo;

    @FXML
    private TextField txtCantidad;

    @FXML
    private ListView<Producto> lvCarrito;

    @FXML
    private TableView<Producto> tvLista;

    @FXML
    private TableColumn<Producto, String> colNombre;

    @FXML
    private TableColumn<Producto, Double> colPrecio;

    @FXML
    private TableColumn<Producto, Integer> colStock;

    // ******* inicializacion ******* 
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //iniciar listas visibles
        listaProductos = observableArrayList();
        listaCarrito = observableArrayList();
        lvCarrito.setItems(listaCarrito);

        //boolean aviso = false;
        //configuracion de columnas
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        // cargar archivo .dat
        try {
            Object cargado = archivoProductosDat.cargar();

            if (cargado != null) {
                List<Producto> productos = (List<Producto>) cargado;
                listaProductos = observableArrayList(productos);
            } else {
                listaProductos = observableArrayList();
                VentanaParaAvisos.mostrar("archivo vacio o no existe archivo: ", 300, 100);
            }

        } catch (Exception ex) {
            listaProductos = observableArrayList();
            lblInformativo.setText("Error cargando archivo: " + ex.getMessage());
            VentanaParaAvisos.mostrar("Error cargando archivo: " + ex.getMessage(), 300, 100);
        }

// conectar lista con TableView
        tvLista.setItems(listaProductos);

    }

    // ******* funciones ******* 
    @FXML
    private void agregarAlCarrito() {

        int cantidad;
        Producto pCarrito;

        //logica de seleccion
        Producto seleccionado = tvLista.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            //System.out.println("No hay nada seleccionado");

            lblInformativo.setText("No hay nada seleccionado");
            return;
        } else {
            cantidad = Integer.parseInt(txtCantidad.getText());

            if (cantidad > 0 && cantidad < seleccionado.getStock()) {
                pCarrito = new Producto(seleccionado.getNombre(), seleccionado.getPrecio(), cantidad);
                listaCarrito.add(pCarrito);

            }
        }

    }

    @FXML
    private void confirmarCompra() {

        //validar que no este vacio
        if (listaCarrito.size() <= 0) {
            lblInformativo.setText("Lista de compras vacia");
        } else {
            double total = 0;

            /*Una vez finalizada la compra se debe validar que tenga productos en el carrito, 
            calcular el total de la compra y generar el ticket de compra, 
            que se guardara en un archivo txt denominado ticket, 
            con el detalle de la compra y el valor total:  
             */
            StringBuilder textoSalida = new StringBuilder("Nombre producto\t-\tCantidad\t-\tSubtotal\n");
            for (Producto p : listaCarrito) {
                int cantidad = p.getStock();
                double precio = p.getPrecio();
                double subtotal = cantidad * precio;
                textoSalida.append(p.getNombre()).append("\t-\t");
                textoSalida.append(cantidad).append("\t-\t");
                textoSalida.append(subtotal).append("\n");
                total += subtotal;
            }
            textoSalida.append("\nTOTAL A PAGAR: $").append(total);

            /*Una vez confirmada la compra debe actualizarse el stock real de cada producto afectado 
            sobrescribirse el archivo .dat original con la nueva lista serializada y limpiar el carrito.*/
            //actualizar stock real con for anidado
            for (Producto p : listaProductos) {
                for (Producto pCarrito : listaCarrito) {
                    if (p.getNombre().equals(pCarrito.getNombre())) {
                        p.setStock(p.getStock() - pCarrito.getStock());

                        break; //salida del segundo for hacia el primero en caso de encontrar coincidencia

                    }

                }
            }

            //actualizar archivo productos.dat
            try {
                archivoProductosDat.guardar(new ArrayList<>(listaProductos));

            } catch (IOException ex) {
                Logger.getLogger(SegundoParcialController.class.getName()).log(Level.SEVERE, null, ex);
            }
            //limpiar carrito
            listaCarrito.clear();

            //termina logica de venta, empieza logica de guardar ticket
            LocalDateTime ahora = LocalDateTime.now(); // conseguir fecha y hora actual
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            String fechaYHoraActual = ahora.format(formato);

            ArchivoTxt archivo = new ArchivoTxt("ticket " + fechaYHoraActual + ".txt");

            try {
                archivo.guardar(textoSalida.toString());
                System.out.println("Guardado ok");
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }

    }

    /*
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
            Logger.getLogger(SegundoParcialController.class.getName()).log(Level.SEVERE, null, ex);
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

    }*/
}
