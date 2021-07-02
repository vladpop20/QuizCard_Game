import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;

public class QuizCardBuilder {

    private JTextArea question;
    private JTextArea answer;
    private ArrayList<QuizCard> cardList;
    private JFrame frame;

    public static void main(String[] args) {
        QuizCardBuilder builder = new QuizCardBuilder();
        builder.go();
    }


    public void go() {
        //let's build the GUI

        frame = new JFrame("Quiz Card Builder");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel mainPanel = new JPanel();
        Font bigFont = new Font("sanserif", Font.BOLD, 24);

        question = new JTextArea(6, 20);
        question.setLineWrap(true);							// Sets the line-wrapping policy of the text area. If set to true the lines will be wrapped if they are too long to fit within the allocated width.
        question.setWrapStyleWord(true);					// If set to true the lines will be wrapped at word boundaries (whitespace). If set to false, the lines will be wrapped at character boundaries.
        question.setFont(bigFont);

        JScrollPane qScroller = new JScrollPane(question);	// Sets the ScrollPane, where the question TextField will be placed
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        answer = new JTextArea(6, 20);
        answer.setLineWrap(true);
        answer.setWrapStyleWord(true);
        answer.setFont(bigFont);

        JScrollPane aScroller = new JScrollPane(answer);	// Sets the ScrollPane, where the answer TextField will be placed
        aScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        aScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JButton nextButton = new JButton("Next Card");

        cardList = new ArrayList<>();

        JLabel qLabel = new JLabel("Question:");
        JLabel aLabel = new JLabel("Answer:");

        mainPanel.add(qLabel);
        mainPanel.add(qScroller);
        mainPanel.add(aLabel);
        mainPanel.add(aScroller);
        mainPanel.add(nextButton);
        nextButton.addActionListener(new NextCardListener());	// NextCardListener is the inner class that implements ActionListener interface

        JMenuBar menuBar = new JMenuBar();                      // We make a menu bar, make a File-menu, then put 'new' and save menu items
        JMenu fileMenu = new JMenu("File");                  // into the File-menu.  We add the File-menu to the menu bar, then tell the frame
        JMenuItem newMenuItem = new JMenuItem("New");
        JMenuItem saveMenuItem = new JMenuItem("Save");     // to use this menu bar. Menu items can give an ActionEvent

        newMenuItem.addActionListener(new NewMenuListener());	// NewMenuListener is the inner class that implements ActionListener interface
        saveMenuItem.addActionListener(new SaveMenuListener());	// NewMenuListener is the inner class that implements ActionListener interface

        fileMenu.add(newMenuItem);
        fileMenu.add(saveMenuItem);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);
        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.setSize(500, 600);
        frame.setVisible(true);
    }

    public class NextCardListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {

            QuizCard card = new QuizCard(question.getText(), answer.getText());
            cardList.add(card);
            clearCard();
        }
    }

    public class NewMenuListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            cardList.clear();
            clearCard();
        }
    }

    public class SaveMenuListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            QuizCard card = new QuizCard(question.getText(), answer.getText());
            cardList.add(card);

            JFileChooser fileSave = new JFileChooser();		// Brings up a file dialog box and waits on this line until the user chooses 'Save' from the dialog box.
            fileSave.showSaveDialog(frame);
            saveFile(fileSave.getSelectedFile());
        }
    }

    private void clearCard() {
        question.setText("");
        answer.setText("");
        question.requestFocus();
    }

    private void saveFile(File file) {					// This method does the actual file writing (called by the SaveMenuListener's event handler)
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));	// We chain a BufferedWriter on to a new FileWriter, to make writing more efficient.

            for(QuizCard card : cardList) {
                writer.write(card.getQuestion() + "/");
                writer.write(card.getAnswer() + "\n");
            }
            writer.close();

        } catch(IOException ex) {
            System.out.println("couldn't write the cardList out");
            ex.printStackTrace();
        }
    }
}

	