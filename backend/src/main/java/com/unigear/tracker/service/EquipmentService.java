package com.unigear.tracker.service;

import com.unigear.tracker.dto.CreateEquipmentDto;
import com.unigear.tracker.dto.EquipmentDto;
import com.unigear.tracker.entity.Equipment;
import com.unigear.tracker.entity.User;
import com.unigear.tracker.repository.EquipmentRepository;
import com.unigear.tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipmentService {

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private UserRepository userRepository;

    public List<EquipmentDto> getAllEquipment(String category, String search) {
        List<Equipment> equipment;

        if (search != null && !search.isBlank()) {
            equipment = equipmentRepository.findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCaseOrderByNameAsc(search, search);
        } else if (category != null && !category.isBlank() && !"all".equalsIgnoreCase(category)) {
            equipment = equipmentRepository.findByCategoryIgnoreCaseOrderByNameAsc(category);
        } else {
            equipment = equipmentRepository.findAllByOrderByNameAsc();
        }

        return equipment.stream()
                .sorted(Comparator.comparing(Equipment::getName, String.CASE_INSENSITIVE_ORDER))
                .map(EquipmentDto::fromEntity)
                .collect(Collectors.toList());
    }

    public EquipmentDto getEquipmentById(Long id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipment not found"));

        return EquipmentDto.fromEntity(equipment);
    }

    @Transactional
    public EquipmentDto createEquipment(String userEmail, CreateEquipmentDto dto) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != User.Role.ADMIN) {
            throw new SecurityException("Only admins can add equipment");
        }

        if (equipmentRepository.findByNameIgnoreCase(dto.getName()).isPresent()) {
            throw new RuntimeException("Equipment with this name already exists");
        }

        if (dto.getAvailableQuantity() > dto.getTotalQuantity()) {
            throw new RuntimeException("Available quantity cannot exceed total quantity");
        }

        Equipment equipment = new Equipment();
        equipment.setName(dto.getName().trim());
        equipment.setCategory(dto.getCategory().trim());
        equipment.setLocation(dto.getLocation().trim());
        equipment.setDescription(dto.getDescription().trim());
        equipment.setSpecifications(String.join("\n", dto.getSpecifications()));
        equipment.setTotalQuantity(dto.getTotalQuantity());
        equipment.setAvailableQuantity(dto.getAvailableQuantity());

        Equipment.EquipmentStatus status = resolveStatus(dto.getStatus(), dto.getAvailableQuantity());
        equipment.setStatus(status);

        Equipment saved = equipmentRepository.save(equipment);
        return EquipmentDto.fromEntity(saved);
    }

    private Equipment.EquipmentStatus resolveStatus(String statusText, Integer availableQuantity) {
        if (statusText != null && !statusText.isBlank()) {
            try {
                return Equipment.EquipmentStatus.valueOf(statusText.trim().toUpperCase());
            } catch (IllegalArgumentException ignored) {
                // Fall back to quantity-based default below.
            }
        }

        if (availableQuantity != null && availableQuantity > 0) {
            return Equipment.EquipmentStatus.AVAILABLE;
        }
        return Equipment.EquipmentStatus.IN_USE;
    }
}
