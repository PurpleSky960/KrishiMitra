package microsoftAI.KrishiMitra.krishiApp.service.implementation;

import microsoftAI.KrishiMitra.krishiApp.dto.GovSchemeDTO;
import microsoftAI.KrishiMitra.krishiApp.entity.FarmProfile;
import microsoftAI.KrishiMitra.krishiApp.entity.GovScheme;
import microsoftAI.KrishiMitra.krishiApp.entity.User;
import microsoftAI.KrishiMitra.krishiApp.exception.ResourceNotFoundException;
import microsoftAI.KrishiMitra.krishiApp.repository.GovSchemeRepository;
import microsoftAI.KrishiMitra.krishiApp.repository.UserRepository;
import microsoftAI.KrishiMitra.krishiApp.service.PolicyService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PolicyServiceImpl implements PolicyService {

    private final GovSchemeRepository schemeRepository;
    private final UserRepository userRepository;

    public PolicyServiceImpl(GovSchemeRepository schemeRepository, UserRepository userRepository) {
        this.schemeRepository = schemeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<GovSchemeDTO> getEligibleSchemesForUser(Long userId) {
        // 1. Fetch the user (and their farm profiles)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        List<FarmProfile> userFarms = user.getFarmProfiles();
        List<GovScheme> allSchemes = schemeRepository.findAll();
        List<GovSchemeDTO> eligibleSchemes = new ArrayList<>();

        // 2. The Matching Logic
        for (GovScheme scheme : allSchemes) {
            for (FarmProfile farm : userFarms) {

                boolean isCropMatch = scheme.getTargetCrop().equalsIgnoreCase("ALL") ||
                        scheme.getTargetCrop().equalsIgnoreCase(farm.getCropType());

                boolean isSizeMatch = scheme.getMaxLandAcres() == null ||
                        farm.getFarmSizeAcres() <= scheme.getMaxLandAcres();

                if (isCropMatch && isSizeMatch) {
                    eligibleSchemes.add(GovSchemeDTO.builder()
                            .id(scheme.getId())
                            .schemeName(scheme.getSchemeName())
                            .description(scheme.getDescription())
                            .payoutAmount(scheme.getPayoutAmount())
                            .matchReason("Eligible based on your " + farm.getFarmSizeAcres() + " acre " + farm.getCropType() + " farm.")
                            .build());
                    break; // Once matched for this scheme, move to the next scheme to avoid duplicates
                }
            }
        }

        return eligibleSchemes;
    }
}