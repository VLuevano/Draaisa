package com.draaisa.controller;

import com.draaisa.database.DatabaseConnection;
import com.draaisa.model.Usuario;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioController {

    // Método para iniciar sesión
    public static boolean iniciarSesion(String nombreUsuario, String contrasenaUsuario) {
        String query = "SELECT * FROM usuario WHERE nombreusuario = ? AND contrasenausuario = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, nombreUsuario);
            stmt.setString(2, contrasenaUsuario);

            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Retorna true si hay resultados (usuario válido)
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Método para cambiar la contraseña de un usuario
    public static boolean cambiarContrasena(int idUsuario, String nuevaContrasena) {
        String query = "UPDATE usuario SET contrasenausuario = ? WHERE idusuario = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, nuevaContrasena);
            stmt.setInt(2, idUsuario);

            return stmt.executeUpdate() > 0; // Retorna true si se actualizó al menos una fila
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Método para registrar un nuevo usuario
    public static boolean registrarUsuario(Usuario usuario) {
        String query = "INSERT INTO usuario (idusuario, nombreusuario, contrasenausuario, permiso) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, usuario.getIdUsuario());
            stmt.setString(2, usuario.getNombreUsuario());
            stmt.setString(3, usuario.getContrasenaUsuario());
            stmt.setBoolean(4, usuario.isPermiso()); // Añadir el valor del permiso

            return stmt.executeUpdate() > 0; // Retorna true si se agregó un usuario
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Método para eliminar un usuario
    public static boolean eliminarUsuario(int idUsuario) {
        String query = "DELETE FROM usuario WHERE idusuario = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idUsuario);

            return stmt.executeUpdate() > 0; // Retorna true si se eliminó al menos una fila
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Método para consultar todos los usuarios
    public static List<Usuario> consultarUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        String query = "SELECT * FROM usuario";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getInt("idusuario"),
                        rs.getString("nombreusuario"),
                        rs.getString("contrasenausuario"),
                        rs.getBoolean("permiso") // Leer el valor del permiso
                );
                usuarios.add(usuario);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

}
