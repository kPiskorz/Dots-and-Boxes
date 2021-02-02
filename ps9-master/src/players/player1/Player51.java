// file: Player*.java
// authors: Katelyn Goglia & Kamil Piskorz
// date: April 29, 2018
//
// purpose: A client which makes moves in dots & boxes.


package players.player1;

import players.*;
import board.*;
import util.*;

import java.util.*;
import javafx.scene.paint.Color;

public class Player51 implements Player {

    private DBG dbg;

    public Player51() {
        dbg = new DBG(DBG.PLAYERS, "Player1");
    }


    public Line makePlay(Board board, Line oppPlay, long timeRemaining) {

        if (board.gameOver())
            return null;

    //STEP 1: (best case) completes 2 squares at same time
        //find the squares with 3 marked sides
        Set<Square> marked3SidesSquares = board.squaresWithMarkedSides(3);
        //iterate through these squares
        Iterator<Square> squareIterator = marked3SidesSquares.iterator();
        while (squareIterator.hasNext()) {
            Square square = squareIterator.next(); // the square with three sides that we're testing
            Square adjoining = null;  //sets variable for potential adjoining square
            int row = square.getRow(); //define row, found in SquareC.java
            int col = square.getCol(); //define column, found in SquareC.java
            //find which side is missing, and check to see if adjacent square is also only missing that side
            if (! square.sideIsMarked(Side.NORTH) && (row != 0) ) {
                adjoining = board.getSquare(row - 1, col);
            }
            else if (! square.sideIsMarked(Side.EAST) && (col != (Util.N - 1)) ) {
                adjoining = board.getSquare(row, col + 1);
            }
            else if (! square.sideIsMarked(Side.SOUTH) && (row != (Util.N - 1)) ) {
                adjoining = board.getSquare(row + 1, col );
            }
            else if (! square.sideIsMarked(Side.WEST) && (col != 0) ) {
                adjoining = board.getSquare(row, col - 1 );
            }
            if (adjoining == null)  continue; // if there is no adjoining square, then continue to the next square with three sides to test
            //if adjoining square also has 3 lines complete, add line to complete both squares
            if (adjoining.hasThreeSides()) {
              return square.openLines().iterator().next();
            }
        } //end while loop

    //STEP 2: (second best case) completes 1 square, also becoming third line on adjoining square
        //find the squares with 3 marked sides
        Set<Square> marked3SidesSquares2 = board.squaresWithMarkedSides(3);
        //iterate through these squares
        Iterator<Square> squareIterator2 = marked3SidesSquares2.iterator();
        while (squareIterator2.hasNext()) {
            Square square = squareIterator2.next(); // the square with three sides that we're testing
             Square adjoining = null;  //sets variable for potential adjoining square
             int row = square.getRow(); //define row, found in SquareC.java
             int col = square.getCol(); //define column, found in SquareC.java
            //find which side is missing and find adjoining square
            if (! square.sideIsMarked(Side.NORTH) && (row != 0) ) {
                adjoining = board.getSquare(row - 1, col);
            }
            else if (! square.sideIsMarked(Side.EAST) && (col != (Util.N - 1)) ) {
                adjoining = board.getSquare(row, col + 1);
            }
            else if (! square.sideIsMarked(Side.SOUTH) && (row != (Util.N - 1)) ) {
                adjoining = board.getSquare(row + 1, col );
            }
            else if (! square.sideIsMarked(Side.WEST)&& (col != 0) ) {
                adjoining = board.getSquare(row, col - 1 );
            }
            //if adjoining square doesn't exist or does have 2 marked sides, add line, completing square
            //adding line if it has 2 marked sides will allow for chain thing
            if ( (adjoining == null) || (adjoining.hasNMarkedSides(2)) ) {
                return square.openLines().iterator().next();
            }
        } //end while loop

    //STEP 3: (third best case) completes 1 square
        Set<Square> NEW3SidesSquares = board.squaresWithMarkedSides(3);
        if (!NEW3SidesSquares.isEmpty()) {
            Square square = NEW3SidesSquares.iterator().next();
            return square.openLines().iterator().next();
        }

    //STEP 4: if other steps can't work, add a line to any square that has 0 or 1 lines completed
        Set<Square> safeSquares = board.squaresWithMarkedSides(0);
        safeSquares.addAll(board.squaresWithMarkedSides(1)); //(combine sets)
        //iterate through these squares
        Iterator<Square> squareIterator3 = safeSquares.iterator();
        while (squareIterator3.hasNext()) {
            Square square = squareIterator3.next(); // the square with three sides that we're testing
            int row = square.getRow(); //define row, found in SquareC.java
            int col = square.getCol(); //define column, found in SquareC.java
            //find open line by iterating
            Iterator<Line> openLines = square.openLines().iterator();
            while (openLines.hasNext()) {
                Line line = openLines.next();
                Square adjoining = null;  //defines potential adjoining square
                //go to correct adjacent square
                // from LineC.java lines 17-63
                Side side = line.getSide();
                if ( (side == Side.NORTH) && (row != 0) ) {
                    adjoining = board.getSquare(row - 1, col);
                }
                else if  ((side == Side.EAST)  && (col != (Util.N - 1))) {
                    adjoining = board.getSquare(row, col + 1);
                }
                else if ((side == Side.SOUTH) && (row != (Util.N - 1)) ) {
                    adjoining = board.getSquare(row + 1, col );
                }
                else if ((side == Side.WEST) && (col != 0) ) {
                    adjoining = board.getSquare(row, col - 1 );
                }
                if ( (adjoining == null) || (! adjoining.hasNMarkedSides(2)) ) {
                    return line;
                }
            } //ends inner while loop
          } //ends outer while loop

       //in case all else fails, return random line
       Set<Line> lines = board.openLines();
       List<Line> shuffledLines = new ArrayList<Line>(lines);
       Collections.shuffle(shuffledLines);
       return shuffledLines.get(0);

  } //end makePlay


  //check the square based on the given line to see if the square
  //has <2 marked side.  Return true if square has <2 marked side
    private boolean doesSideHaveLessThan2SidesMarked(Line line, Board board) {
        Set<Square> attachedSquaresSet = line.getSquares(board);
        Iterator<Square> squareIterator = attachedSquaresSet.iterator();
        while (squareIterator.hasNext()) {
            if (squareIterator.next().openLines().size() <= 2) //returns set of open lines square has
                return false;
        }
        return true;
    }

    //given the set of squares, find any open lines of the given square,
    //select the line if it has <2 marked side
    private Line chooseRandomLine(Set<Square> candidates, Board board) {
        List<Square> shuffledCandidates = new ArrayList<Square>(candidates);
        Collections.shuffle(shuffledCandidates);
        for (Square square : shuffledCandidates) {
            Iterator<Line> openLines = square.openLines().iterator();
            while (openLines.hasNext()) {
                Line line = openLines.next();
                if (doesSideHaveLessThan2SidesMarked(line, board))
                    return line;
            }
        }
        return null;
    }

  public String teamName() { return "Player 51"; }
  public String teamMembers() { return "Katie Goglia & Kamil Piskorz"; }
  public Color getSquareColor() { return Util.PLAYER1_COLOR; }
  public Color getLineColor() { return Util.PLAYER1_LINE_COLOR; }
  public int getId() { return 1; }
  public String toString() { return teamName(); }
}
