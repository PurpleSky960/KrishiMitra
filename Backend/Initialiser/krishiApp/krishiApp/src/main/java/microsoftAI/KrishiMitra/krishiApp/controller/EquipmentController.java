package microsoftAI.KrishiMitra.krishiApp.controller;

import microsoftAI.KrishiMitra.krishiApp.dto.AvailableEquipmentDTO;
import microsoftAI.KrishiMitra.krishiApp.dto.BookingConfirmationDTO;
import microsoftAI.KrishiMitra.krishiApp.dto.BookingRequestDTO;
import microsoftAI.KrishiMitra.krishiApp.service.EquipmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/equipment")
@CrossOrigin(origins = "http://localhost:3000")
public class EquipmentController {

    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    /**
     * GET /api/v1/equipment/search?type=Harvester
     */
    @GetMapping("/search")
    public ResponseEntity<List<AvailableEquipmentDTO>> searchEquipment(
            @RequestParam(defaultValue = "Tractor") String type) {

        List<AvailableEquipmentDTO> results = equipmentService.searchAvailableEquipment(type);
        return ResponseEntity.ok(results);
    }

    /**
     * POST /api/v1/equipment/book
     * Expects a JSON body matching BookingRequestDTO
     */
    @PostMapping("/book")
    public ResponseEntity<BookingConfirmationDTO> bookEquipment(@RequestBody BookingRequestDTO request) {

        BookingConfirmationDTO confirmation = equipmentService.bookEquipment(request);
        return ResponseEntity.ok(confirmation);
    }
}