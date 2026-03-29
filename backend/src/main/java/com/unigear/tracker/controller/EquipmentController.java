package com.unigear.tracker.controller;

import com.unigear.tracker.dto.CreateEquipmentDto;
import com.unigear.tracker.dto.EquipmentDto;
import com.unigear.tracker.service.EquipmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment")
@CrossOrigin(origins = "*")
public class EquipmentController {

    @Autowired
    private EquipmentService equipmentService;

    @GetMapping
    public ResponseEntity<?> getAllEquipment(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search) {
        try {
            List<EquipmentDto> items = equipmentService.getAllEquipment(category, search);
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEquipmentById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(equipmentService.getEquipmentById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createEquipment(
            @Valid @RequestBody CreateEquipmentDto dto,
            Authentication authentication) {
        try {
            String email = getUserEmail(authentication);
            EquipmentDto created = equipmentService.createEquipment(email, dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private String getUserEmail(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof com.unigear.tracker.entity.User) {
            return ((com.unigear.tracker.entity.User) principal).getEmail();
        }

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }

        return authentication.getName();
    }
}
