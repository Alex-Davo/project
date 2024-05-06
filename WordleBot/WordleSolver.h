#ifndef WORDLESOLVER_H
#define WORDLESOLVER_H

#include <iostream>
#include <limits>
#include <fstream>
#include <vector>
#include <algorithm>


//Seen in wordleAlgo.cpp
//This is a function that will create a vector that stores the information regarding a character.
//Because of ASCII values, you can index the Vector through using vector[charVal-'a]
//This is similar to a map, but because of the ways characters index themselve, we can save on some memory.
std::vector<double> createValVector();

//Seen in wordleAlgo.cpp
//this is pretty much a copy of the previous function. It is seperate and not templated because I want to make it clear when a
//vector<bool> is created as opposed to a vector<double>. While a vector<double> could do the same concept, it takes up loads more memory
//as opposed to a vector<bool>.
std::vector<bool> createBoolVector();

//Seen in wordleInpOut.cpp
//This function just introduces the program
void introduce();

//Seen in wordleInpOut.cpp
//this function ends the program if we lose
//hopefully, the user adds any words that are answers that are not in the text file.
void endGame();

//Seen in wordleInpOut.cpp
//this is a function to get the guess results from the user
//provides a form of exception handling to prevent invalid strings from being entered.
void getGuessResults(std::string &guessResults);

//Seen in wordleInpOut.cpp
//This function grabs the dictionary file that is needed.
std::vector<std::string> getDictionary();

//Seen in wordleAlgo.cpp
//This is the function that goes through and makes changes to the possible answer list.
//It goes through and makes sure any words that have the greens or yellows in thew wrong spot or
//grays in at all are removed. It then makes sure that any yellow letters are present in the words.
std::vector<std::string> updatePossAnswers(std::vector<char> greenArr, std::vector<char> yellowArr, std::vector<bool> nullChar, std::vector<std::string> answerVector);

//Seen in wordleAlgo.cpp
//This is where the game interaction happens. It accepts a string and updates the vectors
//that contain existing letters and non-existent letters.
//It calls another function to help prevent a user from inputting an invalid string.
void guessInteraction(std::string typedGuess, std::vector<bool> &nullChar, std::vector<bool> &existVector, std::vector<char> &greenArr, std::vector<char> &yellowArr, bool &gameOver);

//Seen in wordleAlgo.cpp
//Finds the "best" available word to guess using the information gathered.
std::string findWord(std::vector<std::string> wordVector, std::vector<double> sumVector, std::vector<std::string> answerVector);

//Seen in wordleAlgo.cpp
//This function prevents rewarded guesses for info that is already known.
//Yellows, Whites, and Greens are valued at zero.
void nullifyVector(std::vector<double> &sumVector, std::vector<bool> existVector);

//Seen in wordleAlgo.cpp
//This function sums up the value within the vectors, and then calls the other functions to perform their duties.
std::vector<std::string> findAnswer(std::vector<std::string> wordVector, std::vector<double> sumVector, std::vector<std::string> answerVector, bool &gameOver);

#endif