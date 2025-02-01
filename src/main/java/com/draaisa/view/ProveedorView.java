package com.draaisa.view;

import com.draaisa.controller.ProveedorController;
import com.draaisa.model.Categoria;
import com.draaisa.model.Proveedor;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ProveedorView extends Application {

    private ProveedorController controller = new ProveedorController();

    private String usuarioActual;

    public ProveedorView(String usuarioActual) {
        this.usuarioActual = usuarioActual; // Guardamos el usuario actual
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Proveedor Management");

        // Crear menú
        MenuBar menuBar = new MenuBar();

        Menu menu = new Menu("Opciones");
        MenuItem registroItem = new MenuItem("Registrar Proveedor");
        MenuItem modificarItem = new MenuItem("Modificar Proveedor");
        MenuItem buscarItem = new MenuItem("Buscar Proveedores");
        MenuItem eliminarItem = new MenuItem("Eliminar Proveedor");
        MenuItem consultarItem = new MenuItem("Consultar Todos Proveedores");
        MenuItem salirItem = new MenuItem("Salir");

        menu.getItems().addAll(registroItem, modificarItem, buscarItem, eliminarItem, consultarItem, salirItem);
        menuBar.getMenus().add(menu);

        // Crear panel principal
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(20));

        // Funcionalidades del menú
        registroItem.setOnAction(e -> showRegistroForm(vbox));
        modificarItem.setOnAction(e -> showModificarForm(vbox));
        buscarItem.setOnAction(e -> showBuscarForm(vbox));
        eliminarItem.setOnAction(e -> showEliminarForm(vbox));
        consultarItem.setOnAction(e -> showConsultarForm(vbox));
        salirItem.setOnAction(e -> mostrarMenuPrincipal(primaryStage));

        // Layout principal
        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(vbox);

        showConsultarForm(vbox);

        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Mostrar formulario para registrar proveedor con validaciones
    private void showRegistroForm(VBox vbox) {
        vbox.getChildren().clear();

        // Título del apartado
        Label titleLabel = new Label("Registrar Proveedor");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 10px;");

        // Crear campos de texto
        TextField nombreField = new TextField();
        nombreField.setPromptText("Nombre del proveedor");

        TextField rfcField = new TextField();
        rfcField.setPromptText("RFC");

        TextField telefonoField = new TextField();
        telefonoField.setPromptText("Teléfono");

        TextField cpField = new TextField();
        cpField.setPromptText("Código Postal");

        TextField noExtField = new TextField();
        noExtField.setPromptText("Número exterior");

        TextField noIntField = new TextField();
        noIntField.setPromptText("Número interior");

        TextField calleField = new TextField();
        calleField.setPromptText("Calle");

        TextField coloniaField = new TextField();
        coloniaField.setPromptText("Colonia");

        TextField ciudadField = new TextField();
        ciudadField.setPromptText("Ciudad");

        TextField municipioField = new TextField();
        municipioField.setPromptText("Municipio");

        TextField estadoField = new TextField();
        estadoField.setPromptText("Estado");

        TextField paisField = new TextField();
        paisField.setPromptText("País");

        TextField correoField = new TextField();
        correoField.setPromptText("Correo electrónico");

        TextField curpField = new TextField();
        curpField.setPromptText("CURP");

        CheckBox personaFisicaCheck = new CheckBox("Es persona física");

        // Crear botón para registrar
        Button registrarButton = new Button("Registrar");
        registrarButton.setOnAction(e -> {
            String nombre = nombreField.getText().trim();
            String rfc = rfcField.getText().trim();
            String telefono = telefonoField.getText().trim();
            String cp = cpField.getText().trim();
            String noExt = noExtField.getText().trim();
            String noInt = noIntField.getText().trim();
            String calle = calleField.getText().trim();
            String colonia = coloniaField.getText().trim();
            String ciudad = ciudadField.getText().trim();
            String municipio = municipioField.getText().trim();
            String estado = estadoField.getText().trim();
            String pais = paisField.getText().trim();
            String correo = correoField.getText().trim();
            String curp = curpField.getText().trim();
            boolean esPersonaFisica = personaFisicaCheck.isSelected();

            // Validaciones
            if (nombre.isEmpty() || rfc.isEmpty() || telefono.isEmpty() || cp.isEmpty() || calle.isEmpty()
                    || colonia.isEmpty() || ciudad.isEmpty() || municipio.isEmpty() || estado.isEmpty()
                    || pais.isEmpty() || correo.isEmpty() || curp.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Todos los campos son obligatorios.");
                return;
            }

            // Validar formato de correo
            if (!correo.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                showAlert(Alert.AlertType.ERROR, "Correo electrónico no válido.");
                return;
            }

            // Validar formato de RFC (solo como ejemplo simple)
            if (!rfc.matches("[A-Za-z]{3,4}[0-9]{6}[A-Za-z0-9]{3}")) {
                showAlert(Alert.AlertType.ERROR, "RFC no válido.");
                return;
            }

            // Validar formato de CURP (solo como ejemplo simple)
            if (!curp.matches("[A-Z]{4}[0-9]{6}[A-Z]{6,7}[0-9]{1,2}")) {
                showAlert(Alert.AlertType.ERROR, "CURP no válido.");
                return;
            }

            // Validar que los campos numéricos contengan solo números
            if (!cp.matches("[0-9]+") || !noExt.matches("[0-9]+") || (!noInt.isEmpty() && !noInt.matches("[0-9]+"))) {
                showAlert(Alert.AlertType.ERROR,
                        "Código Postal, Número exterior y Número interior deben ser numéricos.");
                return;
            }

            // Registrar proveedor
            int cpInt = Integer.parseInt(cp);
            int noExtInt = Integer.parseInt(noExt);
            int noIntInt = noInt.isEmpty() ? 0 : Integer.parseInt(noInt);

            Proveedor proveedor = new Proveedor(0, nombre, cpInt, noExtInt, noIntInt, rfc, municipio, estado, calle,
                    colonia, ciudad, pais, telefono, correo, curp, esPersonaFisica);
            List<Categoria> categorias = new ArrayList<>();
            // Agregar categorías de ejemplo
            categorias.add(new Categoria(0, "Categoría1", "Descripción"));

            controller.registrarProveedor(proveedor, categorias);
            showAlert(Alert.AlertType.INFORMATION, "Proveedor registrado con éxito.");
        });

        // Crear un contenedor con scroll para ajustar el tamaño y permitir que se vea
        // todo el formulario
        ScrollPane scrollPane = new ScrollPane();
        VBox formContainer = new VBox(10);
        formContainer.getChildren().addAll(
                titleLabel, nombreField, rfcField, telefonoField, cpField, noExtField, noIntField, calleField,
                coloniaField, ciudadField, municipioField, estadoField, paisField, correoField, curpField,
                personaFisicaCheck, registrarButton);

        scrollPane.setContent(formContainer);
        scrollPane.setFitToHeight(true); // Ajusta la altura al contenido
        scrollPane.setFitToWidth(true); // Ajusta el ancho al contenido

        // Asegurarse de que la ventana es lo suficientemente grande
        Stage stage = (Stage) vbox.getScene().getWindow();
        stage.setWidth(600);
        stage.setHeight(600);

        // Agregar el contenedor con scroll al VBox
        vbox.getChildren().add(scrollPane);
    }

    // Método para mostrar alertas
    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType, message);
        alert.show();
    }

    // Mostrar formulario para modificar proveedor
    private void showModificarForm(VBox vbox) {
        vbox.getChildren().clear();

        // Crear campos de texto
        TextField idField = new TextField();
        idField.setPromptText("ID del proveedor a modificar");
        TextField nombreField = new TextField();
        nombreField.setPromptText("Nuevo nombre del proveedor");
        TextField telefonoField = new TextField();
        telefonoField.setPromptText("Nuevo teléfono");

        // Crear botón para modificar
        Button modificarButton = new Button("Modificar");
        modificarButton.setOnAction(e -> {
            int idProveedor = Integer.parseInt(idField.getText());
            String nombre = nombreField.getText();
            String telefono = telefonoField.getText();

            Proveedor proveedor = new Proveedor(idProveedor, nombre, 0, 0, 0, "", "", "", "", "", "", "", telefono, "",
                    "", false);
            controller.modificarProveedor(proveedor);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Proveedor modificado con éxito.");
            alert.show();
        });

        // Agregar elementos al panel
        vbox.getChildren().addAll(idField, nombreField, telefonoField, modificarButton);
    }

    // Mostrar formulario para buscar proveedores
    private void showBuscarForm(VBox vbox) {
        vbox.getChildren().clear();

        // Crear campo de búsqueda
        TextField filtroField = new TextField();
        filtroField.setPromptText("Introduce un filtro de búsqueda");

        // Crear botón para buscar
        Button buscarButton = new Button("Buscar");
        buscarButton.setOnAction(e -> {
            String filtro = filtroField.getText();
            List<Proveedor> proveedoresEncontrados = controller.buscarProveedores(filtro);
            if (proveedoresEncontrados.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "No se encontraron proveedores.");
                alert.show();
            } else {
                StringBuilder resultados = new StringBuilder("Proveedores encontrados:\n");
                for (Proveedor p : proveedoresEncontrados) {
                    resultados.append(p.getNombreProv()).append("\n");
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION, resultados.toString());
                alert.show();
            }
        });

        // Agregar elementos al panel
        vbox.getChildren().addAll(filtroField, buscarButton);
    }

    // Mostrar formulario para eliminar proveedor
    private void showEliminarForm(VBox vbox) {
        vbox.getChildren().clear();

        // Crear campo para el ID
        TextField idField = new TextField();
        idField.setPromptText("ID del proveedor a eliminar");

        // Crear botón para eliminar
        Button eliminarButton = new Button("Eliminar");
        eliminarButton.setOnAction(e -> {
            int idProveedor = Integer.parseInt(idField.getText());
            controller.eliminarProveedor(idProveedor);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Proveedor eliminado con éxito.");
            alert.show();
        });

        // Agregar elementos al panel
        vbox.getChildren().addAll(idField, eliminarButton);
    }

    private void showConsultarForm(VBox vbox) {
        vbox.getChildren().clear();

        // Crear campo de búsqueda
        TextField filtroField = new TextField();
        filtroField.setPromptText("Introduce palabras clave para filtrar");

        // Crear el TableView
        TableView<Proveedor> tableView = new TableView<>();

        // Crear columnas para cada atributo del proveedor
        TableColumn<Proveedor, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idProveedor"));

        TableColumn<Proveedor, String> nombreColumn = new TableColumn<>("Nombre");
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombreProv"));

        TableColumn<Proveedor, String> rfcColumn = new TableColumn<>("RFC");
        rfcColumn.setCellValueFactory(new PropertyValueFactory<>("rfcProveedor"));

        TableColumn<Proveedor, String> telefonoColumn = new TableColumn<>("Teléfono");
        telefonoColumn.setCellValueFactory(new PropertyValueFactory<>("telefonoProv"));

        TableColumn<Proveedor, Integer> cpColumn = new TableColumn<>("Código Postal");
        cpColumn.setCellValueFactory(new PropertyValueFactory<>("cpProveedor"));

        TableColumn<Proveedor, Integer> noExtColumn = new TableColumn<>("Número Exterior");
        noExtColumn.setCellValueFactory(new PropertyValueFactory<>("noExtProv"));

        TableColumn<Proveedor, Integer> noIntColumn = new TableColumn<>("Número Interior");
        noIntColumn.setCellValueFactory(new PropertyValueFactory<>("noIntProv"));

        TableColumn<Proveedor, String> calleColumn = new TableColumn<>("Calle");
        calleColumn.setCellValueFactory(new PropertyValueFactory<>("calle"));

        TableColumn<Proveedor, String> coloniaColumn = new TableColumn<>("Colonia");
        coloniaColumn.setCellValueFactory(new PropertyValueFactory<>("colonia"));

        TableColumn<Proveedor, String> ciudadColumn = new TableColumn<>("Ciudad");
        ciudadColumn.setCellValueFactory(new PropertyValueFactory<>("ciudad"));

        TableColumn<Proveedor, String> municipioColumn = new TableColumn<>("Municipio");
        municipioColumn.setCellValueFactory(new PropertyValueFactory<>("municipio"));

        TableColumn<Proveedor, String> estadoColumn = new TableColumn<>("Estado");
        estadoColumn.setCellValueFactory(new PropertyValueFactory<>("estado"));

        TableColumn<Proveedor, String> paisColumn = new TableColumn<>("País");
        paisColumn.setCellValueFactory(new PropertyValueFactory<>("pais"));

        TableColumn<Proveedor, String> correoColumn = new TableColumn<>("Correo");
        correoColumn.setCellValueFactory(new PropertyValueFactory<>("correoProv"));

        TableColumn<Proveedor, String> curpColumn = new TableColumn<>("CURP");
        curpColumn.setCellValueFactory(new PropertyValueFactory<>("curp"));

        TableColumn<Proveedor, String> personaFisicaColumn = new TableColumn<>("Es Persona Física");
        personaFisicaColumn.setCellValueFactory(new PropertyValueFactory<>("esPersonaFisica"));

        // Agregar las columnas al TableView
        tableView.getColumns().addAll(idColumn, nombreColumn, rfcColumn, telefonoColumn, cpColumn, noExtColumn,
                noIntColumn, calleColumn, coloniaColumn, ciudadColumn, municipioColumn, estadoColumn, paisColumn,
                correoColumn,
                curpColumn, personaFisicaColumn);

        // Obtener todos los proveedores y agregarlos al TableView
        List<Proveedor> proveedores = controller.consultarTodosProveedores();
        tableView.getItems().addAll(proveedores);

        // Crear un filtro para la búsqueda
        filtroField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Limpiar la lista de proveedores filtrados
            List<Proveedor> proveedoresFiltrados = new ArrayList<>();
        
            // Dividir el texto de búsqueda en palabras clave separadas por comas
            String[] palabras = newValue.split(",");
            
            // Recorrer cada proveedor y verificar si coincide con todas las palabras clave
            for (Proveedor proveedor : proveedores) {
                boolean coincide = true;
        
                // Comprobar cada palabra clave
                for (String palabra : palabras) {
                    palabra = palabra.trim().toLowerCase();  // Eliminar espacios y pasar a minúsculas
                    if (!String.valueOf(proveedor.getIdProveedor()).contains(palabra) &&
                        !proveedor.getNombreProv().toLowerCase().contains(palabra) &&
                        !proveedor.getEstado().toLowerCase().contains(palabra) &&
                        !proveedor.getMunicipio().toLowerCase().contains(palabra)) {
                        coincide = false; // Si no coincide con algún filtro, romper el bucle
                        break;
                    }
                }
        
                // Si todas las palabras clave coinciden, añadir el proveedor
                if (coincide) {
                    proveedoresFiltrados.add(proveedor);
                }
            }
        
            // Actualizar la tabla con los proveedores filtrados
            tableView.getItems().setAll(proveedoresFiltrados);
        });

        // Crear un ScrollPane para que la tabla sea desplazable si es necesario
        ScrollPane scrollPane = new ScrollPane(tableView);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        // Agregar el ScrollPane con la tabla al VBox
        vbox.getChildren().addAll(filtroField, scrollPane);
    }

    private void mostrarMenuPrincipal(Stage primaryStage) {
        // Crea una nueva instancia del MenuPrincipalScreen con el usuario actual
        MenuPrincipalScreen menuPrincipalScreen = new MenuPrincipalScreen(primaryStage, usuarioActual);
        menuPrincipalScreen.mostrarMenu(); // Mostrar el menú principal
    }

}
