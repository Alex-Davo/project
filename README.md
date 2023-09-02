## _Wordle Bot, C++_

**What Is It**

This is a Wordle Bot I created in order to beat my friends in a daily competition. It is usually able to answer the word in 3-4 guesss, making it a pretty efficient bot. It uses the two text files that are supplied in the folder with it as a dictionary of words that can be used.

**How I Would Improve This**

As this is a personal project, a lot of the quality of life features are not up to snuff for standard consumer use. It also relies on the word dictionaries I supplied being 100% accurate, and from my testing the Wordle dictionary and the dictionary my bot uses as a wordle dictionary are not the 100% accurate as of now. However, in terms of the algorithmic changes I would make next:
- Whenever there are only two possible answers that the Bot can find, it will always guess one of the answers. This is understandable, as two possible answers is a 50/50 guess, followed by a 100% guess, whereas if the bot guesses an outside word, it becomes a 0% guess into a 100% guess. However, there are times when there are more answers than just two where it still only makes sense to guess. An example of this would be a final list of words that are: {blood, brood, flood, snood}. In this situation, a guess of blood would lead to a 25% chance of being correct, into a 100% chance of being correct in the following turn. My bot would choose something along the lines of blast, ensuring the next turn is a 100% chance of an answer, but it loses out on the immediate chance. I don't know how often situations like this arise, but having this is an important optimization.
- In most of the top wordle bots, the first guess is usually between one of two words: Slate and Salet. My bot chooses the word Stare as its starting guess. While this starting guess shares a lot of traits with these, it is less optimal compared to these two. This is because my bot does not incorporate positional value as completely as a more optimal bot. It really only looks into the positional value of the first letter, which causes my bot to be more likely to settle for a yellow, as opposed to a more desirable green.

## _Chess Engine, Python_


**What Is It**

It is a functional chess program that was created in Python. It utilizes several different algorithms, such as alpha-beta pruning minimax, depth first search, time limited search,  in order to try and find the most optimal set of moves for the board at hand. This script was created for a school project my senior year of college. It was used to teach about different types of algorithms that exist, while also requiring they be implemented in an actual sense. Although a lot of the implementations are less than perfect, it showcases and helps explain a lot of my knowledge on the matter.

**How I Would Improve Next Time**

There are many ways I would improve my chess AI, but there are two things that stick out to me more immediately than any other:
- When starting the script, we were allowed to choose any language to write in. Like many others, I chose to write in Python because I thought it would be the simplest one to write a larger project in. While this may have been true, almost all of the "best" Chess AI's are written in C++. This is due to the "speed" of C++ compared to other programming languages. C++ can go deeper into its search at a similar time limit to something like Python, immediately improving the results provided by the program. This [article][C++Speed] does a good job of demonstrating the magnitude of which this can help.
- Developing a heuristic function that works well within the confines of chess was less important within the class than implementing the search algorithms. However, a heuristic function that is able to give a proper understanding of who is winning a chess game is absolutely vital to a successful chess engine. My heuristic at the moment is basically who has more [value][PieceValue] available within their pieces. This is far too simplistic for a game as complex as chess. Things such as potential checkmates, possible piece captures, promotions, castling, and many others have to be considered. When all of those things are combined, a better picture of who is winning the game can be formed by the algorithm.

Now, this is only scratching the surface of what is possible within a chess engine, and many more things could be improved upon it. A few of these could be adding quiescent search, move ordering, and null move pruning, as well as altering the way chess boards are stored (such as bitboards).


## _Home Server_

While not a programming project, one other thing that I have done is built a home server. At the current moment, it is used as a NAS, a game server, as well as a machine to play around in virtual machines with. While I still have a lot to explore with the machine (such as an email server), it has been a successful little project of mine.


 [key]: <https://www.techtarget.com/searchsecurity/definition/private-key> 
 [C++Speed]: <https://towardsdatascience.com/how-fast-is-c-compared-to-python-978f18f474c7> 
 [PieceValue]: <https://herculeschess.com/chess-piece-numerical-values/#:~:text=Point%20Value%20Of%20The%20Chess%20Pieces%20%20,%20%205%20%202%20more%20rows%20>
 
 
