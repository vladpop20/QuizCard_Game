import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;

public class QuizCardPlayer {
    private JTextArea display;
    private JTextArea answer;
    private ArrayList<QuizCard> cardList;
    private QuizCard currentCard;
    private int currentCardIndex;
    private JFrame frame;
    private JButton nextButton;
    private boolean isShowAnswer;

    public static void main(String[] args) {
        QuizCardPlayer reader = new QuizCardPlayer();
        reader.go();
    }

    public void go() {
        frame = new JFrame("Quiz Card Player");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel mainPanel = new JPanel();
        Font bigFont = new Font("sanserif", Font.BOLD, 24);

        display = new JTextArea(10, 20);
        display.setFont(bigFont);

        display.setLineWrap(true);
        display.setEditable(false);

        JScrollPane qScroller = new JScrollPane(display);
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        nextButton = new JButton("Show Question");
        mainPanel.add(qScroller);
        mainPanel.add(nextButton);
        nextButton.addActionListener(new NextCardListener());

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem loadMenuItem = new JMenuItem("Load card set");
        loadMenuItem.addActionListener(new OpenMenuListener());

        fileMenu.add(loadMenuItem);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);
        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.setSize(500, 500);
        frame.setVisible(true);
    }

    public class NextCardListener implements ActionListener {       // Check the isShowAnswer boolean flag to see it they're currently viewing a question or an answer
        @Override
        public void actionPerformed(ActionEvent event) {
            if(isShowAnswer) {          // show the answer because they've seen the question
                display.setText(currentCard.getAnswer());
                nextButton.setText("Next Card");
                isShowAnswer = false;
            } else {                    // show the next question
                if(currentCardIndex < cardList.size()) {
                    showNextCard();
                } else {                // there are no more card to show!
                    display.setText("That was last card");
                    nextButton.setEnabled(false);
                }
            }
        }
    }

    public class OpenMenuListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            JFileChooser fileOpen = new JFileChooser();
            fileOpen.showOpenDialog(frame);
            loadFile(fileOpen.getSelectedFile());
        }
    }

    public void loadFile(File file) {
        cardList = new ArrayList<>();
        try {                                                                   // Make a BufferedREader chained to a new FileReader,
            BufferedReader reader = new BufferedReader(new FileReader(file));   // giving the FileReader, the File Object the user chose from the open file dialog.
            String line = null;
            while((line = reader.readLine()) != null) {                         // Read a line at a time, passing the line tot the makeCard method, that parses it and turns it,
                makeCard(line);                                                 // into a real QuizCard and adds it to the ArrayList.
            }
            reader.close();

        } catch (IOException ex) {
            System.out.println("Couldn't read the card file");
            ex.printStackTrace();
        }
    }

    public void makeCard(String lineToParse) {
        String[] result = lineToParse.split("/");
        QuizCard card = new QuizCard(result[0], result[1]);
        cardList.add(card);
        System.out.println("made a card");
    }

    public void showNextCard() {
        currentCard = cardList.get(currentCardIndex);
        currentCardIndex++;
        display.setText(currentCard.getQuestion());
        nextButton.setText("show Answer");
        isShowAnswer = true;
    }
}
