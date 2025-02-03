package com.draaisa.controller;

import com.draaisa.database.DatabaseConnection;
import com.draaisa.model.Categoria;
import com.draaisa.model.Fabricante;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FabricanteController {

    // Método para registrar Fabricante desde formulario
    public void registrarFabricante(Fabricante fabricante, List<Categoria> categorias) {
        String sqlFabricante = "INSERT INTO fabricante (nombrefabricante, cpFabricante, noExtFabricante, noIntFabricante, rfcFabricante, municipio, estado, calle, colonia, ciudad, pais, telefonoFabricante, correoFabricante, curpFabricante, pfisicaFabricante) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING idFabricante";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmtFabricante = conn.prepareStatement(sqlFabricante,
                        Statement.RETURN_GENERATED_KEYS)) {

            stmtFabricante.setString(1, fabricante.getNombreFabricante());
            stmtFabricante.setInt(2, fabricante.getCpFabricante());
            stmtFabricante.setInt(3, fabricante.getNoExtFabricante());
            stmtFabricante.setInt(4, fabricante.getNoIntFabricante());
            stmtFabricante.setString(5, fabricante.getRfcFabricante());
            stmtFabricante.setString(6, fabricante.getMunicipio());
            stmtFabricante.setString(7, fabricante.getEstado());
            stmtFabricante.setString(8, fabricante.getCalle());
            stmtFabricante.setString(9, fabricante.getColonia());
            stmtFabricante.setString(10, fabricante.getCiudad());
            stmtFabricante.setString(11, fabricante.getPais());
            stmtFabricante.setString(12, fabricante.getTelefonoFabricante());
            stmtFabricante.setString(13, fabricante.getCorreoFabricante());
            stmtFabricante.setString(14, fabricante.getCurp());
            stmtFabricante.setBoolean(15, fabricante.isEsPersonaFisica());
            stmtFabricante.executeUpdate();

            ResultSet generatedKeys = stmtFabricante.getGeneratedKeys();
            int idFabricante = -1;
            if (generatedKeys.next()) {
                idFabricante = generatedKeys.getInt(1);
            }

            for (Categoria categoria : categorias) {
                int idCategoria = obtenerOCrearCategoria(categoria);
                asociarFabricanteConCategoria(idFabricante, idCategoria);
            }

            System.out.println("Fabricante registrado exitosamente.");

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            System.out.println("Error al registrar fabricante.");
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

    // Método para asociar fabricante con categoría
    private void asociarFabricanteConCategoria(int idFabricante, int idCategoria) throws SQLException, IOException {
        String sql = "INSERT INTO fabricantecategoria (idfabricante, idcategoria) VALUES (?, ?) ON CONFLICT DO NOTHING";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idFabricante);
            stmt.setInt(2, idCategoria);
            stmt.executeUpdate();
        }
    }

    // Método para modificar un fabricante
    public void modificarFabricante(Fabricante fabricante) {
        String sql = "UPDATE fabricante SET nombrefabricante = ?, cpfabricante = ?, noExtFabricante = ?, noIntFabricante = ?, rfcFabricante = ?, municipio = ?, estado = ?, calle = ?, colonia = ?, ciudad = ?, pais = ?, telefonoFabricante = ?, correofabricante = ?, curpfabricante = ?, pfisicafabricante = ? WHERE idfabricante = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fabricante.getNombreFabricante());
            stmt.setInt(2, fabricante.getCpFabricante());
            stmt.setInt(3, fabricante.getNoExtFabricante());
            stmt.setInt(4, fabricante.getNoIntFabricante());
            stmt.setString(5, fabricante.getRfcFabricante());
            stmt.setString(6, fabricante.getMunicipio());
            stmt.setString(7, fabricante.getEstado());
            stmt.setString(8, fabricante.getCalle());
            stmt.setString(9, fabricante.getColonia());
            stmt.setString(10, fabricante.getCiudad());
            stmt.setString(11, fabricante.getPais());
            stmt.setString(12, fabricante.getTelefonoFabricante());
            stmt.setString(13, fabricante.getCorreoFabricante());
            stmt.setString(14, fabricante.getCurp());
            stmt.setBoolean(15, fabricante.isEsPersonaFisica());
            stmt.setInt(16, fabricante.getIdFabricante());
            stmt.executeUpdate();

            System.out.println("Fabricante actualizado correctamente.");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            System.out.println("Error al modificar fabricante.");
        }
    }

    public List<Fabricante> buscarFabricantes(String filtro) {
        List<Fabricante> fabricantes = new ArrayList<>();
        String[] filtros = filtro.split(","); // Separar por comas

        // Crear condiciones dinámicas para la búsqueda de fabricantes
        StringBuilder sql = new StringBuilder("SELECT f.* FROM fabricante f WHERE ");
        List<String> condiciones = new ArrayList<>();

        // Condiciones de búsqueda por cada filtro
        for (String palabra : filtros) {
            palabra = palabra.trim();

            if (palabra.matches("\\d+")) { // Filtrar por ID
                condiciones.add("f.idFabricante = ?");
            } else if (palabra.matches("[a-zA-Z]+")) { // Filtrar por categoría
                condiciones.add(
                        "f.idFabricante IN (SELECT fc.idFabricante FROM fabricantecategoria fc INNER JOIN categoria c ON fc.idCategoria = c.idCategoria WHERE c.nombreCategoria ILIKE ?)");
            } else if (palabra.matches("\\d{10}")) { // Filtrar por teléfono
                condiciones.add("f.telefonoFabricante = ?");
            } else if (palabra.matches("[A-Za-z0-9]{13}")) { // Filtrar por RFC
                condiciones.add("f.rfcFabricante = ?");
            } else {
                // Filtrar por nombre, estado, municipio
                condiciones.add(
                        "f.nombrefabricante ILIKE ? OR f.estado ILIKE ? OR f.municipio ILIKE ? OR f.rfcFabricante ILIKE ?");
            }
        }

        // Si no hay filtros, traer todos los fabricantes
        if (condiciones.isEmpty()) {
            sql.append("1=1"); // Agregar condición que siempre es verdadera
        } else {
            // Unir las condiciones con 'OR' si hay filtros
            sql.append(String.join(" OR ", condiciones));
        }

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int index = 1;
            for (String palabra : filtros) {
                palabra = palabra.trim();

                if (palabra.matches("\\d+")) {
                    stmt.setInt(index++, Integer.parseInt(palabra)); // Filtrar por ID
                } else if (palabra.matches("[a-zA-Z]+")) {
                    stmt.setString(index++, "%" + palabra + "%"); // Filtrar por categoría
                } else if (palabra.matches("\\d{10}")) {
                    stmt.setString(index++, palabra); // Filtrar por teléfono
                } else if (palabra.matches("[A-Za-z0-9]{13}")) {
                    stmt.setString(index++, palabra); // Filtrar por RFC
                } else {
                    // Filtrar por nombre, estado, municipio
                    stmt.setString(index++, "%" + palabra + "%");
                    stmt.setString(index++, "%" + palabra + "%");
                    stmt.setString(index++, "%" + palabra + "%");
                    stmt.setString(index++, "%" + palabra + "%");
                }
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Fabricante fabricante = new Fabricante(
                        rs.getInt("idFabricante"),
                        rs.getString("nombreFabricante"),
                        rs.getInt("cpFabricante"),
                        rs.getInt("noExtFabricante"),
                        rs.getInt("noIntFabricante"),
                        rs.getString("rfcFabricante"),
                        rs.getString("municipio"),
                        rs.getString("estado"),
                        rs.getString("calle"),
                        rs.getString("colonia"),
                        rs.getString("ciudad"),
                        rs.getString("pais"),
                        rs.getString("telefonoFabricante"),
                        rs.getString("correoFabricante"),
                        rs.getString("curpfabricante"),
                        rs.getBoolean("pfisicafabricante"));

                // Inicializar lista de categorías vacía
                List<Categoria> categorias = new ArrayList<>();

                // Consulta para obtener las categorías asociadas al fabricante
                String categoriaSql = """
                        SELECT c.idCategoria, c.nombreCategoria
                        FROM categoria c
                        INNER JOIN fabricantecategoria fc ON c.idCategoria = fc.idCategoria
                        WHERE fc.idFabricante = ?""";

                try (PreparedStatement stmtCategorias = conn.prepareStatement(categoriaSql)) {
                    stmtCategorias.setInt(1, fabricante.getIdFabricante());
                    ResultSet rsCategorias = stmtCategorias.executeQuery();

                    while (rsCategorias.next()) {
                        int idCat = rsCategorias.getInt("idCategoria");
                        String nombreCat = rsCategorias.getString("nombreCategoria");
                        Categoria categoria = new Categoria(idCat, nombreCat);
                        categorias.add(categoria);
                    }
                }

                // Asignar categorías solo si se encontraron
                if (!categorias.isEmpty()) {
                    fabricante.setCategorias(categorias);
                }

                // Agregar fabricante a la lista
                fabricantes.add(fabricante);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        return fabricantes;
    }

    // Método para eliminar fabricante
    public void eliminarFabricante(int idFabricante) {
        String sqlEliminarCategoria = "DELETE FROM fabricantecategoria WHERE idfabricante = ?";
        String sqlEliminarFabricante = "DELETE FROM fabricante WHERE idFabricante = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Iniciar una transacción
            conn.setAutoCommit(false);

            try (PreparedStatement stmtCategoria = conn.prepareStatement(sqlEliminarCategoria);
                    PreparedStatement stmtFabricante = conn.prepareStatement(sqlEliminarFabricante)) {

                // Eliminar las categorías asociadas con el fabricante
                stmtCategoria.setInt(1, idFabricante);
                stmtCategoria.executeUpdate();

                // Eliminar el fabricante
                stmtFabricante.setInt(1, idFabricante);
                stmtFabricante.executeUpdate();

                // Si todo va bien, confirmar la transacción
                conn.commit();
                System.out.println("Fabricante y su categoría eliminados exitosamente.");
            } catch (SQLException e) {
                // Si ocurre un error, deshacer la transacción
                conn.rollback();
                e.printStackTrace();
                System.out.println("Error al eliminar fabricante y su categoría.");
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            System.out.println("Error al eliminar fabricante.");
        }
    }

    // Método para consultar todos los fabricantes
    public List<Fabricante> consultarTodosFabricantes() {
        List<Fabricante> fabricantes = new ArrayList<>();
        String sql = "SELECT * FROM fabricante";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            Map<Integer, Fabricante> fabricantesMap = new HashMap<>(); // Usar un mapa para evitar duplicados

            while (rs.next()) {
                // Crear fabricante
                Fabricante fabricante = new Fabricante(
                        rs.getInt("idFabricante"),
                        rs.getString("nombreFabricante"),
                        rs.getInt("cpFabricante"),
                        rs.getInt("noExtFabricante"),
                        rs.getInt("noIntFabricante"),
                        rs.getString("rfcFabricante"),
                        rs.getString("municipio"),
                        rs.getString("estado"),
                        rs.getString("calle"),
                        rs.getString("colonia"),
                        rs.getString("ciudad"),
                        rs.getString("pais"),
                        rs.getString("telefonoFabricante"),
                        rs.getString("correoFabricante"),
                        rs.getString("curpfabricante"),
                        rs.getBoolean("pfisicafabricante"));

                // Si el fabricante ya está en el mapa, usamos el existente y agregamos las
                // categorías
                Fabricante existingFabricante = fabricantesMap.get(fabricante.getIdFabricante());
                if (existingFabricante == null) {
                    fabricantesMap.put(fabricante.getIdFabricante(), fabricante);
                } else {
                    fabricante = existingFabricante; // Usar el fabricante existente
                }

                // Consulta para obtener las categorías asociadas al fabricante
                String categoriaSql = """
                        SELECT c.idCategoria, c.nombreCategoria
                        FROM categoria c
                        INNER JOIN fabricantecategoria fc ON c.idCategoria = fc.idCategoria
                        WHERE fc.idFabricante = ?""";

                try (PreparedStatement stmtCategorias = conn.prepareStatement(categoriaSql)) {
                    stmtCategorias.setInt(1, fabricante.getIdFabricante());
                    ResultSet rsCategorias = stmtCategorias.executeQuery();

                    List<Categoria> categorias = new ArrayList<>();
                    while (rsCategorias.next()) {
                        int idCat = rsCategorias.getInt("idCategoria");
                        String nombreCat = rsCategorias.getString("nombreCategoria");
                        categorias.add(new Categoria(idCat, nombreCat));
                    }

                    if (!categorias.isEmpty()) {
                        fabricante.setCategorias(categorias); // Usar lista de categorías
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

            // Convertir el mapa a una lista de fabricantes sin duplicados
            fabricantes.addAll(fabricantesMap.values());

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        return fabricantes;
    }

    // Método para registrar fabricante desde archivo Excel
    public void registrarFabricanteDesdeExcel(File excelFile) {
        try (FileInputStream fis = new FileInputStream(excelFile);
                Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0)
                    continue;

                try {
                    // Usar el método obtenerValorCelda para obtener los valores de las celdas
                    String nombre = getStringCellValue(row.getCell(0));
                    String cp = getStringCellValue(row.getCell(1));
                    String noExt = getStringCellValue(row.getCell(2));
                    String noInt = getStringCellValue(row.getCell(3));
                    String rfc = getStringCellValue(row.getCell(4));
                    String municipio = getStringCellValue(row.getCell(5));
                    String estado = getStringCellValue(row.getCell(6));
                    String calle = getStringCellValue(row.getCell(7));
                    String colonia = getStringCellValue(row.getCell(8));
                    String ciudad = getStringCellValue(row.getCell(9));
                    String pais = getStringCellValue(row.getCell(10));
                    String telefono = getStringCellValue(row.getCell(11));
                    String correo = getStringCellValue(row.getCell(12));
                    String curp = getStringCellValue(row.getCell(13));
                    boolean esFisica = Boolean.parseBoolean(getStringCellValue(row.getCell(14)));

                    Fabricante fabricante = new Fabricante(0, nombre, Integer.parseInt(cp), Integer.parseInt(noExt),
                            Integer.parseInt(noInt), rfc, municipio, estado, calle, colonia, ciudad, pais, telefono,
                            correo, curp, esFisica);

                    List<Categoria> categorias = new ArrayList<>();
                    for (int i = 15; i < row.getLastCellNum(); i++) {
                        Cell cell = row.getCell(i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if (cell != null && cell.getCellType() == CellType.STRING) {
                            String categoriaNombre = getStringCellValue(cell).trim();
                            if (!categoriaNombre.isEmpty()) {
                                // Directamente se agrega la categoría sin obtener el ID
                                categorias.add(new Categoria(0, categoriaNombre, "Descripción de " + categoriaNombre));
                                System.out.println("Categoría " + categoriaNombre + " registrada.");
                            }
                        }
                    }

                    // Ahora solo llamas al método registrarFabricante una sola vez
                    registrarFabricante(fabricante, categorias);

                } catch (Exception e) {
                    System.out.println("Error procesando fila " + row.getRowNum() + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
            System.out.println("Fabricantes registrados desde el archivo Excel.");

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al leer el archivo Excel.");
        }
    }

    private String getStringCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            case BLANK:
                return "";
            default:
                return "";
        }
    }

}