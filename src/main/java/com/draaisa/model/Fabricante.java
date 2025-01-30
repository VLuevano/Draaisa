package com.draaisa.model;

import java.util.ArrayList;
import java.util.List;

public class Fabricante {
    private int idFabricante;
    private String nombreFabricante;
    private String rfcFabricante;
    private String telefonoFabricante;
    private String correoFabricante;
    private boolean esPersonaFisica;
    private List<Categoria> categorias;

    public Fabricante(int idFabricante, String nombreFabricante, String rfcFabricante, String telefonoFabricante,
            String correoFabricante, boolean esPersonaFisica) {
        this.idFabricante = idFabricante;
        this.nombreFabricante = nombreFabricante;
        this.rfcFabricante = rfcFabricante;
        this.telefonoFabricante = telefonoFabricante;
        this.correoFabricante = correoFabricante;
        this.esPersonaFisica = esPersonaFisica;
        this.categorias = new ArrayList<>();
    }

    // Métodos: agregarCategoria, buscarFabricantesPorFiltro, etc.
}
