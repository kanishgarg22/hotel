package com.ums.ums.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.ums.ums.entity.Booking;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class PDFService {

    private static final String FILE_DIRECTORY = "E://airbnb-bookings//";
    private static final String FILE_EXTENSION = ".pdf";

    public String generateBookingDetailsPdf(Booking booking) {
        Path filePath = null;
        if (booking == null) {
            throw new IllegalArgumentException("Booking cannot be null");
        }

        try {
            filePath = getFilePath(booking.getId());
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath.toFile()));
            document.open();

            // Define font for the table
            Font tableFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);

            // Create a table with 2 columns
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Define table header
            PdfPCell headerCell = createCell("Booking Details", headerFont, Element.ALIGN_CENTER, BaseColor.LIGHT_GRAY, 2);
            table.addCell(headerCell);

            // Create table cells and add them to the table
            addTableCell(table, "Booking ID", String.valueOf(booking.getId()), tableFont);
            addTableCell(table, "Guest Name", booking.getGuestName(), tableFont);
            addTableCell(table, "Total Nights", String.valueOf(booking.getTotalNights()), tableFont);
            addTableCell(table, "Total Price", String.valueOf(booking.getTotalPrice()), tableFont);
            addTableCell(table, "Booking Date", String.valueOf(booking.getBookingDate()), tableFont);
            addTableCell(table, "Check-In Time", String.valueOf(booking.getCheckInTime()), tableFont);
            addTableCell(table, "Property ID", String.valueOf(booking.getProperty().getId()), tableFont);

            // Check if appUser is not null
            if (booking.getAppUser() != null) {
                addTableCell(table, "User ID", String.valueOf(booking.getAppUser().getId()), tableFont);
            }

            // Add the table to the document
            document.add(table);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
            // Optionally, rethrow the exception as a custom exception
            throw new RuntimeException("Error generating PDF for booking ID: " + booking.getId(), e);
        }
        System.out.println(filePath.toFile());
        return filePath.toString();
    }

    private void addTableCell(PdfPTable table, String header, String value, Font font) {
        table.addCell(createCell(header, font));
        table.addCell(createCell(value, font));
    }

    private PdfPCell createCell(String content, Font font) {
        return createCell(content, font, Element.ALIGN_LEFT, BaseColor.WHITE, 1);
    }

    private PdfPCell createCell(String content, Font font, int alignment, BaseColor bgColor, int colspan) {
        PdfPCell cell = new PdfPCell(new Paragraph(content, font));
        cell.setHorizontalAlignment(alignment);
        cell.setBackgroundColor(bgColor);
        cell.setColspan(colspan);
        return cell;
    }

    private Path getFilePath(long bookingId) throws IOException {
        Path directoryPath = Paths.get(FILE_DIRECTORY);
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }
        return directoryPath.resolve("booking-confirmation" + bookingId + FILE_EXTENSION);
    }
}
