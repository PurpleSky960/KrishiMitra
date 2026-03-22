package microsoftAI.KrishiMitra.krishiApp.controller;

import microsoftAI.KrishiMitra.krishiApp.dto.MarketPriceDTO;
import microsoftAI.KrishiMitra.krishiApp.service.MarketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/market")
@CrossOrigin(origins = "http://localhost:3000") // Letting your frontend teammate's local server in
public class MarketController {

    // Dependency Injection: Spring automatically provides the MarketServiceImpl here
    private final MarketService marketService;

    public MarketController(MarketService marketService) {
        this.marketService = marketService;
    }

    /**
     * GET /api/v1/market/prices?location=Udupi&cropName=Tomato
     * * Test this in your browser! The first load will take 2 seconds (cache miss).
     * Refresh the page, and the second load will be instant (cache hit).
     */
    @GetMapping("/prices")
    public ResponseEntity<List<MarketPriceDTO>> getTodayMarketPrices(
            @RequestParam(defaultValue = "Manipal") String location,
            @RequestParam(defaultValue = "Tomato") String cropName) {

        // Hand off the work to the service layer
        List<MarketPriceDTO> prices = marketService.fetchPricesFromDummyApi(location, cropName);

        return ResponseEntity.ok(prices);
    }
}