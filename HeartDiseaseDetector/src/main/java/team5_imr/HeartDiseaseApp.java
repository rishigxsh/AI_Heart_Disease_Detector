package team5_imr;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class HeartDiseaseApp extends JFrame {
    private JLabel fileNameLabel;
    private JLabel imagePreviewLabel;
    private JButton uploadButton;
    private JButton predictButton;
    private JLabel resultLabel;
    private JLabel loadingSpinner;
    private JLabel cholesterolLabel;
    private JTextField cholesterolTextField;
    private JLabel ageLabel;
    private JTextField ageTextField;
    private JLabel sexLabel;
    private JTextField sexTextField;
    private JLabel bpLabel;
    private JTextField bpTextField;
    private File selectedFile;
    private logReg imagePredictor = new logReg();
    private static Model wekaModel = new Model();

    public HeartDiseaseApp() {
    	
    	System.setProperty("com.github.fommil.netlib.BLAS", "com.github.fommil.netlib.F2jBLAS");
    	System.setProperty("com.github.fommil.netlib.LAPACK", "com.github.fommil.netlib.F2jLAPACK");
    	System.setProperty("com.github.fommil.netlib.ARPACK", "com.github.fommil.netlib.F2jARPACK");

    	
        setTitle("AI Heart Disease Detector");
        setSize(800, 600); // Increased height to accommodate new fields
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(204, 255, 204)); // Light pastel green

        // Create a white floating panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setPreferredSize(new Dimension(700, 550)); // Adjusted size
        mainPanel.setLayout(new GridBagLayout()); // Centering components

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2); // Padding between components
        gbc.fill = GridBagConstraints.HORIZONTAL; // Make components fill horizontally

        // Title and Subtitle
        JLabel titleLabel = new JLabel("AI Heart Disease Detector");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(46, 125, 50));
        gbc.gridx = 0; // Column
        gbc.gridy = 0; // Row
        gbc.gridwidth = 2; // Span two columns
        gbc.anchor = GridBagConstraints.CENTER; // Center the title
        mainPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1; // Reset to one column span

        // Upload Section
        uploadButton = new JButton("Upload X-ray");
        fileNameLabel = new JLabel("No file chosen");
        gbc.anchor = GridBagConstraints.CENTER; // Center the component
        gbc.gridy = 1;
        gbc.gridx = 0;
        mainPanel.add(uploadButton, gbc);
        gbc.gridx = 1;
        mainPanel.add(fileNameLabel, gbc);

        // Image Preview Section
        imagePreviewLabel = new JLabel();
        imagePreviewLabel.setPreferredSize(new Dimension(250, 250));
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; // Span two columns
        mainPanel.add(imagePreviewLabel, gbc);
        gbc.gridwidth = 1; // Reset to one column span

        // Blood Pressure Input
        bpLabel = new JLabel("Blood Pressure:");
        bpTextField = new JTextField();
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(bpLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(bpTextField, gbc);

        // Cholesterol Level Input
        cholesterolLabel = new JLabel("Cholesterol Level:");
        cholesterolTextField = new JTextField();
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(cholesterolLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(cholesterolTextField, gbc);

        // Age Input
        ageLabel = new JLabel("Age:");
        ageTextField = new JTextField();
        gbc.gridx = 0;
        gbc.gridy = 5;
        mainPanel.add(ageLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(ageTextField, gbc);

        // Sex Input
        sexLabel = new JLabel("Sex (F/M):");
        sexTextField = new JTextField();
        gbc.gridx = 0;
        gbc.gridy = 6;
        mainPanel.add(sexLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(sexTextField, gbc);

        // Prediction Button
        predictButton = new JButton("Predict Heart Disease");
        predictButton.setEnabled(false);
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2; // Span two columns
        mainPanel.add(predictButton, gbc);
        gbc.gridwidth = 1; // Reset to one column span

        // Result Label
        resultLabel = new JLabel("Prediction:");
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2; // Span two columns
        mainPanel.add(resultLabel, gbc);
        gbc.gridwidth = 1; // Reset to one column span

        // Loading Spinner
        loadingSpinner = new JLabel("Loading...");
        loadingSpinner.setForeground(Color.BLUE);
        loadingSpinner.setVisible(false);
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 2; // Span two columns
        mainPanel.add(loadingSpinner, gbc);

        // Add the main panel to the frame, centered within the window
        add(mainPanel);
        setLocationRelativeTo(null); // Center the window on the screen

        // Button Listeners
        uploadButton.addActionListener(new UploadButtonListener());
        predictButton.addActionListener(new PredictButtonListener());
    }

    private class UploadButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                fileNameLabel.setText(selectedFile.getName());
                predictButton.setEnabled(true);

                // Display Image Preview in a smaller size
                ImageIcon icon = new ImageIcon(selectedFile.getAbsolutePath());
                Image img = icon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH); // Resize to 250x250
                imagePreviewLabel.setIcon(new ImageIcon(img));
            }
        }
    }

    private class PredictButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedFile != null) {
                // Retrieve additional inputs
                String cholesterol = cholesterolTextField.getText().trim();
                String bp = bpTextField.getText().trim();
                String age = ageTextField.getText().trim();
                String sex = sexTextField.getText().trim();
                double[] inputs = new double[4];

                // Basic input validation
                if (cholesterol.isEmpty() || age.isEmpty() || sex.isEmpty() || bp.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill in all additional fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!cholesterol.matches("\\d+") && Integer.parseInt(cholesterol) > 0) {
                    JOptionPane.showMessageDialog(null, "Cholesterol level must be a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (!bp.matches("\\d+") && Integer.parseInt(bp) > 0) {
                    JOptionPane.showMessageDialog(null, "Cholesterol level must be a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!age.matches("\\d+") && Integer.parseInt(age) > 0) {
                    JOptionPane.showMessageDialog(null, "Age must be a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Optionally, validate family history input
                if (!sex.equalsIgnoreCase("Male") && !sex.equalsIgnoreCase("Female")) {
                    JOptionPane.showMessageDialog(null, "Sex must be 'Male' or 'Female'.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                inputs[0] = Double.parseDouble(age);
                
                if(sex.equalsIgnoreCase("Male")) inputs[1] = 1;
                else inputs[1] = 0;
                
                inputs[2] = Double.parseDouble(bp);
                inputs[3] = Double.parseDouble(cholesterol);

                loadingSpinner.setVisible(true);

                new Thread(() -> {
                    boolean imagePrediction = sendImageForPrediction(selectedFile);
                    boolean factorPrediction = sendFactorsForPrediction(inputs);
                    SwingUtilities.invokeLater(() -> {
                        loadingSpinner.setVisible(false);
                        if(imagePrediction == true && factorPrediction == true)resultLabel.setText("Prediction: High Risk of Heart Disease");
                        else if(imagePrediction == false && factorPrediction == false ) resultLabel.setText("Prediction: Low Risk of Heart Disease");
                        else resultLabel.setText("Prediction: Moderate Risk of Heart Disease");
                    });
                }).start();
            } else {
                resultLabel.setText("Prediction: Please upload an X-ray image.");
            }
        }
    }

    private boolean sendImageForPrediction(File file) {
        try {
            // Convert file to base64
            byte[] imageBytes = Files.readAllBytes(file.toPath());
            String modelPath = "src/main/resources/heart_disease_model.model";
            String prediction = imagePredictor.classifyImage(modelPath, imageBytes);

            // Read response
            if (prediction.equals("Heart Disease")) {
                // Handle successful response
                return true; // Replace with actual parsing logic
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean sendFactorsForPrediction(double[] inputs) {
    	
    	int prediction = wekaModel.useModel(inputs);
    	
    	if(prediction == 1) return true;
    	
    	return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HeartDiseaseApp ui = new HeartDiseaseApp();
            ui.setVisible(true);
        });
    }
}
