package com.draaisa.view;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.draaisa.controller.ProductoController;
import com.draaisa.model.Producto;
import com.draaisa.model.Categoria;
import com.draaisa.model.Empresa;
import com.draaisa.model.Proveedor;
import com.draaisa.model.Servicio;
import com.draaisa.model.Fabricante;
import com.draaisa.model.Cliente;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ProductoView extends Application {
    private final ProductoController productoController = new ProductoController();
    private String usuarioActual;
    private VBox formularioRegistro;
    private VBox consultaProductos;
    private TableView<Producto> productoTable;
    private VBox root;

    public ProductoView(String usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Gestión de Productos");

        MenuBar menuBar = new MenuBar();
        Menu menuVista = new Menu("Opciones");
        MenuItem registrarItem = new MenuItem("Registrar producto");
        MenuItem consultarItem = new MenuItem("Consultar productos");
        MenuItem salirItem = new MenuItem("Salir");

        registrarItem.setOnAction(e -> mostrarFormularioRegistro());
        consultarItem.setOnAction(e -> mostrarConsultaProductos());
        salirItem.setOnAction(e -> mostrarMenuPrincipal(primaryStage));

        menuVista.getItems().addAll(registrarItem, consultarItem, salirItem);
        menuBar.getMenus().add(menuVista);

        formularioRegistro = crearFormularioRegistro();
        consultaProductos = crearConsultaProductos();

        root = new VBox(10, menuBar, consultaProductos);
        root.setPadding(new Insets(10));
        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox crearFormularioRegistro() throws IOException {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label titulo = new Label("Registrar Producto");
        titulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextField nombreField = new TextField();
        nombreField.setStyle("-fx-margin: 5px;");

        TextField fichaField = new TextField();
        fichaField.setStyle("-fx-margin: 5px;");

        TextField alternoField = new TextField();
        alternoField.setStyle("-fx-margin: 5px;");

        TextField existenciaField = new TextField();
        existenciaField.setStyle("-fx-margin: 5px;");

        ComboBox<String> monedaCombo = new ComboBox<>();
        monedaCombo.getItems().addAll("USD", "MXN");
        monedaCombo.setStyle("-fx-margin: 5px;");

        List<ComboBox<Categoria>> categoriaList = new ArrayList<>();
        List<ComboBox<Empresa>> empresaList = new ArrayList<>();
        List<TextField> precioEmpresaList = new ArrayList<>();
        List<ComboBox<Proveedor>> proveedorList = new ArrayList<>();
        List<TextField> precioProveedorList = new ArrayList<>();
        List<ComboBox<Fabricante>> fabricanteList = new ArrayList<>();
        List<TextField> precioFabricanteList = new ArrayList<>();
        List<ComboBox<Cliente>> clienteList = new ArrayList<>();
        List<TextField> precioClienteList = new ArrayList<>();
        List<ComboBox<Servicio>> servicios = new ArrayList<>();

        categoriaList.add(new ComboBox<>(FXCollections.observableList(productoController.obtenerCategorias())));
        empresaList.add(new ComboBox<>(FXCollections.observableList(productoController.obtenerEmpresas())));
        proveedorList.add(new ComboBox<>(FXCollections.observableList(productoController.obtenerProveedores())));
        fabricanteList.add(new ComboBox<>(FXCollections.observableList(productoController.obtenerFabricantes())));
        clienteList.add(new ComboBox<>(FXCollections.observableList(productoController.obtenerClientes())));
        servicios.add(new ComboBox<>(FXCollections.observableList(productoController.obtenerServicios())));

        // Botones con colores
        Button agregarCategoriaBtn = new Button("Agregar Categoría");
        agregarCategoriaBtn.setId("agregarCategoriaBtn");
        agregarCategoriaBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;"); // Verde
        agregarCategoriaBtn.setOnAction(e -> {
            try {
                ComboBox<Categoria> categoriaCombo = new ComboBox<>(
                        FXCollections.observableList(productoController.obtenerCategorias()));
                categoriaList.add(categoriaCombo);
                layout.getChildren().addAll(new Separator(), new Label("Categoría:"), categoriaCombo);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        Button agregarEmpresaBtn = new Button("Agregar Empresa");
        agregarEmpresaBtn.setId("agregarEmpresaBtn");
        agregarEmpresaBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;"); // Azul
        agregarEmpresaBtn.setOnAction(e -> {
            try {
                ComboBox<Empresa> empresaCombo = new ComboBox<>(
                        FXCollections.observableList(productoController.obtenerEmpresas()));
                TextField precioEmpresa = new TextField();
                empresaList.add(empresaCombo);
                precioEmpresaList.add(precioEmpresa);
                layout.getChildren().addAll(new Separator(), new Label("Empresa:"), empresaCombo,
                        new Label("Precio Empresa:"), precioEmpresa);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        Button agregarProveedorBtn = new Button("Agregar Proveedor");
        agregarProveedorBtn.setId("agregarProveedorBtn");
        agregarProveedorBtn.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white;"); // Naranja
        agregarProveedorBtn.setOnAction(e -> {
            try {
                ComboBox<Proveedor> proveedorCombo = new ComboBox<>(
                        FXCollections.observableList(productoController.obtenerProveedores()));
                TextField precioProveedor = new TextField();
                proveedorList.add(proveedorCombo);
                precioProveedorList.add(precioProveedor);
                layout.getChildren().addAll(new Separator(), new Label("Proveedor:"), proveedorCombo,
                        new Label("Precio Proveedor:"), precioProveedor);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        Button agregarFabricanteBtn = new Button("Agregar Fabricante");
        agregarFabricanteBtn.setId("agregarFabricanteBtn");
        agregarFabricanteBtn.setStyle("-fx-background-color: #FFC107; -fx-text-fill: white;"); // Amarillo
        agregarFabricanteBtn.setOnAction(e -> {
            try {
                ComboBox<Fabricante> fabricanteCombo = new ComboBox<>(
                        FXCollections.observableList(productoController.obtenerFabricantes()));
                TextField precioFabricante = new TextField();
                fabricanteList.add(fabricanteCombo);
                precioFabricanteList.add(precioFabricante);
                layout.getChildren().addAll(new Separator(), new Label("Fabricante:"), fabricanteCombo,
                        new Label("Precio Fabricante:"), precioFabricante);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        Button agregarClienteBtn = new Button("Agregar Cliente");
        agregarClienteBtn.setId("agregarClienteBtn");
        agregarClienteBtn.setStyle("-fx-background-color: #673AB7; -fx-text-fill: white;"); // Púrpura
        agregarClienteBtn.setOnAction(e -> {
            try {
                ComboBox<Cliente> clienteCombo = new ComboBox<>(
                        FXCollections.observableList(productoController.obtenerClientes()));
                TextField precioCliente = new TextField();
                clienteList.add(clienteCombo);
                precioClienteList.add(precioCliente);
                layout.getChildren().addAll(new Separator(), new Label("Cliente:"), clienteCombo,
                        new Label("Precio Cliente:"), precioCliente);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        Button agregarServicioBtn = new Button("Agregar Servicio");
        agregarServicioBtn.setId("agregarServicioBtn");
        agregarServicioBtn.setStyle("-fx-background-color: #9C27B0; -fx-text-fill: white;"); // Violeta
        agregarServicioBtn.setOnAction(e -> {
            try {
                ComboBox<Servicio> servicioCombo = new ComboBox<>(
                        FXCollections.observableList(productoController.obtenerServicios()));
                servicios.add(servicioCombo);
                layout.getChildren().addAll(new Separator(), new Label("Servicio:"), servicioCombo);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        Button registrarBtn = new Button("Registrar");
        registrarBtn.setId("registrarBtn");
        registrarBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;"); // Verde
        registrarBtn.setOnAction(e -> {
            try {
                Producto nuevoProducto = new Producto(0, nombreField.getText(), fichaField.getText(),
                        alternoField.getText(), Integer.parseInt(existenciaField.getText()));

                List<Categoria> categoriasSeleccionadas = categoriaList.stream().map(ComboBox::getValue)
                        .collect(Collectors.toList());
                List<Empresa> empresasSeleccionadas = empresaList.stream().map(ComboBox::getValue)
                        .filter(Objects::nonNull).collect(Collectors.toList());

                List<Double> preciosEmpresas = precioEmpresaList.stream().map(tf -> Double.parseDouble(tf.getText()))
                        .collect(Collectors.toList());

                List<Proveedor> proveedoresSeleccionados = proveedorList.stream().map(ComboBox::getValue)
                        .filter(Objects::nonNull).collect(Collectors.toList());
                List<Double> preciosProveedores = precioProveedorList.stream()
                        .map(tf -> Double.parseDouble(tf.getText())).collect(Collectors.toList());

                List<Fabricante> fabricantesSeleccionados = fabricanteList.stream().map(ComboBox::getValue)
                        .filter(Objects::nonNull).collect(Collectors.toList());
                List<Double> preciosFabricantes = precioFabricanteList.stream()
                        .map(tf -> Double.parseDouble(tf.getText())).collect(Collectors.toList());

                List<Cliente> clientesSeleccionados = clienteList.stream().map(ComboBox::getValue)
                        .filter(Objects::nonNull).collect(Collectors.toList());
                List<Double> preciosClientes = precioClienteList.stream().map(tf -> Double.parseDouble(tf.getText()))
                        .collect(Collectors.toList());

                List<Servicio> serviciosSeleccionados = servicios.stream().map(ComboBox::getValue)
                        .filter(Objects::nonNull).collect(Collectors.toList());

                String moneda = monedaCombo.getValue();

                productoController.registrarProducto(nuevoProducto, categoriasSeleccionadas, empresasSeleccionadas,
                        proveedoresSeleccionados, fabricantesSeleccionados, clientesSeleccionados,
                        serviciosSeleccionados, preciosEmpresas, preciosProveedores, preciosFabricantes,
                        preciosClientes, moneda);
                showAlert("Éxito", "Producto registrado correctamente", Alert.AlertType.INFORMATION);

            layout.getChildren().clear();

            VBox nuevoFormulario = crearFormularioRegistro();
            nuevoFormulario.setPadding(new Insets(-21));
            layout.getChildren().add(nuevoFormulario);

            } catch (Exception ex) {
                showAlert("Error", "Error al registrar el producto", Alert.AlertType.ERROR);
                System.out.println(ex.getMessage());
            }
        });

        HBox botonera = new HBox(10);
        botonera.getChildren().addAll(agregarCategoriaBtn, agregarEmpresaBtn, agregarProveedorBtn, agregarFabricanteBtn,
                agregarClienteBtn, agregarServicioBtn);
        botonera.setStyle("-fx-alignment: center;");

        layout.getChildren().addAll(titulo, new Label("Nombre:"), nombreField, new Label("Ficha:"), fichaField,
                new Label("Alterno:"), alternoField, new Label("Existencia:"), existenciaField, new Label("Moneda:"),
                monedaCombo, new Label("Categoría:"), categoriaList.get(0), botonera, registrarBtn);

        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);
        return new VBox(scrollPane);
    }

    @SuppressWarnings("unchecked")
    private VBox crearConsultaProductos() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-border-color: black; -fx-border-width: 1px; -fx-border-radius: 5px;");

        productoTable = new TableView<>();
        TableColumn<Producto, Integer> idColumn = new TableColumn<>("ID");
        TableColumn<Producto, String> nombreColumn = new TableColumn<>("Nombre");
        TableColumn<Producto, String> fichaColumn = new TableColumn<>("Ficha");
        TableColumn<Producto, String> alternoColumn = new TableColumn<>("Alterno");
        TableColumn<Producto, Integer> existenciaColumn = new TableColumn<>("Existencia");

        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProductoProperty().asObject());
        nombreColumn.setCellValueFactory(cellData -> cellData.getValue().nombreProductoProperty());
        fichaColumn.setCellValueFactory(cellData -> cellData.getValue().fichaProductoProperty());
        alternoColumn.setCellValueFactory(cellData -> cellData.getValue().alternoProductoProperty());
        existenciaColumn.setCellValueFactory(cellData -> cellData.getValue().existenciaProductoProperty().asObject());

        productoTable.getColumns().addAll(idColumn, nombreColumn, fichaColumn, alternoColumn, existenciaColumn);
        cargarProductos();

        layout.getChildren().add(productoTable);
        return layout;
    }

    private void mostrarFormularioRegistro() {
        root.getChildren().set(1, formularioRegistro);
    }

    private void mostrarConsultaProductos() {
        cargarProductos();
        root.getChildren().set(1, consultaProductos);
    }

    private void cargarProductos() {
        try {
            List<Producto> productos = productoController.consultarProductos();
            productoTable.getItems().setAll(productos);
        } catch (Exception e) {
            showAlert("Error", "Error al cargar los productos", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void mostrarMenuPrincipal(Stage primaryStage) {
        MenuPrincipalScreen menuPrincipalScreen = new MenuPrincipalScreen(primaryStage, usuarioActual);
        menuPrincipalScreen.mostrarMenu();
    }
}
