package com.example.practica_1_algoritmos;

import DeckOfCards.CartaInglesa;
import DeckOfCards.Palo;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import solitaire.SolitaireGame;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import java.util.Optional;

public class Controller {
    // Instancia del juego
    private SolitaireGame game = new SolitaireGame();

    // UI
    private final BorderPane mainPane; // Pane principal
    private final HBox filaSuperior = new HBox(16); // Contiene stock/waste y las fundaciones
    private final HBox filaInferior = new HBox(16); // Contiene botones salir y reset
    private final StackPane mazoPane = new StackPane(); // StackPane para presentar las cartas en mazo
    private final StackPane descartePane = new StackPane(); // Stack pane de las cartas que se pidieron en el mazo
    private final StackPane[] fundacionesPanes = new StackPane[4]; // Vector StackPane para poner los 4 palos de las fundaciones

    // Tablero gráfico
    private final TableroGrafico tableroGrafico;

    // Botones
    private final Button btnReciclar = new Button("Reciclar mazo"),
            btnSalir = new Button("Salir"),
            btnReset = new Button("Reset Juego");

    // Constantes
    private static double ANCHO_CARTA = 90;
    private static double ALTO_CARTA = 130;
    private static double GAP_CUBIERTA = 14; // Separación vertical cuando la carta está volteada
    private static double GAP_DESCUBIERTA = 28; // Separación vertical cuando la carta esta boca arriba

    // Efecto de hover
    private final DropShadow EFECTO_HOVER = new DropShadow(18, Color.WHITE);
    private final DropShadow EFECTO_SELECCION = new DropShadow(22, Color.web("#4fc3ff"));

    // Selección simple
    private boolean haySeleccion = false;  // Determina si hay algo seleccionado
    private String origenSeleccion = "";   // Origen: WASTE o TABLEAU
    private int columnaSeleccionada = -1;   // 1 - 7 si origen es TABLEAU
    private StackPane vistaSeleccionada = null; // Referencia a la carta actualmente seleccionada

    public Controller(BorderPane mainPane) {
        this.mainPane = mainPane;
        // Instanciamos el tablero gráfico que ahora maneja la UI de las 7 columnas
        this.tableroGrafico = new TableroGrafico(ANCHO_CARTA, ALTO_CARTA, GAP_CUBIERTA, GAP_DESCUBIERTA);
        construirUI();
        refrescar();
    }

    /* Método que se encarga de inicializar todos los componentes que integran
     *  a la ventana
     */
    private void construirUI() {
        // Fondo y barras
        // Pane principal color verde
        mainPane.setBackground(new Background(new BackgroundFill(Color.web("#0b5d2a"), CornerRadii.EMPTY, Insets.EMPTY)));

        filaSuperior.setPadding(new Insets(16)); // 16px de espacio en todos sus bordes
        filaSuperior.setAlignment(Pos.TOP_LEFT); // alinear arriba a la izquierda
        filaSuperior.setFillHeight(false);

        filaInferior.setPadding(new Insets(16));
        filaInferior.setAlignment(Pos.BOTTOM_CENTER); // alineado al centro hasta abajo
        filaInferior.setFillHeight(false);

        // Ranuras de mazo y descarte
        configurarRanura(mazoPane, "MAZO");
        configurarRanura(descartePane, "DESCARTE");

        /* Evento cuando quieres tomar cartas del mazo
         *  si hay cartas, roba y refresca
         */
        mazoPane.setOnMouseClicked(e -> {
            if (e.getButton() != MouseButton.PRIMARY) return; // solo funciona con click izquierdo
            if (game.getDrawPile().hayCartas()) { // Si hay cartas en el mazo
                game.drawCards(); // Toma cartas
                limpiarSeleccion(); // Si estaba en progreso una selección la anula
                refrescar(); // Vuelve a cargar o dibujar los elementos
            }
        });

        // Evento reiniciar el mazo
        btnReciclar.setOnAction(e -> {
            game.reloadDrawPile();
            limpiarSeleccion();
            refrescar();
        });
        btnReciclar.setPrefWidth(ANCHO_CARTA);

        // Reset juego completo
        btnReset.setOnAction(e -> {
            game = new SolitaireGame(); // Inicia la instancia del juego de nuevo
            limpiarSeleccion();
            refrescar();
        });

        // Salir con confirmación
        btnSalir.setOnAction(e -> confirmarSalida());
        btnSalir.setPrefWidth(ANCHO_CARTA);

        // 4 ranuras, una por cada tipo de palo
        for (int i = 0; i < 4; i++) {
            fundacionesPanes[i] = new StackPane(); // Creo StackPane en c/d posición
            // Genera una etiqueta con la simbología respectiva del palo
            configurarRanura(fundacionesPanes[i], Palo.values()[i].getFigura());
            // Evento
            fundacionesPanes[i].setOnMouseClicked(e -> {
                if (e.getButton() != MouseButton.PRIMARY) return;
                if (!haySeleccion) return; // Si no está nada seleccionado
                boolean movido = false; // No se ha movido nada
                if ("WASTE".equals(origenSeleccion)) { // Si se quiere mover desde el waste
                    movido = game.moveWasteToFoundation(); // Método para mover waste>foundation
                } else if ("TABLEAU".equals(origenSeleccion)) { // Si se quiere mover desde el tablero
                    movido = game.moveTableauToFoundation(columnaSeleccionada);
                }
                limpiarSeleccion();
                if (movido) refrescar(); // Si se movió actualiza
            });
        }

        // Fila superior
        Region separador = new Region();
        HBox.setHgrow(separador, Priority.ALWAYS); // Con el separador empujo las fundaciones
        /* Agrego al HBox todos los elementos:
         * paneles para el mazo, descarte, boton de reciclajen de mazo,
         * el separador, y coloco los valores en el vector de las fundaciones
         */
        filaSuperior.getChildren().setAll(
                mazoPane,
                descartePane,
                btnReciclar,
                separador,
                fundacionesPanes[0], fundacionesPanes[1], fundacionesPanes[2], fundacionesPanes[3]
        );
        // Fila inferior, agrego los botones
        filaInferior.getChildren().setAll(btnReset, btnSalir);
        // agrego los elementos al panel principal
        mainPane.setTop(filaSuperior);
        mainPane.setBottom(filaInferior);

        // Se agrega el tablero gráfico de columnas en el centro
        mainPane.setCenter(tableroGrafico.getHBox());

        // Eventos de click para las columnas
        for (int i = 0; i < 7; i++) {
            final int idxCol = i + 1; // Ajuste para el índice, la lógica usa 1 - 7
            var columnaNodo = tableroGrafico.getColumna(i);
            columnaNodo.setOnMouseClicked(e -> {
                if (e.getButton() != MouseButton.PRIMARY) return;
                clickColumna(idxCol); // Determina si la columna es el origen o el destino
            });
            columnaNodo.setCursor(Cursor.HAND); // Cambiar el cursor a mano al pasar por encima
        }
    }

