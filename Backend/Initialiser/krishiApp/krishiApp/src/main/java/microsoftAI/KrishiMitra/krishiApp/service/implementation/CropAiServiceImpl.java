package microsoftAI.KrishiMitra.krishiApp.service.implementation;

import lombok.RequiredArgsConstructor;
import microsoftAI.KrishiMitra.krishiApp.dto.CropDiagnosisResultDTO;
import microsoftAI.KrishiMitra.krishiApp.entity.CropDiagnosis;
import microsoftAI.KrishiMitra.krishiApp.entity.User;
import microsoftAI.KrishiMitra.krishiApp.exception.ResourceNotFoundException;
import microsoftAI.KrishiMitra.krishiApp.repository.CropDiagnosisRepository;
import microsoftAI.KrishiMitra.krishiApp.repository.UserRepository;
import microsoftAI.KrishiMitra.krishiApp.service.CropAiService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CropAiServiceImpl implements CropAiService {

    private final UserRepository userRepository;
    private final CropDiagnosisRepository diagnosisRepository;

    // Creates an "uploads" folder in the root of your project
    private final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    @Override
    public CropDiagnosisResultDTO processCropImage(Long userId, MultipartFile file) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        // 1. Save the file locally
        String fileName = UUID.randomUUID() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
        Path uploadPath = Paths.get(UPLOAD_DIR);
        String savedImagePath = "";

        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            savedImagePath = "/uploads/" + fileName; // This is the path the frontend will use to fetch the image later
        } catch (IOException e) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", e);
        }

        // 2. MOCK THE AI RESPONSE (Replace this block later with a real HTTP call to your ML model)
        String mockCrop = "Paddy";
        String mockDisease = "Brown Leafhopper";
        Double mockConfidence = 0.94;
        String mockSymptoms = "Yellowing of leaves, wilting, and 'hopperburn' patches in the field.";
        String mockCause = "High humidity and excessive nitrogen application favoring insect breeding.";
        String mockIcar = "Drain the field for 3-4 days. Apply systemic insecticides if hopper population exceeds economic threshold level (ETL).";
        String mockProduct = "Dinotefuran 20% SG or Pymetrozine 50% WG";

        // 3. Save to Database
        CropDiagnosis diagnosis = CropDiagnosis.builder()
                .user(user)
                .imagePath(savedImagePath)
                .crop(mockCrop)
                .disease(mockDisease)
                .confidence(mockConfidence)
                .visualSymptoms(mockSymptoms)
                .likelyCause(mockCause)
                .icarTreatment(mockIcar)
                .treatmentProduct(mockProduct)
                .build();

        diagnosisRepository.save(diagnosis);

        // 4. Return the DTO
        return CropDiagnosisResultDTO.builder()
                .diagnosisId(diagnosis.getId())
                .imagePath(savedImagePath)
                .crop(mockCrop)
                .disease(mockDisease)
                .confidence(mockConfidence)
                .visualSymptoms(mockSymptoms)
                .likelyCause(mockCause)
                .icarTreatment(mockIcar)
                .treatmentProduct(mockProduct)
                .build();
    }
}