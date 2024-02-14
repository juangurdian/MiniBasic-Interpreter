import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.io.*;

public class lab3Controller {
	private lab3View view;
	private lab3Model model;
	
	public lab3Controller(lab3View view, lab3Model model) {
		this.view = view;
		this.model = model;
		initController();
	}
	
	private void initController() {
		view.readButton.addActionListener(e -> saveFile(view, view.codeInputArea));
		view.runButton.addActionListener(e -> runFile());
		view.saveButton.addActionListener(e -> readFile());
		view.resetButton.addActionListener(e -> reset());
	}
	
	public void saveFile(JFrame parentFrame, JTextArea codeTextArea) {
		JFileChooser fileChooser = new JFileChooser();
	    int userSelection = fileChooser.showSaveDialog(parentFrame);

	    if (userSelection == JFileChooser.APPROVE_OPTION) {
	        File fileToSave = fileChooser.getSelectedFile();
	        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
	            writer.write(codeTextArea.getText());
	            JOptionPane.showMessageDialog(parentFrame, "File saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
	        } catch (IOException ex) {
	            JOptionPane.showMessageDialog(parentFrame, "An error occurred while saving the file.", "Error", JOptionPane.ERROR_MESSAGE);
	        }
	    }
	}
	void runFile() {
		String code = view.codeInputArea.getText();
	    String output = model.execute(code);
	    displayOutput(output);
	}
	void readFile() {
		JFileChooser fileChooser = new JFileChooser();
        int userSelection = fileChooser.showOpenDialog(view);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToRead = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(fileToRead))) {
                view.codeInputArea.read(reader, null);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(view, "An error occurred while reading the file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
	}
	void reset() {
		view.codeInputArea.setText("");
        view.outputArea.setText("");
    }
	private void displayOutput(String output) {
	    JDialog outputDialog = new JDialog(view, "Output", true); // true makes the dialog modal
	    outputDialog.setSize(400, 300);
	    outputDialog.setLocationRelativeTo(view); // Center the dialog relative to the main frame

	    JTextArea outputArea = new JTextArea(output);
	    outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Use a monospace font
	    outputArea.setEditable(false); // Make it read-only
	    outputArea.setWrapStyleWord(true);
	    outputArea.setLineWrap(true);
	    outputArea.setCaretPosition(0); // Set scroll to the top
	    outputArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add some padding

	    JScrollPane scrollPane = new JScrollPane(outputArea);
	    outputDialog.add(scrollPane);

	    outputDialog.setVisible(true);
	}

	}
