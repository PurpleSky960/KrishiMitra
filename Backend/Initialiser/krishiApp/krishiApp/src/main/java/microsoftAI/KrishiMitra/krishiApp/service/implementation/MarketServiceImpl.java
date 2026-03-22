package microsoftAI.KrishiMitra.krishiApp.service.implementation;

import microsoftAI.KrishiMitra.krishiApp.dto.MarketPriceDTO;
import microsoftAI.KrishiMitra.krishiApp.service.MarketService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarketServiceImpl implements MarketService {

    @Override
    @Cacheable(value = "marketPrices", key = "#location + '-' + #cropName")
    public List<MarketPriceDTO> fetchPricesFromDummyApi(String location, String cropName) {

        try {
            System.out.println("CACHE MISS: Fetching fresh data from Dummy API for " + location + "...");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Better practice than just printStackTrace
        }

        MarketPriceDTO dummyData = new MarketPriceDTO();
        dummyData.setCropName(cropName);
        dummyData.setMandiLocation(location);
        dummyData.setPricePerQuintal(2840.0);
        dummyData.setTrendPercentage(12.4);

        return List.of(dummyData);
    }
}