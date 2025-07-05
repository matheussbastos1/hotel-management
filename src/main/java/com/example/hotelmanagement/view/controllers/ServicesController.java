package com.example.hotelmanagement.view.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.text.NumberFormat;
import java.util.Locale;

public class ServicesController {

    // --- Componentes da Aba "Gerenciar Serviços" ---
    @FXML private TableView<Service> tabelaServices;
    @FXML private TableColumn<Service, String> colunaNome;
    @FXML private TableColumn<Service, String> colunaCategoria;
    @FXML private TableColumn<Service, Double> colunaPreco;

    // --- Componentes da Aba "Lançar Consumo" ---
    @FXML private ComboBox<String> quartoComboBox;
    @FXML private Label hospedeLabel;
    @FXML private ComboBox<Service> servicoComboBox;
    @FXML private Spinner<Integer> quantidadeSpinner;
    @FXML private Label valorTotalLabel;
    @FXML private Button btnLancar;

    // --- Lista de Dados e Formatação ---
    private final ObservableList<Service> listaDeServicos = FXCollections.observableArrayList();
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    @FXML
    public void initialize() {
        configurarTabela();
        carregarDadosIniciais();
        configurarLancamento();
    }

    private void configurarTabela() {
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colunaPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));

        colunaPreco.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                setText(empty ? null : currencyFormat.format(price));
            }
        });

        tabelaServices.setItems(listaDeServicos);
    }

    private void carregarDadosIniciais() {
        listaDeServicos.add(new Service("Serviço 1", "Categoria 1", 10.00));
        listaDeServicos.add(new Service("Serviço 2", "Categoria 2", 20.00));
        listaDeServicos.add(new Service("Serviço 3", "Categoria 3", 30.00));
        listaDeServicos.add(new Service("Serviço 4", "Categoria 4", 40.00));
    }

    private void configurarLancamento() {
        quartoComboBox.setItems(FXCollections.observableArrayList("Quarto 1", "Quarto 2", "Quarto 3", "Quarto 4"));
        servicoComboBox.setItems(listaDeServicos);

        quartoComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) ->
                hospedeLabel.setText("Hóspede: " + getHospedePeloQuarto(newV))
        );

        servicoComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> atualizarValorTotal());
        quantidadeSpinner.valueProperty().addListener((obs, oldV, newV) -> atualizarValorTotal());

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
        quantidadeSpinner.setValueFactory(valueFactory);
    }

    private void atualizarValorTotal() {
        Service servico = servicoComboBox.getValue();
        int quantidade = quantidadeSpinner.getValue();
        double total = (servico != null && quantidade > 0) ? servico.getPreco() * quantidade : 0;
        valorTotalLabel.setText(currencyFormat.format(total));
    }

    @FXML
    private void handleLancarConsumo() {
        if (quartoComboBox.getValue() == null || servicoComboBox.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Selecione um quarto e um serviço para continuar.");
            return;
        }
        String msg = String.format("Serviço '%s' (x%d) lançado com sucesso na conta do quarto %s.",
                servicoComboBox.getValue().getNome(),
                quantidadeSpinner.getValue(),
                quartoComboBox.getValue());
        showAlert(Alert.AlertType.INFORMATION, "Sucesso!", msg);
    }

    private String getHospedePeloQuarto(String quarto) {
        if (quarto == null || quarto.isEmpty()) return "-";
        return "Nome do hóspede";
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Classe interna para representar o serviço
    public static class Service {
        private String nome;
        private String categoria;
        private double preco;

        public Service(String nome, String categoria, double preco) {
            this.nome = nome;
            this.categoria = categoria;
            this.preco = preco;
        }

        public String getNome() { return nome; }
        public String getCategoria() { return categoria; }
        public double getPreco() { return preco; }

        @Override
        public String toString() { return nome; }
    }
}