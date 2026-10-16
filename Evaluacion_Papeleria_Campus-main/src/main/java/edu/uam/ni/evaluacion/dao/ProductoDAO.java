package edu.uam.ni.evaluacion.dao;

import edu.uam.ni.evaluacion.config.DatabaseConfig;
import edu.uam.ni.evaluacion.model.Producto;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public List<Producto> buscar(String texto, String categoria,
                                 boolean soloConExistencias)
            throws SQLException {

        List<Producto> productos = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT id_producto, codigo, nombre, categoria, precio, stock " +
                        "FROM producto WHERE 1=1"
        );

        boolean filtrarTexto =
                texto != null && !texto.trim().isEmpty();

        boolean filtrarCategoria =
                categoria != null && !categoria.equals("Todas");

        if (filtrarTexto) {
            sql.append(" AND (nombre ILIKE ? OR codigo ILIKE ?)");
        }

        if (filtrarCategoria) {
            sql.append(" AND categoria = ?");
        }

        if (soloConExistencias) {
            sql.append(" AND stock > 0");
        }

        sql.append(" ORDER BY codigo ASC");

        try (Connection conexion = DatabaseConfig.conectar();
             PreparedStatement sentencia =
                     conexion.prepareStatement(sql.toString())) {

            int indice = 1;

            if (filtrarTexto) {
                String patron = "%" + texto.trim() + "%";
                sentencia.setString(indice++, patron);
                sentencia.setString(indice++, patron);
            }

            if (filtrarCategoria) {
                sentencia.setString(indice++, categoria);
            }

            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    Producto producto = new Producto(
                            resultado.getInt("id_producto"),
                            resultado.getString("codigo"),
                            resultado.getString("nombre"),
                            resultado.getString("categoria"),
                            resultado.getBigDecimal("precio"),
                            resultado.getInt("stock")
                    );

                    productos.add(producto);
                }
            }
        }

        return productos;
    }

    public boolean existeCodigo(String codigo, Integer idExcluir)
            throws SQLException {

        String sql = "SELECT 1 FROM producto WHERE codigo = ?";

        if (idExcluir != null) {
            sql += " AND id_producto <> ?";
        }

        try (Connection conexion = DatabaseConfig.conectar();
             PreparedStatement sentencia =
                     conexion.prepareStatement(sql)) {

            sentencia.setString(1, codigo);

            if (idExcluir != null) {
                sentencia.setInt(2, idExcluir);
            }

            try (ResultSet resultado = sentencia.executeQuery()) {
                return resultado.next();
            }
        }
    }

    public int insertar(String codigo, String nombre, String categoria,
                        BigDecimal precio, int stock)
            throws SQLException {

        String sql = "INSERT INTO producto " +
                "(codigo, nombre, categoria, precio, stock) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conexion = DatabaseConfig.conectar();
             PreparedStatement sentencia =
                     conexion.prepareStatement(sql)) {

            sentencia.setString(1, codigo);
            sentencia.setString(2, nombre);
            sentencia.setString(3, categoria);
            sentencia.setBigDecimal(4, precio);
            sentencia.setInt(5, stock);

            return sentencia.executeUpdate();
        }
    }

    public int actualizar(int id, String codigo, String nombre,
                          String categoria, BigDecimal precio, int stock)
            throws SQLException {

        String sql = "UPDATE producto SET codigo = ?, nombre = ?, " +
                "categoria = ?, precio = ?, stock = ? " +
                "WHERE id_producto = ?";

        try (Connection conexion = DatabaseConfig.conectar();
             PreparedStatement sentencia =
                     conexion.prepareStatement(sql)) {

            sentencia.setString(1, codigo);
            sentencia.setString(2, nombre);
            sentencia.setString(3, categoria);
            sentencia.setBigDecimal(4, precio);
            sentencia.setInt(5, stock);
            sentencia.setInt(6, id);

            return sentencia.executeUpdate();
        }
    }

    public int eliminar(int id) throws SQLException {

        String sql = "DELETE FROM producto WHERE id_producto = ?";

        try (Connection conexion = DatabaseConfig.conectar();
             PreparedStatement sentencia =
                     conexion.prepareStatement(sql)) {

            sentencia.setInt(1, id);

            return sentencia.executeUpdate();
        }
    }
}
