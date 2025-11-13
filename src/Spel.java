import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Spel extends JFrame implements ActionListener {
    private JPanel controlPanel = new JPanel();
    private JButton newGameButton = new JButton("New game");
    private JButton startGameButton = new JButton("Start");
    private JButton pauseGameButton = new JButton("Pause");
    private JButton resumeGameButton = new JButton("Resume");

    private Timer timer;
    private int elapsedTime;
    private JLabel currentTime = new JLabel("Timer: ");

    private JPanel gamePanel = new JPanel();
    private JButton blankButton = new JButton("");

    private JPanel highScorePanel = new JPanel();
    private JTextArea highScoreBoard = new JTextArea(20,20);

    List<JButton> buttonList = new ArrayList<>();
    List<JButton> correctButtonList = new ArrayList<>();

    Scanner sc;
    String fileName = "highscores.txt";

    public Spel() {
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        newGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        newGameButton.setMaximumSize(new Dimension(100, 30));
        startGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startGameButton.setMaximumSize(new Dimension(100, 30));
        pauseGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        pauseGameButton.setMaximumSize(new Dimension(100, 30));
        resumeGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        resumeGameButton.setMaximumSize(new Dimension(100, 30));
        currentTime.setAlignmentX(Component.CENTER_ALIGNMENT);
        controlPanel.add(newGameButton);
        controlPanel.add(startGameButton);
        controlPanel.add(pauseGameButton);
        controlPanel.add(resumeGameButton);
        controlPanel.add(currentTime);

        newGameButton.addActionListener(this);
        startGameButton.addActionListener(this);
        pauseGameButton.addActionListener(this);
        resumeGameButton.addActionListener(this);

        gamePanel.setLayout(new GridLayout(4, 4));

        for (int i = 1; i < 16; i++) {
            JButton button = new JButton(String.valueOf(i));
            button.addActionListener(this);
            buttonList.add(button);
        }
        buttonList.add(blankButton);

        for (int i = 0; i <= 15; i++) {
            correctButtonList.add(buttonList.get(i));
        }

        highScorePrinter();
        highScorePanel.add(highScoreBoard);


        timer = new Timer(100, e -> {
            elapsedTime += 100;
            currentTime.setText("Timer: " + formatTime(elapsedTime));
        });

        controlPanel.setPreferredSize(new Dimension(100, 400));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        gamePanel.setPreferredSize(new Dimension(400, 400));
        gamePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        highScorePanel.setPreferredSize(new Dimension(400, 400));
        highScorePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(controlPanel, BorderLayout.CENTER);
        add(gamePanel, BorderLayout.WEST);
        add(highScoreBoard, BorderLayout.EAST);

        this.setTitle("15 Block Game");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newGameButton) {
            gameGenerate();
        } else if (buttonList.contains((JButton) e.getSource())) {
            JButton selectedButton = (JButton) e.getSource();
            movingButton(selectedButton);
        }
        if (e.getSource() == startGameButton) {
            timer.start();
        } else if (e.getSource() == pauseGameButton) {
            timer.stop();
        } else if (e.getSource() == resumeGameButton) {
            timer.restart();
        }
    }

    public void gameGenerate() {
        Collections.shuffle(buttonList);

        while (!GameUtilities.checkSolvable(buttonList, blankButton)) {
            Collections.shuffle(buttonList);
        }
        gamePanel.removeAll();
        timer.stop();
        elapsedTime = 0;
        currentTime.setText("Timer: 00:00");
        for (JButton b : buttonList) {
            gamePanel.add(b);
        }
        gamePanel.revalidate();
        gamePanel.repaint();
    }

    // this "moving-Button" method control the movement of each number button:
    // using the button list from gameGenerate method in the 4 by 4 game panel, we will have the following matrix of index of the buttons in the list:

    // row/column     0  1  2  3
    //       0      | 0| 1| 2| 3|
    //       1      | 4| 5| 6| 7|
    //       2      | 8| 9|10|11|
    //       3      |12|13|14|15|

    // the method will get the row and column of the clicked button and the blank button and compare those
    // if they are the same row then we check their column, if the difference of column is 1 -> they are next to each other and can be swapped
    // otherwise they are not to be swapped (same logic if it is on the same column, then we check their row)

    public void movingButton(JButton selectedButton) {

        int buttonIndex = buttonList.indexOf(selectedButton);
        int buttonIndexRow = buttonIndex / 4;
        int buttonIndexColumn = buttonIndex % 4;

        int blankButtonIndex = buttonList.indexOf(blankButton);
        int blankButtonIndexRow = blankButtonIndex / 4;
        int blankButtonIndexColumn = blankButtonIndex % 4;

        if (buttonIndexRow == blankButtonIndexRow && Math.abs(buttonIndexColumn - blankButtonIndexColumn) == 1) {
            Collections.swap(buttonList, buttonIndex, blankButtonIndex);
        } else if (buttonIndexColumn == blankButtonIndexColumn && Math.abs(buttonIndexRow - blankButtonIndexRow) == 1) {
            Collections.swap(buttonList, buttonIndex, blankButtonIndex);
        }

        gamePanel.removeAll();

        for (JButton b : buttonList) {
            gamePanel.add(b);
        }

        gamePanel.revalidate();
        gamePanel.repaint();
        boolean hasWon = GameUtilities.hasThePlayerWon(buttonList, correctButtonList);
        if (hasWon) {
            String name = JOptionPane.showInputDialog(null, "Congratulations! You won! What's your name?");
            timer.stop();
            highScoreWriter(elapsedTime, name);
            highScorePrinter();
        }
    }

    public void highScoreWriter (int time, String name) {
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write(name + " " + formatTime(time) + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void highScorePrinter () {
        try {
            List<String> lines = Files.readAllLines(Paths.get(fileName));
            highScoreBoard.removeAll();
            highScoreBoard.setText("High Scores:\n");
            for (String line : lines) {
                highScoreBoard.append(line);
            }

        } catch (IOException e) {
            IO.println("Could not read file: " + e.getMessage());
        }
    }

    public String formatTime (int time) {
        int seconds = (time / 1000) % 60;
        int minutes = (time / 60000);
        int tenths = (time / 100) % 10;
        return String.format("%02d:%02d.%d", minutes, seconds, tenths);
    }
}
