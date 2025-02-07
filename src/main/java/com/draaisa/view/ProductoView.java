package com.draaisa.view;

import com.draaisa.controller.ProductoController;
import com.draaisa.model.Producto;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

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
    public void start(Stage primaryStage) {
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
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox crearFormularioRegistro() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-border-color: black; -fx-border-width: 1px; -fx-border-radius: 5px;");

        Label titulo = new Label("Registrar Producto");
        titulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        TextField nombreField = new TextField();
        TextField fichaField = new TextField();
        TextField alternoField = new TextField();
        TextField existenciaField = new TextField();
        Button registrarBtn = new Button("Registrar");

        registrarBtn.setOnAction(e -> {
            try {
                Producto nuevoProducto = new Producto(0, nombreField.getText(), fichaField.getText(), alternoField.getText(), Integer.parseInt(existenciaField.getText()));
                productoController.registrarProducto(nuevoProducto, List.of(), List.of(), List.of(), List.of(), List.of(), List.of());
                limpiarFormulario(nombreField, fichaField, alternoField, existenciaField);
                mostrarConsultaProductos();
            } catch (Exception ex) {
                showAlert("Error", "Error al registrar el producto", Alert.AlertType.ERROR);
            }
        });

        layout.getChildren().addAll(
                titulo,
                new Label("Nombre:"), nombreField,
                new Label("Ficha:"), fichaField,
                new Label("Alterno:"), alternoField,
                new Label("Existencia:"), existenciaField,
                registrarBtn
        );
        return layout;
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

    private void limpiarFormulario(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
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
