package com.example.hotelmanagement.view.controllers;

import com.example.hotelmanagement.models.Guest;
import com.example.hotelmanagement.models.Reservation;
import com.example.hotelmanagement.models.ReservationStatus;
import com.example.hotelmanagement.models.Room;
import com.example.hotelmanagement.repository.ReservationRepository;
import com.example.hotelmanagement.repository.impl.ReservationRepositoryImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.*;
import java.util.Locale;
import java.util.stream.Collectors;

public class ServicesController {

    // --- Componentes da Aba "Gerenciar Serviços" ---
    @FXML private TableView<Service> tabelaServices;
    @FXML private TableColumn<Service, String> colunaNome;
    @FXML private TableColumn<Service, Double> colunaPreco;

    // --- Componentes da Aba "Lançar Consumo" ---
    @FXML private ComboBox<String> quartoComboBox;
    @FXML private Label hospedeLabel;
    @FXML private ComboBox<Service> servicoComboBox;
    @FXML private Spinner<Integer> quantidadeSpinner;
    @FXML private Label valorTotalLabel;
    @FXML private Button btnLancar;

    // Novos componentes para exibir lançamentos e total
    @FXML private ListView<String> servicosLancadosListView;
    @FXML private Label totalAPagarLabel;

    // --- Lista de Dados e Formatação ---
    private final ObservableList<Service> listaDeServicos = FXCollections.observableArrayList();
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    // Mapa para guardar lançamentos por quarto
    private final Map<String, List<LancamentoServico>> servicosPorQuarto = new HashMap<>();

    @FXML
    public void initialize() {
        configurarTabela();
        carregarDadosIniciais();
        configurarLancamento();
    }

    private void configurarTabela() {
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
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

    @FXML
    private void handleVoltar(javafx.event.ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/DashboardForm.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Dashboard Principal");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void carregarDadosIniciais() {
        listaDeServicos.add(new Service("Café da Manhã", "Categoria 1", 30.00));
        listaDeServicos.add(new Service("Jantar", "Categoria 2", 20.00));
        listaDeServicos.add(new Service("Almoço", "Categoria 3", 20.00));
        listaDeServicos.add(new Service("Serviço de Quarto", "Categoria 4", 40.00));
    }

    private void configurarLancamento() {
        atualizarQuartosComCheckin();
        servicoComboBox.setItems(listaDeServicos);

        quartoComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            hospedeLabel.setText("Hóspede: " + getHospedePeloQuarto(newV));
            atualizarListaServicosLancados(newV);
        });

        servicoComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> atualizarValorTotal());
        quantidadeSpinner.valueProperty().addListener((obs, oldV, newV) -> atualizarValorTotal());

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
        quantidadeSpinner.setValueFactory(valueFactory);
    }

    // Atualiza o ComboBox com quartos que estão com check-in realizado
    private void atualizarQuartosComCheckin() {
        ReservationRepository reservationRepo = ReservationRepositoryImpl.getInstance();
        List<Reservation> checkedInReservations = reservationRepo.findByStatus(ReservationStatus.CHECKED_IN);

        List<String> quartosOcupados = checkedInReservations.stream()
                .map(reserva -> {
                    Room room = reserva.getRoom();
                    return room != null ? room.toString() : null;
                })
                .filter(q -> q != null)
                .distinct()
                .collect(Collectors.toList());

        quartoComboBox.setItems(FXCollections.observableArrayList(quartosOcupados));
    }

    private void atualizarValorTotal() {
        Service servico = servicoComboBox.getValue();
        int quantidade = quantidadeSpinner.getValue();
        double total = (servico != null && quantidade > 0) ? servico.getPreco() * quantidade : 0;
        valorTotalLabel.setText(currencyFormat.format(total));
    }

    @FXML
    private void handleLancarConsumo() {
        String quarto = quartoComboBox.getValue();
        Service servico = servicoComboBox.getValue();
        int quantidade = quantidadeSpinner.getValue();

        if (quarto == null || servico == null) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Selecione um quarto e um serviço para continuar.");
            return;
        }

        // Adiciona ao manager global E ao mapa local
        LancamentoServico lancamento = new LancamentoServico(servico, quantidade);
        ServicoManager.adicionarServico(quarto, lancamento);

        servicosPorQuarto.putIfAbsent(quarto, new ArrayList<>());
        servicosPorQuarto.get(quarto).add(lancamento);

        atualizarListaServicosLancados(quarto);

        String msg = String.format("Serviço '%s' (x%d) lançado com sucesso na conta do quarto %s.",
                servico.getNome(), quantidade, quarto);
        showAlert(Alert.AlertType.INFORMATION, "Sucesso!", msg);
    }

    private void atualizarListaServicosLancados(String quarto) {
        List<LancamentoServico> lancamentos = servicosPorQuarto.getOrDefault(quarto, List.of());
        if (servicosLancadosListView != null) {
            servicosLancadosListView.setItems(FXCollections.observableArrayList(
                    lancamentos.stream().map(LancamentoServico::toString).toList()
            ));
        }
        double total = lancamentos.stream().mapToDouble(LancamentoServico::getTotal).sum();
        if (totalAPagarLabel != null) {
            totalAPagarLabel.setText("Total a pagar: " + currencyFormat.format(total));
        }
    }

    private String getHospedePeloQuarto(String quarto) {
        if (quarto == null || quarto.isEmpty()) return "-";

        try {
            ReservationRepository reservationRepo = ReservationRepositoryImpl.getInstance();
            List<Reservation> checkedInReservations = reservationRepo.findByStatus(ReservationStatus.CHECKED_IN);

            // Busca a reserva do quarto específico
            Optional<Reservation> reservaDoQuarto = checkedInReservations.stream()
                    .filter(reserva -> {
                        Room room = reserva.getRoom();
                        return room != null && quarto.equals(room.toString());
                    })
                    .findFirst();

            if (reservaDoQuarto.isPresent()) {
                Guest guest = reservaDoQuarto.get().getPrincipalGuest();
                if (guest != null && guest.getName() != null && !guest.getName().trim().isEmpty()) {
                    return guest.getName();
                }
            }

            return "Hóspede não encontrado";
        } catch (Exception e) {
            System.err.println("Erro ao buscar hóspede: " + e.getMessage());
            return "Erro ao carregar";
        }
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

    // Classe para representar um lançamento de serviço
    public static class LancamentoServico {
        private final Service servico;
        private final int quantidade;

        public LancamentoServico(Service servico, int quantidade) {
            this.servico = servico;
            this.quantidade = quantidade;
        }

        public double getTotal() {
            return servico.getPreco() * quantidade;
        }

        @Override
        public String toString() {
            return servico.getNome() + " x" + quantidade + " = " +
                    NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(getTotal());
        }
    }

    // Classe estática para compartilhar dados entre controllers
    public static class ServicoManager {
        private static final Map<String, List<LancamentoServico>> servicosPorQuarto = new HashMap<>();

        public static void adicionarServico(String quarto, LancamentoServico servico) {
            servicosPorQuarto.putIfAbsent(quarto, new ArrayList<>());
            servicosPorQuarto.get(quarto).add(servico);
        }

        public static List<LancamentoServico> getServicosPorQuarto(String quarto) {
            return servicosPorQuarto.getOrDefault(quarto, new ArrayList<>());
        }

        public static double getTotalServicosPorQuarto(String quarto) {
            return getServicosPorQuarto(quarto).stream()
                    .mapToDouble(LancamentoServico::getTotal)
                    .sum();
        }

        public static void limparServicosPorQuarto(String quarto) {
            servicosPorQuarto.remove(quarto);
        }
    }
}