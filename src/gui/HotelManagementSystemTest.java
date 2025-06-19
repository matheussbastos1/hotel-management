package gui;

import repository.*;
import repository.impl.*;
import service.exceptions.*;
import service.models.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class HotelManagementSystemTest {

    // Repositórios
    private static final RoomRepository roomRepository = new RoomRepositoryImpl();
    private static final GuestRepository guestRepository = new GuestRepositoryImpl();
    private static final ReservationRepository reservationRepository = new ReservationRepositoryImpl();
    private static final StayRepository stayRepository = new StayRepositoryImpl();
    private static final InvoiceRepository invoiceRepository = new InvoiceRepositoryImpl();
    private static final PaymentRepository paymentRepository = new PaymentRepositoryImpl();

    // Contadores para gerar IDs únicos
    private static long guestIdCounter = 1L;
    private static long reservationIdCounter = 1L;
    private static long stayIdCounter = 1L;
    private static int invoiceIdCounter = 1;
    private static int paymentIdCounter = 1;

    public static void main(String[] args) throws Exception {
        System.out.println("--- INICIANDO TESTES DO SISTEMA DE GERENCIAMENTO DE HOTEL ---");

        testRoomManagement();
        testGuestManagement();
        testReservationManagement();
        testStayManagement();
        testBillingManagement();

        System.out.println("\n--- FIM DOS TESTES ---");
    }

    private static void testRoomManagement() throws RoomNotFoundException {
        System.out.println("\n--- MÓDULO DE GERENCIAMENTO DE QUARTOS ---");
        System.out.println("\n1. Adicionando quartos...");
        roomRepository.addRoom(new Room(101, RoomType.COUPLE, 250.0, RoomStatus.AVAILABLE, 2, "Queen"));
        roomRepository.addRoom(new Room(102, RoomType.INDIVIDUAL, 150.0, RoomStatus.AVAILABLE, 1, "Single"));
        System.out.println("Quartos adicionados com sucesso.");
    }

    private static void testGuestManagement() throws GuestNotFoundException {
        System.out.println("\n--- MÓDULO DE GERENCIAMENTO DE HÓSPEDES ---");
        System.out.println("\n1. Adicionando hóspedes...");
        guestRepository.addGuest(new Guest(guestIdCounter++, "Kelven Alvess", "kelven.a@test.com", "111-2222", "Rua A, 123"));
        System.out.println("Hóspedes adicionados com sucesso.");
    }

    private static void testReservationManagement() throws Exception {
        System.out.println("\n--- MÓDULO DE GERENCIAMENTO DE RESERVAS ---");
        System.out.println("\n1. Preparando para criar reserva...");
        Room roomForReservation = roomRepository.findRoomByNumber(102);
        Guest guestForReservation = guestRepository.findGuestByEmail("kelven.a@test.com");

        System.out.println("\n2. Criando uma nova reserva...");
        Reservation res1 = new Reservation(reservationIdCounter++, guestForReservation, roomForReservation, LocalDate.now(), LocalDate.now().plusDays(3), ReservationStatus.BOOKED);
        reservationRepository.addReservation(res1);
        roomForReservation.setStatus(RoomStatus.BOOKED);
        roomRepository.updateRoom(roomForReservation);
        System.out.println("Reserva ID " + res1.getReservationId() + " criada.");

        System.out.println("\n3. Buscando reserva ID " + res1.getReservationId() + "...");
        Reservation foundReservation = reservationRepository.findReservationById(Math.toIntExact(res1.getReservationId()));
        System.out.println("Encontrada: Reserva para " + foundReservation.getGuest().getGuestName());
    }

    private static void testStayManagement() throws StayNotFoundException, ReservationNotFoundException {
        System.out.println("\n--- MÓDULO DE GERENCIAMENTO DE ESTADIAS ---");
        System.out.println("\n1. Buscando reserva para criar uma estadia...");
        Reservation reservation = reservationRepository.findReservationById(Math.toIntExact(1L));
        System.out.println("Reserva ID " + reservation.getReservationId() + " encontrada.");

        System.out.println("\n2. Criando uma nova estadia (Check-in)...");
        Stay stay = new Stay(stayIdCounter++, reservation, "CHECKED_IN");
        stayRepository.addStay(stay);
        System.out.println("Estadia ID " + stay.getStayId() + " criada para o hóspede " + stay.getGuest().getGuestName());

        System.out.println("\n3. Atualizando status da estadia para CHECKED_OUT...");
        stay.setStatus("CHECKED_OUT");
        stayRepository.updateStay(stay);
        Stay updatedStay = stayRepository.findStayById(Math.toIntExact(stay.getStayId()));
        System.out.println("Status da estadia ID " + updatedStay.getStayId() + " atualizado para: " + updatedStay.getStatus());
    }

    private static void testBillingManagement() throws Exception {
        System.out.println("\n--- MÓDULO DE FATURAMENTO E PAGAMENTOS ---");
        System.out.println("\n1. Buscando estadia para gerar fatura...");
        Stay stay = stayRepository.findStayById(Math.toIntExact(1L));
        System.out.println("Estadia ID " + stay.getStayId() + " encontrada.");

        System.out.println("\n2. Criando uma fatura para a estadia...");
        Map<String, BigDecimal> charges = new HashMap<>();
        charges.put("Diárias", new BigDecimal("450.00"));
        charges.put("Serviço de Quarto", new BigDecimal("50.00"));
        Invoice invoice = new Invoice(invoiceIdCounter++, stay, charges);
        invoiceRepository.addInvoice(invoice);
        System.out.println("Fatura ID " + invoice.getInvoiceId() + " criada com valor total de R$" + invoice.getAmountSpent());

        System.out.println("\n3. Processando um pagamento para a fatura...");
        Payment payment = new Payment(paymentIdCounter++, invoice, invoice.getAmountSpent(), PaymentMethod.CREDIT_CARD);
        paymentRepository.addPayment(payment);
        System.out.println("Pagamento ID " + payment.getPaymentId() + " criado com status " + payment.getPaymentStatus());

        System.out.println("\n4. Atualizando status do pagamento para COMPLETED...");
        payment.setPaymentStatus(PaymentStatus.COMPLETED);
        paymentRepository.updatePayment(payment);
        System.out.println("Status do pagamento ID " + payment.getPaymentId() + " atualizado para " + payment.getPaymentStatus());

        System.out.println("\n5. Atualizando status da fatura para PAGA...");
        if (payment.getPaymentStatus() == PaymentStatus.COMPLETED) {
            invoice.setPaid(true);
            invoiceRepository.updateInvoice(invoice);
        }
        Invoice updatedInvoice = invoiceRepository.findInvoiceById(Math.toIntExact(invoice.getInvoiceId()));
        System.out.println("Status da fatura ID " + updatedInvoice.getInvoiceId() + " atualizado para Paga: " + updatedInvoice.isPaid());
    }
}