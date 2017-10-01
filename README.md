Mastermind Play Algorithm
-------------------------

The original draft of this program was developed by Tom Simmons (tomasimmons@gmail.com) as part of an undergraduate research project conducted at Virginia Wesleyan College (VWC), Norfolk, Virginia, USA, under the leadership of Dr. Audrey Malagon (amalagon@vwc.edu) and in conjunction with the efforts of co-researchers Tyler Chang (thchang@vwc.edu) and Samantha Eeanes (sceanes@vwc.edu). Our research objectives were to uncover the mathematical principles governing the 1970 board game [Mastermind](https://en.wikipedia.org/wiki/Mastermind_(board_game)) and the effect that permutations on the game's rules had on turns to win (e.g, the effects of varying the number of pegs or colors of pegs allowed in codes). The results of this research were presented at the 2014 Joint Mathematics Meetings (JMM) in Baltimore, Maryland, USA, under the title "The New Mastermind".

The program as presented on Git is a refactored version of the original code that was used for research purposes. The program does not yet fully mirror the original design's capabilities; the original did precisely one thing relatively quickly and at the expense of flexibility and readability. This draft aims to be as extensible and intelligible as possible while maintaining as much performance as can be wrangled.

Basic Usage
-----------
This project uses Apache Maven to manage dependencies and the build process. To build, call `mvn clean install` from the root of the project. To run the provided example configuration, call `java -Dedu.vwc.mastermind.config="../examples/mastermind.xml" -jar mastermind-0.0.1-SNAPSHOT-jar-with-dependencies.jar`from the target directory. This will build a strategy tree for a game with 3 colors and 2 pegs.

Interpreting Output
-------------------
The program generates strategy trees for playing Mastermind. They indicate the maximum number of turns it should take to win regardless of what the ultimate answer is, and provide the precise steps a player can make to win in the prescribed number of turns. The following is example output from a game with 3 colors and 2 pegs:
`````
Strategy tree maximum turns: 3
[1, 0]->[1, 0, 1][2, 0]->[1, 0, 1][0, 0]
[1, 0]->[1, 0, 1][2, 0]->[0, 0, 2][1, 1]
[1, 0]->[1, 0, 1][2, 0]->[0, 1, 1][1, 2]
[1, 0]->[0, 2, 0][0, 1]
[1, 0]->[0, 1, 1][0, 0]->[0, 0, 2][2, 1]
[1, 0]->[0, 1, 1][0, 0]->[1, 0, 1][0, 2]
[1, 0]->[0, 0, 2][2, 2]

`````
The first line indicates the maximum number of turns it should take to win a game in 3 colors and 2 pegs using the strategy the program has devised. Below that are rows of the form `[code guessed] -> [response received] <next code guessed> -> ...` which indicate the sequence of guesses a player should make in response to what feedback in order to win. The first bracket, `[code guessed]`, represents a code one could guess in array notation, where the integer in each position in the array represents a peg of a particular color. In the diagram, the strategy recommends making the guess `[1, 0]` first. The next column, `[response received]`, indicates what response the player may receive after making the preceding guess. A response is represented by three numbers, which indicate how many pegs in the guess lined up with the answer code ("correct" or "exact"), how many of the remaining pegs were the correct color but in the wrong position ("misplaced" or "inexact"), and finally how many pegs garnered no feedback ("wrong"), in that order. In the diagram above, after guessing `[1, 0]` the player may be informed that one peg in the guess was correct, none were misplaced, and one was incorrect (`[1, 0, 1]`). If this is the case, they should proceed to guess `[2, 0]`. If instead the player was informed that no pegs were exact, two were misplaced, and none were wrong (`[0, 2, 0]`), the player should then guess `[0, 1]`, which would be the answer in this case.

To clarify, each line of output is a distinct sequence of events that transpire in one particular play of Mastermind. When using the strategy tree to play a game, the player uses the feedback from their successive guesses to narrow down which particular line of output to follow. Multiple lines suggest the same initial set of moves (in the example above, three lines suggest guessing `[1, 0]` followed by `[2, 0]`), but all eventually differentiate with successive feedback.

Note that some lines are intentionally absent from the strategy tree. Some codes, such as `[1, 0]` in the above strategy guide, are suggested as a guess to the player in every scenario where they could possibly be an answer. These need not be specifically indicated, so they are not. Additionally, a “correct” response code like `[2, 0, 0]` is never printed in the output, since the termination of a line of output is sufficient to indicate that the last guess is correct.
