package microsoftAI.KrishiMitra.krishiApp.controller;

import microsoftAI.KrishiMitra.krishiApp.dto.CropDiagnosisResultDTO;
import microsoftAI.KrishiMitra.krishiApp.service.CropAiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/crops")
@CrossOrigin(origins = "http://localhost:3000")
public class CropAiController {

    private final CropAiService cropAiService;

    public CropAiController(CropAiService cropAiService) {
        this.cropAiService = cropAiService;
    }

    /**
     * POST /api/v1/crops/diagnose
     * * Make sure frontend sends this as 'multipart/form-data'
     */
    @PostMapping(value = "/diagnose", consumes = "multipart/form-data")
    public ResponseEntity<CropDiagnosisResultDTO> diagnoseCropImage(
            @RequestParam("userId") Long userId,
            @RequestParam("file") MultipartFile file) {

        CropDiagnosisResultDTO result = cropAiService.processCropImage(userId, file);
        return ResponseEntity.ok(result);
    }
}