package microsoftAI.KrishiMitra.krishiApp.controller;

import microsoftAI.KrishiMitra.krishiApp.dto.FarmRiskProfileDTO;
import microsoftAI.KrishiMitra.krishiApp.dto.RiskAnalysisRequestDTO;
import microsoftAI.KrishiMitra.krishiApp.service.implementation.RiskServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/risk")
@CrossOrigin(origins = "http://localhost:3000")
public class RiskController {

    private final RiskServiceImpl riskService;

    public RiskController(RiskServiceImpl riskService) {
        this.riskService = riskService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<FarmRiskProfileDTO> analyzeFarmRisk(@RequestBody RiskAnalysisRequestDTO request) {
        FarmRiskProfileDTO profile = riskService.generateRiskProfile(request);
        return ResponseEntity.ok(profile);
    }
}