import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class HighScore {
    static String fileName = "highscores.txt";
    static List<PlayerScore> playerScoreList = new ArrayList<>();

    public static void highScoreWriter (int time, String name) {
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write(name + " " + time + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void highScorePrinter(JTextArea highScoreBoard) throws IOException {
        List<PlayerScore> scores = highScoreSorting();
        highScoreBoard.setText("High Scores:\n");
        for (PlayerScore playerScore : scores) {
            highScoreBoard.append(playerScore.toString() + "\n");
        }
    }

    public static List<PlayerScore> highScoreSorting () throws IOException {

        Scanner sc = new Scanner(Paths.get(fileName));
        playerScoreList.clear();

        while (sc.hasNext()) {
            String name = sc.next();
            int timeRecord = sc.nextInt();

            PlayerScore playerScore = new PlayerScore(name, timeRecord);

            playerScoreList.add(playerScore);
        }

        Collections.sort(playerScoreList);
        return playerScoreList;
    }

    public static String formatTime (int time) {
        int minutes = time / 60000;
        int seconds = (time % 60000) / 1000;
        int millisecond = (time % 60000) % 1000;
        return String.format("%02d:%02d.%03d", minutes, seconds, millisecond);
    }

    public static class PlayerScore implements Comparable<PlayerScore> {
        String name;
        int time;

        public PlayerScore (String name, int time) {
            this.name = name;
            this.time = time;
        }

        @Override
        public int compareTo(PlayerScore other) {
            return Integer.compare(this.time, other.time);
        }

        @Override
        public String toString() {
            int minute = time / 60000;
            int second = (time % 60000) / 1000;
            int millisecond = (time % 60000) % 1000;
            return String.format("%s %02d:%02d.%03d", name, minute, second, millisecond);
        }
    }
}
