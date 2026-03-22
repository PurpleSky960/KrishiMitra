package microsoftAI.KrishiMitra.krishiApp.service;

import microsoftAI.KrishiMitra.krishiApp.dto.CropDiagnosisResultDTO;
import org.springframework.web.multipart.MultipartFile;

public interface CropAiService {
    CropDiagnosisResultDTO processCropImage(Long userId, MultipartFile file);
}