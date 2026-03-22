package microsoftAI.KrishiMitra.krishiApp.service;

import microsoftAI.KrishiMitra.krishiApp.dto.DashboardSummaryDTO;

public interface DashboardService {
    DashboardSummaryDTO getDashboardOverview(Long userId);
}