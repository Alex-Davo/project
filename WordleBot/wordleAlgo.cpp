#include "WordleSolver.h"

//This is a function that will create a vector that stores the information regarding a character.
//Because of ASCII values, you can index the Vector through using vector[charVal-'a]
//This is similar to a map, but because of the ways characters index themselve, we can save on some memory.
std::vector<long> createValVector(){
    std::vector<long> a (26);
    long val = 0;
    std::fill(a.begin(), a.end(), val);
    return a;
}

//this is pretty much a copy of the previous function. It is seperate and not templated because I want to make it clear when a
//vector<bool> is created as opposed to a vector<long>. While a vector<long> could do the same concept, it takes up loads more memory
//as opposed to a vector<bool>.
std::vector<bool> createBoolVector(){
    std::vector<bool> a (26);
    bool val = 0;
    std::fill(a.begin(), a.end(), val);
    return a;
}

//This is the function that goes through and makes changes to the possible answer list.
//It goes through and makes sure any words that have the greens or yellows in thew wrong spot or
//grays in at all are removed. It then makes sure that any yellow letters are present in the words.
std::vector<std::string> updatePossAnswers(std::vector<char> greenArr, std::vector<char> yellowArr, std::vector<bool> nullChar, std::vector<std::string> answerVector) {
    std::vector<std::string> updatedAnswerVector;
    bool invalid = 0;
    bool match = 0;
    int count = 0;
    int size = 0;
    for (std::string i: answerVector) {
        invalid = 0;
        for (int j = 0; j < 5; j++) {
            //makes sure green is in right spot
            if (greenArr[j] != '_' && greenArr[j] !=  i[j]) {
                invalid = 1;
            }
            //makes sure yellow is not in wrong spot
            else if (yellowArr[j] != '_' && yellowArr[j] == i[j]) {
                invalid = 1;
            }
            //makes sure there isn't a null char within this
            else if (nullChar[i[j]-'a'] == 1){
                invalid = 1;
            }
        }
        //updates the list if it passes this test
        if (!invalid) {
            updatedAnswerVector.push_back(i);
        }
    }
    answerVector = {};
    //gets rid of all '_' so it only checks for words
    yellowArr.erase(remove(yellowArr.begin(), yellowArr.end(), '_'), yellowArr.end());
    size = yellowArr.size();
    //if there are yellow letters in the guessed string make sure all possible words left contain them
    if (size != 0) {
        for (std::string i: updatedAnswerVector) {
            count = 0;
            for (int j = 0; j < size; j++) {
                match = 0;
                for (int k = 0; k < 5; k++) {
                    if (yellowArr[j] == i[k]) {
                        match = 1;
                    } 
                }
                if (match == 1) {
                    count++;
                }
            }
            if (count == size) {
                answerVector.push_back(i);
            }
        }
        return answerVector;
    }
    else {
        return updatedAnswerVector;
    }
}

//This is where the game interaction happens. It accepts a string and updates the vectors
//that contain existing letters and non-existent letters.
//It calls another function to help prevent a user from inputting an invalid string.
void guessInteraction(std::string typedGuess, std::vector<bool> &nullChar, std::vector<bool> &existVector,
                    std::vector<char> &greenArr, std::vector<char> &yellowArr, bool &gameOver) {
    std::string guessResults;
    greenArr = {'_', '_', '_', '_', '_'};
    yellowArr = {'_', '_', '_', '_', '_'};
    //this is the function to get a error-proof string back from the user.
    getGuessResults(guessResults);
    std::cout << std::endl;
    //if we answered with the right word, we have won the game.
    if (guessResults == "ggggg") {
        gameOver = 1;
        std::cout << "Congrats! We have won the game!!" << std::endl;
    }
    //goes through and updates the corresponding vectors that need it
    for (int i = 0; i < 5; i++) {
        if (guessResults[i] == 'g') {
            greenArr[i] = typedGuess[i];
            existVector[typedGuess[i]-'a'] = 1;
            nullChar[typedGuess[i]-'a'] = 0;
        }
        else if (guessResults[i] == 'y') {
            yellowArr[i] = typedGuess[i];
            existVector[typedGuess[i]-'a'] = 1;
        }
        else {
            if (existVector[typedGuess[i]-'a'] != 1) {
                nullChar[typedGuess[i]-'a'] = 1;
            } 
        }
    }
}

