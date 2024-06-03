package com.ums.ums.controller;

import com.ums.ums.entity.AppUser;
import com.ums.ums.entity.Booking;
import com.ums.ums.entity.Property;
import com.ums.ums.repository.BookingRepository;
import com.ums.ums.repository.PropertyRepository;
import com.ums.ums.service.PDFService;
import com.ums.ums.service.TwilioService;
import com.ums.ums.service.BucketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private final BookingRepository bookingRepository;
    private final PropertyRepository propertyRepository;
    private final TwilioService twilioService;
    private final PDFService pdfService;
    private final BucketService bucketService;

    public BookingController(BookingRepository bookingRepository, PropertyRepository propertyRepository,
                             TwilioService twilioService, PDFService pdfService, BucketService bucketService) {
        this.bookingRepository = bookingRepository;
        this.propertyRepository = propertyRepository;
        this.twilioService = twilioService;
        this.pdfService = pdfService;
        this.bucketService = bucketService;
    }

    @PostMapping("/createBooking")
    public ResponseEntity<Booking> createBooking(
            @RequestParam long propertyId,
            @RequestBody Booking booking,
            @AuthenticationPrincipal AppUser user) {

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));
        int nightlyPrice = property.getNightlyPrice();
        int totalPrice = nightlyPrice * booking.getTotalNights();
        booking.setTotalPrice(totalPrice);
        booking.setProperty(property);
        booking.setAppUser(user);
        Booking savedBooking = bookingRepository.save(booking);

        String filePath = pdfService.generateBookingDetailsPdf(savedBooking);
        try {
            MultipartFile fileMultiPart = convertFileToMultipartFile(filePath);
            String fileUploadedUrl = bucketService.uploadFile(fileMultiPart, "myairbnb123");
            System.out.println(fileUploadedUrl);
            sendMessage(fileUploadedUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(savedBooking, HttpStatus.CREATED);
    }

    public void sendMessage(String url) {
        twilioService.sendMessage("+917050859734", "Your booking is confirmed. Click here: " + url);
    }

    @PostMapping("/generatePDF")
    public void generatePDF() {
        // Implementation for generating PDF
    }

    public static MultipartFile convertFileToMultipartFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        File file = path.toFile();
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + filePath);
        }

        String name = file.getName();
        String originalFileName = file.getName();
        String contentType = Files.probeContentType(path);
        byte[] content = Files.readAllBytes(path);

        return new MockMultipartFile(name, originalFileName, contentType, content);
    }

    public static void main(String[] args) {
        try {
            MultipartFile multipartFile = convertFileToMultipartFile("path/to/your/file.txt");
            System.out.println("MultipartFile: " + multipartFile.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
