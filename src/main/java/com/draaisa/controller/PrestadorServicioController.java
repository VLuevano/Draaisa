package com.draaisa.controller;

import com.draaisa.model.PrestadorServicio;
import com.draaisa.model.Servicio;
import com.draaisa.model.Ruta;
import com.draaisa.database.DatabaseConnection;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrestadorServicioController {
    private static final String TABLE_PRESTADOR_SERVICIO = "PrestadorServicio";
    private static final String TABLE_SERVICIO = "Servicio";
    private static final String TABLE_RUTA = "Ruta";
    private static final String TABLE_PRESTADOR_RUTA = "PrestadorRuta";
    private Connection connection;

    public PrestadorServicioController() throws SQLException, IOException {
        this.connection = DatabaseConnection.getConnection();
    }

    // Registrar Prestador de Servicio
    public boolean registrarPrestadorServicio(PrestadorServicio prestador) {
        String insertPrestadorSQL = "INSERT INTO " + TABLE_PRESTADOR_SERVICIO
                + " (nombrePrestador, cpPrestador, noExtPrestador, " +
                "noIntPrestador, rfcPrestador, municipio, estado, calle, colonia, ciudad, pais, telefonoPrest, correoPrest, curpprestador, pfisicaprestador) "
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(insertPrestadorSQL,
                Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, prestador.getNombrePrestador());
            stmt.setInt(2, prestador.getCpPrestador());
            stmt.setInt(3, prestador.getNoExtPrestador());
            stmt.setInt(4, prestador.getNoIntPrestador());
            stmt.setString(5, prestador.getRfcPrestador());
            stmt.setString(6, prestador.getMunicipio());
            stmt.setString(7, prestador.getEstado());
            stmt.setString(8, prestador.getCalle());
            stmt.setString(9, prestador.getColonia());
            stmt.setString(10, prestador.getCiudad());
            stmt.setString(11, prestador.getPais());
            stmt.setString(12, prestador.getTelefonoPrestador());
            stmt.setString(13, prestador.getCorreoPrestador());
            stmt.setString(14, prestador.getCurp());
            stmt.setBoolean(15, prestador.isEsPersonaFisica());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int prestadorId = rs.getInt(1);
                    System.out.println("Prestador registrado con ID: " + prestadorId);

                    // Verificar que las listas de servicios y rutas no estén vacías
                    System.out.println("Servicios a registrar: " + prestador.getServicios().size());
                    System.out.println("Rutas a registrar: " + prestador.getRutas().size());

                    // Registrar Servicios
                    for (Servicio servicio : prestador.getServicios()) {
                        if (registrarServicio(servicio, prestadorId)) {
                            System.out.println("Servicio registrado: " + servicio.getDescripcionServicio());
                        } else {
                            System.out.println("Error al registrar servicio: " + servicio.getDescripcionServicio());
                        }
                    }

                    // Registrar Rutas
                    for (Ruta ruta : prestador.getRutas()) {
                        if (registrarRuta(ruta, prestadorId)) {
                            System.out.println("Ruta registrada: " + ruta.getSalida() + " → " + ruta.getDestino());
                        } else {
                            System.out.println(
                                    "Error al registrar ruta: " + ruta.getSalida() + " → " + ruta.getDestino());
                        }
                    }
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Registrar Servicio
    private boolean registrarServicio(Servicio servicio, int prestadorId) {
        String insertServicioSQL = "INSERT INTO " + TABLE_SERVICIO
                + " (descripcionServicio, costoServicio, monedaServicio, idprestador) " +
                "VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(insertServicioSQL)) {
            stmt.setString(1, servicio.getDescripcionServicio());
            stmt.setDouble(2, servicio.getCostoServicio());
            stmt.setString(3, servicio.getMonedaServicio());
            stmt.setInt(4, prestadorId);

            int rows = stmt.executeUpdate();
            System.out.println("Servicio insertado en la BD: " + servicio.getDescripcionServicio()
                    + " (Filas afectadas: " + rows + ")");
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Registrar Ruta
    private boolean registrarRuta(Ruta ruta, int prestadorId) {
        String insertRutaSQL = "INSERT INTO " + TABLE_RUTA + " (salidaruta, destinoruta) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(insertRutaSQL, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, ruta.getSalida());
            stmt.setString(2, ruta.getDestino());
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int rutaId = rs.getInt(1);
                    System.out.println("Ruta insertada con ID: " + rutaId);
                    return registrarPrestadorRuta(prestadorId, rutaId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Registrar relación entre Prestador y Ruta
    private boolean registrarPrestadorRuta(int prestadorId, int rutaId) {
        String insertPrestadorRutaSQL = "INSERT INTO " + TABLE_PRESTADOR_RUTA
                + " (idprestador, idruta) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(insertPrestadorRutaSQL)) {
            stmt.setInt(1, prestadorId);
            stmt.setInt(2, rutaId);

            int rows = stmt.executeUpdate();
            System.out.println("Relación Prestador-Ruta insertada (Filas afectadas: " + rows + ")");
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<PrestadorServicio> buscarPrestadoresServicio(String filtro) {
        List<PrestadorServicio> prestadores = new ArrayList<>();
        String[] filtros = filtro.split(","); // Separar por comas y eliminar espacios

        // Crear la base de la consulta
        StringBuilder sql = new StringBuilder("SELECT DISTINCT p.* FROM prestadorservicio p ");
        sql.append("LEFT JOIN prestadorruta pr ON p.idPrestador = pr.idPrestador ");
        sql.append("LEFT JOIN ruta r ON pr.idRuta = r.idRuta ");
        sql.append("LEFT JOIN servicio s ON s.idPrestador = p.idPrestador WHERE ");

        List<String> condiciones = new ArrayList<>();

        // Agregar condición general para cada palabra en todas las columnas relevantes
        for (String palabra : filtros) {
            palabra = palabra.trim();
            condiciones.add("(" +
                    "p.nombrePrestador ILIKE ? OR p.estado ILIKE ? OR p.municipio ILIKE ? OR " +
                    "p.calle ILIKE ? OR p.colonia ILIKE ? OR p.ciudad ILIKE ? OR p.pais ILIKE ? OR " +
                    "p.telefonoPrest ILIKE ? OR p.rfcPrestador ILIKE ? OR " +
                    "s.descripcionServicio ILIKE ? OR r.salidaRuta ILIKE ? OR r.destinoRuta ILIKE ?" +
                    ")");
        }

        // Unir todas las condiciones con AND para que todas las palabras sean
        // consideradas sin importar el orden
        sql.append(String.join(" AND ", condiciones));

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int index = 1;
            for (String palabra : filtros) {
                palabra = "%" + palabra.trim() + "%"; // Agregar comodines para búsqueda flexible
                for (int i = 0; i < 12; i++) { // Se repite porque cada palabra se aplica a 12 columnas
                    stmt.setString(index++, palabra);
                }
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                PrestadorServicio prestador = new PrestadorServicio(
                        rs.getInt("idPrestador"),
                        rs.getString("nombrePrestador"),
                        rs.getInt("cpPrestador"),
                        rs.getInt("noExtPrestador"),
                        rs.getInt("noIntPrestador"),
                        rs.getString("rfcPrestador"),
                        rs.getString("municipio"),
                        rs.getString("estado"),
                        rs.getString("calle"),
                        rs.getString("colonia"),
                        rs.getString("ciudad"),
                        rs.getString("pais"),
                        rs.getString("telefonoPrest"),
                        rs.getString("correoPrest"),
                        rs.getString("curpPrestador"),
                        rs.getBoolean("pfisicaPrestador"));

                prestadores.add(prestador);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        return prestadores;
    }

    // Consultar todos los prestadores de servicio, incluyendo sus servicios y rutas
    // Consultar todos los prestadores de servicio
public List<PrestadorServicio> consultarPrestadores() {
    List<PrestadorServicio> prestadores = new ArrayList<>();
    String query = "SELECT * FROM " + TABLE_PRESTADOR_SERVICIO;

    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            PrestadorServicio prestador = new PrestadorServicio();
            prestador.setIdPrestador(rs.getInt("idPrestador"));
            prestador.setNombrePrestador(rs.getString("nombrePrestador"));
            prestador.setCpPrestador(rs.getInt("cpPrestador"));
            prestador.setNoExtPrestador(rs.getInt("noExtPrestador"));
            prestador.setNoIntPrestador(rs.getInt("noIntPrestador"));
            prestador.setRfcPrestador(rs.getString("rfcPrestador"));
            prestador.setMunicipio(rs.getString("municipio"));
            prestador.setEstado(rs.getString("estado"));
            prestador.setCalle(rs.getString("calle"));
            prestador.setColonia(rs.getString("colonia"));
            prestador.setCiudad(rs.getString("ciudad"));
            prestador.setPais(rs.getString("pais"));
            prestador.setTelefonoPrestador(rs.getString("telefonoPrest"));
            prestador.setCorreoPrestador(rs.getString("correoPrest"));
            prestador.setCurp(rs.getString("curpPrestador"));
            prestador.setEsPersonaFisica(rs.getBoolean("pFisicaPrestador"));

            // Consultar servicios y rutas para el prestador
            prestador.setServicios(obtenerServiciosPrestador(prestador.getIdPrestador()));
            prestador.setRutas(obtenerRutasPrestador(prestador.getIdPrestador()));

            prestadores.add(prestador);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    System.out.println("Prestadores consultados: " + prestadores.size());
    return prestadores;
}


    // Consultar servicios asociados a un prestador
    private List<Servicio> obtenerServiciosPrestador(int idPrestador) {
        List<Servicio> servicios = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_SERVICIO + " WHERE idPrestador = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idPrestador);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Servicio servicio = new Servicio();
                servicio.setIdServicio(rs.getInt("idServicio"));
                servicio.setDescripcionServicio(rs.getString("descripcionServicio"));
                servicio.setCostoServicio(rs.getDouble("costoServicio"));
                servicios.add(servicio);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return servicios;
    }

    // Consultar rutas asociadas a un prestador
    private List<Ruta> obtenerRutasPrestador(int idPrestador) {
        List<Ruta> rutas = new ArrayList<>();
        String query = "SELECT r.* FROM " + TABLE_RUTA + " r " +
                "JOIN " + TABLE_PRESTADOR_RUTA + " pr ON r.idRuta = pr.idRuta " +
                "WHERE pr.idPrestador = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idPrestador);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Ruta ruta = new Ruta();
                ruta.setIdRuta(rs.getInt("idRuta"));
                ruta.setSalida(rs.getString("salidaRuta"));
                ruta.setDestino(rs.getString("destinoRuta"));
                rutas.add(ruta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rutas;
    }

    // Modificar Prestador de Servicio
    public boolean modificarPrestadorServicio(PrestadorServicio prestador) {
        String sql = "UPDATE prestadorServicio SET nombrePrestador = ?, cpprestador = ?, noExtPrestador = ?, noIntPrestador = ?, rfcPrestador = ?, municipio = ?, estado = ?, calle = ?, colonia = ?, ciudad = ?, pais = ?, telefonoPrest = ?, correoPrest = ?, curpprestador = ?, pfisicaprestador = ? WHERE idprestador = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, prestador.getNombrePrestador());
            stmt.setInt(2, prestador.getCpPrestador());
            stmt.setInt(3, prestador.getNoExtPrestador());
            stmt.setInt(4, prestador.getNoIntPrestador());
            stmt.setString(5, prestador.getRfcPrestador());
            stmt.setString(6, prestador.getMunicipio());
            stmt.setString(7, prestador.getEstado());
            stmt.setString(8, prestador.getCalle());
            stmt.setString(9, prestador.getColonia());
            stmt.setString(10, prestador.getCiudad());
            stmt.setString(11, prestador.getPais());
            stmt.setString(12, prestador.getTelefonoPrestador());
            stmt.setString(13, prestador.getCorreoPrestador());
            stmt.setString(14, prestador.getCurp());
            stmt.setBoolean(15, prestador.isEsPersonaFisica());
            stmt.setInt(16, prestador.getIdPrestador());
            stmt.executeUpdate();

            int filasActualizadas = stmt.executeUpdate();
            return filasActualizadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Eliminar Prestador de Servicio
    public boolean eliminarPrestadorServicio(int prestadorId) {
        String deletePrestadorRutaSQL = "DELETE FROM PrestadorRuta WHERE idPrestador = ?";
        String deleteServicioSQL = "DELETE FROM Servicio WHERE idPrestador = ?";
        String selectRutasSQL = "SELECT idRuta FROM PrestadorRuta WHERE idPrestador = ?";
        String deleteRutaSQL = "DELETE FROM Ruta WHERE idRuta = ?";
        String deletePrestadorSQL = "DELETE FROM " + TABLE_PRESTADOR_SERVICIO + " WHERE idPrestador = ?";

        try (PreparedStatement stmt1 = connection.prepareStatement(deletePrestadorRutaSQL);
                PreparedStatement stmt2 = connection.prepareStatement(deleteServicioSQL);
                PreparedStatement stmt3 = connection.prepareStatement(selectRutasSQL);
                PreparedStatement stmt4 = connection.prepareStatement(deleteRutaSQL);
                PreparedStatement stmt5 = connection.prepareStatement(deletePrestadorSQL)) {

            connection.setAutoCommit(false); // Iniciar transacción

            // Obtener las rutas asociadas antes de eliminarlas
            stmt3.setInt(1, prestadorId);
            ResultSet rs = stmt3.executeQuery();
            List<Integer> rutasAEliminar = new ArrayList<>();
            while (rs.next()) {
                rutasAEliminar.add(rs.getInt("idRuta"));
            }

            // Eliminar referencias en PrestadorRuta
            stmt1.setInt(1, prestadorId);
            stmt1.executeUpdate();

            // Eliminar registros en Servicio
            stmt2.setInt(1, prestadorId);
            stmt2.executeUpdate();

            // Eliminar rutas asociadas si ya no tienen referencias en PrestadorRuta
            for (int idRuta : rutasAEliminar) {
                stmt4.setInt(1, idRuta);
                stmt4.executeUpdate();
            }

            // Eliminar el prestador
            stmt5.setInt(1, prestadorId);
            int filasEliminadas = stmt5.executeUpdate();

            connection.commit(); // Confirmar transacción
            return filasEliminadas > 0;

        } catch (SQLException e) {
            try {
                connection.rollback(); // Revertir si hay error
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true); // Restaurar auto-commit
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    // Método auxiliar para obtener el valor de la celda como String
    private String obtenerValorComoString(Cell cell) {
        if (cell == null)
            return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf((long) cell.getNumericCellValue()); // Convertir a String
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
            case ERROR:
            default:
                return "";
        }
    }

    // Método modificado para importar prestadores desde Excel
    public void importarPrestadoresDesdeExcel(File archivoExcel) {
        try (FileInputStream fis = new FileInputStream(archivoExcel);
                Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0)
                    continue; // Saltar la primera fila (encabezados)

                // Extraer los datos y manejar conversiones
                String nombrePrestador = obtenerValorComoString(row.getCell(0));
                int cpPrestador = (int) row.getCell(1).getNumericCellValue();
                int noExtPrestador = (int) row.getCell(2).getNumericCellValue();
                int noIntPrestador = (int) row.getCell(3).getNumericCellValue();
                String rfcPrestador = obtenerValorComoString(row.getCell(4));
                String municipio = obtenerValorComoString(row.getCell(5));
                String estado = obtenerValorComoString(row.getCell(6));
                String calle = obtenerValorComoString(row.getCell(7));
                String colonia = obtenerValorComoString(row.getCell(8));
                String ciudad = obtenerValorComoString(row.getCell(9));
                String pais = obtenerValorComoString(row.getCell(10));
                String telefonoPrestador = obtenerValorComoString(row.getCell(11));
                String correoPrestador = obtenerValorComoString(row.getCell(12));
                String curp = obtenerValorComoString(row.getCell(13));
                boolean esPersonaFisica = Boolean.parseBoolean(obtenerValorComoString(row.getCell(14)));

                // Crear el objeto PrestadorServicio
                PrestadorServicio prestador = new PrestadorServicio();
                prestador.setNombrePrestador(nombrePrestador);
                prestador.setCpPrestador(cpPrestador);
                prestador.setNoExtPrestador(noExtPrestador);
                prestador.setNoIntPrestador(noIntPrestador);
                prestador.setRfcPrestador(rfcPrestador);
                prestador.setMunicipio(municipio);
                prestador.setEstado(estado);
                prestador.setCalle(calle);
                prestador.setColonia(colonia);
                prestador.setCiudad(ciudad);
                prestador.setPais(pais);
                prestador.setTelefonoPrestador(telefonoPrestador);
                prestador.setCorreoPrestador(correoPrestador);
                prestador.setCurp(curp);
                prestador.setEsPersonaFisica(esPersonaFisica);

                // Cargar Servicios
                List<Servicio> serviciosList = new ArrayList<>();
                String descripcionServicio = obtenerValorComoString(row.getCell(15));
                double costoServicio = row.getCell(16).getNumericCellValue();
                String monedaServicio = obtenerValorComoString(row.getCell(17));

                Servicio servicio = new Servicio();
                servicio.setDescripcionServicio(descripcionServicio);
                servicio.setCostoServicio(costoServicio);
                servicio.setMonedaServicio(monedaServicio);

                serviciosList.add(servicio);
                prestador.setServicios(serviciosList);

                // Cargar Rutas
                List<Ruta> rutasList = new ArrayList<>();
                String salidaRuta = obtenerValorComoString(row.getCell(18));
                String destinoRuta = obtenerValorComoString(row.getCell(19));

                Ruta ruta = new Ruta();
                ruta.setSalida(salidaRuta);
                ruta.setDestino(destinoRuta);

                rutasList.add(ruta);
                prestador.setRutas(rutasList);

                // Registrar el prestador con sus servicios y rutas
                registrarPrestadorServicio(prestador);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
