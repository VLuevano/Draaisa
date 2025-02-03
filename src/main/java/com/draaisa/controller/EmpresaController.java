package com.draaisa.controller;

import com.draaisa.database.DatabaseConnection;
import com.draaisa.model.Categoria;
import com.draaisa.model.Empresa;

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

public class EmpresaController {

    // Método para registrar Empresa desde formulario
    public void registrarEmpresa(Empresa empresa, List<Categoria> categorias) {
        String sqlEmpresa = "INSERT INTO empresa (nombreempresa, cpEmpresa, noExtEmpresa, noIntEmpresa, rfcEmpresa, municipio, estado, calle, colonia, ciudad, pais, telefonoEmpresa, correoEmpresa, curpEmpresa, pfisicaEmpresa) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING idEmpresa";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmtEmpresa = conn.prepareStatement(sqlEmpresa,
                        Statement.RETURN_GENERATED_KEYS)) {

            stmtEmpresa.setString(1, empresa.getNombreEmpresa());
            stmtEmpresa.setInt(2, empresa.getCpEmpresa());
            stmtEmpresa.setInt(3, empresa.getNoExtEmpresa());
            stmtEmpresa.setInt(4, empresa.getNoIntEmpresa());
            stmtEmpresa.setString(5, empresa.getRfcEmpresa());
            stmtEmpresa.setString(6, empresa.getMunicipio());
            stmtEmpresa.setString(7, empresa.getEstado());
            stmtEmpresa.setString(8, empresa.getCalle());
            stmtEmpresa.setString(9, empresa.getColonia());
            stmtEmpresa.setString(10, empresa.getCiudad());
            stmtEmpresa.setString(11, empresa.getPais());
            stmtEmpresa.setString(12, empresa.getTelefonoEmpresa());
            stmtEmpresa.setString(13, empresa.getCorreoEmpresa());
            stmtEmpresa.setString(14, empresa.getCurp());
            stmtEmpresa.setBoolean(15, empresa.isEsPersonaFisica());
            stmtEmpresa.executeUpdate();

            ResultSet generatedKeys = stmtEmpresa.getGeneratedKeys();
            int idEmpresa = -1;
            if (generatedKeys.next()) {
                idEmpresa = generatedKeys.getInt(1);
            }

            for (Categoria categoria : categorias) {
                int idCategoria = obtenerOCrearCategoria(categoria);
                asociarEmpresaConCategoria(idEmpresa, idCategoria);
            }

            System.out.println("Empresa registrado exitosamente.");

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            System.out.println("Error al registrar empresa.");
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

    // Método para asociar empresa con categoría
    private void asociarEmpresaConCategoria(int idEmpresa, int idCategoria) throws SQLException, IOException {
        String sql = "INSERT INTO empresacategoria (idempresa, idcategoria) VALUES (?, ?) ON CONFLICT DO NOTHING";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idEmpresa);
            stmt.setInt(2, idCategoria);
            stmt.executeUpdate();
        }
    }

    // Método para modificar un empresa
    public void modificarEmpresa(Empresa empresa) {
        String sql = "UPDATE empresa SET nombreempresa = ?, cpempresa = ?, noExtEmpresa = ?, noIntEmpresa = ?, rfcEmpresa = ?, municipio = ?, estado = ?, calle = ?, colonia = ?, ciudad = ?, pais = ?, telefonoEmpresa = ?, correoempresa = ?, curpempresa = ?, pfisicaempresa = ? WHERE idempresa = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, empresa.getNombreEmpresa());
            stmt.setInt(2, empresa.getCpEmpresa());
            stmt.setInt(3, empresa.getNoExtEmpresa());
            stmt.setInt(4, empresa.getNoIntEmpresa());
            stmt.setString(5, empresa.getRfcEmpresa());
            stmt.setString(6, empresa.getMunicipio());
            stmt.setString(7, empresa.getEstado());
            stmt.setString(8, empresa.getCalle());
            stmt.setString(9, empresa.getColonia());
            stmt.setString(10, empresa.getCiudad());
            stmt.setString(11, empresa.getPais());
            stmt.setString(12, empresa.getTelefonoEmpresa());
            stmt.setString(13, empresa.getCorreoEmpresa());
            stmt.setString(14, empresa.getCurp());
            stmt.setBoolean(15, empresa.isEsPersonaFisica());
            stmt.setInt(16, empresa.getIdEmpresa());
            stmt.executeUpdate();

            System.out.println("Empresa actualizado correctamente.");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            System.out.println("Error al modificar empresa.");
        }
    }

    public List<Empresa> buscarEmpresas(String filtro) {
        List<Empresa> empresas = new ArrayList<>();
        String[] filtros = filtro.split(","); // Separar por comas

        // Crear condiciones dinámicas para la búsqueda de empresas
        StringBuilder sql = new StringBuilder("SELECT e.* FROM empresa e WHERE ");
        List<String> condiciones = new ArrayList<>();

        // Condiciones de búsqueda por cada filtro
        for (String palabra : filtros) {
            palabra = palabra.trim();

            if (palabra.matches("\\d+")) { // Filtrar por ID
                condiciones.add("e.idEmpresa = ?");
            } else if (palabra.matches("[a-zA-Z]+")) { // Filtrar por categoría
                condiciones.add(
                        "e.idEmpresa IN (SELECT ec.idEmpresa FROM empresacategoria ec INNER JOIN categoria c ON ec.idCategoria = c.idCategoria WHERE c.nombreCategoria ILIKE ?)");
            } else if (palabra.matches("\\d{10}")) { // Filtrar por teléfono
                condiciones.add("e.telefonoEmpresa = ?");
            } else if (palabra.matches("[A-Za-z0-9]{13}")) { // Filtrar por RFC
                condiciones.add("e.rfcEmpresa = ?");
            } else {
                // Filtrar por nombre, estado, municipio
                condiciones.add(
                        "e.nombreempresa ILIKE ? OR e.estado ILIKE ? OR e.municipio ILIKE ? OR e.rfcEmpresa ILIKE ?");
            }
        }

        // Si no hay filtros, traer todos los empresas
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
                Empresa empresa = new Empresa(
                        rs.getInt("idEmpresa"),
                        rs.getString("nombreEmpresa"),
                        rs.getInt("cpEmpresa"),
                        rs.getInt("noExtEmpresa"),
                        rs.getInt("noIntEmpresa"),
                        rs.getString("rfcEmpresa"),
                        rs.getString("municipio"),
                        rs.getString("estado"),
                        rs.getString("calle"),
                        rs.getString("colonia"),
                        rs.getString("ciudad"),
                        rs.getString("pais"),
                        rs.getString("telefonoEmpresa"),
                        rs.getString("correoEmpresa"),
                        rs.getString("curpempresa"),
                        rs.getBoolean("pfisicaempresa"));

                // Inicializar lista de categorías vacía
                List<Categoria> categorias = new ArrayList<>();

                // Consulta para obtener las categorías asociadas al empresa
                String categoriaSql = """
                        SELECT c.idCategoria, c.nombreCategoria
                        FROM categoria c
                        INNER JOIN empresacategoria ec ON c.idCategoria = ec.idCategoria
                        WHERE ec.idEmpresa = ?""";

                try (PreparedStatement stmtCategorias = conn.prepareStatement(categoriaSql)) {
                    stmtCategorias.setInt(1, empresa.getIdEmpresa());
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
                    empresa.setCategorias(categorias);
                }

                // Agregar empresa a la lista
                empresas.add(empresa);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        return empresas;
    }

    // Método para eliminar empresa
    public void eliminarEmpresa(int idEmpresa) {
        String sqlEliminarCategoria = "DELETE FROM empresacategoria WHERE idempresa = ?";
        String sqlEliminarEmpresa = "DELETE FROM empresa WHERE idEmpresa = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Iniciar una transacción
            conn.setAutoCommit(false);

            try (PreparedStatement stmtCategoria = conn.prepareStatement(sqlEliminarCategoria);
                    PreparedStatement stmtEmpresa = conn.prepareStatement(sqlEliminarEmpresa)) {

                // Eliminar las categorías asociadas con el empresa
                stmtCategoria.setInt(1, idEmpresa);
                stmtCategoria.executeUpdate();

                // Eliminar el empresa
                stmtEmpresa.setInt(1, idEmpresa);
                stmtEmpresa.executeUpdate();

                // Si todo va bien, confirmar la transacción
                conn.commit();
                System.out.println("Empresa y su categoría eliminados exitosamente.");
            } catch (SQLException e) {
                // Si ocurre un error, deshacer la transacción
                conn.rollback();
                e.printStackTrace();
                System.out.println("Error al eliminar empresa y su categoría.");
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            System.out.println("Error al eliminar empresa.");
        }
    }

    // Método para consultar todos los empresas
    public List<Empresa> consultarTodosEmpresas() {
        List<Empresa> empresas = new ArrayList<>();
        String sql = "SELECT * FROM empresa";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            Map<Integer, Empresa> empresasMap = new HashMap<>(); // Usar un mapa para evitar duplicados

            while (rs.next()) {
                // Crear empresa
                Empresa empresa = new Empresa(
                        rs.getInt("idEmpresa"),
                        rs.getString("nombreEmpresa"),
                        rs.getInt("cpEmpresa"),
                        rs.getInt("noExtEmpresa"),
                        rs.getInt("noIntEmpresa"),
                        rs.getString("rfcEmpresa"),
                        rs.getString("municipio"),
                        rs.getString("estado"),
                        rs.getString("calle"),
                        rs.getString("colonia"),
                        rs.getString("ciudad"),
                        rs.getString("pais"),
                        rs.getString("telefonoEmpresa"),
                        rs.getString("correoEmpresa"),
                        rs.getString("curpempresa"),
                        rs.getBoolean("pfisicaempresa"));

                // Si el empresa ya está en el mapa, usamos el existente y agregamos las
                // categorías
                Empresa existingEmpresa = empresasMap.get(empresa.getIdEmpresa());
                if (existingEmpresa == null) {
                    empresasMap.put(empresa.getIdEmpresa(), empresa);
                } else {
                    empresa = existingEmpresa; // Usar el empresa existente
                }

                // Consulta para obtener las categorías asociadas al empresa
                String categoriaSql = """
                        SELECT c.idCategoria, c.nombreCategoria
                        FROM categoria c
                        INNER JOIN empresacategoria ec ON c.idCategoria = ec.idCategoria
                        WHERE ec.idEmpresa = ?""";

                try (PreparedStatement stmtCategorias = conn.prepareStatement(categoriaSql)) {
                    stmtCategorias.setInt(1, empresa.getIdEmpresa());
                    ResultSet rsCategorias = stmtCategorias.executeQuery();

                    List<Categoria> categorias = new ArrayList<>();
                    while (rsCategorias.next()) {
                        int idCat = rsCategorias.getInt("idCategoria");
                        String nombreCat = rsCategorias.getString("nombreCategoria");
                        categorias.add(new Categoria(idCat, nombreCat));
                    }

                    if (!categorias.isEmpty()) {
                        empresa.setCategorias(categorias); // Usar lista de categorías
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

            // Convertir el mapa a una lista de empresas sin duplicados
            empresas.addAll(empresasMap.values());

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        return empresas;
    }

    // Método para registrar empresa desde archivo Excel
    public void registrarEmpresaDesdeExcel(File excelFile) {
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

                    Empresa empresa = new Empresa(0, nombre, Integer.parseInt(cp), Integer.parseInt(noExt),
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

                    // Ahora solo llamas al método registrarEmpresa una sola vez
                    registrarEmpresa(empresa, categorias);

                } catch (Exception e) {
                    System.out.println("Error procesando fila " + row.getRowNum() + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
            System.out.println("Empresas registrados desde el archivo Excel.");

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