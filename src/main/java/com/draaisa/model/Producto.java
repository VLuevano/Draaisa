package com.draaisa.model;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class Producto {
    private IntegerProperty idProducto;
    private StringProperty nombreProducto;
    private StringProperty fichaProducto;
    private StringProperty alternoProducto;
    private IntegerProperty existenciaProducto;

    // Listas para relaciones con otras entidades
    private List<Proveedor> proveedores;
    private List<Fabricante> fabricantes;
    private List<Cliente> clientes;
    private List<Empresa> empresas;
    private List<Precio> precios;
    private List<Categoria> categorias;

    public Producto(int idProducto, String nombreProducto, String fichaProducto, String alternoProducto,
            int existenciaProducto) {
        this.idProducto = new SimpleIntegerProperty(idProducto);
        this.nombreProducto = new SimpleStringProperty(nombreProducto);
        this.fichaProducto = new SimpleStringProperty(fichaProducto);
        this.alternoProducto = new SimpleStringProperty(alternoProducto);
        this.existenciaProducto = new SimpleIntegerProperty(existenciaProducto);
        this.proveedores = new ArrayList<>();
        this.fabricantes = new ArrayList<>();
        this.clientes = new ArrayList<>();
        this.empresas = new ArrayList<>();
        this.precios = new ArrayList<>();
        this.categorias = new ArrayList<>();
    }

    // Getters y Setters con propiedades
    public int getIdProducto() {
        return idProducto.get();
    }

    public void setIdProducto(int idProducto) {
        this.idProducto.set(idProducto);
    }

    public IntegerProperty idProductoProperty() {
        return idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto.get();
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto.set(nombreProducto);
    }

    public StringProperty nombreProductoProperty() {
        return nombreProducto;
    }

    public String getFichaProducto() {
        return fichaProducto.get();
    }

    public void setFichaProducto(String fichaProducto) {
        this.fichaProducto.set(fichaProducto);
    }

    public StringProperty fichaProductoProperty() {
        return fichaProducto;
    }

    public String getAlternoProducto() {
        return alternoProducto.get();
    }

    public void setAlternoProducto(String alternoProducto) {
        this.alternoProducto.set(alternoProducto);
    }

    public StringProperty alternoProductoProperty() {
        return alternoProducto;
    }

    public int getExistenciaProducto() {
        return existenciaProducto.get();
    }

    public void setExistenciaProducto(int existenciaProducto) {
        this.existenciaProducto.set(existenciaProducto);
    }

    public IntegerProperty existenciaProductoProperty() {
        return existenciaProducto;
    }

    // Métodos adicionales para manejar listas de relaciones
    public List<Proveedor> getProveedores() {
        return proveedores;
    }

    public void setProveedores(List<Proveedor> proveedores) {
        this.proveedores = proveedores;
    }

    public List<Fabricante> getFabricantes() {
        return fabricantes;
    }

    public void setFabricantes(List<Fabricante> fabricantes) {
        this.fabricantes = fabricantes;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    public List<Empresa> getEmpresas() {
        return empresas;
    }

    public void setEmpresas(List<Empresa> empresas) {
        this.empresas = empresas;
    }

    public List<Precio> getPrecios() {
        return precios;
    }

    public void setPrecios(List<Precio> precios) {
        this.precios = precios;
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }
}
