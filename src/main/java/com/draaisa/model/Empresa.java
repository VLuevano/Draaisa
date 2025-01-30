package com.draaisa.model;

import java.util.ArrayList;
import java.util.List;

public class Empresa {
    private int idEmpresa;
    private String nombreEmp;
    private String rfcEmpresa;
    private String telefonoEmpresa;
    private String correoEmpresa;
    private boolean esPersonaFisica;
    private List<Categoria> categorias;

    public Empresa(int idEmpresa, String nombreEmp, String rfcEmpresa, String telefonoEmpresa, String correoEmpresa,
            boolean esPersonaFisica) {
        this.idEmpresa = idEmpresa;
        this.nombreEmp = nombreEmp;
        this.rfcEmpresa = rfcEmpresa;
        this.telefonoEmpresa = telefonoEmpresa;
        this.correoEmpresa = correoEmpresa;
        this.esPersonaFisica = esPersonaFisica;
        this.categorias = new ArrayList<>();
    }

    // Métodos: agregarCategoria, buscarEmpresaPorFiltro, etc.
}
