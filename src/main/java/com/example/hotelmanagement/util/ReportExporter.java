package com.example.hotelmanagement.util;

import com.example.hotelmanagement.dto.ReservationDetailsDTO;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReportExporter {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static void exportToCSV(List<ReservationDetailsDTO> reservations, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            // Cabeçalho CSV
            writer.append("ID Reserva,Nome Hóspede,Email,Telefone,Número Quarto,Tipo Quarto,Check-in,Check-out,Status,Valor Total,Pago,Método Pagamento,Status Pagamento\n");

            // Dados
            for (ReservationDetailsDTO dto : reservations) {
                writer.append(String.valueOf(dto.getReservationId())).append(",")
                        .append(escapeCSV(dto.getGuestName())).append(",")
                        .append(escapeCSV(dto.getGuestEmail())).append(",")
                        .append(escapeCSV(dto.getGuestPhone())).append(",")
                        .append(String.valueOf(dto.getRoomNumber())).append(",")
                        .append(dto.getRoomType() != null ? dto.getRoomType().name() : "").append(",")
                        .append(dto.getCheckInDate() != null ? dto.getCheckInDate().format(DATE_FORMATTER) : "").append(",")
                        .append(dto.getCheckOutDate() != null ? dto.getCheckOutDate().format(DATE_FORMATTER) : "").append(",")
                        .append(dto.getReservationStatus() != null ? dto.getReservationStatus().name() : "").append(",")
                        .append(dto.getTotalAmount() != null ? dto.getTotalAmount().toString() : "0.00").append(",")
                        .append(dto.isPaid() ? "Sim" : "Não").append(",")
                        .append(escapeCSV(dto.getPaymentMethod())).append(",")
                        .append(dto.getPaymentStatus() != null ? dto.getPaymentStatus().name() : "")
                        .append("\n");
            }
        }
    }

    public static void exportToPDF(List<ReservationDetailsDTO> reservations, String filePath) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, new FileOutputStream(filePath));

        document.open();

        // Título
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Paragraph title = new Paragraph("Relatório de Reservas", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // Data de geração
        Font dateFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        Paragraph dateP = new Paragraph("Gerado em: " + java.time.LocalDateTime.now().format(DATETIME_FORMATTER), dateFont);
        dateP.setAlignment(Element.ALIGN_RIGHT);
        dateP.setSpacingAfter(20);
        document.add(dateP);

        // Tabela
        PdfPTable table = new PdfPTable(8); // 8 colunas principais
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        // Larguras das colunas
        float[] columnWidths = {1f, 2f, 1.5f, 1.5f, 1.5f, 1.5f, 1f, 1.5f};
        table.setWidths(columnWidths);

        // Cabeçalhos
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        addTableHeader(table, "ID", headerFont);
        addTableHeader(table, "Hóspede", headerFont);
        addTableHeader(table, "Quarto", headerFont);
        addTableHeader(table, "Check-in", headerFont);
        addTableHeader(table, "Check-out", headerFont);
        addTableHeader(table, "Status", headerFont);
        addTableHeader(table, "Valor", headerFont);
        addTableHeader(table, "Pagamento", headerFont);

        // Dados
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
        for (ReservationDetailsDTO dto : reservations) {
            addTableCell(table, String.valueOf(dto.getReservationId()), cellFont);
            addTableCell(table, dto.getGuestName(), cellFont);
            addTableCell(table, "Quarto " + dto.getRoomNumber() + "\n" +
                    (dto.getRoomType() != null ? dto.getRoomType().name() : ""), cellFont);
            addTableCell(table, dto.getCheckInDate() != null ? dto.getCheckInDate().format(DATE_FORMATTER) : "", cellFont);
            addTableCell(table, dto.getCheckOutDate() != null ? dto.getCheckOutDate().format(DATE_FORMATTER) : "", cellFont);
            addTableCell(table, dto.getReservationStatus() != null ? dto.getReservationStatus().name() : "", cellFont);
            addTableCell(table, "R$ " + (dto.getTotalAmount() != null ? dto.getTotalAmount().toString() : "0,00"), cellFont);
            addTableCell(table, (dto.isPaid() ? "Pago" : "Pendente") + "\n" +
                    (dto.getPaymentMethod() != null ? dto.getPaymentMethod() : ""), cellFont);
        }

        document.add(table);

        // Resumo
        if (!reservations.isEmpty()) {
            Paragraph summary = new Paragraph("\nResumo:", headerFont);
            summary.setSpacingBefore(20);
            document.add(summary);

            long totalReservations = reservations.size();
            long paidReservations = reservations.stream().mapToLong(r -> r.isPaid() ? 1 : 0).sum();

            Paragraph stats = new Paragraph(
                    String.format("Total de reservas: %d\nReservas pagas: %d\nReservas pendentes: %d",
                            totalReservations, paidReservations, totalReservations - paidReservations),
                    cellFont);
            document.add(stats);
        }

        document.close();
    }

    private static void addTableHeader(PdfPTable table, String headerTitle, Font font) {
        PdfPCell header = new PdfPCell();
        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
        header.setBorderWidth(1);
        header.setPhrase(new Phrase(headerTitle, font));
        header.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(header);
    }

    private static void addTableCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell();
        cell.setPhrase(new Phrase(text != null ? text : "", font));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5);
        table.addCell(cell);
    }

    private static String escapeCSV(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}