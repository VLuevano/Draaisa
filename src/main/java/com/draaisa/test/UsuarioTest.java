package com.draaisa.test;

import com.draaisa.controller.UsuarioController;
import com.draaisa.model.Usuario;

import java.util.List;
import java.util.Scanner;

public class UsuarioTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n===== MENÚ DE PRUEBA - GESTIÓN DE USUARIOS =====");
            System.out.println("1. Registrar usuario");
            System.out.println("2. Iniciar sesión");
            System.out.println("3. Cambiar contraseña");
            System.out.println("4. Eliminar usuario");
            System.out.println("5. Listar usuarios");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer

            switch (opcion) {
                case 1:
                    registrarUsuario(scanner);
                    break;
                case 2:
                    iniciarSesion(scanner);
                    break;
                case 3:
                    cambiarContrasena(scanner);
                    break;
                case 4:
                    eliminarUsuario(scanner);
                    break;
                case 5:
                    listarUsuarios();
                    break;
                case 0:
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opción inválida. Intente de nuevo.");
            }
        } while (opcion != 0);

        scanner.close();
    }

    private static void registrarUsuario(Scanner scanner) {
        System.out.print("Ingrese ID de usuario: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Ingrese nombre de usuario: ");
        String nombre = scanner.nextLine();

        System.out.print("Ingrese contraseña: ");
        String contrasena = scanner.nextLine();

        Usuario usuario = new Usuario(id, nombre, contrasena);
        boolean registrado = UsuarioController.registrarUsuario(usuario);

        if (registrado) {
            System.out.println("Usuario registrado exitosamente.");
        } else {
            System.out.println("Error al registrar usuario.");
        }
    }

    private static void iniciarSesion(Scanner scanner) {
        System.out.print("Ingrese nombre de usuario: ");
        String nombre = scanner.nextLine();

        System.out.print("Ingrese contraseña: ");
        String contrasena = scanner.nextLine();

        boolean loginExitoso = UsuarioController.iniciarSesion(nombre, contrasena);

        if (loginExitoso) {
            System.out.println("Inicio de sesión exitoso.");
        } else {
            System.out.println("Credenciales incorrectas.");
        }
    }

    private static void cambiarContrasena(Scanner scanner) {
        System.out.print("Ingrese ID del usuario: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Ingrese nueva contraseña: ");
        String nuevaContrasena = scanner.nextLine();

        boolean cambioExitoso = UsuarioController.cambiarContrasena(id, nuevaContrasena);

        if (cambioExitoso) {
            System.out.println("Contraseña actualizada correctamente.");
        } else {
            System.out.println("Error al actualizar la contraseña.");
        }
    }

    private static void eliminarUsuario(Scanner scanner) {
        System.out.print("Ingrese ID del usuario a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        boolean eliminado = UsuarioController.eliminarUsuario(id);

        if (eliminado) {
            System.out.println("Usuario eliminado correctamente.");
        } else {
            System.out.println("Error al eliminar usuario.");
        }
    }

    private static void listarUsuarios() {
        List<Usuario> usuarios = UsuarioController.consultarUsuarios();

        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
        } else {
            System.out.println("\nLista de usuarios:");
            for (Usuario usuario : usuarios) {
                System.out.println("ID: " + usuario.getIdUsuario() + " | Nombre: " + usuario.getNombreUsuario());
            }
        }
    }
}