    /*
     *   Ventana de confirmación para salir. Si acepta, cierra la aplicación.
     */
    private void confirmarSalida() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Salir");
        alert.setHeaderText("¿Cerrar la aplicación?");
        alert.setContentText("Se perderá el progreso actual.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Platform.exit(); // Cierra JavaFx
        }
    }

    /*
     *   Dibuja la ranura con borde redondeado y con su etiqueta respectiva
     */
    private void configurarRanura(StackPane contenedor, String titulo) {
        contenedor.setPrefSize(ANCHO_CARTA, ALTO_CARTA);

        // Rectángulo para simular el espacio donde se coloca la carta
        Rectangle ranura = new Rectangle(ANCHO_CARTA, ALTO_CARTA);
        ranura.setArcWidth(12); // Borde rondeado
        ranura.setArcHeight(12);
        ranura.setFill(Color.color(1, 1, 1, 0.12)); // Relleno
        ranura.setStroke(Color.color(1, 1, 1, 0.35)); // Color del contorno
        ranura.setStrokeWidth(1.5); // Grosor del borde
        contenedor.getChildren().add(ranura); // Agrega el rectángulo al stackPane
        // Etiqueta en la esquina superior izquierda
        Label lbl = new Label(titulo);
        lbl.setTextFill(Color.color(1, 1, 1, 0.85));
        lbl.setStyle("-fx-font-size: 11px;");
        StackPane.setAlignment(lbl, Pos.TOP_LEFT);
        StackPane.setMargin(lbl, new Insets(4, 0, 0, 6));
        contenedor.getChildren().add(lbl);

        contenedor.setCursor(Cursor.HAND); // Se ve clickeable
    }

    // Redibuja toda la UI
    private void refrescar() {
        dibujarMazo();
        dibujarDescarte();
        actualizarBotonReciclar();
        dibujarTablero();
        dibujarFundaciones();
    }

    // Dibuja las 4 fundaciones
    private void dibujarFundaciones() {
        // Ciclo for para iterar cada fundación
        for (int i = 0; i < fundacionesPanes.length; i++){
            StackPane pane = fundacionesPanes[i];
            // Elimina cualquier carta dejando la etiqueta
            pane.getChildren().removeIf(n -> n.getUserData() != null);
            // Se obtiene la fundation en base al índice
            var f = game.getFoundation(i);
            if (f == null) continue;

            // Se toma la carta de arriba si existe
            CartaInglesa topCarta = f.getUltimaCarta();
            if (topCarta != null) {
                StackPane vista = CartaGrafica.crear(topCarta, false, ANCHO_CARTA, ALTO_CARTA);
                /*
                 *   setUserData() funciona como una etiqueta oculta
                 *   evita usar estructuras extra cuando solo se requiere
                 *   un dato mínimo
                 */
                vista.setUserData("card");
                pane.getChildren().add(vista);
            }
        }
    }

    /*
     * Dibuja las 7 columnas del tablero. Cada carta se agrega
     * como un StackPane con un desplazamiento en Y.
     * La carta superior es clickeable.
     */
    private void dibujarTablero() {
        // El TableroGrafico dibuja cada columna y regresa la vista gráfica de la carta superior
        StackPane[] vistasSuperiores = tableroGrafico.dibujar(game.getTableau());

        // Para cada columna habilita el Hover y el click event
        for (int i = 0; i < vistasSuperiores.length; i++) {
            final int idxColTabla = i + 1; // Ajuste del índice, 1 - 7
            StackPane vista = vistasSuperiores[i];
            if (vista == null) continue; // nada que clickear si no hay carta superior descubierta

            vista.setCursor(Cursor.HAND); // Símbolo de mano al pasar mouse por encima

            // HOVER blanco
            vista.setOnMouseEntered(ev -> {
                if (vistaSeleccionada != vista) vista.setEffect(EFECTO_HOVER);
            });
            vista.setOnMouseExited(ev -> {
                if (vistaSeleccionada != vista) vista.setEffect(null);
            });

            // Evento (click)
            vista.setOnMouseClicked(e -> {
                if (e.getButton() != MouseButton.PRIMARY) return;
                // Al seleccionar origen, brillo celeste
                if (!haySeleccion) {
                    vistaSeleccionada = vista;
                    vista.setEffect(EFECTO_SELECCION);
                }
                clickColumna(idxColTabla);
                e.consume(); // Marca el evento como consumido
            });
        }
    }

    /*
     * Se encarga de habilitar y deshabilitar el botón para
     * reciclar el mazo, solo se habilita si draw está vacío,
     * y waste tiene cartas.
     */
    private void actualizarBotonReciclar() {
        boolean mazoVacio = !game.getDrawPile().hayCartas();
        boolean hayEnDescarte = game.getWastePile().hayCartas();
        btnReciclar.setDisable(!(mazoVacio && hayEnDescarte));
    }

    /*
     *   Dibuja el mazo, si hay cartas en draw muestra una carta
     */
    private void dibujarMazo() {
        // Elimina cualquier carta dibujada
        mazoPane.getChildren().removeIf(n -> n.getUserData() != null);
        // Si hay cartas en el mazo
        if (game.getDrawPile().hayCartas()) {
            StackPane carta = CartaGrafica.crear(null, true, ANCHO_CARTA, ALTO_CARTA);// Crea una carta volteada
            carta.setUserData("card");
            mazoPane.getChildren().add(carta);
        }
    }

    // Dibuja en la zona descarte la carta superior del waste
    private void dibujarDescarte() {
        // Limpia las cartas
        descartePane.getChildren().removeIf(n -> n.getUserData() != null);
        // Si hay cartas en el waste la muestra boca arriba
        if (game.getWastePile().hayCartas()) {
            // Recibe la carta superior
            CartaInglesa topCarta = game.getWastePile().verCarta();
            StackPane carta = CartaGrafica.crear(topCarta, false, ANCHO_CARTA, ALTO_CARTA);
            carta.setUserData("card");

            // Seleccionar WASTE como origen sino había otro
            carta.setOnMouseClicked(e -> {
                if (e.getButton() != MouseButton.PRIMARY) return;
                if (haySeleccion) return;// Si ya hay una selección no mueve
                // Brillo celeste al seleccionar el descarte
                if (!haySeleccion) {
                    vistaSeleccionada = carta;
                    carta.setEffect(EFECTO_SELECCION);
                }
                haySeleccion = true;
                origenSeleccion = "WASTE";// Determina origen waste
                columnaSeleccionada = -1;// -1 ya que las columnas no son el destino
            });
            // HOVER blanco en la carta del descarte
            carta.setOnMouseEntered(ev -> {
                if (vistaSeleccionada != carta) carta.setEffect(EFECTO_HOVER);
            });
            carta.setOnMouseExited(ev -> {
                if (vistaSeleccionada != carta) carta.setEffect(null);
            });
            descartePane.getChildren().add(carta);
        }
    }

    // Limpia las selecciones, origen y destino, resetea la selección de columna
    private void limpiarSeleccion() {
        haySeleccion = false;
        origenSeleccion = "";
        columnaSeleccionada = -1;
        // Si no hay seleccionado quita el brillo
        if (vistaSeleccionada != null){
            vistaSeleccionada.setEffect(null);
            vistaSeleccionada = null;
        }
    }

    // Se encarga de la selección/movimiento entre columnas o desde waste, recibe el índice
    private void clickColumna(int idxCol) {
        // Si no hay nada seleccionado se determina que tablero es el origen
        if (!haySeleccion) {
            haySeleccion = true;
            origenSeleccion = "TABLEAU";
            columnaSeleccionada = idxCol;
        } else {
            // Si hay algo previo seleccionado
            // Bandera de movimiento
            boolean movido = false;
            if ("WASTE".equals(origenSeleccion)) {// Si el origen es Waste
                movido = game.moveWasteToTableau(idxCol);// Función de colocación waste a tablero
            } else if ("TABLEAU".equals(origenSeleccion)) {// Si el origen es Tablero
                if (columnaSeleccionada != idxCol) {// Se evalúa que la columna no sea la misma
                    movido = game.moveTableauToTableau(columnaSeleccionada, idxCol);// Función de movimiento columna - columna
                } else {
                    limpiarSeleccion();
                    return;
                }
            }
            limpiarSeleccion();
            if (movido) refrescar();
        }
    }
}
