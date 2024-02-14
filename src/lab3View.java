import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class lab3View extends JFrame{

    public JFrame frame;
    public JTextArea codeInputArea;
    public JTextArea outputArea;
    public JButton runButton, readButton, saveButton, resetButton;

    public lab3View() {
    	
        setTitle("MINIBASIC LAB 3");
        setSize(600,400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(77, 25, 121));
        
        codeInputArea = new JTextArea();
        JScrollPane codeScrollPane = new JScrollPane(codeInputArea);

        // Create a JTextArea for line numbers
        JTextArea lineNumbers = new JTextArea("1");
        lineNumbers.setBackground(Color.LIGHT_GRAY);
        lineNumbers.setEditable(false);

        // Add lineNumbers to the scroll pane
        codeScrollPane.setRowHeaderView(lineNumbers);

        // Add a DocumentListener to update line numbers
        codeInputArea.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                updateLineNumbers();
            }
            public void removeUpdate(DocumentEvent e) {
                updateLineNumbers();
            }
            public void changedUpdate(DocumentEvent e) {
                // Plain text components don't fire these events
            }
            void updateLineNumbers() {
                int lines = codeInputArea.getLineCount();
                StringBuilder numbers = new StringBuilder();
                for (int i = 1; i <= lines; i++) {
                    numbers.append(i).append("\n");
                }
                lineNumbers.setText(numbers.toString());
            }
        });
        
        add(codeScrollPane, BorderLayout.CENTER);
        
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane outputScrollPane = new JScrollPane(outputArea);
        add(outputScrollPane, BorderLayout.SOUTH);

        JPanel controlPanel = new JPanel();
        runButton = new JButton("Run");
        readButton = new JButton("Read");
        saveButton = new JButton("Save");
        resetButton = new JButton("Reset");

        controlPanel.add(runButton);
        controlPanel.add(readButton);
        controlPanel.add(saveButton);
        controlPanel.add(resetButton);

        add(controlPanel, BorderLayout.NORTH);
        setVisible(true);
        
        

    }
    

		
	}
