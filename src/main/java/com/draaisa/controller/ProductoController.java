package com.draaisa.controller;

import com.draaisa.database.DatabaseConnection;
import com.draaisa.model.Producto;
import com.draaisa.model.Categoria;
import com.draaisa.model.Empresa;
import com.draaisa.model.Proveedor;
import com.draaisa.model.Fabricante;
import com.draaisa.model.Cliente;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoController {

    public void registrarProducto(Producto producto, List<Categoria> categorias, List<Empresa> empresas,
            List<Proveedor> proveedores, List<Fabricante> fabricantes, List<Cliente> clientes, List<Double> precios)
            throws IOException {
        String sql = "INSERT INTO producto (nombreProducto, fichaproducto, alternoproducto, existenciaproducto) VALUES (?, ?, ?, ?) RETURNING idProducto";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, producto.getNombreProducto());
            stmt.setString(2, producto.getFichaProducto());
            stmt.setString(3, producto.getAlternoProducto());
            stmt.setInt(4, producto.getExistenciaProducto());
            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int idProducto = generatedKeys.getInt(1);
                asociarProductoConEntidades(idProducto, categorias, empresas, proveedores, fabricantes, clientes,
                        precios);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void asociarProductoConEntidades(int idProducto, List<Categoria> categorias, List<Empresa> empresas,
            List<Proveedor> proveedores, List<Fabricante> fabricantes, List<Cliente> clientes, List<Double> precios)
            throws SQLException, IOException {
        int index = 0;
        for (Categoria categoria : categorias) {
            asociarProductoConCategoria(idProducto, categoria.getIdCategoria());
        }
        for (Empresa empresa : empresas) {
            asociarProductoConEmpresa(idProducto, empresa.getIdEmpresa(), precios.get(index++));
        }
        for (Proveedor proveedor : proveedores) {
            asociarProductoConProveedor(idProducto, proveedor.getIdProveedor(), precios.get(index++));
        }
        for (Fabricante fabricante : fabricantes) {
            asociarProductoConFabricante(idProducto, fabricante.getIdFabricante(), precios.get(index++));
        }
        for (Cliente cliente : clientes) {
            asociarProductoConCliente(idProducto, cliente.getIdCliente(), precios.get(index++));
        }
    }

    public List<Producto> consultarProductos() throws IOException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM producto";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                productos.add(new Producto(
                        rs.getInt("idProducto"),
                        rs.getString("nombreProducto"),
                        rs.getString("fichaProducto"),
                        rs.getString("alternoProducto"),
                        rs.getInt("existenciaProducto")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productos;
    }

    public void modificarProducto(Producto producto) throws IOException {
        String sql = "UPDATE producto SET nombreProducto = ?, fichaproducto = ?, alternoproducto = ?, existenciaproducto = ? WHERE idProducto = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, producto.getNombreProducto());
            stmt.setString(2, producto.getFichaProducto());
            stmt.setString(3, producto.getAlternoProducto());
            stmt.setInt(4, producto.getExistenciaProducto());
            stmt.setInt(5, producto.getIdProducto());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminarProducto(int idProducto) throws IOException {
        String sql = "DELETE FROM producto WHERE idProducto = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idProducto);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void asociarProductoConCategoria(int idProducto, int idCategoria) throws SQLException, IOException {
        ejecutarAsociacion("INSERT INTO productocategoria (idProducto, idCategoria) VALUES (?, ?)", idProducto,
                idCategoria);
    }

    private void asociarProductoConEmpresa(int idProducto, int idEmpresa, double precio)
            throws SQLException, IOException {
        ejecutarAsociacion("INSERT INTO productoempresa (idProducto, idEmpresa, precio) VALUES (?, ?, ?)", idProducto,
                idEmpresa, precio);
    }

    private void asociarProductoConProveedor(int idProducto, int idProveedor, double precio)
            throws SQLException, IOException {
        ejecutarAsociacion("INSERT INTO productoproveedor (idProducto, idProveedor, precio) VALUES (?, ?, ?)",
                idProducto, idProveedor, precio);
    }

    private void asociarProductoConFabricante(int idProducto, int idFabricante, double precio)
            throws SQLException, IOException {
        ejecutarAsociacion("INSERT INTO productofabricante (idProducto, idFabricante, precio) VALUES (?, ?, ?)",
                idProducto, idFabricante, precio);
    }

    private void asociarProductoConCliente(int idProducto, int idCliente, double precio)
            throws SQLException, IOException {
        ejecutarAsociacion("INSERT INTO productocliente (idProducto, idCliente, precio) VALUES (?, ?, ?)", idProducto,
                idCliente, precio);
    }

    private void ejecutarAsociacion(String sql, int idProducto, int idEntidad, double... precio)
            throws SQLException, IOException {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idProducto);
            stmt.setInt(2, idEntidad);
            if (precio.length > 0) {
                stmt.setDouble(3, precio[0]);
            }
            stmt.executeUpdate();
        }
    }
}
