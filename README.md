# 15-Puzzle Game (Java Swing)

This project is a desktop implementation of the classic 15-Puzzle game, built using Java Swing.
It includes shuffle logic that guarantees solvable configurations, real-time gameplay using a timer, and a persistent high-score system stored locally.

## Features:
### Gameplay

Classic 4×4 sliding puzzle (numbers 1–15 + blank tile).

Tiles move only when adjacent to the blank tile (up, down, left, right).

A solvable random layout is always generated using correct inversion-count logic.

### Timer

The timer starts when the user hits Start button.

Can be paused and resumed.

Stops automatically when the puzzle is solved.

### High Scores

Saved to a local file: highscores.txt.

Automatically sorted by fastest time.

Displayed on the right side of the UI in the format of <name> <mm:ss:SSS>.

### GUI

Built using Java Swing.

Three sections:

1/ Puzzle grid

2/ Controls (start, pause, resume, new game, timer)

3/ High score display

## Project Structure

The project has 4 classes and 1 text file:

1/ Main.java: creates instances of Spel

2/ Spel.java: Main game window & logic

3/ GameUtilities.java: Puzzle solvability + win-check logic

4/ HighScore.java: High score handling

5/ highscores.txt: Created automatically if missing and stores high scores. The file stores entries in the form: <name> <time_in_milliseconds>

## How to Run

Run the main program

This initial window will appear:
![](https://github.com/ngocmai-do/15BlockGame/blob/master/documentation/startwindow.png?raw=true)

Press New Game for the puzzle to appear and Start to start the timer (New Game also regenerates and gives a new random puzzle):

![](https://github.com/ngocmai-do/15BlockGame/blob/master/documentation/Gamestarted.png?raw=true)

When won, the time stops automatically and a win message appears:
![](https://github.com/ngocmai-do/15BlockGame/blob/master/documentation/winmessage.png?raw=true)

Highscore board is then updated:
![](https://github.com/ngocmai-do/15BlockGame/blob/master/documentation/Highscoreboardupdated.png?raw=true)

## Improvements to come

Ability to change the size of the puzzle


