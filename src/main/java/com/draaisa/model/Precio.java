package com.draaisa.model;

import java.sql.Date;

public class Precio {
    private double monto;
    private String moneda;
    private Date fechaCambio;
    
    public Precio(double monto, String moneda, Date fechaCambio) {
        this.monto = monto;
        this.moneda = moneda;
        this.fechaCambio = fechaCambio;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public Date getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(Date fechaCambio) {
        this.fechaCambio = fechaCambio;
    }

    
    
    // Métodos: convertirMoneda, agregarHistorial, etc.
}