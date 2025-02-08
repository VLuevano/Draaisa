package com.draaisa.model;

public class Ruta {
    private int idRuta;
    private String salida;
    private String destino;

    public Ruta(int idRuta, String salida, String destino) {
        this.idRuta = idRuta;
        this.salida = salida;
        this.destino = destino;
    }

    public Ruta() {
        // TODO Auto-generated constructor stub
    }

    // Getters and Setters
    public int getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(int idRuta) {
        this.idRuta = idRuta;
    }

    public String getSalida() {
        return salida;
    }

    public void setSalida(String salida) {
        this.salida = salida;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

}