# 🫀 AI Heart Disease Detector

An intelligent application that analyzes **chest X-ray images** and **medical factors** to predict the risk of heart disease using machine learning and image-processing techniques. Built to assist healthcare professionals and everyday users by providing fast, AI-driven insights — **not as a replacement for medical diagnosis**, but as a reference tool to encourage timely consultation.

---

## 📘 Project Overview

The **AI Heart Disease Detector** leverages **OpenCV** for image preprocessing and **Weka’s Logistic Regression model** for classification.  
It evaluates X-ray images and user-provided medical data through two prediction models:
- **Logistic Regression (LogReg)** → Analyzes X-ray images.  
- **Deep Neural Network (DNN_model)** → Analyzes medical factors such as age, cholesterol, blood pressure, etc.

The application outputs the user’s **risk level** of heart disease based on both analyses.

---

## 🎯 Purpose & Goals

**Purpose:**  
Provide a user-friendly AI tool that quickly estimates heart disease risk to promote early medical checkups.

**Goals:**
- Implement machine learning algorithms for image and medical data classification.  
- Deliver a clean and intuitive interface.  
- Maximize accuracy and minimize false results.  
- Allow cross-platform deployment via Java and Maven.

---

## ⚙️ Features

✅ Upload chest X-ray images for automated analysis.  
✅ Input personal medical factors for secondary evaluation.  
✅ Combined AI decision from image & data analysis.  
✅ Immediate “Low / Moderate / High” risk feedback.  
✅ Error handling for invalid or missing inputs.  
✅ Cross-platform compatibility (Windows / macOS / Linux).  

---

## 🧠 Technical Architecture

**Core Classes:**
- **Controller** – Coordinates UI actions and model communication.  
- **LogReg** – Handles image preprocessing and logistic regression predictions.  
- **DNN_model** – Analyzes medical factors via a neural network.  
- **UploadButtonListener & PredictButtonListener** – Manage user inputs and trigger predictions.

**Technologies Used:**
- **Language:** Java  
- **Libraries:** OpenCV (v4.10), Weka  
- **IDE:** Eclipse  
- **Build Tool:** Maven  
- **Design Tools:** Draw.io  
- **Documentation:** MS Word, PowerPoint  

---

## 🧪 Testing Summary

Comprehensive manual tests were performed across:
1. **Unit Testing** – Verified each class and listener component.  
2. **Integration Testing** – Ensured seamless communication between models and UI.  
3. **System Testing** – Validated full workflows from input to prediction output.  

✅ Most cases **passed** successfully, except two cases related to **x-ray model accuracy (60–70%)** — expected to improve with retraining.

---

## 🚀 Installation & Setup

### Prerequisites
- Java (latest version)
- Maven installed
- OpenCV 4.10 (ensure `opencv4100.dll` is in the system PATH)

### Steps
1. Clone this repository:
   ```bash
   git clone https://github.com/<your-username>/ai-heart-disease-detector.git
   cd ai-heart-disease-detector
   ```
2. Build the project:
   ```bash
   mvn clean install
   ```
3. Run the application:
   ```bash
   mvn exec:java
   ```
4. Upload an X-ray and input medical factors to get your risk prediction.

---

## 🧩 Model Logic

| Model | Input Type | Algorithm | Output |
|--------|-------------|------------|----------|
| **LogReg** | X-ray Image | Logistic Regression | Heart Disease / No Heart Disease |
| **DNN_model** | Medical Factors | Deep Neural Network | Heart Disease / No Heart Disease |

**Prediction Output Categories:**
- 🟢 **Low Risk** → Both models show no indicators.  
- 🟡 **Moderate Risk** → Either model detects potential signs.  
- 🔴 **High Risk** → Both models detect heart disease indicators.

---

## 📷 Demo Preview

| Screen | Description |
|--------|--------------|
| 🖼️ Initial UI | Launch screen before input |
| 📤 Upload | X-ray preview with entered medical data |
| 📊 Result | Displays heart disease risk prediction |
| ⚠️ Warning | Shows medical warning for moderate/high risk |
| ❌ Error | Alerts invalid or missing medical input |

---

## 🧰 Tools Used

| Tool | Purpose |
|------|----------|
| Eclipse IDE | Development |
| OpenCV | Image Preprocessing |
| Weka | Machine Learning |
| Draw.io | UML & Design Diagrams |
| Maven | Build Management |
| Microsoft Office | Documentation & Presentation |

---


## ⚠️ Disclaimer
This tool is intended **for educational and research purposes only** and **should not be used for medical diagnosis**. Always consult a qualified healthcare professional for medical decisions.

---

## 📜 License
This project is licensed under the [MIT License](LICENSE).
# AI_Heart_Disease_Detector
