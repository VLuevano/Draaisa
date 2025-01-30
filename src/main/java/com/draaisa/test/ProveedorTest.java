package com.draaisa.test;

import com.draaisa.controller.ProveedorController;
import com.draaisa.model.Categoria;
import com.draaisa.model.Proveedor;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProveedorTest {

    public static void main(String[] args) {
        ProveedorController controller = new ProveedorController();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("---- MENÚ ----");
            System.out.println("1. Registrar proveedor");
            System.out.println("2. Modificar proveedor");
            System.out.println("3. Buscar proveedores");
            System.out.println("4. Eliminar proveedor");
            System.out.println("5. Consultar todos los proveedores");
            System.out.println("6. Salir");
            System.out.print("Selecciona una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1:
                    // Registrar proveedor
                    System.out.println("---- REGISTRANDO PROVEEDOR ----");
                    System.out.print("Nombre del proveedor: ");
                    String nombre = scanner.nextLine();
                    System.out.print("Código Postal: ");
                    int cp = scanner.nextInt();
                    System.out.print("Número exterior: ");
                    int noExt = scanner.nextInt();
                    System.out.print("Número interior: ");
                    int noInt = scanner.nextInt();
                    scanner.nextLine(); // Consumir el salto de línea
                    System.out.print("RFC: ");
                    String rfc = scanner.nextLine();
                    System.out.print("Municipio: ");
                    String municipio = scanner.nextLine();
                    System.out.print("Estado: ");
                    String estado = scanner.nextLine();
                    System.out.print("Calle: ");
                    String calle = scanner.nextLine();
                    System.out.print("Colonia: ");
                    String colonia = scanner.nextLine();
                    System.out.print("Ciudad: ");
                    String ciudad = scanner.nextLine();
                    System.out.print("País: ");
                    String pais = scanner.nextLine();
                    System.out.print("Teléfono: ");
                    String telefono = scanner.nextLine();
                    System.out.print("Correo: ");
                    String correo = scanner.nextLine();
                    System.out.print("CURP: ");
                    String curp = scanner.nextLine();
                    System.out.print("¿Es persona física? (true/false): ");
                    boolean esPersonaFisica = scanner.nextBoolean();
                    scanner.nextLine(); // Consumir el salto de línea

                    System.out.print("Nombre de la categoría: ");
                    String categoriaNombre = scanner.nextLine();
                    System.out.print("Descripción de la categoría: ");
                    String categoriaDescripcion = scanner.nextLine();

                    Proveedor proveedor = new Proveedor(0, nombre, cp, noExt, noInt, rfc, municipio, estado, calle,
                            colonia, ciudad, pais, telefono, correo, curp, esPersonaFisica);
                    List<Categoria> categorias = new ArrayList<>();
                    categorias.add(new Categoria(0, categoriaNombre, categoriaDescripcion));

                    controller.registrarProveedor(proveedor, categorias);
                    break;

                case 2:
                    // Modificar proveedor
                    System.out.println("---- MODIFICANDO PROVEEDOR ----");
                    System.out.print("ID del proveedor a modificar: ");
                    int idProveedorModificar = scanner.nextInt();
                    scanner.nextLine(); // Consumir el salto de línea
                    System.out.print("Nuevo nombre del proveedor: ");
                    String nuevoNombre = scanner.nextLine();
                    System.out.print("Nuevo estado: ");
                    String nuevoEstado = scanner.nextLine();
                    System.out.print("Nuevo teléfono: ");
                    String nuevoTelefono = scanner.nextLine();
                    System.out.print("Nuevo correo: ");
                    String nuevoCorreo = scanner.nextLine();

                    Proveedor proveedorModificar = new Proveedor(idProveedorModificar, nuevoNombre, 0, 0, 0, "", "",
                            nuevoEstado, "", "", "", "", nuevoTelefono, nuevoCorreo, "", false);
                    controller.modificarProveedor(proveedorModificar);
                    break;

                case 3:
                    // Buscar proveedores
                    System.out.println("---- BUSCANDO PROVEEDORES ----");
                    System.out.print("Introduce un filtro de búsqueda (nombre, estado, municipio): ");
                    String filtro = scanner.nextLine();
                    List<Proveedor> proveedoresEncontrados = controller.buscarProveedores(filtro);
                    if (proveedoresEncontrados.isEmpty()) {
                        System.out.println("No se encontraron proveedores.");
                    } else {
                        for (Proveedor p : proveedoresEncontrados) {
                            System.out.println("Proveedor encontrado: " + p.getNombreProv());
                        }
                    }
                    break;

                case 4:
                    // Eliminar proveedor
                    System.out.println("---- ELIMINANDO PROVEEDOR ----");
                    System.out.print("ID del proveedor a eliminar: ");
                    int idProveedorEliminar = scanner.nextInt();
                    controller.eliminarProveedor(idProveedorEliminar);
                    break;

                case 5:
                    // Consultar todos los proveedores
                    System.out.println("---- CONSULTANDO TODOS LOS PROVEEDORES ----");
                    List<Proveedor> proveedores = controller.consultarTodosProveedores();
                    if (proveedores.isEmpty()) {
                        System.out.println("No se encontraron proveedores.");
                    } else {
                        for (Proveedor p : proveedores) {
                            System.out.println("ID: " + p.getIdProveedor());
                            System.out.println("Nombre: " + p.getNombreProv());
                            System.out.println("Código Postal: " + p.getCpProveedor());
                            System.out.println("Número Exterior: " + p.getNoExtProv());
                            System.out.println("Número Interior: " + p.getNoIntProv());
                            System.out.println("RFC: " + p.getRfcProveedor());
                            System.out.println("Municipio: " + p.getMunicipio());
                            System.out.println("Estado: " + p.getEstado());
                            System.out.println("Calle: " + p.getCalle());
                            System.out.println("Colonia: " + p.getColonia());
                            System.out.println("Ciudad: " + p.getCiudad());
                            System.out.println("País: " + p.getPais());
                            System.out.println("Teléfono: " + p.getTelefonoProv());
                            System.out.println("Correo: " + p.getCorreoProv());
                            System.out.println("CURP: " + p.getCurp());
                            System.out.println("Es Persona Física: " + p.isEsPersonaFisica());
                            System.out.println("----------------------------------------");
                        }
                    }
                    break;

                case 6:
                    // Salir
                    System.out.println("¡Hasta luego!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Opción no válida, por favor selecciona una opción válida.");
            }
        }
    }
}
