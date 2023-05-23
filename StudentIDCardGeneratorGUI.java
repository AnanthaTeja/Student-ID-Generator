import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class StudentIDCardGeneratorGUI extends JFrame implements ActionListener {
    private static final int SD = 0;
    private JTextField nameField, idField, programField, yearField, departmentField;
    private JButton generateButton;
    private JButton picgen;
    private JLabel previewLabel;
    private JPanel picPanel;
    public String path;
    File selectedFile;

    public StudentIDCardGeneratorGUI() {
        // Create the GUI components
        JLabel nameLabel = new JLabel("Name:");
        JLabel idLabel = new JLabel("ID:");
        JLabel programLabel = new JLabel("Course:");
        JLabel yearLabel = new JLabel("Year:");
        JLabel departmentLabel = new JLabel("Department:");

        nameField = new JTextField(20);
        idField = new JTextField(20);
        programField = new JTextField(20);
        yearField = new JTextField(20);
        departmentField = new JTextField(20);

        JLabel uploadPic = new JLabel("Upload Photo:");
        picgen = new JButton("Upload");
        picgen.addActionListener(this);

        // Preview label of the uploaded image
        previewLabel = new JLabel();

        picPanel = new JPanel(new BorderLayout());
        JPanel uploadPanel = new JPanel();
        uploadPanel.add(uploadPic);
        uploadPanel.add(picgen);
        picPanel.add(uploadPanel, BorderLayout.NORTH);
        picPanel.add(previewLabel, BorderLayout.CENTER);

        // Create the GUI layout
        JPanel inputPanel = new JPanel(new GridLayout(6, 3));
        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(idLabel);
        inputPanel.add(idField);
        inputPanel.add(programLabel);
        inputPanel.add(programField);
        inputPanel.add(yearLabel);
        inputPanel.add(yearField);
        inputPanel.add(departmentLabel);
        inputPanel.add(departmentField);

        JPanel buttonPanel = new JPanel();
        generateButton = new JButton("Generate");
        generateButton.addActionListener(this);
        buttonPanel.add(generateButton);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(picPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Set up the JFrame
        setTitle("Student ID Card Generator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null); // Center the JFrame on the screen
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == generateButton) {
            // Get the user input
            String name = nameField.getText();
            int id = Integer.parseInt(idField.getText());
            String program = programField.getText();
            int year = Integer.parseInt(yearField.getText());
            String department = departmentField.getText();

            // Generate the student ID card
            BufferedImage idCardImage = generateIDCard(name, id, program, year, department);

            // Display the ID card with the uploaded image
            BufferedImage uploadedImage = getUploadedImage();
            BufferedImage combinedImage = combineImages(idCardImage, uploadedImage);

            // Display the combined image
            ImageIcon combinedIcon = new ImageIcon(combinedImage);
            JLabel combinedLabel = new JLabel(combinedIcon);
            JOptionPane.showMessageDialog(this, combinedLabel, "Student ID Card", JOptionPane.PLAIN_MESSAGE);
        } else if (e.getSource() == picgen) {
            // Open file chooser dialog
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                this.selectedFile = fileChooser.getSelectedFile();
                this.path = selectedFile.getAbsolutePath();
                try {
                    // Read the selected image file
                    BufferedImage image = ImageIO.read(selectedFile);

                int desiredWidth = (int) (1.5 * Toolkit.getDefaultToolkit().getScreenResolution());
                int desiredHeight = (int) (1.7 * Toolkit.getDefaultToolkit().getScreenResolution());

                    // Scale the image to fit the preview label if it has a non-zero size
                    if (previewLabel.getWidth() > 0 && previewLabel.getHeight() > 0) {

                        int SD = Math.max(previewLabel.getWidth(), previewLabel.getHeight());
                        ImageIcon icon = new ImageIcon(image.getScaledInstance((int)(SD*0.5), (int)(SD*0.5), Image.SCALE_SMOOTH));
                        previewLabel.setIcon(icon);
                    }
    

                    // Update the size of the picPanel to accommodate the image
                    picPanel.setPreferredSize(new Dimension(desiredWidth, desiredHeight));
                    picPanel.revalidate(); // Refresh the panel to reflect the new size
                    // Resize the JFrame to fit the updated panel size
                    pack();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error loading image", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        }
    
        private BufferedImage generateIDCard(String name, int id, String program, int year, String department) {
            // Generate the student ID card image
            int cardWidth = 500;
            int cardHeight = 500;
        
            BufferedImage idCardImage = new BufferedImage(cardWidth, cardHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = idCardImage.createGraphics();
            g2d.setColor(new Color(230, 230, 250));
            g2d.fillRect(0, 0, cardWidth, cardHeight);
            g2d.setColor(Color.BLACK);
        
            Font titleFont = new Font("Arial", Font.BOLD, 28);
            Font labelFont = new Font("Helvetica", Font.PLAIN, 20);

            Font picFont = new Font("Arial", Font.PLAIN, 12);
            Color lightColor = new Color(200, 200, 200); 


            g2d.setFont(titleFont);
            g2d.drawString("STUDENT ID CARD", 40, 50);
            
            //Display selectedFile image in g2d Frame at top center after STUDENT ID CARD
            if (selectedFile != null) {
                try {
                    BufferedImage image = ImageIO.read(selectedFile);
                    int imageWidth = cardWidth / 5; // Adjust the image size as needed
                    int imageHeight = cardHeight / 5;
                    int imageX = cardWidth - imageWidth - 40; // Adjust the image position as needed
                    int imageY = cardHeight - imageHeight - 40;
                    //display at center
                    imageX = 250;
                    imageY = 130;
                    
                    g2d.drawImage(image, imageX, imageY, imageWidth, imageHeight, null);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error loading image", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } 
            else {
                // Display a placeholder image if no image is uploaded
                g2d.drawRect(cardWidth - 200, cardHeight - 200, 160, 160);
                g2d.setColor(lightColor);
                g2d.setFont(picFont);
                g2d.drawString("Upload a photo",cardWidth - 200 + 10, cardHeight - 160+20);
            }

            g2d.setColor(Color.blue);
            g2d.setFont(labelFont);
            g2d.drawString("Name:", 40, 120);
            g2d.drawString(name, 150, 120);

            
        
            g2d.drawString("ID:", 40, 160);
            g2d.drawString(String.valueOf(id), 150, 160);
        
            g2d.drawString("Course:", 40, 200);
            g2d.drawString(program, 150, 200);
        
            g2d.drawString("Year:", 40, 240);
            g2d.drawString(String.valueOf(year), 150, 240);
        
            g2d.drawString("Department:", 40, 280);
            g2d.drawString(department, 150, 280);
        
            g2d.dispose();
            return idCardImage;
        }

        
        
        private BufferedImage combineImages(BufferedImage idCardImage, BufferedImage uploadedImage) {
            // Combine the ID card image and the uploaded image
            BufferedImage combinedImage = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = combinedImage.createGraphics();
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, combinedImage.getWidth(), combinedImage.getHeight());
            g2d.drawImage(idCardImage, 0, 0, null);
            if (uploadedImage != null) {
                int targetWidth = (int) (2 * 72); // Convert 2 cm to pixels assuming 72 dpi
                int targetHeight = (int) (2 * 72);
                Image scaledImage = uploadedImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
                int x = (idCardImage.getWidth() - targetWidth) / 2;
                int y = (idCardImage.getHeight() - targetHeight) / 2;
                //g2d.drawImage(scaledImage, x, y, null);
            }
            g2d.dispose();
            return combinedImage;
        }
        
    
        private BufferedImage getUploadedImage() {
            // Get the uploaded image from the preview label
            Icon icon = previewLabel.getIcon();
            if (icon instanceof ImageIcon) {
                Image image = ((ImageIcon) icon).getImage();
                BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = bufferedImage.createGraphics();
                g2d.drawImage(image, 0, 0, null);
                g2d.dispose();
                return bufferedImage;
            }
            return null;
        }
    
                public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> {
                StudentIDCardGeneratorGUI gui = new StudentIDCardGeneratorGUI();
                gui.setVisible(true);
            });
        }
    }