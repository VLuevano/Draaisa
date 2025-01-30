package com.draaisa.model;

import java.util.List;

public class PrestadorServicio {
    private int idPrestador;
    private String nombrePrestador;
    private String rfcPrestador;
    private String telefonoPrest;
    private String correoPrest;
    private boolean esPersonaFisica;

    // Relación con rutas y servicios
    private List<Ruta> rutas;
    private List<Servicio> servicios;

    // Métodos: agregarRuta, agregarServicio, buscarPorFiltros, etc.
}
