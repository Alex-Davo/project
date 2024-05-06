#include "WordleSolver.h"

//This is the main function. It is used to start the program and initialize some variables. It has the general framework of the code.
//To keep the main clean and organized, wordleAlgo is where most of the search algorithm happens, and wordleInpOut is where most of the 
//I/O is happening. To compile, all three .cpp files, the header WordleSolver.h, and the word.txt and Wordle_words.txt are needed.
int main() {
    bool gameOver = 0;
    int counter = 0;
    std::vector<double> letterVector;
    std::vector<std::string> wordVector;
    std::vector<std::string> answerVector;
    introduce();
    letterVector = createValVector();
    wordVector = getDictionary();
    answerVector = wordVector;
    while (!gameOver){
        if (counter == 6) {
            endGame();
            gameOver = 1;
        }
        else { 
            answerVector = findAnswer(wordVector, letterVector, answerVector, gameOver);
            if (answerVector.size() == 0){
                endGame();
                gameOver=1;
            }
        }
        counter++;
    }
}   