import torch
import torch.nn as nn
from torchvision import models, transforms
from PIL import Image
from flask import Flask, request, jsonify
import io

app = Flask(__name__)

DEVICE = torch.device("cpu")

# Load model
NUM_CLASSES = 17
cnn_model = models.resnet18(weights=None)
cnn_model.fc = nn.Linear(cnn_model.fc.in_features, NUM_CLASSES)
cnn_model.load_state_dict(torch.load("model.pth", map_location=DEVICE))
cnn_model.eval()

# Classes
class_names = ['Corn___Common_Rust', 'Corn___Gray_Leaf_Spot', 'Corn___Healthy', 'Corn___Northern_Leaf_Blight', 
               'Potato___Early_Blight', 'Potato___Healthy', 'Potato___Late_Blight',
                 'Rice___Brown_Spot', 'Rice___Healthy', 'Rice___Leaf_Blast', 'Rice___Neck_Blast', 
                 'Sugarcane_Bacterial_Blight', 'Sugarcane_Healthy', 'Sugarcane_Red_Rot',
                   'Wheat___Brown_Rust', 'Wheat___Healthy', 'Wheat___Yellow_Rust']

transform = transforms.Compose([
    transforms.Resize((224,224)),
    transforms.ToTensor()
])

def extract_crop_disease(label):
    return label.split("___") if "___" in label else label.split("__")

@app.route("/predict", methods=["POST"])
def predict():
    file = request.files["file"]
    image = Image.open(io.BytesIO(file.read())).convert("RGB")

    image = transform(image).unsqueeze(0)

    with torch.no_grad():
        output = cnn_model(image)
        probs = torch.softmax(output, dim=1)
        top2_prob, top2_idx = torch.topk(probs, 2)

    primary = class_names[top2_idx[0][0]]
    secondary = class_names[top2_idx[0][1]]

    crop, disease = extract_crop_disease(primary)

    return jsonify({
        "prediction": {
            "crop": crop,
            "disease": disease,
            "confidence": float(top2_prob[0][0]) * 100
        },
        "alternative": {
            "disease": extract_crop_disease(secondary)[1],
            "confidence": float(top2_prob[0][1]) * 100
        }
    })

if __name__ == "__main__":
    app.run(port=5001)