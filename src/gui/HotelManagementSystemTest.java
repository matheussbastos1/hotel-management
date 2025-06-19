package gui;

import repository.*;
import repository.impl.*;
import service.exceptions.*;
import service.models.*;

import java.time.LocalDate;
import java.util.List;

public class HotelManagementSystemTest {

    // Contadores para gerar IDs únicos durante os testes
    private static int guestIdCounter = 1;
    private static int reservationIdCounter = 1;

    public static void main(String[] args) throws Exception {
        // 1. Inicialização dos repositórios
        RoomRepository roomRepository = new RoomRepositoryImpl();
        GuestRepository guestRepository = new GuestRepositoryImpl();
        ReservationRepository reservationRepository = new ReservationRepositoryImpl();

        System.out.println("--- INICIANDO TESTES DO SISTEMA DE GERENCIAMENTO DE HOTEL ---");

        // 2. Execução dos testes para cada módulo em ordem de dependência
        testRoomManagement(roomRepository);
        testGuestManagement(guestRepository);
        testReservationManagement(reservationRepository, roomRepository, guestRepository);

        System.out.println("\n--- FIM DOS TESTES ---");
    }

    private static void testRoomManagement(RoomRepository roomRepository) throws RoomNotFoundException {
        System.out.println("\n--- MÓDULO DE GERENCIAMENTO DE QUARTOS ---");

        System.out.println("\n1. Adicionando quartos...");
        roomRepository.addRoom(new Room(101, RoomType.COUPLE, 250.0, Status.AVAILABLE, 2, "Queen"));
        roomRepository.addRoom(new Room(102, RoomType.INDIVIDUAL, 150.0, Status.AVAILABLE, 1, "Single"));
        roomRepository.addRoom(new Room(201, RoomType.FAMILY, 450.0, Status.MAINTENANCE, 5, "King + Twin"));
        roomRepository.addRoom(new Room(202, RoomType.COUPLE, 280.0, Status.AVAILABLE, 2, "King"));
        System.out.println("Quartos adicionados com sucesso.");

        System.out.println("\n2. Listando todos os quartos:");
        roomRepository.getAllRooms().forEach(r -> System.out.println(" - Quarto: " + r.getRoomNumber() + ", Tipo: " + r.getRoomType() + ", Status: " + r.getStatus()));

        System.out.println("\n3. Buscando quarto 101...");
        Room foundRoom = roomRepository.findRoomByNumber(101);
        System.out.println("Encontrado: Quarto " + foundRoom.getRoomNumber());

        System.out.println("\n4. Testando exceção para quarto inexistente (999)...");
        try {
            roomRepository.findRoomByNumber(999);
        } catch (RoomNotFoundException e) {
            System.out.println("SUCESSO: " + e.getMessage());
        }

        System.out.println("\n5. Atualizando status do quarto 101 para BOOKED...");
        foundRoom.setStatus(Status.BOOKED);
        roomRepository.updateRoom(foundRoom);
        System.out.println("Status do quarto 101 atualizado para: " + roomRepository.findRoomByNumber(101).getStatus());

        System.out.println("\n6. Listando quartos disponíveis:");
        List<Room> availableRooms = roomRepository.getAvailableRooms();
        availableRooms.forEach(r -> System.out.println(" - Quarto disponível: " + r.getRoomNumber()));
    }

    private static void testGuestManagement(GuestRepository guestRepository) throws GuestNotFoundException, RoomNotFoundException {
        System.out.println("\n--- MÓDULO DE GERENCIAMENTO DE HÓSPEDES ---");

        System.out.println("\n1. Adicionando hóspedes...");
        Guest guest1 = new Guest(guestIdCounter++, "Kelven Alvess", "kelven.a@test.com", "111-2222", "Rua A, 123");
        Guest guest2 = new Guest(guestIdCounter++, "Jane Doe", "jane.d@test.com", "333-4444", "Rua B, 456");
        guestRepository.addGuest(guest1);
        guestRepository.addGuest(guest2);
        System.out.println("Hóspedes adicionados com sucesso.");

        System.out.println("\n2. Listando todos os hóspedes:");
        guestRepository.getGuests().forEach(g -> System.out.println(" - Hóspede: " + g.getGuestName() + ", Email: " + g.getGuestEmail()));

        System.out.println("\n3. Buscando hóspede 'jane.d@test.com'...");
        Guest foundGuest = guestRepository.findGuestByEmail("jane.d@test.com");
        System.out.println("Encontrado: " + foundGuest.getGuestName());

        System.out.println("\n4. Testando exceção para hóspede inexistente...");
        try {
            guestRepository.findGuestByEmail("no-one@test.com");
        } catch (GuestNotFoundException e) {
            System.out.println("SUCESSO: " + e.getMessage());
        }

        System.out.println("\n5. Atualizando telefone do hóspede 'Kelven Alvess'...");
        Guest guestToUpdate = guestRepository.findGuestByEmail("kelven.a@test.com");
        guestToUpdate.setGuestPhone("999-8888");
        guestRepository.updateGuest(guestToUpdate);
        System.out.println("Telefone atualizado: " + guestRepository.findGuestByEmail("kelven.a@test.com").getGuestPhone());
    }

    private static void testReservationManagement(ReservationRepository reservationRepository, RoomRepository roomRepository, GuestRepository guestRepository) throws Exception {
        System.out.println("\n--- MÓDULO DE GERENCIAMENTO DE RESERVAS ---");

        System.out.println("\n1. Preparando para criar reserva...");
        Room roomForReservation = roomRepository.findRoomByNumber(102);
        Guest guestForReservation = guestRepository.findGuestByEmail("kelven.a@test.com");
        System.out.println("Usando Quarto " + roomForReservation.getRoomNumber() + " e Hóspede " + guestForReservation.getGuestName());

        System.out.println("\n2. Criando uma nova reserva...");
        Reservation res1 = new Reservation(reservationIdCounter++, guestForReservation, roomForReservation, LocalDate.now(), LocalDate.now().plusDays(3), "CONFIRMED");

        reservationRepository.addReservation(res1);
        roomForReservation.setStatus(Status.BOOKED); // Simula a lógica de negócio de ocupar o quarto
        roomRepository.updateRoom(roomForReservation);
        System.out.println("Reserva ID " + res1.getReservationId() + " criada. Status do quarto " + roomForReservation.getRoomNumber() + " atualizado para BOOKED.");

        System.out.println("\n3. Listando todas as reservas:");
        reservationRepository.getAllReservations().forEach(r -> System.out.println(" - ID: " + r.getReservationId() + ", Hóspede: " + r.getGuest().getGuestName()));

        System.out.println("\n4. Buscando reserva ID " + res1.getReservationId() + "...");
        Reservation foundReservation = reservationRepository.findReservationById(res1.getReservationId());
        System.out.println("Encontrada: Reserva para " + foundReservation.getGuest().getGuestName());

        System.out.println("\n5. Testando exceção para reserva inexistente (999)...");
        try {
            reservationRepository.findReservationById(999);
        } catch (ReservationNotFoundException e) {
            System.out.println("SUCESSO: " + e.getMessage());
        }

        System.out.println("\n6. Removendo reserva ID " + res1.getReservationId() + "...");
        reservationRepository.removeReservation(foundReservation);
        roomForReservation.setStatus(Status.AVAILABLE); // Libera o quarto após o cancelamento
        roomRepository.updateRoom(roomForReservation);
        System.out.println("Reserva removida. Status do quarto " + roomForReservation.getRoomNumber() + " atualizado para AVAILABLE.");
        System.out.println("Total de reservas agora: " + reservationRepository.getAllReservations().size());
    }
}