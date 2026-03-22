package microsoftAI.KrishiMitra.krishiApp.repository;

import microsoftAI.KrishiMitra.krishiApp.entity.MarketPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarketPriceRepository extends JpaRepository<MarketPrice, Long> {
    // Find prices for a specific crop in a specific location
    List<MarketPrice> findByCropNameAndMandiLocationOrderByRecordedDateDesc(String cropName, String mandiLocation);
}