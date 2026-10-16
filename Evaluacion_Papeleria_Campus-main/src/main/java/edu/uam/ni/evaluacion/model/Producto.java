
package edu.uam.ni.evaluacion.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.math.BigDecimal;

public class Producto {

    private final IntegerProperty idProducto =
            new SimpleIntegerProperty();

    private final StringProperty codigo =
            new SimpleStringProperty();

    private final StringProperty nombre =
            new SimpleStringProperty();

    private final StringProperty categoria =
            new SimpleStringProperty();

    private BigDecimal precio;

    private final IntegerProperty stock =
            new SimpleIntegerProperty();

    public Producto(int idProducto, String codigo, String nombre,
                    String categoria, BigDecimal precio, int stock) {

        this.idProducto.set(idProducto);
        this.codigo.set(codigo);
        this.nombre.set(nombre);
        this.categoria.set(categoria);
        this.precio = precio;
        this.stock.set(stock);
    }

    public int getIdProducto() {
        return idProducto.get();
    }

    public IntegerProperty idProductoProperty() {
        return idProducto;
    }

    public String getCodigo() {
        return codigo.get();
    }

    public void setCodigo(String valor) {
        codigo.set(valor);
    }

    public StringProperty codigoProperty() {
        return codigo;
    }

    public String getNombre() {
        return nombre.get();
    }

    public void setNombre(String valor) {
        nombre.set(valor);
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    public String getCategoria() {
        return categoria.get();
    }

    public void setCategoria(String valor) {
        categoria.set(valor);
    }

    public StringProperty categoriaProperty() {
        return categoria;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal valor) {
        precio = valor;
    }

    public int getStock() {
        return stock.get();
    }

    public void setStock(int valor) {
        stock.set(valor);
    }

    public IntegerProperty stockProperty() {
        return stock;
    }
}
