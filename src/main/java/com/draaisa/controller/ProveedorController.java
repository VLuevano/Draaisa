package com.draaisa.controller;

import com.draaisa.database.DatabaseConnection;
import com.draaisa.model.Categoria;
import com.draaisa.model.Proveedor;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProveedorController {

    // Método para registrar proveedor desde formulario
    public void registrarProveedor(Proveedor proveedor, List<Categoria> categorias) {
        String sqlProveedor = "INSERT INTO proveedor (nombreprov, cpProveedor, noExtProv, noIntProv, rfcProveedor, municipio, estado, calle, colonia, ciudad, pais, telefonoProv, correoProv, curpproveedor, pfisicaproveedor) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING idProveedor";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmtProveedor = conn.prepareStatement(sqlProveedor, Statement.RETURN_GENERATED_KEYS)) {

            stmtProveedor.setString(1, proveedor.getNombreProv());
            stmtProveedor.setInt(2, proveedor.getCpProveedor());
            stmtProveedor.setInt(3, proveedor.getNoExtProv());
            stmtProveedor.setInt(4, proveedor.getNoIntProv());
            stmtProveedor.setString(5, proveedor.getRfcProveedor());
            stmtProveedor.setString(6, proveedor.getMunicipio());
            stmtProveedor.setString(7, proveedor.getEstado());
            stmtProveedor.setString(8, proveedor.getCalle());
            stmtProveedor.setString(9, proveedor.getColonia());
            stmtProveedor.setString(10, proveedor.getCiudad());
            stmtProveedor.setString(11, proveedor.getPais());
            stmtProveedor.setString(12, proveedor.getTelefonoProv());
            stmtProveedor.setString(13, proveedor.getCorreoProv());
            stmtProveedor.setString(14, proveedor.getCurp());
            stmtProveedor.setBoolean(15, proveedor.isEsPersonaFisica());
            stmtProveedor.executeUpdate();

            ResultSet generatedKeys = stmtProveedor.getGeneratedKeys();
            int idProveedor = -1;
            if (generatedKeys.next()) {
                idProveedor = generatedKeys.getInt(1);
            }

            for (Categoria categoria : categorias) {
                int idCategoria = obtenerOCrearCategoria(categoria);
                asociarProveedorConCategoria(idProveedor, idCategoria);
            }

            System.out.println("Proveedor registrado exitosamente.");

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            System.out.println("Error al registrar proveedor.");
        }
    }

    // Método para obtener o registrar una categoría
    private int obtenerOCrearCategoria(Categoria categoria) throws SQLException, IOException {
        String sqlObtenerId = "SELECT idCategoria FROM categoria WHERE nombreCategoria = ?";
        String sqlInsertCategoria = "INSERT INTO categoria (nombrecategoria, desccategoria) VALUES (?, ?) ON CONFLICT (nombrecategoria) DO NOTHING RETURNING idcategoria";
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmtObtenerId = conn.prepareStatement(sqlObtenerId);
             PreparedStatement stmtInsertCategoria = conn.prepareStatement(sqlInsertCategoria)) {
    
            // Verificar si la categoría ya existe
            stmtObtenerId.setString(1, categoria.getNombreCategoria());
            ResultSet rs = stmtObtenerId.executeQuery();
            if (rs.next()) {
                return rs.getInt("idCategoria");
            }
    
            // Si no existe, insertarla
            stmtInsertCategoria.setString(1, categoria.getNombreCategoria());
            stmtInsertCategoria.setString(2, categoria.getDescripcionCategoria());
            ResultSet generatedKeys = stmtInsertCategoria.executeQuery();
            if (generatedKeys.next()) {
                return generatedKeys.getInt("idCategoria");
            }
        }
        return -1;
    }

    // Método para asociar proveedor con categoría
    private void asociarProveedorConCategoria(int idProveedor, int idCategoria) throws SQLException, IOException {
        String sql = "INSERT INTO proveedorcategoria (idproveedor, idcategoria) VALUES (?, ?) ON CONFLICT DO NOTHING";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idProveedor);
            stmt.setInt(2, idCategoria);
            stmt.executeUpdate();
        }
    }

    // Método para modificar un proveedor
    public void modificarProveedor(Proveedor proveedor) {
        String sql = "UPDATE proveedor SET nombreprov = ?, cpproveedor = ?, estado = ?, telefonoprov = ?, correoprov = ? WHERE idproveedor = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, proveedor.getNombreProv());
            stmt.setInt(2, proveedor.getCpProveedor());
            stmt.setString(3, proveedor.getEstado());
            stmt.setString(4, proveedor.getTelefonoProv());
            stmt.setString(5, proveedor.getCorreoProv());
            stmt.setInt(6, proveedor.getIdProveedor());
            stmt.executeUpdate();

            System.out.println("Proveedor actualizado correctamente.");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            System.out.println("Error al modificar proveedor.");
        }
    }

    // Método para buscar proveedores con filtros
    public List<Proveedor> buscarProveedores(String filtro) {
        List<Proveedor> proveedores = new ArrayList<>();
        String sql = "SELECT * FROM proveedor WHERE nombreprov ILIKE ? OR estado ILIKE ? OR municipio ILIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + filtro + "%");
            stmt.setString(2, "%" + filtro + "%");
            stmt.setString(3, "%" + filtro + "%");

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Proveedor proveedor = new Proveedor(
                    rs.getInt("idProveedor"),
                    rs.getString("nombreProv"),
                    rs.getInt("cpProveedor"),
                    rs.getInt("noExtProv"),
                    rs.getInt("noIntProv"),
                    rs.getString("rfcProveedor"),
                    rs.getString("municipio"),
                    rs.getString("estado"),
                    rs.getString("calle"),
                    rs.getString("colonia"),
                    rs.getString("ciudad"),
                    rs.getString("pais"),
                    rs.getString("telefonoProv"),
                    rs.getString("correoProv"),
                    rs.getString("curpproveedor"),
                    rs.getBoolean("pfisicaproveedor")
                );
                proveedores.add(proveedor);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return proveedores;
    }

    // Método para eliminar proveedor
    public void eliminarProveedor(int idProveedor) {
        String sql = "DELETE FROM proveedor WHERE idProveedor = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idProveedor);
            stmt.executeUpdate();
            System.out.println("Proveedor eliminado exitosamente.");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            System.out.println("Error al eliminar proveedor.");
        }
    }

    // Método para consultar todos los proveedores
    public List<Proveedor> consultarTodosProveedores() {
        List<Proveedor> proveedores = new ArrayList<>();
        String sql = "SELECT * FROM proveedor";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Proveedor proveedor = new Proveedor(
                    rs.getInt("idProveedor"),
                    rs.getString("nombreProv"),
                    rs.getInt("cpProveedor"),
                    rs.getInt("noExtProv"),
                    rs.getInt("noIntProv"),
                    rs.getString("rfcProveedor"),
                    rs.getString("municipio"),
                    rs.getString("estado"),
                    rs.getString("calle"),
                    rs.getString("colonia"),
                    rs.getString("ciudad"),
                    rs.getString("pais"),
                    rs.getString("telefonoProv"),
                    rs.getString("correoProv"),
                    rs.getString("curpproveedor"),
                    rs.getBoolean("pfisicaproveedor")
                );
                proveedores.add(proveedor);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return proveedores;
    }
}