//Finds the "best" available word to guess using the information gathered.
std::string findWord(std::vector<std::string> wordVector, std::vector<long> sumVector, std::vector<std::string> answerVector) {
    static std::vector<std::string> prevWord = {};
    int count = 0;
    int value = -1;
    bool foundWord = 0;
    bool foundChar = 0;
    std::string guessString;
    std::string tempString;
    //if there are only 2 possible answers, guess one of them
    if (answerVector.size() <= 2) {
        std::cout << answerVector[0] << std::endl;
        return answerVector[0];
    }

    //if there are more than 2 answers, look for the guess that will trim the list the most.
    for (int i = 0; i < wordVector.size(); i++) {
        foundWord = 0;
        count = 0;
        tempString.clear();
        //make sure you haven't guessed this word before
        if (find(begin(prevWord), end(prevWord), wordVector[i]) != end(prevWord)) {
            foundWord = 1;
        }
        for (char &c: wordVector[i]) {
            foundChar = 0;
            //don't give extra value to words that have the same letter twice
            for (int j = 0; j < tempString.length(); j++) {
                if (tempString[j] == c) {
                    foundChar = 1;
                }
            }
            //this makes sure the value is not added twice
            if (!foundChar){
                count += (sumVector[c-'a']);
            }
            tempString += c;
        }
        if (count > value && !foundWord) {
            value = count;
            guessString = wordVector[i];
        }
    }
    //this prints the best guess
    std::cout << guessString << std::endl;
    prevWord.push_back(guessString);
    return guessString;
}


//This function prevents rewarded guesses for info that is already known.
//Yellows, Whites, and Greens are valued at zero.
void nullifyVector(std::vector<long> &sumVector, std::vector<bool> existVector) {
    //for loop checks through all 26 values in the vector (better thought of as the 26 letters in the alphabet)
    for(int i = 0; i < 26; i++){
        if (existVector[i] == 1) {
            sumVector[i] = 0;
        }
    }
}

//This function sums up the value within the vectors, and then calls the other functions to perform their duties.
std::vector<std::string> findAnswer(std::vector<std::string> wordVector, std::vector<long> sumVector, std::vector<std::string> answerVector, bool &gameOver) {
    static std::vector<bool> nullChar = createBoolVector();
    static std::vector<bool> existVector = createBoolVector();
    std::vector<char> greenArr;
    std::vector<char> yellowArr;
    std::string bestGuess;

    //this is the beginning of our search for the perfect word. We count all valid answers that are left, and we use that information to gauge
    //which letters are going to reveal the most information
    //also, the letters that start a word are deemed more valuable
    for (std::string i: answerVector) {
        for (int j = 0; j < 5; j++) {
            if (j == 0) {
                sumVector[i[j]-'a']++;
            }
            sumVector[i[j]-'a']++;
        }
    }
    //this calls a function that will zero out values that we have already checked for
    nullifyVector(sumVector, existVector);

    //findWord is the function that determine the value of all the possible words, and then uses that to return what the algorithm deems to be most valuable word.
    //in other words, this function below is the search algorithm
    bestGuess = findWord(wordVector, sumVector, answerVector);
    //this function is used by the program to gather the information needed from the guess.
    guessInteraction(bestGuess, nullChar, existVector, greenArr, yellowArr, gameOver);
    //this function updates the possible answers
    answerVector = updatePossAnswers(greenArr, yellowArr, nullChar, answerVector);
    return answerVector;
}
