package microsoftAI.KrishiMitra.krishiApp.service.implementation;

import lombok.RequiredArgsConstructor;
import microsoftAI.KrishiMitra.krishiApp.dto.DashboardSummaryDTO;
import microsoftAI.KrishiMitra.krishiApp.dto.MarketPriceDTO;
import microsoftAI.KrishiMitra.krishiApp.entity.User;
import microsoftAI.KrishiMitra.krishiApp.exception.ResourceNotFoundException;
import microsoftAI.KrishiMitra.krishiApp.repository.UserRepository;
import microsoftAI.KrishiMitra.krishiApp.service.DashboardService;
import microsoftAI.KrishiMitra.krishiApp.service.MarketService;
import microsoftAI.KrishiMitra.krishiApp.service.PolicyService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor // Automatically injects final fields!
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final MarketService marketService;
    private final PolicyService policyService;

    @Override
    public DashboardSummaryDTO getDashboardOverview(Long userId) {
        // 1. Fetch the user details
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        // 2. Fetch how many policies they are eligible for
        int schemeCount = policyService.getEligibleSchemesForUser(userId).size();

        // 3. Fetch market prices for their primary crop
        String primaryCrop = user.getFarmProfiles().isEmpty() ? "Paddy" : user.getFarmProfiles().get(0).getCropType();
        List<MarketPriceDTO> prices = marketService.fetchPricesFromDummyApi(user.getLocation(), primaryCrop);

        // 4. Build and return the aggregated summary
        return DashboardSummaryDTO.builder()
                .farmerName(user.getFullName())
                .location(user.getLocation())
                .eligibleSchemesCount(schemeCount)
                .topMarketPrices(prices)
                .urgentAction("Spray pesticide within 24 hrs") // Mocked UI alert
                .build();
    }
}