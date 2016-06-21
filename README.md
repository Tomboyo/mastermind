#Mastermind Play Algorithm

The original draft of this program was developed by Tom Simmons (tomasimmons@gmail.com) as part of an undergraduate research project conducted at Virginia Wesleyan College (VWC), Norfolk, Virginia, USA, under the leadership of Dr. Audrey Malagon (amalagon@vwc.edu) and in conjunction with the efforts of co-researchers Tyler Chang (thchang@vwc.edu) and Samantha Eeanes (sceanes@vwc.edu). Our research objectives were to uncover the mathematical principles governing the 1970 board game [Mastermind](https://en.wikipedia.org/wiki/Mastermind_(board_game)) and the effect that permutations on the game's rules had on turns to win (e.g, the effects of varying the number of pegs or colors of pegs allowed in codes). The results of this research were presented at the 2014 Joint Mathematics Meetings (JMM) in Baltimore, Maryland, USA, under the title "The New Mastermind".

The program as presented on Git is a refactored version of the original code (it's more readable) that was used for research purposes. The program does not yet fully mirror the original design's capabilities (heuristics have not yet been implemented to improve algorithm speed). This draft aims to be as extensible and intelligible as possible while maintaining as much performance as can be wrangled.

#####Basic Usage
In the future, a Mastermind.java source file will be available demonstrating the implementation of the API. Currently, the program is run by instantiating a `Tree` object with a given number of pegs and colors, then calling its `compute` member. This method takes an optional boolean `log` parameter to inform the algorithm whether or not a complete print-out of the game is desired; if not, the algorithm will only output the minimum number of turns necessary to play the game regardless of what the master code was. The `compute` method directly returns turns to win. After computation, the game log can be accessed via `root.getGameTree().displayGameLog()` provided the `log` flag was set.

#####Interpreting Output
The following is example output from a game with 2 colors and 2 pegs:
`````
3

[1, 0][1, 0] -> [2, 0][2, 0] -> [2, 1][2, 2].
[1, 0][1, 0] -> [2, 0][1, 1] -> [0, 2][2, 2].
[1, 0][2, 0] -> [2, 0][1, 0] -> [1, 2][2, 2].
[1, 0][2, 0] -> [2, 0][2, 0] -> [0, 0][2, 2].
[1, 0][2, 0] -> [2, 0][0, 0] -> [1, 1][2, 2].
[1, 0][2, 0] -> [2, 0][2, 2].
[1, 0][0, 0] -> [2, 2][2, 2].
[1, 0][1, 1] -> [0, 1][2, 2].
[1, 0][2, 2].
`````
The first number indicates the number of turns within which one is guaranteed to win a game with the given number of pegs and colors. Below that are rows of the form
```
[code guessed][response received] → <next turn>
```
The code guessed is represented in array notation, where each number identifies the color of peg used. The immediately adjacent response is also in array notation, representing the number of pegs correctly guessed and the number of pegs in the incorrect position (as per traditional mastermind rules). Each 2 in this array indicates one peg in the guess is in the correct location (but note that the position of the 2 does not correspond to which peg is correct; 2s float to the left of the array), while each 1 indicates a peg in the incorrect position. Array elements with the value of 0 indicate no further response. When playing a game of mastermind, then, the above table indicates the player should do the following to win:

1. Make the guess corresponding to [1, 0].
2. If the feedback is \[1, 0\] (i.e, one peg is correct only), the player should then guess \[2, 0\] (indicated to the right, following the arrow).
3. If the feedback for this guess is [1, 1], the player should then guess [0, 2], which is the correct code.

#####Design
The algorithm generates this output by comparing each of the possible codes one could guess (currently, it takes the enumerative approach and evaluates each and every single one) against whatever codes could possibly be the answer. The answer codes are then categorized by the response they give to the guess, and the process starts again recursively. The most optimal plays are then reported. Note that a play *must* be able to arrive at any answer faster than any other play to be considered optimal by this program (selection criteria may be moved to a visitor object in the future to allow different rules, such as “best on average”.)
