package com.draaisa.model;

import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private int idCliente;
    private String nombreCliente;
    private String nombreFiscal;
    private String rfcCliente;
    private String telefonoCliente;
    private String correoCliente;
    private boolean esPersonaFisica;
    private List<Categoria> categorias;

    public Cliente(int idCliente, String nombreCliente, String nombreFiscal, String rfcCliente, String telefonoCliente,
            String correoCliente, boolean esPersonaFisica) {
        this.idCliente = idCliente;
        this.nombreCliente = nombreCliente;
        this.nombreFiscal = nombreFiscal;
        this.rfcCliente = rfcCliente;
        this.telefonoCliente = telefonoCliente;
        this.correoCliente = correoCliente;
        this.esPersonaFisica = esPersonaFisica;
        this.categorias = new ArrayList<>();
    }

    // Métodos: agregarCategoria, buscarClientesPorFiltro, etc.
}
