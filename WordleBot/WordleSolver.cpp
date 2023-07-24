#include <iostream>
#include <limits>
#include <fstream>
#include <vector>
#include <map>
#include <algorithm>

using namespace std;

//This is a typename function that returns either a char, bool map or a char, int map
//there are used for sumMap (char, int) and existMap/nullChar (char, bool)
template <typename A>
map<char, A> createMap(A val){
    map<char, A> returnMap;
    returnMap = {
        {'a', 0},
        {'b', 0},
        {'c', 0},
        {'d', 0},
        {'e', 0},
        {'f', 0},
        {'g', 0},
        {'h', 0},
        {'i', 0},
        {'j', 0},
        {'k', 0},
        {'l', 0},
        {'m', 0},
        {'n', 0},
        {'o', 0},
        {'p', 0},
        {'q', 0},
        {'r', 0},
        {'s', 0},
        {'t', 0},
        {'u', 0},
        {'v', 0},
        {'w', 0},
        {'x', 0},
        {'y', 0},
        {'z', 0}
    };
    return returnMap;
}

//This is the function that goes through and makes changes to the possible answer list.
//It goes through and makes sure any words that have the greens or yellows in thew wrong spot or
//grays in at all are removed. It then makes sure that any yellow letters are present in the words.
vector<string> updatePossAnswers(vector<char> greenArr, vector<char> yellowArr, map<char,bool> nullChar, vector<string> answerVector) {
    vector<string> updatedAnswerVector;
    bool invalid = 0;
    bool match = 0;
    int count = 0;
    int size = 0;
    for (string i: answerVector) {
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
            else if (nullChar[i[j]] == 1){
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
        for (string i: updatedAnswerVector) {
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

//This is where the game interaction happens. It accepts a string and updates the maps
//that contain existing letters and non-existent letters
void gameInteraction(string typedGuess, map<char, bool> &nullChar, map<char,bool> &existMap,
                    vector<char> &greenArr, vector<char> &yellowArr, bool &gameOver) {
    string guessResults;
    greenArr = {'_', '_', '_', '_', '_'};
    yellowArr = {'_', '_', '_', '_', '_'};
    cin >> guessResults;
    cout << endl;
    if (guessResults == "ggggg") {
        gameOver = 1;
        cout << "Congrats! We have won the game!!" << endl;
    }
    //goes through and updates the corresponding maps that need it
    for (int i = 0; i < 5; i++) {
        if (guessResults[i] == 'g') {
            greenArr[i] = typedGuess[i];
            existMap[typedGuess[i]] = 1;
            nullChar[typedGuess[i]] = 0;
        }
        else if (guessResults[i] == 'y') {
            yellowArr[i] = typedGuess[i];
            existMap[typedGuess[i]] = 1;
        }
        else {
            if (existMap[typedGuess[i]] != 1) {
                nullChar[typedGuess[i]] = 1;
            } 
        }
    }
}

//Finds the "best" available word to guess using the information gathered.
string findWord(vector<string> wordVector, map<char, int> sumMap, vector<string> answerVector) {
    static vector<string> prevWord = {};
    int count = 0;
    int value = -1;
    bool foundWord = 0;
    bool foundChar = 0;
    string guessString;
    string tempString;

    //if there are only 2 possible answers, guess one of them
    if (answerVector.size() <= 2) {
        cout << answerVector[0] << endl;
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
                count += (sumMap[c]);
            }
            tempString += c;
        }
        if (count > value && !foundWord) {
            value = count;
            guessString = wordVector[i];
        }
    }
    cout << guessString << endl;
    prevWord.push_back(guessString);
    return guessString;
}

//This function prevents rewarded guesses for info that is already known.
//Yellows, Whites, and Greens are valued at zero.
void nullifyMap(map<char, int> &sumMap, map<char, bool> existMap) {
    map<char,int>::iterator it;
    for(it=sumMap.begin(); it!=sumMap.end();++it){
        if (existMap[it->first] == 1) {
            sumMap[it->first] = 0;
        }
    }
}

//This function sums up the value within the map, and then calls the other functions to perform their duties.
vector<string> findAnswer(vector<string> wordVector, map<char, int> sumMap, vector<string> answerVector, bool &gameOver) {
    bool indicator;
    static map<char, bool> nullChar = createMap(indicator);
    static map<char, bool> existMap = createMap(indicator);
    vector<char> greenArr;
    vector<char> yellowArr;
    string bestGuess;
    for (string i: answerVector) {
        for (int j = 0; j < 5; j++) {
            if (j == 0) {
                sumMap[i[j]]++;
            }
            sumMap[i[j]]++;
        }
    }
    nullifyMap(sumMap, existMap);
    bestGuess = findWord(wordVector, sumMap, answerVector);
    gameInteraction(bestGuess, nullChar, existMap, greenArr, yellowArr, gameOver);
    answerVector = updatePossAnswers(greenArr, yellowArr, nullChar, answerVector);
    return answerVector;
}

//This function just introduces the program
void introduce() {
    cout << "Welcome to the Wordle Solver!" << endl;
    cout << "Would you like to use the Wordle dictionary or the whole dictionary?" << endl;
}

//This function grabs the dictionary file that is needed.
vector<string> getDictionary(){
    string data;
    vector<string> wordVector;
    ifstream fin;
    int choice = 0;
    cout << "1. Wordle\n2. Whole" << endl;
    cin >> choice;
    while ((choice != 1 && choice != 2) || cin.fail()) {
        cin.clear();
        cin.ignore(numeric_limits<streamsize>::max(),'\n');
        cout << "Oops!, you did not input 1 or 2. Please try again!!" << endl;
        cout << "1. Wordle\n2. Whole" << endl;
        cin >> choice;
    }
    switch (choice) {
        case 1:
            cout << "You chose the wordle dictionary" << endl;
            fin.open("Wordle_words.txt");
            if (fin.is_open()) {
                while (fin) {
                    getline(fin, data);
                    wordVector.push_back(data);
                }
            }
            break;
        case 2:
            cout << "You chose the whole dictionary" << endl;
            fin.open("word.txt");
            if (fin.is_open()) {
                while (fin) {
                    getline(fin, data);
                    wordVector.push_back(data);
                }
            }
            break;
    }
    cout << "Whenever you guess a word, enter the info into the bot in the following way." << endl;
    cout << "Gray word: w. Yellow word: y. Green word: g." << endl;
    cout << "The entered string should look something like this: ygwww" << endl;
    return wordVector;
}

//This is the main function. It is used to start the program and initialize some variables
int main() {
    bool gameOver = 0;
    int counter = 0;
    map<char, int> letterMap;
    vector<string> wordVector;
    vector<string> answerVector;
    introduce();
    letterMap = createMap(counter);
    wordVector = getDictionary();
    answerVector = wordVector;
    while (!gameOver){
        if (counter == 6) {
            cout << "We did not win!" << endl;
            gameOver = 1;
        }
        else { 
            answerVector = findAnswer(wordVector, letterMap, answerVector, gameOver);
        }
        counter++;
    }
}   