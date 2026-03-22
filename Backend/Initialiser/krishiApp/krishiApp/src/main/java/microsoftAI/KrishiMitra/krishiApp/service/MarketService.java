package microsoftAI.KrishiMitra.krishiApp.service;

import microsoftAI.KrishiMitra.krishiApp.dto.MarketPriceDTO;
import java.util.List;

public interface MarketService {
    List<MarketPriceDTO> fetchPricesFromDummyApi(String location, String cropName);
}