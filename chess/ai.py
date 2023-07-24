# This is where you build your AI for the Chess game.

from joueur.base_ai import BaseAI
import random, copy, time

# <<-- Creer-Merge: imports -->> - Code you add between this comment and the end comment will be preserved between Creer re-runs.
# you can add additional import(s) here
# <<-- /Creer-Merge: imports -->>


class AI(BaseAI):
    """ The AI you add and improve code inside to play Chess. """

    @property
    def game(self) -> "games.chess.game.Game":
        """games.chess.game.Game: The reference to the Game instance this AI is playing.
        """
        return self._game  # don't directly touch this "private" variable pls

    @property
    def player(self) -> "games.chess.player.Player":
        """games.chess.player.Player: The reference to the Player this AI controls in the Game.
        """
        return self._player  # don't directly touch this "private" variable pls

    def get_name(self) -> str:
        """This is the name you send to the server so your AI will control the player named this string.

        Returns:
            str: The name of your Player.
        """
        # <<-- Creer-Merge: get-name -->> - Code you add between this comment and the end comment will be preserved between Creer re-runs.
        return "Chess Python Player"  # REPLACE THIS WITH YOUR TEAM NAME
        # <<-- /Creer-Merge: get-name -->>

    def start(self) -> None:
        """This is called once the game starts and your AI knows its player and game. You can initialize your AI here.
        """
        # <<-- Creer-Merge: start -->> - Code you add between this comment and the end comment will be preserved between Creer re-runs.
        # replace with your start logic
        # <<-- /Creer-Merge: start -->>

    def game_updated(self) -> None:
        """This is called every time the game's state updates, so if you are tracking anything you can update it here.
        """
        # <<-- Creer-Merge: game-updated -->> - Code you add between this comment and the end comment will be preserved between Creer re-runs.
        # replace with your game updated logic
        # <<-- /Creer-Merge: game-updated -->>

    def end(self, won: bool, reason: str) -> None:
        """This is called when the game ends, you can clean up your data and dump files here if need be.

        Args:
            won (bool): True means you won, False means you lost.
            reason (str): The human readable string explaining why your AI won or lost.
        """
        # <<-- Creer-Merge: end -->> - Code you add between this comment and the end comment will be preserved between Creer re-runs.
        # replace with your end logic
        # <<-- /Creer-Merge: end -->>

    def make_move(self) -> str:
        """This is called every time it is this AI.player's turn to make a move.

        Returns:
            str: A string in Universal Chess Interface (UCI) or Standard Algebraic Notation (SAN) formatting for the move you want to make. If the move is invalid or not properly formatted you will lose the game.
        """
        # <<-- Creer-Merge: makeMove -->> - Code you add between this comment and the end comment will be preserved between Creer re-runs.
        
        #declaration of variables used within main
        #some global variables to allow access to all functions
        global filespot
        global whitePieces
        global blackPieces
        global maxDepth
        global transposeTable
        transposeTable = []
        filespot = ["a", "b", "c", "d", "e", "f", "g", "h"]
        whitePieces = ["R", "P", "K", "Q", "N", "B"]
        blackPieces = ["r", "p", "k", "q", "n", "b"]
        maxDepth = 10
        currentDepth = 0
        #sets the player for the minmax function
        if self._player.color == 'white':
           player = 'w'
        else:
            player = 'b'
        #this is the iterative deeping portion of the code
        #it starts out at 0, then goes to a depth of 1, then to a depth
        #of 2
        maxTime = 1000000000
        startTimeVal = time.time_ns()
        timeVal = 0
        #this is the time limited IDMiniMax Function. Its a fairly basic time function
        #that allows a move to be checked if all the other mvoes have taken one second
        #ive found that this works pretty alright, as it will allow fairly deep search
        #in the range of 4-5 moves deep at the very start.
        for i in range(0, maxDepth):
            #this calls the minimax function with i as the goal depth. It sends -1000 as 
            #alpha, and 1000 as beta, and goes into check the moves.
            value, move = self.MiniMax(self.game.fen, i, player, currentDepth, -1000, 1000)
            timeVal = time.time_ns()
            timeVal = timeVal - startTimeVal
            if timeVal > maxTime:
                break
        print(move)
        return move
        # <<-- /Creer-Merge: makeMove -->>

    # <<-- Creer-Merge: functions -->> - Code you add between this comment and the end comment will be preserved between Creer re-runs.
    # if you need additional functions for your AI you can add them here
    
    #very basic heuristic that only bases who is winning on generic chess values
    def heur(self, fenstring) -> int:
        value = 0
        moveString = fenstring.split(' ')
        fenstring = moveString[0]
        for char in fenstring:
            if char == 'p':
                value -= 1
            elif char == 'P':
                value += 1
            elif char == 'n' or char == 'b':
                value -= 3
            elif char == 'N' or char == 'B':
                value += 3
            elif char == 'r':
                value -= 5
            elif char == 'R':
                value += 5
            elif char == 'q':
                value -= 9
            elif char == 'Q':
                value += 9
        return value
    
    #checks the very next move when there is a high likelihood of the horizon
    #effect
    def quiescentSearch(self, FenString, player):
        quietVal = self.heur(FenString)
        possibleMoves = self.IDDFChessMove(FenString)
        if player != 'w':
            maxVal = -1000
            #check all moves
            for i in possibleMoves:
                #get the value of the move
                value = self.heur(i[1])
                #if the value is equal to maxval, choose a random one
                if value == maxVal:
                    boolrand = bool(random.getrandbits(1))
                    if boolrand == 1:
                        move = i[0]
                elif value > maxVal:
                    maxVal = value
                    move = i[0]
            #return the value and the move
            return maxVal
        else:
            minVal = 1000
            #goes through all possible moves
            for i in possibleMoves:
                #gets the value of the move
                value = self.heur(i[1])
                #if value is equal to minval, choose a random one
                if value == minVal:
                    boolrand = bool(random.getrandbits(1))
                    if boolrand == 1:
                        move = i[0]
                elif value < minVal:
                    minVal = value
                    move = i[0]
            #return the value and the move
            return minVal
    
    
    #miniMax function. will accept a fenstring, maxdepth allowed for IDDFS,
    #which team, (black for min, white for max), and what the current Depth is
    def MiniMax(self, FenString, maxdepth, player, currDepth, alpha, beta):
        quietVal = self.heur(FenString)
        #gets all possible moves
        possibleMoves = self.IDDFChessMove(FenString)
        #random bool (if two values are the same, choose random one of two)
        boolrand = 0
        move = ''
        #if at the deepest value
        if currDepth == maxdepth:
            #if on white team
            if player == 'w':
                #set low max value
                maxVal = -1000
                #check all moves
                for i in possibleMoves:
                    #get the value of the move
                    value = self.heur(i[1])
                    #whenever a major move is made at the end of the depth, look
                    #for what happens just over the horizon
                    if value > quietVal:
                        value = self.quiescentSearch(i[1], player)
                    #if the value is equal to maxval, choose a random one
                    if value == maxVal:
                        boolrand = bool(random.getrandbits(1))
                        if boolrand == 1:
                            move = i[0]
                    elif value > maxVal:
                        maxVal = value
                        move = i[0]
                        if alpha < maxVal:
                            alpha = maxVal
                    if beta <= alpha:
                        break
                #return the value and the move
                return maxVal, move
            #if on blackteam
            else:
                #sets minval
                minVal = 1000
                #goes through all possible moves
                for i in possibleMoves:
                    #gets the value of the move
                    value = self.heur(i[1])
                    #whenever a major move is made at the end of the depth, look
                    #for what happens just over the horizon
                    if value < quietVal:
                        value = self.quiescentSearch(i[1], player)
                    #if value is equal to minval, choose a random one
                    if value == minVal:
                        boolrand = bool(random.getrandbits(1))
                        if boolrand == 1:
                            move = i[0]
                    elif value < minVal:
                        minVal = value
                        move = i[0]
                        if beta > minVal:
                            beta = minVal
                    if beta <= alpha:
                        break
                #return the value and the move
                return minVal, move
        #if white player and not at depth
        elif player == 'w':
            maxVal = -1000
            #sees if there is a possible move
            if len(possibleMoves) == 0:
                #if not, end the function
                return maxVal, ''
            move = possibleMoves[0][0]
            for i in possibleMoves:
                #recrusive minmax function to add depth
                value, minmaxMove = self.MiniMax(i[1], maxdepth, 'b', currDepth + 1, alpha, beta)
                if value == maxVal:
                    boolrand = bool(random.getrandbits(1))
                    if boolrand == 1:
                        move = i[0]
                elif value > maxVal:
                    maxVal = value
                    move = i[0]
                    if alpha < maxVal:
                        alpha = maxVal
                if beta <= alpha:
                    break
            return maxVal, move
        else:
            minVal = 1000
            #sees if there is a possible move
            if len(possibleMoves) == 0:
                #if not, end the function
                return minVal, ''
            move = possibleMoves[0][0]
            for i in possibleMoves:
                #recursive minmax function to add depth
                value, minmaxMove = self.MiniMax(i[1], maxdepth, 'w', currDepth + 1, alpha, beta)
                if value == minVal:
                    boolrand = bool(random.getrandbits(1))
                    if boolrand == 1:
                        move = i[0]
                elif value < minVal:
                    minVal = value
                    move = i[0]
                    if beta > minVal:
                        beta = minVal
                if beta <= alpha:
                    break
            return minVal, move
        
    #chess move function to generate a list of possible moves based on a fenstring
    def IDDFChessMove(self, FenString):
        board = {"a": [], "b": [], "c": [], "d": [], "e": [], "f": [], "g": [], "h": []}
        castleString, enPass, turnToMove = self.fillTeam(board, FenString)
        possMoves = []
        oppTeamPossibleMoves = []
        possibleValidMoves = []
        whiteTeam = []
        blackTeam = []
        pawnBool = 0
        #fills out the board
        #fills up the white and black teams positions and spaces
        for key in board:
            for value in board[key]:
                if value[0] in whitePieces:
                    newStr = str(value) + str(key)
                    whiteTeam.append(newStr)
                elif value[0] in blackPieces:
                    newStr = str(value) + str(key)
                    blackTeam.append(newStr)
        
        #enter here if you are the white team
        if turnToMove == 'w':
            #sets the team boolean, white is 0
            team = 0
            #calls the calc moves functions
            #an array with all the valid moves bar a potential check
            possMoves = self.calcMoves(
                board, whiteTeam, team, pawnBool, 0, castleString, enPass
            )
            #gets the opponents potential moves a the current state
            oppTeamPossibleMoves = self.calcOppMoves(
                oppTeamPossibleMoves, board, blackTeam, team, 1
            )
            #adds the ability to perform a castle
            possMoves = self.checkValidCastle(
                castleString, board, team, oppTeamPossibleMoves, possMoves
            )
            #iterates through all possible moves and only returns ones that
            #don't end in check
            for i in possMoves:
                #gets a copy of the other team
                tempBlackTeam = copy.deepcopy(blackTeam)
                #creates what the board would look like had the move been made
                tempBoard = self.updateTempBoard(board, i, team, tempBlackTeam)
                #gets the oppenents valid moves in the temp state
                oppTeamPossibleMoves = self.calcOppMoves(
                    oppTeamPossibleMoves, tempBoard, tempBlackTeam, team, 1
                )
                # if the king is not in check after the move, add the move
                #to valid moves
                if self.inCheck(tempBoard, team, oppTeamPossibleMoves) == 0:
                    fenStringForMove = self.turnToFen(tempBoard, FenString, i)
                    possibleValidMoves.append((i, fenStringForMove))
        
        #enter here if you are the black team
        else:
            #sets the team boolean, white is 0
            team = 1
            #calls the calc moves functions
            #an array with all the valid moves bar a potential check
            possMoves = self.calcMoves(
                board, blackTeam, team, pawnBool, 0, castleString, enPass
            )
            #gets the opponents potential moves a the current state
            oppTeamPossibleMoves = self.calcOppMoves(
                oppTeamPossibleMoves, board, whiteTeam, team, 1
            )
            
            possMoves = self.checkValidCastle(
                castleString, board, team, oppTeamPossibleMoves, possMoves
            )
            #iterates through all possible moves and only returns ones that
            #don't end in check
            for i in possMoves:
                #gets a copy of the other team
                tempWhiteTeam = copy.deepcopy(whiteTeam)
                #creates what the board would look like had the move been made
                tempBoard = self.updateTempBoard(board, i, team, tempWhiteTeam)
                #gets the oppenents valid moves in the temp state
                oppTeamPossibleMoves = self.calcOppMoves(
                    oppTeamPossibleMoves, tempBoard, tempWhiteTeam, team, 1
                )
                # if the king is not in check after the move, add the move
                #to valid moves
                if self.inCheck(tempBoard, team, oppTeamPossibleMoves) == 0:
                    fenStringForMove = self.turnToFen(tempBoard, FenString, i)
                    possibleValidMoves.append((i, fenStringForMove))
        #prints all valid mvoes, and selects one and prints and returns that
        return possibleValidMoves
        
    #this function was created so that all moves can use a FEN string to generate a
    #move
    def turnToFen(self, tempBoard, FenString, move):
        fenBoard = {
            "1": [],
            "2": [],
            "3": [],
            "4": [],
            "5": [],
            "6": [],
            "7": [],
            "8": [],
        }
        #creates a board in 1a formula instead of a1
        for value in tempBoard:
            for i in tempBoard[value]:
                fenBoard[i[1]].append(str(i[0]) + value)
        #this is a little complicated, but basically, this function creates a fen
        #string by checking if its a castle move
        appendString = ''
        movestring = '--------'
        counter = 0
        newString = FenString.split(' ')
        fenMoveString = newString[0].split('/')
        
        #enter if a castle
        if move[0] == 'O':
            #enter if on white team
            if newString[1] == 'w':
                #use the fenBoard from above to update string
                for j in fenBoard['1']:
                    pieceLoc = int(filespot.index(j[1]))
                    movestring = movestring[:pieceLoc] + str(j[0]) + movestring[pieceLoc+1:]
                for char in movestring:
                    if char == '-':
                        counter += 1
                    elif counter == 0:
                        appendString += char
                    else:
                        appendString += str(counter) + char
                        counter = 0
                if counter != 0:
                    appendString += str(counter)
                    counter = 0
                #makes the string into a format that is appendable to a FEN String
                fenMoveString[7] = str(appendString)
                #sets the turn to white
                newString[1] = 'b'
                #sets enpass to nothing
                newString[3] = '-'
                #updates castle possibilities
                if len(newString[2]) == 4:
                    newString[2] = 'kq'
                else:
                    newString[2] += '-'
                    if 'K' in newString[2]:
                        newString[2] = newString[2].replace('K', '')
                    if 'Q' in newString[2]:
                        newString[2] = newString[2].replace('Q', '')
                    if len(newString[2]) != 1:
                        newString[2] = newString[2].replace('-', '')
            else:
                #same thing as above except for black team
                for j in fenBoard['8']:
                    pieceLoc = int(filespot.index(j[1]))
                    movestring = movestring[:pieceLoc] + str(j[0]) + movestring[pieceLoc+1:]
                for char in movestring:
                    if char == '-':
                        counter += 1
                    elif counter == 0:
                        appendString += char
                    else:   
                        appendString += str(counter) + char
                        counter = 0
                if counter != 0:
                    appendString += str(counter)
                    counter = 0
                fenMoveString[1] = str(appendString)
                newString[1] = 'w'
                newString[3] = '-'
                if len(newString[2]) == 4:
                    newString[2] = 'KQ'
                else:
                    newString[2] += '-'
                    if 'k' in newString[2]:
                        newString[2] = newString[2].replace('k', '')
                    if 'q' in newString[2]:
                        newString[2] = newString[2].replace('q', '')
                    if len(newString[2]) != 1:
                        newString[2] = newString[2].replace('-', '')
        #if not a castle, enter here
        else:
            #if move stayed in same rank, only update one rank
            if move[2] == move[4]:
                for j in fenBoard[move[2]]:
                    pieceLoc = int(filespot.index(j[1]))
                    movestring = movestring[:pieceLoc] + str(j[0]) + movestring[pieceLoc+1:]
                for char in movestring:
                    if char == '-':
                        counter += 1
                    elif counter == 0:
                        appendString += char
                    else:
                        appendString += str(counter) + char
                        counter = 0
                if counter != 0:
                    appendString += str(counter)
                    counter = 0
                fenLoc = 8 - int(move[2])
                fenMoveString[fenLoc] = str(appendString)
                #this code updates the rank for append to FEN String
            #if chagnes rank during move, enter in here
            else:
                #updates the move for the first row of the FEN String
                for j in fenBoard[move[2]]:
                    pieceLoc = int(filespot.index(j[1]))
                    movestring = movestring[:pieceLoc] + str(j[0]) + movestring[pieceLoc+1:]
                for char in movestring:
                    if char == '-':
                        counter += 1
                    elif counter == 0:
                        appendString += char
                    else:
                        appendString += str(counter) + char
                        counter = 0
                if counter != 0:
                    appendString += str(counter)
                    counter = 0
                fenLoc = 8 - int(move[2])
                fenMoveString[fenLoc] = str(appendString)
                #gets ready for the second rank update
                movestring = '--------'
                appendString = ''
                counter = 0
                for j in fenBoard[move[4]]:
                    pieceLoc = int(filespot.index(j[1]))
                    movestring = movestring[:pieceLoc] + str(j[0]) + movestring[pieceLoc+1:]
                for char in movestring:
                    if char == '-':
                        counter += 1
                    elif counter == 0:
                        appendString += char
                    else:
                        appendString += str(counter) + char
                        counter = 0
                if counter != 0:
                    appendString += str(counter)
                    counter = 0
                fenLoc = 8 - int(move[4])
                fenMoveString[fenLoc] = str(appendString)
                #the FEN move string is updated, now check to see if we need
                #to update the castle availability based on mvoes
                if move[3:] == 'h8' and 'k' in newString[2]:
                    newString[2] = newString[2].replace('k', '')
                elif move[3:] == 'h1' and 'K' in newString[2]:
                    newString[2] = newString[2].replace('K', '')
                elif move[3:] == 'a8' and 'q' in newString[2]:
                    newString[2] = newString[2].replace('q', '')
                elif move[3:] == 'a1' and 'Q' in newString[2]:
                    newString[2] = newString[2].replace('Q', '')
                    
            #this code goes through and checks castling ability based on rook
            #and king movements
            if newString[1] == 'w':
                newString[1] = 'b'
                if move[0] == 'K':
                    if newString[2] == '-':
                        pass
                    elif len(newString[2]) == 4:
                        newString[2] = 'kq'
                    else:
                        newString[2] += '-'
                        if 'K' in newString[2]:
                            newString[2] = newString[2].replace('K', '')
                        if 'Q' in newString[2]:
                            newString[2] = newString[2].replace('Q', '')
                        if len(newString[2]) != 1:
                            newString[2] = newString[2].replace('-', '')
                elif move[0] == 'R':
                    if newString[2] == '-':
                        pass
                    elif move[1:3] == 'a1' and 'Q' in newString[2]:
                        newString[2] = newString[2].replace('Q', '')
                    elif move[1:3] == 'h1' and 'K' in newString[2]:
                        newString[2] = newString[2].replace('K', '')
                    if len(newString[2]) == 0:
                        newString[2] = '-'
                if move[0] == 'P' and move[4] == '4' and move[2] == '2':
                    newString[3] = str(move[1]) + str(3)
                else:
                    newString[3] = '-'
            else:
                newString[1] = 'w'
                if move[0] == 'k':
                    if newString[2] == '-':
                        pass
                    elif len(newString[2]) == 4:
                        newString[2] = 'kq'
                    else:
                        newString[2] += '-'
                        if 'k' in newString[2]:
                            newString[2] = newString[2].replace('k', '')
                        if 'q' in newString[2]:
                            newString[2] = newString[2].replace('q', '')
                        if len(newString[2]) != 1:
                            newString[2] = newString[2].replace('-', '')
                elif move[0] == 'r':
                    if newString[2] == '-':
                        pass
                    elif move[1:3] == 'a8' and 'q' in newString[2]:
                            newString[2] = newString[2].replace('q', '')
                    elif move[1:3] == 'h8' and 'k' in newString[2]:
                            newString[2] = newString[2].replace('k', '')
                    if len(newString[2]) == 0:
                        newString[2] = '-'
                if move[0] == 'p' and move[4] == '5' and move[2] == '7':
                    newString[3] = str(move[1]) + str(3)
                else:
                    newString[3] = '-'
        #updates the final FEN String for use in other code
        finalMoveString = ''
        for i in fenMoveString:
            finalMoveString += i + '/'
        finalMoveString = finalMoveString[:-1] + ' '
        finalMoveString += newString[1] + ' '
        finalMoveString += newString[2] + ' '
        finalMoveString += newString[3] + ' '
        finalMoveString += newString[4] + ' ' + newString[5]
        return finalMoveString
        
    #this function is essentially a copy function that also updates the array
    #to a new state
    def updateTempBoard(self, board, realMove, team, teamArray):
        tempBoard = {
            "a": [],
            "b": [],
            "c": [],
            "d": [],
            "e": [],
            "f": [],
            "g": [],
            "h": [],
        }
        
        #goes through and gets all the values from the FEN Board
        for value in board:
            for i in board[value]:
                tempBoard[value].append(i)
        #if queenside castle, do the move for that team
        if realMove == "O-O-O":
            if team == 0:
                tempBoard["a"].remove("R1")
                tempBoard["e"].remove("K1")
                tempBoard["c"].append("K1")
                tempBoard["d"].append("R1")
            else:
                tempBoard["a"].remove("r8")
                tempBoard["e"].remove("k8")
                tempBoard["c"].append("k8")
                tempBoard["d"].append("r8")
                
        #if kingside castle, do the move for that team
        elif realMove == "O-O":
            if team == 0:
                tempBoard["h"].remove("R1")
                tempBoard["e"].remove("K1")
                tempBoard["g"].append("K1")
                tempBoard["f"].append("R1")
            else:
                tempBoard["h"].remove("r8")
                tempBoard["e"].remove("k8")
                tempBoard["g"].append("k8")
                tempBoard["f"].append("r8")
        # if not a castle, go through and remove old location, and add new
        #location for temporary move
        else:
            removeString = str(realMove[0]) + str(realMove[2])
            tempBoard[realMove[1]].remove(removeString)
            #if a value is within your current spot, remove it
            for value in tempBoard[realMove[3]]:
                if value[1] == realMove[4]:
                    tempBoard[realMove[3]].remove(value)
                    teamArray.remove(
                        str(value[0]) + str(realMove[4]) + str(realMove[3])
                    )
            #add the new piece to the board
            if realMove[0] == 'p' and realMove[4] == '1':
                newPotentialVal = str(realMove[6]) + str(realMove[4])
            elif realMove[0] == 'P' and realMove[4] == '8':
                newPotentialVal = str(realMove[6]) + str(realMove[4])
            else:
                newPotentialVal = str(realMove[0]) + str(realMove[4])
            tempBoard[realMove[3]].append(newPotentialVal)
        #return the board
        return tempBoard
    
    #this function sees if it is possible to perform a castle. If so,
    #add the moves to a possible move
    def checkValidCastle(
        self, castleString, board, team, oppTeamPossibleMoves, possibleMoves
    ):
        bigKCastle = ["e1", "f1", "g1"]
        bigQCastle = ["e1", "d1", "c1", "b1"]
        littleKCastle = ["e8", "f8", "g8"]
        littleQCastle = ["e8", "d8", "c8", "b8"]
        if team == 0:
            #checks to see if kingside castle works
            #if ant spot is blocked or in check, say its not valid
            if "K" in castleString:
                canCastleBool = 1
                for i in bigKCastle:
                    if i in oppTeamPossibleMoves:
                        canCastleBool = 0
                    if i == 'e1':
                        pass
                    else:
                        for j in board[i[0]]:    
                            if j[1] == '1':
                                canCastleBool = 0
                if canCastleBool == 1:
                    possibleMoves.append("O-O")
            #checks to see if any white queenside castle works
            #if any spot is blocked or in check, say its not valid
            if "Q" in castleString:
                canCastleBool = 1
                for i in bigQCastle:
                    if i in oppTeamPossibleMoves:
                        if i == bigQCastle[3]:
                            pass
                        else:
                            canCastleBool = 0
                    if i == 'e1':
                        pass
                    else:
                        for j in board[i[0]]:
                            if j[1] == '1':
                                canCastleBool = 0
                if canCastleBool == 1:
                    possibleMoves.append("O-O-O")
        else:
            #checks to see if any white queenside castle works
            #if any spot is blocked or in check, say its not valid
            if "k" in castleString:
                canCastleBool = 1
                for i in littleKCastle:
                    if i in oppTeamPossibleMoves:
                        canCastleBool = 0
                    if i == 'e8':
                        pass
                    else:
                        for j in board[i[0]]:
                            if j[1] == '8':
                                canCastleBool = 0
                if canCastleBool == 1:
                    possibleMoves.append("O-O")
            #checks to see if any white queenside castle works
            #if any spot is blocked or in check, say its not valid
            if "q" in castleString:
                canCastleBool = 1
                for i in littleQCastle:
                    if i in oppTeamPossibleMoves:
                        if i == bigQCastle[3]:
                            pass
                        else:
                            canCastleBool = 0
                    if i == 'e8':
                        pass
                    else:
                        for j in board[i[0]]:
                            if j[1] == '8':
                                canCastleBool = 0
                if canCastleBool == 1:
                    possibleMoves.append("O-O-O")
        #return moves wth potential castles
        return possibleMoves
    
    #this move will collect all possible moves regardless of check by iterating
    #through a team array
    def calcMoves(self, board, teamArr, team, pawnBool, oppMove, castleString, enPass):
        newstr = ""
        realMove = ""
        allMoveArray = []
        for i in teamArr:
            newstr = str(i[0]) + str(i[2]) + str(i[1])
            possibleMoves = []
            #sends individual pieces to get their moves
            possibleMoves = self.validMove(board, i, team, pawnBool, oppMove, enPass)
            for j in possibleMoves:
                realMove = newstr + str(j)
                allMoveArray.append(realMove)
                realMove = ""
        #returns array of possible moves
        return allMoveArray
    
    #this function calculates the oppenents moves using a team array
    #it only stores values if it can take a piece at that movement
    #ie: a pawn moving straight is not recorded
    def calcOppMoves(self, oppTeamPossibleMoves, board, teamArr, team, oppMove):
        pawnBool = 1
        newTeam = 0
        oppTeamPossibleMoves = []
        if team == 0:
            newTeam = 1
        for i in teamArr:
            possibleMoves = []
            #similar thing to last function, except this one will only get moves
            #that can take a piece
            possibleMoves = self.validMove(board, i, newTeam, pawnBool, oppMove, "")
            for j in possibleMoves:
                if j not in oppTeamPossibleMoves:
                    oppTeamPossibleMoves.append(j)
        return oppTeamPossibleMoves

    #if an oppenent can attack the king in the current state, set a boolean
    #to true to signal that the move was not valid
    def inCheck(self, tempBoard, team, possibleMoves=[]) -> bool:
        # tries to see if the king is in check
        checkBool = 0
        for key in tempBoard:
            for i in tempBoard[key]:
                if i[0] == "k" and team == 1:
                    newStr = str(key) + i[1]
                    break
                elif i[0] == "K" and team == 0:
                    newStr = str(key) + i[1]
                    break
        #if king is in  a spot that can be reached by the enemy,
        #you are in check
        if newStr in possibleMoves:
            checkBool = 1
        return checkBool
    
    #has lists of valid moves for pieces, and will collect every possible move
    #that is available to that piece regardless of check
    def validMove(self, board, movestr, team, pawnBool, oppMove, enPass) -> [str]:
        # generates all possible moves for a piece
        bishop = ([1, 1], [-1, 1], [-1, -1], [1, -1])
        kandq = ([0, 1], [1, 1], [1, 0], [1, -1], [-1, 1], [-1, -1], [0, -1], [-1, 0])
        rook = ([0, 1], [-1, 0], [0, -1], [1, 0])
        knight = (
            [2, 1],
            [2, -1],
            [1, 2],
            [1, -2],
            [-1, -2],
            [-1, 2],
            [-2, -1],
            [-2, 1],
        )
        pic = movestr[0]
        possibleMoves = []
        file = filespot.index(movestr[2])
        rank = int(movestr[1])
        #if a pawn, go to special functions
        if pic == "p" or pic == "P":
            possibleMoves = self.getPawnMove(team, file, rank, board, pawnBool, enPass)
        #if queen, look for all moves a queen can make
        elif pic == "q" or pic == "Q":
            for i in kandq:
                keepGoing = 1
                tempfile = file
                temprank = rank
                while keepGoing == 1:
                    tempfile += i[0]
                    temprank += i[1]
                    keepGoing = self.seeIfEmpty(
                        team, board, tempfile, temprank, possibleMoves, oppMove
                    )
        #if bishop, look for all moves a bishop can make
        elif pic == "b" or pic == "B":
            for i in bishop:
                keepGoing = 1
                tempfile = file
                temprank = rank
                while keepGoing == 1:
                    tempfile += i[0]
                    temprank += i[1]
                    keepGoing = self.seeIfEmpty(
                        team, board, tempfile, temprank, possibleMoves, oppMove
                    )
        #if knight, look for all moves a knight can make
        elif pic == "n" or pic == "N":
            for i in knight:
                tempfile = file
                temprank = rank
                tempfile += i[0]
                temprank += i[1]
                keepGoing = self.seeIfEmpty(
                    team, board, tempfile, temprank, possibleMoves, oppMove
                )
        #if rook, look for all moves a rook can make
        elif pic == "r" or pic == "R":
            for i in rook:
                keepGoing = 1
                tempfile = file
                temprank = rank
                while keepGoing == 1:
                    tempfile += i[0]
                    temprank += i[1]
                    keepGoing = self.seeIfEmpty(
                        team, board, tempfile, temprank, possibleMoves, oppMove
                    )
        #if king, look for all moves a king can make
        elif pic == "k" or pic == "K":
            for i in kandq:
                tempfile = file
                temprank = rank
                tempfile += i[0]
                temprank += i[1]
                keepGoing = self.seeIfEmpty(
                    team, board, tempfile, temprank, possibleMoves, oppMove
                )
        #return array of possible moves
        return possibleMoves
    
    #this will fill the board and the enPass and Castle strings
    #it uses the FEN String provided
    def fillTeam(self, board, fenstring) -> None:
        file = 0
        rank = 8
        castleStr = ""
        enPass = ""
        turn = ""
        boardstr = fenstring
        boardStar = boardstr.split(" ")
        newBoard = boardStar[0]
        turn = boardStar[1]
        castleStr = boardStar[2]
        enPass = boardStar[3]
        
        #go through the FEN String, and updates values within the board
        for i in newBoard:
            if i in whitePieces or i in blackPieces:
                tempstring = ""
                tempstring += i
                tempstring += str(rank)
                tempVal = filespot[file]
                board[tempVal].append(tempstring)
                tempstring, tempVal = None, None
                file += 1
            elif i == "/":
                rank -= 1
                file = 0
            else:
                file += int(i)
        #return castleStr and enPass values
        return castleStr, enPass, turn
    
    #since pawns are wonky, and have a bunch of extra rules,
    #this function goes through and ensures that pawns move validly
    def getPawnMove(self, team, file, rank, board, pawnBool, enPass) -> [str]:
        newStr = ""
        possibleMoves = []
        #if on team white, enter
        if team == 0:
            if rank == 2:
                #all possible moves for a pawn from rank 2
                pawn = ([0, 1], [0, 2], [1, 1], [-1, 1])
                for value in pawn:
                    #update move
                    tempFile = file
                    tempRank = rank
                    tempFile += value[0]
                    tempRank += value[1]
                    rankArray = []
                    if tempFile < 0 or tempFile > 7:
                        continue
                    elif tempRank < 1 or tempRank > 8:
                        continue
                    #see if you can move diagonally to attack
                    for strings in board[filespot[tempFile]]:
                        if strings[-1] == str(tempRank):
                            if value[0] != 0:
                                if strings[0] in blackPieces:
                                    newStr += str(filespot[tempFile]) + str(tempRank)
                                    possibleMoves.append(newStr)
                                    newStr = ""
                        rankArray.append(int(strings[-1]))
                    #if looking for spaces that a pawn can attack, enter here
                    if (pawnBool == 1) and (value[0] != 0):
                        newStr += str(filespot[tempFile]) + str(tempRank)
                        if newStr not in possibleMoves:
                            possibleMoves.append(newStr)
                        newStr = ""
                    #if looking for spaces to move to without attacking, enter here
                    if tempRank not in rankArray and pawnBool != 1:
                        if value[0] == 0 and value[1] == 1:
                            newStr += str(filespot[tempFile]) + str(tempRank)
                            possibleMoves.append(newStr)
                            newStr = ""
                        else:
                            tempStr = str(filespot[tempFile]) + str(tempRank - 1)
                            if tempStr in possibleMoves:
                                newStr += str(filespot[tempFile]) + str(tempRank)
                                possibleMoves.append(newStr)
                                newStr = ""

            else:
                #all possible moves for a pawn not at start
                pawn = ([0, 1], [1, 1], [-1, 1])
                for value in pawn:
                    #update move
                    tempFile = file
                    tempRank = rank
                    tempFile += value[0]
                    tempRank += value[1]
                    rankArray = []
                    #stay in bounds
                    if tempFile < 0 or tempFile > 7:
                        continue
                    elif tempRank < 1 or tempRank > 8:
                        continue
                    #check for enPass
                    if (str(filespot[tempFile]) + str(tempRank)) == enPass:
                        possibleMoves.append(enPass)
                    #see if you can move diagonally to attack
                    for strings in board[filespot[tempFile]]:
                        if strings[-1] == str(tempRank):
                            if value[0] != 0:
                                if strings[0] in blackPieces:
                                    newStr += str(filespot[tempFile]) + str(tempRank)    
                                    possibleMoves.append(newStr)
                                    newStr = ""
                        rankArray.append(int(strings[-1]))

                    #if looking for spaces that a pawn can attack, enter here
                    if (pawnBool == 1) and (value[0] != 0):
                        newStr += str(filespot[tempFile]) + str(tempRank)
                        if newStr not in possibleMoves:
                            possibleMoves.append(newStr)
                        newStr = ""
                    
                    #if looking for spaces to move to without attacking, enter here
                    elif tempRank not in rankArray and pawnBool != 1:
                        if value[0] == 0:
                            newStr += str(filespot[tempFile]) + str(tempRank)
                            possibleMoves.append(newStr)
                            newStr = ""
        #if on team black enter
        else:
            if rank == 7:
                #all possible moves for a pawn from rank 7
                pawn = ([0, -1], [0, -2], [1, -1], [-1, -1])
                for value in pawn:
                    #update move
                    tempFile = file
                    tempRank = rank
                    tempFile += value[0]
                    tempRank += value[1]
                    rankArray = []
                    #stay in bounds
                    if tempFile < 0 or tempFile > 7:
                        continue
                    elif tempRank < 1 or tempRank > 8:
                        continue
                    #see if you can move diagonally to attack
                    for strings in board[filespot[tempFile]]:
                        if strings[-1] == str(tempRank):
                            if value[0] != 0:
                                if strings[0] in whitePieces:
                                    newStr += str(filespot[tempFile]) + str(tempRank)
                                    possibleMoves.append(newStr)
                                    newStr = ""
                        rankArray.append(int(strings[-1]))
                    #if looking for spaces that a pawn can attack, enter here
                    if (pawnBool == 1) and (value[0] != 0):
                        newStr += str(filespot[tempFile]) + str(tempRank)
                        if newStr not in possibleMoves:
                            possibleMoves.append(newStr)
                        newStr = ""
                    #if looking for spaces to move to without attacking, enter here
                    elif tempRank not in rankArray and pawnBool != 1:
                        if value[0] == 0 and value[1] == -1:
                            newStr += str(filespot[tempFile]) + str(tempRank)
                            possibleMoves.append(newStr)
                            newStr = ""
                        else:
                            tempStr = str(filespot[tempFile]) + str(tempRank + 1)
                            if tempStr in possibleMoves:
                                newStr += str(filespot[tempFile]) + str(tempRank)
                                possibleMoves.append(newStr)
                                newStr = ""
            else:
                #all possible moves for a pawn not at start
                pawn = ([0, -1], [1, -1], [-1, -1])
                for value in pawn:
                    #update move
                    tempFile = file
                    tempRank = rank
                    tempFile += value[0]
                    tempRank += value[1]
                    rankArray = []
                    #stay in bounds
                    if tempFile < 0 or tempFile > 7:
                        continue
                    elif tempRank < 1 or tempRank > 8:
                        continue
                    #check for enPass
                    if (str(filespot[tempFile]) + str(tempRank)) == enPass:
                        possibleMoves.append(enPass)
                    #check for pieces to attack
                    for strings in board[filespot[tempFile]]:
                        if strings[-1] == str(tempRank):
                            if value[0] == -1 or value[0] == 1:
                                if strings[0] in whitePieces:
                                    newStr += str(filespot[tempFile]) + str(tempRank)
                                    possibleMoves.append(newStr)
                                    newStr = ""
                        rankArray.append(int(strings[-1]))
                    #if looking for spaces that a pawn can attack, enter here
                    if (pawnBool == 1) and (value[0] != 0):
                        newStr += str(filespot[tempFile]) + str(tempRank)
                        if newStr not in possibleMoves:
                            possibleMoves.append(newStr)
                        newStr = ""
                    #if looking for spaces to move to without attacking, enter here
                    elif tempRank not in rankArray and pawnBool != 1:
                        if value[0] == 0:
                            newStr += str(filespot[tempFile]) + str(tempRank)
                            possibleMoves.append(newStr)
                            newStr = ""
        #promotions in case that happens
        whiteProm = ["Q"]
        blackProm = ["q"]
        if pawnBool != 1:
            #pick a random promotion value if at the right spot
            for i in range(len(possibleMoves)):
                if possibleMoves[i][-1] == "1":
                    promPiece = random.choice(blackProm)
                    possibleMoves[i] += "=" + promPiece
                elif possibleMoves[i][-1] == "8":
                    promPiece = random.choice(whiteProm)
                    possibleMoves[i] += "=" + promPiece
        #return possible mvoes
        return possibleMoves

    #this functions sole purpose is to see if the spot you took was empty
    def seeIfEmpty(self, team, board, file, rank, possibleMoves, oppMove) -> bool:
        newStr = ""
        #stay in bounds
        if file < 0 or file > 7:
            return 0
        elif rank < 1 or rank > 8:
            return 0
        #go through all values of a board
        for values in board[filespot[file]]:
            if int(values[-1]) == rank:
                if team == 0:
                    #if member on other team, add move, then stop
                    if values[0] in blackPieces:
                        newStr += str(filespot[file]) + str(rank)
                        possibleMoves.append(newStr)
                        return 0
                    #if looking for Oppositionmoves, then add teammate location
                    #else, it is not a valid move
                    elif values[0] in whitePieces:
                        if oppMove:
                            newStr += str(filespot[file]) + str(rank)
                            possibleMoves.append(newStr)
                            return 0
                        else:
                            return 0
                else:
                    #if member on other team, add move, then stop
                    if values[0] in whitePieces:
                        newStr += str(filespot[file]) + str(rank)
                        possibleMoves.append(newStr)
                        return 0
                    #if looking for Oppositionmoves, then add teammate location
                    #else, it is not a valid move
                    elif values[0] in blackPieces:
                        if oppMove:
                            newStr += str(filespot[file]) + str(rank)
                            possibleMoves.append(newStr)
                            return 0
                        else:
                            return 0
        #if havent returned yet, tell piece to continue searching
        #and add move to list
        newStr += str(filespot[file]) + str(rank)
        possibleMoves.append(newStr)
        return 1
    

    # <<-- /Creer-Merge: functions -->>
