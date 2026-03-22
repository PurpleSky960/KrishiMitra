package microsoftAI.KrishiMitra.krishiApp.controller;

import microsoftAI.KrishiMitra.krishiApp.dto.DashboardSummaryDTO;
import microsoftAI.KrishiMitra.krishiApp.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dashboard")
@CrossOrigin(origins = "http://localhost:3000")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    /**
     * GET /api/v1/dashboard/1
     */
    @GetMapping("/{userId}")
    public ResponseEntity<DashboardSummaryDTO> getDashboardOverview(@PathVariable Long userId) {
        DashboardSummaryDTO summary = dashboardService.getDashboardOverview(userId);
        return ResponseEntity.ok(summary);
    }
}