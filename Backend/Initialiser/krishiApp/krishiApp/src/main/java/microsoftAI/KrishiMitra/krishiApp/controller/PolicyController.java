package microsoftAI.KrishiMitra.krishiApp.controller;

import microsoftAI.KrishiMitra.krishiApp.dto.GovSchemeDTO;
import microsoftAI.KrishiMitra.krishiApp.service.PolicyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/policy")
@CrossOrigin(origins = "http://localhost:3000")
public class PolicyController {

    private final PolicyService policyService;

    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    /**
     * GET /api/v1/policy/eligible/1
     * Fetches all government schemes the specific farmer qualifies for.
     */
    @GetMapping("/eligible/{userId}")
    public ResponseEntity<List<GovSchemeDTO>> getEligiblePolicies(@PathVariable Long userId) {

        List<GovSchemeDTO> eligibleSchemes = policyService.getEligibleSchemesForUser(userId);
        return ResponseEntity.ok(eligibleSchemes);
    }
}