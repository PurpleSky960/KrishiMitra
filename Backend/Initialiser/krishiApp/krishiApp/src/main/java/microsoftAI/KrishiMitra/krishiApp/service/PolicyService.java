package microsoftAI.KrishiMitra.krishiApp.service;

import microsoftAI.KrishiMitra.krishiApp.dto.GovSchemeDTO;
import java.util.List;

public interface PolicyService {
    List<GovSchemeDTO> getEligibleSchemesForUser(Long userId);
}