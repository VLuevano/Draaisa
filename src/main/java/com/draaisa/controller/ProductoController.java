package com.draaisa.controller;

import com.draaisa.database.DatabaseConnection;
import com.draaisa.model.Producto;
import com.draaisa.model.Categoria;
import com.draaisa.model.Empresa;
import com.draaisa.model.Proveedor;
import com.draaisa.model.Fabricante;
import com.draaisa.model.Cliente;
import com.draaisa.model.Servicio;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoController {

    public void registrarProducto(Producto producto, List<Categoria> categorias, List<Empresa> empresas,
            List<Proveedor> proveedores, List<Fabricante> fabricantes, List<Cliente> clientes,
            List<Servicio> servicios,
            List<Double> preciosEmpresas, List<Double> preciosProveedores, List<Double> preciosFabricantes,
            List<Double> preciosClientes, String moneda)
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
                        servicios, preciosEmpresas, preciosProveedores, preciosFabricantes, preciosClientes, moneda);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void asociarProductoConEntidades(int idProducto, List<Categoria> categorias, List<Empresa> empresas,
            List<Proveedor> proveedores, List<Fabricante> fabricantes, List<Cliente> clientes,
            List<Servicio> servicios,
            List<Double> preciosEmpresas, List<Double> preciosProveedores, List<Double> preciosFabricantes,
            List<Double> preciosClientes, String moneda)
            throws SQLException, IOException {

        int index = 0;
        // Asociar con categorías
        for (Categoria categoria : categorias) {
            asociarProductoConCategoria(idProducto, categoria.getIdCategoria());
        }

        // Asociar con empresas
        for (Empresa empresa : empresas) {
            asociarProductoConEmpresa(idProducto, empresa.getIdEmpresa(), preciosEmpresas.get(index), moneda);
            index++;
        }

        // Asociar con proveedores
        index = 0; // Reiniciamos el índice
        for (Proveedor proveedor : proveedores) {
            asociarProductoConProveedor(idProducto, proveedor.getIdProveedor(), preciosProveedores.get(index), moneda);
            index++;
        }

        // Asociar con fabricantes
        index = 0;
        for (Fabricante fabricante : fabricantes) {
            asociarProductoConFabricante(idProducto, fabricante.getIdFabricante(), preciosFabricantes.get(index),
                    moneda);
            index++;
        }

        // Asociar con clientes
        index = 0;
        for (Cliente cliente : clientes) {
            asociarProductoConCliente(idProducto, cliente.getIdCliente(), preciosClientes.get(index), moneda);
            index++;
        }

        // Asociar con servicios
        for (Servicio servicio : servicios) {
            asociarProductoConServicio(idProducto, servicio.getIdServicio(), moneda);
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

    private void asociarProductoConEmpresa(int idProducto, int idEmpresa, double precio, String moneda)
            throws SQLException, IOException {
        ejecutarAsociacion("INSERT INTO productoempresa (idProducto, idEmpresa, costomercado, monedamercado) VALUES (?, ?, ?, ?)",
                idProducto,
                idEmpresa, precio, moneda);
    }

    private void asociarProductoConProveedor(int idProducto, int idProveedor, double precio, String moneda)
            throws SQLException, IOException {
        ejecutarAsociacion(
                "INSERT INTO productoproveedor (idProducto, idProveedor, costocompraprov, monedacompraprov) VALUES (?, ?, ?, ?)",
                idProducto, idProveedor, precio, moneda);
    }

    private void asociarProductoConFabricante(int idProducto, int idFabricante, double precio, String moneda)
            throws SQLException, IOException {
        ejecutarAsociacion(
                "INSERT INTO productofabricante (idProducto, idFabricante, costocomprafab, monedacomprafab) VALUES (?, ?, ?, ?)",
                idProducto, idFabricante, precio, moneda);
    }

    private void asociarProductoConCliente(int idProducto, int idCliente, double precio, String moneda)
            throws SQLException, IOException {
        ejecutarAsociacion("INSERT INTO productocliente (idProducto, idCliente, costoventa, monedaventa) VALUES (?, ?, ?, ?)",
                idProducto,
                idCliente, precio, moneda);
    }

    private void asociarProductoConServicio(int idProducto, int idServicio, String moneda)
            throws SQLException, IOException {
        ejecutarAsociacion("INSERT INTO productoservicio (idProducto, idServicio) VALUES (?, ?)", idProducto,
                idServicio);
    }

    private void ejecutarAsociacion(String sql, int idProducto, int idEntidad, double precio, String moneda)
            throws SQLException, IOException {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idProducto);
            stmt.setInt(2, idEntidad);
            stmt.setDouble(3, precio);
            stmt.setString(4, moneda); // Añadido el parámetro moneda
            stmt.executeUpdate();
        }
    }

    private void ejecutarAsociacion(String sql, int idProducto, int idServicio) throws SQLException, IOException {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idProducto);
            stmt.setInt(2, idServicio);
            stmt.executeUpdate();
        }
    }

    public List<Categoria> obtenerCategorias() throws IOException {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT * FROM categoria";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                categorias.add(new Categoria(
                        rs.getInt("idCategoria"),
                        rs.getString("nombreCategoria")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categorias;
    }

    public List<Empresa> obtenerEmpresas() throws IOException {
        List<Empresa> empresas = new ArrayList<>();
        String sql = "SELECT * FROM empresa";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                empresas.add(new Empresa(
                        rs.getInt("idEmpresa"),
                        rs.getString("nombreEmp")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return empresas;
    }

    public List<Proveedor> obtenerProveedores() throws IOException {
        List<Proveedor> proveedores = new ArrayList<>();
        String sql = "SELECT * FROM proveedor";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                proveedores.add(new Proveedor(
                        rs.getInt("idProveedor"),
                        rs.getString("nombreProv")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return proveedores;
    }

    public List<Fabricante> obtenerFabricantes() throws IOException {
        List<Fabricante> fabricantes = new ArrayList<>();
        String sql = "SELECT * FROM fabricante";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                fabricantes.add(new Fabricante(
                        rs.getInt("idFabricante"),
                        rs.getString("nombreFabricante")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fabricantes;
    }

    public List<Cliente> obtenerClientes() throws IOException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM cliente";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                clientes.add(new Cliente(
                        rs.getInt("idCliente"),
                        rs.getString("nombreCliente")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }

    public List<Servicio> obtenerServicios() throws IOException {
        List<Servicio> servicios = new ArrayList<>();
        String sql = "SELECT * FROM servicio";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                servicios.add(new Servicio(
                        rs.getInt("idServicio"),
                        rs.getString("descripcionServicio")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return servicios;
    }

}
