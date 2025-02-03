package com.draaisa.view;

import com.draaisa.controller.UsuarioController;
import com.draaisa.model.Usuario;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class UsuarioView extends Application {

    private final String usuarioActual;
    private TableView<Usuario> tablaUsuarios;

    public UsuarioView(String usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Módulo de Usuarios");

        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Opciones");
        MenuItem registroItem = new MenuItem("Registrar Usuario");
        MenuItem consultarItem = new MenuItem("Consultar Usuarios");
        MenuItem salirItem = new MenuItem("Salir");

        menu.getItems().addAll(registroItem, consultarItem, salirItem);
        menuBar.getMenus().add(menu);

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));

        registroItem.setOnAction(e -> showRegistroForm(vbox));
        consultarItem.setOnAction(e -> showConsultarForm(vbox));
        salirItem.setOnAction(e -> mostrarMenuPrincipal(primaryStage));

        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(vbox);

        showConsultarForm(vbox);

        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.show();
    }

    private void showRegistroForm(VBox vbox) {
        vbox.getChildren().setAll(crearFormularioRegistro());
    }

    private void showConsultarForm(VBox vbox) {
        vbox.getChildren().setAll(crearTablaUsuarios());
    }

    private GridPane crearFormularioRegistro() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(15));
        grid.setVgap(10);
        grid.setHgap(10);

        Label lblUsuario = new Label("Usuario:");
        TextField txtUsuario = new TextField();
        Label lblContrasena = new Label("Contraseña:");
        PasswordField txtContrasena = new PasswordField();
        CheckBox chkPermiso = new CheckBox("Es Administrador");
        Button btnRegistrar = new Button("Registrar");
        Label lblMensaje = new Label();

        btnRegistrar.setOnAction(e -> {
            Usuario usuario = new Usuario(0, txtUsuario.getText(), txtContrasena.getText(), chkPermiso.isSelected());
            if (UsuarioController.registrarUsuario(usuario)) {
                lblMensaje.setText("Usuario registrado");
                actualizarTablaUsuarios();
            } else {
                lblMensaje.setText("Error en el registro");
            }
        });

        grid.add(lblUsuario, 0, 0);
        grid.add(txtUsuario, 1, 0);
        grid.add(lblContrasena, 0, 1);
        grid.add(txtContrasena, 1, 1);
        grid.add(chkPermiso, 1, 2);
        grid.add(btnRegistrar, 1, 3);
        grid.add(lblMensaje, 1, 4);

        return grid;
    }

    private GridPane crearTablaUsuarios() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(15));

        tablaUsuarios = new TableView<>();
        TableColumn<Usuario, Integer> colId = new TableColumn<>("ID");
        TableColumn<Usuario, String> colNombre = new TableColumn<>("Nombre");
        TableColumn<Usuario, Boolean> colPermiso = new TableColumn<>("Administrador");

        colId.setCellValueFactory(cellData -> cellData.getValue().idUsuarioProperty().asObject());
        colNombre.setCellValueFactory(cellData -> cellData.getValue().nombreUsuarioProperty());
        colPermiso.setCellValueFactory(cellData -> cellData.getValue().permisoProperty().asObject());

        tablaUsuarios.getColumns().addAll(colId, colNombre, colPermiso);
        actualizarTablaUsuarios();

        tablaUsuarios.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !tablaUsuarios.getSelectionModel().isEmpty()) {
                mostrarOpcionesUsuario(tablaUsuarios.getSelectionModel().getSelectedItem());
            }
        });

        grid.add(tablaUsuarios, 0, 0);
        return grid;
    }

    private void mostrarOpcionesUsuario(Usuario usuario) {
        Stage opcionesStage = new Stage();
        opcionesStage.initModality(Modality.APPLICATION_MODAL);
        opcionesStage.setTitle("Opciones de Usuario");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(15));
        grid.setVgap(10);
        grid.setHgap(10);

        Label lblContrasena = new Label("Nueva Contraseña:");
        PasswordField txtNuevaContrasena = new PasswordField();
        Button btnCambiar = new Button("Cambiar Contraseña");
        Button btnEliminar = new Button("Eliminar Usuario");

        btnCambiar.setOnAction(e -> {
            if (UsuarioController.cambiarContrasena(usuario.getIdUsuario(), txtNuevaContrasena.getText())) {
                mostrarAlerta("Éxito", "Contraseña actualizada");
                actualizarTablaUsuarios();
            } else {
                mostrarAlerta("Error", "No se pudo actualizar la contraseña");
            }
        });

        btnEliminar.setOnAction(e -> {
            if (mostrarConfirmacion("Eliminar Usuario", "¿Seguro que quieres eliminar este usuario?")) {
                if (UsuarioController.eliminarUsuario(usuario.getIdUsuario())) {
                    mostrarAlerta("Éxito", "Usuario eliminado");
                    actualizarTablaUsuarios();
                    opcionesStage.close();
                } else {
                    mostrarAlerta("Error", "No se pudo eliminar el usuario");
                }
            }
        });

        grid.add(lblContrasena, 0, 0);
        grid.add(txtNuevaContrasena, 1, 0);
        grid.add(btnCambiar, 1, 1);
        grid.add(btnEliminar, 1, 2);

        opcionesStage.setScene(new Scene(grid, 400, 250));
        opcionesStage.show();
    }

    private void actualizarTablaUsuarios() {
        tablaUsuarios.getItems().setAll(UsuarioController.consultarUsuarios());
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private boolean mostrarConfirmacion(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    private void mostrarMenuPrincipal(Stage primaryStage) {
        new MenuPrincipalScreen(primaryStage, usuarioActual).mostrarMenu();
    }
}
