#include "WordleSolver.h"

//This function just introduces the program
void introduce() {
    std::cout << "Welcome to the Wordle Solver!" << std::endl;
    std::cout << "Would you like to use the Wordle dictionary or the whole dictionary?" << std::endl;
}

//this function ends the program if we lose
//hopefully, the user adds any words that are answers that are not in the text file.
void endGame(){
    std::cout<< "Unfortunately, we did not win the game :(" << std::endl;
    std::cout<< "If you had guesses left, this is because the word is not in the word dictionary. If could do so, please add it in." << std::endl;
    std::cout<< "If we ran out of guesses, I am sorry, this WordleBot has failed you :C" << std::endl;
}

//this is a function to get the guess results from the user
//provides a form of exception handling to prevent invalid strings from being entered.
void getGuessResults(std::string &guessResults){
    std::cin >> guessResults;
    bool tryAgain = 0;
    if (guessResults.length() == 5){
        for (auto i : guessResults){
            if (i != 'w' && i != 'g' && i != 'y'){
                tryAgain = 1;
            }
        }
    } else tryAgain = 1;
    while (tryAgain){
        std::cout<< "Your response did not meet the criteria of a valid response. Please enter a 5 letter string with only w, y, and g's corresponding to their Wordle value." << std::endl;
        tryAgain = 0;
        std::cin >> guessResults;
        if (guessResults.length() == 5){
            for (auto i : guessResults){
                if (i != 'w' && i != 'g' && i != 'y'){
                    tryAgain = 1;
                }
            }
        } else tryAgain = 1;
    }
}

//This function grabs the dictionary file that is needed.
std::vector<std::string> getDictionary(){
    std::string data;
    std::vector<std::string> wordVector;
    std::ifstream fin;
    int choice = 0;
    std::cout << "1. Wordle\n2. Whole" << std::endl;
    std::cin >> choice;
    while ((choice != 1 && choice != 2) || std::cin.fail()) {
        std::cin.clear();
        std::cin.ignore(std::numeric_limits<std::streamsize>::max(),'\n');
        std::cout << "Oops!, you did not input 1 or 2. Please try again!!" << std::endl;
        std::cout << "1. Wordle\n2. Whole" << std::endl;
        std::cin >> choice;
    }
    switch (choice) {
        case 1:
            std::cout << "You chose the wordle dictionary" << std::endl;
            fin.open("Wordle_words.txt");
            if (fin.is_open()) {
                while (fin) {
                    getline(fin, data);
                    wordVector.push_back(data);
                }
            }
            break;
        case 2:
            std::cout << "You chose the whole dictionary" << std::endl;
            fin.open("word.txt");
            if (fin.is_open()) {
                while (fin) {
                    getline(fin, data);
                    wordVector.push_back(data);
                }
            }
            break;
    }
    std::cout << "Whenever you guess a word, enter the info into the bot in the following way." << std::endl;
    std::cout << "Gray letter: w. Yellow letter: y. Green letter: g." << std::endl;
    std::cout << "The entered string should look something like this: ygwww" << std::endl;
    std::cout << "\nThe first guess is:" << std::endl;
    return wordVector;
}