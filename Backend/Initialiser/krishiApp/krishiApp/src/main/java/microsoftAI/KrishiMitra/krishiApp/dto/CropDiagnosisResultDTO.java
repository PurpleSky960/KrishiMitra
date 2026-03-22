package microsoftAI.KrishiMitra.krishiApp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CropDiagnosisResultDTO {
    private Long diagnosisId;
    private String imagePath;
    private String crop;
    private String disease;
    private Double confidence;
    private String visualSymptoms;
    private String likelyCause;
    private String icarTreatment;
    private String treatmentProduct;
}