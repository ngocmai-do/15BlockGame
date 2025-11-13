import javax.swing.*;
import java.util.List;

public class GameUtilities {

    // Principles is from: https://puzzling.stackexchange.com/questions/118080/possible-parity-for-sliding-puzzle

    public static boolean checkSolvable(List<JButton> buttonList, JButton blankButton) {
        boolean blankButtonRow;

        if ((buttonList.indexOf(blankButton) / 4) % 2 == 1) {
            blankButtonRow = false;
        } else {
            blankButtonRow = true;
        }

        int inversionCount = 0;
        for (int i = 0; i < buttonList.size(); i++) {
            if (buttonList.get(i).getText().isEmpty()) {
                continue;
            }
            for (int x = i + 1; x < buttonList.size(); x++) {
                if (!buttonList.get(x).getText().isEmpty() && (Integer.parseInt(buttonList.get(i).getText())) > (Integer.parseInt(buttonList.get(x).getText()))) {
                    inversionCount++;
                }
            }
        }

        if ((!blankButtonRow && inversionCount % 2 == 0) || (blankButtonRow && inversionCount % 2 == 1)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean hasThePlayerWon(List<JButton> correctList, List<JButton> correctButtonList) {
        for (int i = 0; i < correctList.size(); i++) {
            if (!correctList.get(i).equals(correctButtonList.get(i))) {
                return false;
            }
        }
        return true;
    }
}
