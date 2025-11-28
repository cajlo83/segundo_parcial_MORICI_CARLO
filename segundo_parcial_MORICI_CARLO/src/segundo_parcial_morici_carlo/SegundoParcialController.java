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

public class SegundoParcialController implements Initializable {

    //  *****  VARIABLES  ***** 
    private ObservableList<Producto> listaProductos;
    private ObservableList<Producto> listaCarrito;
    private final ArchivoDat archivoProductosDat = new ArchivoDat("productos.dat");

    //  *****  FXML ***** 
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

    // ***** INICIALIZAR ***** 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inicializarListas();
        configurarColumnas();
        cargarProductosDesdeArchivo();
        tvLista.setItems(listaProductos);
    }

    private void inicializarListas() {
        listaProductos = observableArrayList();
        listaCarrito = observableArrayList();
        lvCarrito.setItems(listaCarrito);
    }

    private void configurarColumnas() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
    }

    private void cargarProductosDesdeArchivo() {
        try {
            Object cargado = archivoProductosDat.cargar();

            if (cargado instanceof List<?>) {
                List<?> raw = (List<?>) cargado;

                if (!raw.isEmpty() && !(raw.get(0) instanceof Producto)) {
                    VentanaParaAvisos.mostrar("El archivo no contiene productos válidos", 300, 100);
                    return;
                }

                listaProductos = observableArrayList((List<Producto>) raw);

            } else {
                VentanaParaAvisos.mostrar("Archivo vacío o incompatible", 300, 100);
            }

        } catch (Exception ex) {
            lblInformativo.setText("Error cargando archivo: " + ex.getMessage());
            VentanaParaAvisos.mostrar("Error cargando archivo: " + ex.getMessage(), 300, 100);
        }
    }

    // ***** AGREGAR AL CARRITO ***** 
    @FXML
    private void agregarAlCarrito() {

        Producto seleccionado = obtenerProductoSeleccionado();
        if (seleccionado == null) {
            return;
        }

        Integer cantidad = obtenerCantidad();
        if (cantidad == null) {
            return;
        }

        if (!cantidadValida(cantidad, seleccionado)) {
            return;
        }

        agregarProductoAlCarrito(seleccionado, cantidad);
    }

    private Producto obtenerProductoSeleccionado() {
        Producto p = tvLista.getSelectionModel().getSelectedItem();
        if (p == null) {
            lblInformativo.setText("No hay nada seleccionado");
        }
        return p;
    }

    private Integer obtenerCantidad() {
        try {
            return Integer.parseInt(txtCantidad.getText());
        } catch (NumberFormatException ex) {
            lblInformativo.setText("Cantidad invalida");
            return null;
        }
    }

    private boolean cantidadValida(int cantidad, Producto seleccionado) {
        if (cantidad <= 0) {
            lblInformativo.setText("Cantidad debe ser mayor a 0");
            return false;
        }
        if (cantidad > seleccionado.getStock()) {
            lblInformativo.setText("No hay stock suficiente");
            return false;
        }
        return true;
    }

    private void agregarProductoAlCarrito(Producto original, int cantidad) {
        Producto pCarrito = new Producto(original.getNombre(), original.getPrecio(), cantidad);
        listaCarrito.add(pCarrito);
    }

    // ***** confirmacion de compra y logicas de tk ***** 
    @FXML
    private void confirmarCompra() {

        if (listaCarrito.isEmpty()) {
            lblInformativo.setText("Lista de compras vacía");
            return;
        }

        double total = calcularTotal();

        String ticketTexto = generarTextoTicket(total);

        actualizarStockProductos();
        guardarProductosEnArchivo();

        listaCarrito.clear();
        tvLista.refresh();


        guardarTicket(ticketTexto);
    }

    // calcular total
    private double calcularTotal() {
        double total = 0;
        for (Producto p : listaCarrito) {
            total += p.getPrecio() * p.getStock();
        }
        return total;
    }

    // texto del tk
    private String generarTextoTicket(double total) {
        StringBuilder sb = new StringBuilder("Nombre producto\t-\tCantidad\t-\tSubtotal\n");

        for (Producto p : listaCarrito) {
            double subtotal = p.getPrecio() * p.getStock();
            sb.append(p.getNombre()).append("\t-\t")
                    .append(p.getStock()).append("\t-\t")
                    .append(subtotal).append("\n");
        }

        sb.append("\nTOTAL A PAGAR: $").append(total);
        return sb.toString();
    }

    // control stock
    private void actualizarStockProductos() {
        for (Producto pLista : listaProductos) {
            for (Producto pCarrito : listaCarrito) {
                if (pLista.getNombre().equals(pCarrito.getNombre())) {
                    pLista.setStock(pLista.getStock() - pCarrito.getStock());
                    break;
                }
            }
        }
    }

    private void guardarProductosEnArchivo() {
        try {
            archivoProductosDat.guardar(new ArrayList<>(listaProductos));
        } catch (IOException ex) {
            Logger.getLogger(SegundoParcialController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // guardar tk
    private void guardarTicket(String texto) {
        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String fecha = ahora.format(formato);

        ArchivoTxt archivo = new ArchivoTxt("ticket " + fecha + ".txt");

        try {
            archivo.guardar(texto);
            System.out.println("Ticket guardado correctamente");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
