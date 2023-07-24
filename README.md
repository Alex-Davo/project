## _Wordle Bot, C++_

**What Is It**

This is a Wordle Bot I created in order to beat my friends in a daily competition. It is usually able to answer the word in 3-4 guess, making it a pretty efficient bot.

**How I Would Improve This**

As this is a personal project, a lot of the quality of life features are not up to snuff for a standard consumers use. However, in terms of the algorihtmic changes I would make next:
- Whenever there are only two possible answers that the Bot can find, it will always guess one of the answers. This is understandable, as two possible answers is a 50/50 guess, followed by a 100% gues, whereas if the bot guesses an outside word, it becomes a 0% guess into a 100% guess. However, there are times when there are more answers than just two where it still only makes sense to guess. An example of this would a final list of words that are: {blood, brood, flood, snood}. In this situation, a guess of blood would lead to a 25% chance of being correct, into a 100% chance of being correct in the following turn. My bot would choose something along the the lines of blast, ensuring the next turn is a 100% chance of an answer, but it loses out on the immediate chance. I don't know how often situations like this arise, but having this is an important optimization.
- In most of the top wordle bots, there first guess is usually between one of two words: Slate and Salet. My bot chooses the word Alert, which shares 4 of the same letters and has an R instead of an S. R actually appears in the wordle dictionary more total times than S by around 300 times (think error), while also appearing in seperate entries at a higher rate than S. However, S is the letter that starts the most amount of words in the entire dictionary and still appears in a large amount of seperate entries, giving a guess that starts with S a higher value than a guess that contains an R. Therefore, incorporating positional value within my bot will help it improve. 

## _Blackjack Game, Java_

**What Is It**

This is a simple Blackjack card game that is used to pit two human players against each other, with one CPU being the dealer. It starts off with asking each player privately for two numbers. These numbers are used to alter a players score so that, in theory, only the player who entered the numbers can figure out the score. This is not too disimilar to a [private key][key]. An example of this, using the two numbers 12 and 6 (although larger numbers are recommended in actual practice):
- Player one draws a faceup 7 and a facedown 2. His gamescore at the moment is 9. However, when it is displayed, it is shown as 21 (added the 12). The 6 is then subtracted from the amount (to become a 15). The player hits, and was given a jack (so a game score of 19). The next score is displayed as a 37 (22 added to 15). The player will stay, knowing he has a total of a 19, while the player he is playing against should be none the wiser.

While this implementation is not perfect, it does allow for players to be clued in on their numbers while playing side-by-side to a human opponent. After both people's turns, the dealer will make his turn, and the person closest to 21 without going over is the winner.


**How I Would Improve This**

While playing against another human is fun, I could certainly add more features within this program to expand its functionality. A man vs CPU mode is one of these things, with various difficulties based on how much the computer wants to cheat in order to win/alter difficulty.


## _Chess Engine, Python_


**What Is It**

It is a functional chess program that was created in Python. It utilizes several different algorithms, such as alpha-beta pruning minimax, depth first search, time limited search,  in order to try and find the most optimal set of moves for the board at hand. This script was created for a school project my senior year of college. It was used to teach about different types of algorithms that exist, while also requiring they be implemented in an actual sense. Although a lot of the implementations are less than perfect, it showcases and helps explain a lot of my knowledge on the matter.

**How I Would Improve Next Time**

There are many ways I would improve my chess AI, but there are two things that stick out to me more immediately than any other:
- When starting the script, we were allowed to choose any language to write in. Like many others, I chose to write in Python because I thought it would be the simplest one to write a larger project in. While this may have been true, almost all of the "best" Chess AI's are written in C++. This is due to the "speed" of C++ compared to other programming languages. C++ can go deeper into it's search at a similar time limit to something like Python, immmediately improving the results provided by the program. This [article][C++Speed] does a good job of demonstrating the magnitude of which this can help.
- Developing a heuristic function that works well within the confines of chess was less important within the class than implementing the search algorithms. However, a heuristic function that is able to give a proper understanding of who is winning a chess game is absolutely vital to a successful chess engine. My heuristic at the moment is basically who has more [value][PieceValue] available within their pieces. This is far too simplistic for a game as complex as chess. Things such as potential checkmates, possible piece captures, promotions, castling, and many others have to be considered. When all of those things are combined, a better picture of who is winning the game can be formed by the algorithm.

Now, this is only scratching the surface of what is possible within a chess engine, and many more things could be improved upon it. A few of these could be a quiescent search, move ordering, and null move pruning, as well as altering the way chess boards are stored (such as bitboards).

## _Home Server_

While not a programming project, one other thing that I have done is built a home server. At the current moment, it is used as a NAS, a game server, as well as a machine to play around in vritual machines with. While I still have a lot to explore with the machine (such as a email server), it has been a successful little project of mine.


 [key]: <https://www.techtarget.com/searchsecurity/definition/private-key> 
 [C++Speed]: <https://towardsdatascience.com/how-fast-is-c-compared-to-python-978f18f474c7> 
 [PieceValue]: <https://herculeschess.com/chess-piece-numerical-values/#:~:text=Point%20Value%20Of%20The%20Chess%20Pieces%20%20,%20%205%20%202%20more%20rows%20>
 
 
