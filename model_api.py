import torch
import torch.nn as nn
from torchvision import models, transforms
from PIL import Image
from flask import Flask, request, jsonify
import io
import os

# ------------------------
# INIT
# ------------------------
app = Flask(__name__)

DEVICE = torch.device("cuda" if torch.cuda.is_available() else "cpu")
print("Using device:", DEVICE)

# ------------------------
# LOAD MODEL
# ------------------------
NUM_CLASSES = 17

cnn_model = models.resnet18(weights=None)
cnn_model.fc = nn.Linear(cnn_model.fc.in_features, NUM_CLASSES)

MODEL_PATH = os.path.join(os.path.dirname(__file__), "model.pth")

cnn_model.load_state_dict(torch.load(MODEL_PATH, map_location=DEVICE))
cnn_model = cnn_model.to(DEVICE)
cnn_model.eval()

# ------------------------
# CLASS NAMES (EXACT ORDER)
# ------------------------
class_names = [
    'Corn___Common_Rust', 'Corn___Gray_Leaf_Spot', 'Corn___Healthy', 'Corn___Northern_Leaf_Blight',
    'Potato___Early_Blight', 'Potato___Healthy', 'Potato___Late_Blight',
    'Rice___Brown_Spot', 'Rice___Healthy', 'Rice___Leaf_Blast', 'Rice___Neck_Blast',
    'Sugarcane_Bacterial_Blight', 'Sugarcane_Healthy', 'Sugarcane_Red_Rot',
    'Wheat___Brown_Rust', 'Wheat___Healthy', 'Wheat___Yellow_Rust'
]

# ------------------------
# TRANSFORMS (MATCH TRAINING)
# ------------------------
transform = transforms.Compose([
    transforms.Resize((224, 224)),
    transforms.ToTensor()
])

# ------------------------
# DISEASE DATABASE
# ------------------------
disease_db = {
    "Common_Rust": {
        "symptoms": "Reddish-brown pustules on leaves",
        "treatment": "Apply Mancozeb",
        "prevention": "Use resistant varieties"
    },
    "Gray_Leaf_Spot": {
        "symptoms": "Gray rectangular lesions",
        "treatment": "Use Azoxystrobin",
        "prevention": "Crop rotation"
    },
    "Healthy": {
        "symptoms": "No disease",
        "treatment": "None",
        "prevention": "Maintain proper care"
    },
    "Northern_Leaf_Blight": {
        "symptoms": "Long gray-green lesions",
        "treatment": "Use Propiconazole",
        "prevention": "Resistant hybrids"
    },
    "Early_Blight": {
        "symptoms": "Brown concentric rings",
        "treatment": "Use Chlorothalonil",
        "prevention": "Avoid overhead watering"
    },
    "Late_Blight": {
        "symptoms": "Dark wet lesions",
        "treatment": "Use Metalaxyl",
        "prevention": "Good drainage"
    },
    "Brown_Spot": {
        "symptoms": "Brown patches on leaves",
        "treatment": "Use Carbendazim",
        "prevention": "Balanced fertilization"
    },
    "Leaf_Blast": {
        "symptoms": "Diamond-shaped lesions",
        "treatment": "Use Tricyclazole",
        "prevention": "Avoid excess nitrogen"
    },
    "Neck_Blast": {
        "symptoms": "Black neck of panicle",
        "treatment": "Use Isoprothiolane",
        "prevention": "Water management"
    },
    "Brown_Rust": {
        "symptoms": "Orange-brown pustules",
        "treatment": "Use Propiconazole",
        "prevention": "Resistant varieties"
    },
    "Yellow_Rust": {
        "symptoms": "Yellow stripe pustules",
        "treatment": "Use Tebuconazole",
        "prevention": "Early spraying"
    },
    "Red_Rot": {
        "symptoms": "Red discoloration in stem",
        "treatment": "Use Carbendazim",
        "prevention": "Crop rotation"
    },
    "Bacterial_Blight": {
        "symptoms": "Water-soaked streaks",
        "treatment": "Use Copper Oxychloride",
        "prevention": "Avoid water stagnation"
    }
}

# ------------------------
# HELPERS
# ------------------------
def extract_crop_disease(label):
    if "___" in label:
        return label.split("___")
    else:
        return label.split("_", 1)  # Sugarcane fix


def predict(image):
    image = transform(image).unsqueeze(0).to(DEVICE)

    with torch.no_grad():
        output = cnn_model(image)
        probs = torch.softmax(output, dim=1)
        top2_prob, top2_idx = torch.topk(probs, 2)

    return top2_prob, top2_idx


# ------------------------
# API ROUTE
# ------------------------
@app.route("/predict", methods=["POST"])
def predict_api():

    if "file" not in request.files:
        return jsonify({"error": "No file uploaded"}), 400

    file = request.files["file"]
    image = Image.open(io.BytesIO(file.read())).convert("RGB")

    probs, idx = predict(image)

    primary_label = class_names[idx[0][0]]
    secondary_label = class_names[idx[0][1]]

    crop, disease = extract_crop_disease(primary_label)

    result = {
        "prediction": {
            "crop": crop,
            "disease": disease,
            "confidence": round(float(probs[0][0]) * 100, 2)
        },
        "alternative": {
            "disease": extract_crop_disease(secondary_label)[1],
            "confidence": round(float(probs[0][1]) * 100, 2)
        },
        "details": disease_db.get(disease, {
            "symptoms": "Not available",
            "treatment": "Not available",
            "prevention": "Not available"
        })
    }

    return jsonify(result)


# ------------------------
# RUN
# ------------------------
if __name__ == "__main__":
    port = int(os.environ.get("PORT", 5001))
    app.run(host="0.0.0.0", port=port)