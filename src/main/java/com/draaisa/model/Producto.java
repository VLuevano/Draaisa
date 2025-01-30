package com.draaisa.model;

import java.util.ArrayList;
import java.util.List;

public class Producto {
    private int idProducto;
    private String nombreProducto;
    private String fichaProducto;
    private String alternoProducto;
    private int existenciaProducto;

    // Listas para relaciones con proveedores, fabricantes, clientes, empresas, precios y categorías
    private List<Proveedor> proveedores;
    private List<Fabricante> fabricantes;
    private List<Cliente> clientes;
    private List<Empresa> empresas;
    private List<Precio> precios;
    private List<Categoria> categorias;

    // Constructor
    public Producto(int idProducto, String nombreProducto, String fichaProducto, String alternoProducto, int existenciaProducto) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.fichaProducto = fichaProducto;
        this.alternoProducto = alternoProducto;
        this.existenciaProducto = existenciaProducto;
        this.proveedores = new ArrayList<>();
        this.fabricantes = new ArrayList<>();
        this.clientes = new ArrayList<>();
        this.empresas = new ArrayList<>();
        this.precios = new ArrayList<>();
        this.categorias = new ArrayList<>();
    }

    // Métodos: agregarProveedor, agregarFabricante, buscarPorCriterios, actualizarExistencia, etc.

}

