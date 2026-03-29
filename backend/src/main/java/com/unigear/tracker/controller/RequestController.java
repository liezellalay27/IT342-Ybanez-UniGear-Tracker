package com.unigear.tracker.controller;

import com.unigear.tracker.dto.CreateRequestDto;
import com.unigear.tracker.dto.EquipmentRequestDto;
import com.unigear.tracker.service.RequestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
@CrossOrigin(origins = "*")
public class RequestController {
    
    @Autowired
    private RequestService requestService;
    
    @PostMapping
    public ResponseEntity<?> createRequest(
            @RequestParam String equipmentName,
            @RequestParam String category,
            @RequestParam(required = false) String description,
            @RequestParam Integer quantity,
            @RequestParam String borrowDate,
            @RequestParam String returnDate,
            @RequestParam String studentName,
            @RequestParam String schoolIdNumber,
            @RequestParam String yearLevel,
            @RequestParam String course,
            @RequestParam(required = false) MultipartFile eventApprovalPdf,
            Authentication authentication) {
        try {
            String email = getUserEmail(authentication);
            EquipmentRequestDto request = requestService.createRequest(
                email, 
                equipmentName, 
                category, 
                description, 
                quantity, 
                borrowDate, 
                returnDate, 
                studentName,
                schoolIdNumber,
                yearLevel,
                course,
                eventApprovalPdf
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(request);
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }
    
    @GetMapping
    public ResponseEntity<?> getUserRequests(Authentication authentication) {
        try {
            String email = getUserEmail(authentication);
            List<EquipmentRequestDto> requests = requestService.getUserRequests(email);
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getRequestById(
            @PathVariable Long id,
            Authentication authentication) {
        try {
            String email = getUserEmail(authentication);
            EquipmentRequestDto request = requestService.getRequestById(id, email);
            return ResponseEntity.ok(request);
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRequest(
            @PathVariable Long id,
            Authentication authentication) {
        try {
            String email = getUserEmail(authentication);
            requestService.deleteRequest(id, email);
            return ResponseEntity.ok("Request deleted successfully");
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<?> downloadPdf(
            @PathVariable Long id,
            Authentication authentication) {
        try {
            String email = getUserEmail(authentication);
            byte[] pdfContent = requestService.getPdfContent(id, email);
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=event_approval.pdf")
                    .header("Content-Type", "application/pdf")
                    .body(pdfContent);
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
    }

    private ResponseEntity<String> buildErrorResponse(Exception e) {
        String message = e.getMessage();
        if (message == null || message.isBlank()) {
            Throwable cause = e.getCause();
            message = cause != null ? cause.getMessage() : null;
        }
        if (message == null || message.isBlank()) {
            message = "Request processing failed";
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }
    
    private String getUserEmail(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof com.unigear.tracker.entity.User) {
            return ((com.unigear.tracker.entity.User) principal).getEmail();
        }

        if (authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        }
        return authentication.getName();
    }
}
