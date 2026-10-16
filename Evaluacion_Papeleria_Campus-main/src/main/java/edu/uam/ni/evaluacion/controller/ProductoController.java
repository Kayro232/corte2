package edu.uam.ni.evaluacion.controller;

import edu.uam.ni.evaluacion.dao.ProductoDAO;
import edu.uam.ni.evaluacion.model.Producto;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ProductoController {

    @FXML private TableView<Producto> tabla;

    @FXML private TableColumn<Producto, Integer> colId;
    @FXML private TableColumn<Producto, String> colCodigo;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, String> colCategoria;
    @FXML private TableColumn<Producto, BigDecimal> colPrecio;
    @FXML private TableColumn<Producto, Integer> colStock;

    @FXML private TextField txtBuscar;
    @FXML private CheckBox chkExistencias;
    @FXML private ComboBox<String> cmbFiltroCategoria;

    @FXML private Label lblContador;
    @FXML private Label lblId;

    @FXML private TextField txtCodigo;
    @FXML private TextField txtNombre;
    @FXML private ComboBox<String> cmbCategoria;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtStock;

    private final ProductoDAO dao = new ProductoDAO();

    private final ObservableList<Producto> productos =
            FXCollections.observableArrayList();

    private Integer idSeleccionado = null;

    @FXML
    public void initialize() {

        configurarTabla();

        cmbFiltroCategoria.setItems(
                FXCollections.observableArrayList(
                        "Todas", "Papelería", "Tecnología", "Accesorios"
                )
        );
        cmbFiltroCategoria.setValue("Todas");

        cmbCategoria.setItems(
                FXCollections.observableArrayList(
                        "Papelería", "Tecnología", "Accesorios"
                )
        );

        tabla.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, anterior, actual) -> {
                    if (actual != null) {
                        cargarFormulario(actual);
                    }
                });
    }

    private void configurarTabla() {

        colId.setCellValueFactory(
                new PropertyValueFactory<>("idProducto")
        );

        colCodigo.setCellValueFactory(
                new PropertyValueFactory<>("codigo")
        );

        colNombre.setCellValueFactory(
                new PropertyValueFactory<>("nombre")
        );

        colCategoria.setCellValueFactory(
                new PropertyValueFactory<>("categoria")
        );

        colPrecio.setCellValueFactory(
                new PropertyValueFactory<>("precio")
        );

        colStock.setCellValueFactory(
                new PropertyValueFactory<>("stock")
        );

        tabla.setItems(productos);
        tabla.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );
        tabla.setPlaceholder(
                new Label("No hay productos para mostrar.")
        );
    }

    @FXML
    public void cargarProductosIniciales() {
        buscarProductos();
    }

    @FXML
    public void buscarProductos() {

        try {
            List<Producto> lista = dao.buscar(
                    txtBuscar.getText(),
                    cmbFiltroCategoria.getValue(),
                    chkExistencias.isSelected()
            );

            productos.setAll(lista);

            lblContador.setText(
                    "Productos encontrados: " + productos.size()
            );

        } catch (SQLException e) {
            mostrarError("No se pudieron cargar los productos.", e);
        }
    }

    @FXML
    public void limpiarFiltros() {

        txtBuscar.clear();
        cmbFiltroCategoria.setValue("Todas");
        chkExistencias.setSelected(false);

        buscarProductos();
    }

    private void cargarFormulario(Producto producto) {

        idSeleccionado = producto.getIdProducto();

        lblId.setText("ID: " + idSeleccionado);
        txtCodigo.setText(producto.getCodigo());
        txtNombre.setText(producto.getNombre());
        cmbCategoria.setValue(producto.getCategoria());
        txtPrecio.setText(producto.getPrecio().toPlainString());
        txtStock.setText(String.valueOf(producto.getStock()));
    }

    @FXML
    public void limpiarFormulario() {

        idSeleccionado = null;
        lblId.setText("Nuevo producto");

        txtCodigo.clear();
        txtNombre.clear();
        cmbCategoria.setValue(null);
        txtPrecio.clear();
        txtStock.clear();

        tabla.getSelectionModel().clearSelection();
    }

    private boolean validarFormulario(Integer idExcluir) {

        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();
        String categoria = cmbCategoria.getValue();

        if (codigo.isEmpty() || codigo.length() > 10) {
            mostrarAviso(
                    "El código es obligatorio y debe tener máximo 10 caracteres."
            );
            return false;
        }

        if (nombre.length() < 3 || nombre.length() > 80) {
            mostrarAviso(
                    "El nombre debe tener entre 3 y 80 caracteres."
            );
            return false;
        }

        if (categoria == null) {
            mostrarAviso("Selecciona una categoría.");
            return false;
        }

        try {
            BigDecimal precio = new BigDecimal(
                    txtPrecio.getText().trim()
            );

            if (precio.compareTo(BigDecimal.ZERO) <= 0
                    || precio.scale() > 2
                    || precio.compareTo(
                    new BigDecimal("99999999.99")) > 0) {

                mostrarAviso(
                        "El precio debe ser positivo y tener máximo dos decimales."
                );
                return false;
            }

        } catch (NumberFormatException e) {
            mostrarAviso("Introduce un precio válido.");
            return false;
        }

        try {
            int stock = Integer.parseInt(txtStock.getText().trim());

            if (stock < 0) {
                mostrarAviso("El stock no puede ser negativo.");
                return false;
            }

        } catch (NumberFormatException e) {
            mostrarAviso("El stock debe ser un número entero igual o mayor que cero.");
            return false;
        }

        try {
            if (dao.existeCodigo(codigo, idExcluir)) {
                mostrarAviso("Ya existe un producto con ese código.");
                return false;
            }

        } catch (SQLException e) {
            mostrarError("No se pudo verificar el código.", e);
            return false;
        }

        return true;
    }

    @FXML
    public void guardar() {

        if (!validarFormulario(null)) {
            return;
        }

        try {
            int filas = dao.insertar(
                    txtCodigo.getText().trim(),
                    txtNombre.getText().trim(),
                    cmbCategoria.getValue(),
                    new BigDecimal(txtPrecio.getText().trim()),
                    Integer.parseInt(txtStock.getText().trim())
            );

            if (filas > 0) {
                mostrarInformacion("Producto guardado correctamente.");
                limpiarFormulario();
                buscarProductos();
            }

        } catch (SQLException e) {
            mostrarError("No se pudo guardar el producto.", e);
        }
    }

    @FXML
    public void actualizar() {

        if (idSeleccionado == null) {
            mostrarAviso("Selecciona un producto de la tabla.");
            return;
        }

        if (!validarFormulario(idSeleccionado)) {
            return;
        }

        try {
            int filas = dao.actualizar(
                    idSeleccionado,
                    txtCodigo.getText().trim(),
                    txtNombre.getText().trim(),
                    cmbCategoria.getValue(),
                    new BigDecimal(txtPrecio.getText().trim()),
                    Integer.parseInt(txtStock.getText().trim())
            );

            if (filas > 0) {
                mostrarInformacion("Producto actualizado correctamente.");
                limpiarFormulario();
                buscarProductos();
            } else {
                mostrarAviso("No se encontró el producto.");
            }

        } catch (SQLException e) {
            mostrarError("No se pudo actualizar el producto.", e);
        }
    }

    @FXML
    public void eliminar() {

        if (idSeleccionado == null) {
            mostrarAviso("Selecciona un producto de la tabla.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("Eliminar producto");
        confirmacion.setContentText(
                "¿Deseas eliminar el producto "
                        + txtCodigo.getText() + "?"
        );

        Optional<ButtonType> respuesta = confirmacion.showAndWait();

        if (respuesta.isEmpty()
                || respuesta.get() != ButtonType.OK) {
            return;
        }

        try {
            int filas = dao.eliminar(idSeleccionado);

            if (filas > 0) {
                mostrarInformacion("Producto eliminado correctamente.");
                limpiarFormulario();
                buscarProductos();
            } else {
                mostrarAviso("No se encontró el producto.");
            }

        } catch (SQLException e) {
            mostrarError("No se pudo eliminar el producto.", e);
        }
    }

    private void mostrarAviso(String mensaje) {

        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Validación");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void mostrarInformacion(String mensaje) {

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Papelería Campus");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void mostrarError(String mensaje, SQLException e) {

        System.err.println("Error SQL: " + e.getMessage());
        System.err.println("SQLState: " + e.getSQLState());

        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error de base de datos");
        alerta.setHeaderText(mensaje);
        alerta.setContentText(e.getMessage());
        alerta.showAndWait();
    }
}
