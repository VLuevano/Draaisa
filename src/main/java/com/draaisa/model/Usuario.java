package com.draaisa.model;

public class Usuario {
    private int idUsuario;
    private String nombreUsuario;
    private String contrasenaUsuario;
    private boolean permiso; // Nuevo campo

    // Constructor
    public Usuario(int idUsuario, String nombreUsuario, String contrasenaUsuario, boolean permiso) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.contrasenaUsuario = contrasenaUsuario;
        this.permiso = permiso; // Inicializar el campo permiso
    }

    // Getters y Setters
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasenaUsuario() {
        return contrasenaUsuario;
    }

    public void setContrasenaUsuario(String contrasenaUsuario) {
        this.contrasenaUsuario = contrasenaUsuario;
    }

    public boolean isPermiso() {
        return permiso;
    }

    public void setPermiso(boolean permiso) {
        this.permiso = permiso;
    }
}
