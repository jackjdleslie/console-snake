import java.lang.Math;
import java.util.Scanner;

public class snake {

  //global variable for total score
  public static int TOTAL_SCORE;
  public static int CHANGED;

  /* global variables to store users move choice and the previous move choice */
  public static char move = 'w', prev_move = 'w';

  public static void main(String args[]) {


    //default value of all cells is 0
    int[][] grid = new int[11][11];

    //set initial position of snake, passing length of snake now
    initSnake(grid);

    //method to print current value of grid
    for(int i=0;i<10;i++) System.out.println("\n");
    printGrid(grid);

    //initial number of moves is 0
    int moveNumber = 0;

    //game intro with instructions
    intro();

    //do while loop, main loop for game to quit when enter q, but repeat moving snake otherwise
    do {

      //get users move choice
      getCommand();

      //while command entered isn't right, ask user to re enter
      while(move!='w' & move!='a' & move!='s' & move!='d' & move!='q' & move!='\n') {
        System.out.println("Invalid, Enter one of the following: w=UP, a=LEFT, s=DOWN, d=RIGHT, q=QUIT");
        System.out.println('\n');
        getCommand();
      }
      //once input is has been validated and stored in move, update the grid
      updateGrid(grid, prev_move, moveNumber);

      //increment number of moves by 1
      moveNumber += 1;

      //separates instances of grid
      System.out.println('\n');
      for(int i=0;i<grid[0].length;i++) System.out.print("#  ");
      System.out.println('\n');

    } while (move != 'q');

    //game over message
    System.out.println("Quit. Game over.");

  }

  //method to initialise the snake on the 2d array
  public static void initSnake(int[][] grid) {

    for(int row=5;row<11;row++) grid[row][5] = row-4;

    addBonus(grid);

  }

  /* Saves previous direction so user can simply press 'enter' to move */
  public static void getCommand() {

    Scanner s = new Scanner(System.in);
    //try the following code, catch error caused when user doesn't enter a value
    try {
      //get the users input
      move =  s.nextLine().charAt(0);

      //if the user doesn't enter a value, use the value entered in the last move
      if(move=='\n') move = prev_move;

      //if the user enters a value, set this move to prev_move so that next move this is the previous move
      else prev_move = move;

      //catches error where user does not enter a character, needed for scanner object
    } catch (StringIndexOutOfBoundsException e) {
      //same assignment as when user doesn't enter a value
      move = prev_move;
    }
  }

  /*Intro dialogue shown on start up*/
  public static void intro() {

    System.out.println("\n");
    System.out.println("~~~~~ Welcome to Snake ~~~~~\n");
    System.out.println("Collect @pples to increase your score!\n");
    System.out.println("Controls: w = UP, a = LEFT, s = DOWN, d = RIGHT, q = QUIT\n");
    System.out.println("Type in letter and press enter to move. Or simply leave blank and just press enter to continue movement in current direction.\n");

  }

  /*method to add method to random spot on the board */
  public static void addBonus(int[][] grid) {

    //checks if a bonus has been placed in the grid
    boolean placed = false;

    //integer values to store randomly generated row and column value
    int row, column;

    /* while there has been no bonus placed, means that if a randomly generated
    row and column value is a snake value it tries again */
    while(placed==false) {
      //generate random row and column values in appropriate range
      row = (int)(Math.random() * grid.length);
      column = (int)(Math.random() * grid[0].length);

      //if the randomly genrated position in the grid is a 0 then make bonus cell (-1)
      if(grid[row][column]==0) {
        grid[row][column]= -1;

        //let while loop know that a bonus has been placed to prevent further looping
        placed = true;
      }
    }
  }

  //method to print the current state of the grid with correct spacing
  /*Limit in for loops now relate to grid size instead of simple <9 */
  public static void printGrid(int[][] grid) {

    /* In main the for loop would loop from 0 to 9, however now it will loop to
    the custom grid width and height */

    //nested for loop to iterate though each cell. Cells need to be evenly spaced to meet output spec
    /* Note that grid.length = grid height, and grid[0].lenth is the grid width (in row 0, which
    is the same for row 1, 2 etc...) */

    boolean no_bonus = true;

    for(int row=0;row<grid.length;row++) {
      for(int column=0;column<grid[0].length;column++) {

        //new print format, X is head, - is grid background, @ is apple and O is body
        if(grid[row][column]==1) System.out.print("X  ");
        else if(grid[row][column]==0) System.out.print("-  ");
        else if(grid[row][column]==-1) System.out.print("@  ");
        else System.out.print("O  ");

        //check grid for bonuses, if there's still a bonus set no_bonus to false
        if(grid[row][column]==-1) no_bonus = false;
      }
      //after looped through each column in a row print new line for next row
      System.out.println();
    }

    //if there are no bonuses on the board then add new bonus to the board
    if(no_bonus==true) addBonus(grid);
  }

  //method for updating the grid after user moves snake
  public static void updateGrid(int[][] grid, char move, int moveNumber) {

    //only do the following if the user has not entered q, ensures that the grid is not printed again after quit
    if(move!='q') {
      moveSnake(grid,move,moveNumber);
      printGrid(grid);
    }

  }

  //method to move the snake's head/pointer depending on the users input move
  /* Limit in for loops now relate to grid size instead of simple <9 */
  public static void moveSnake(int[][] grid, char move, int moveNumber) {

    //variables to hold row and column values for the swap position
    int swapA = 0;
    int swapB = 0;

    /* nested for loop to iterate though each cell.if the cell is found with the
    snake head/pointer (1) then move as appropriate and then stop looping */

    //label for outer loop
    outerloop:
    /* Note that grid.length = grid height, and grid[0].lenth is the grid width (in row 0, which
    is the same for row 1, 2 etc...) */
    for(int row=0;row<grid.length;row++) {
      for(int column=0;column<grid[0].length;column++) {
        //if the value of current cell is snake head/pointer
        if(grid[row][column]==1) {
          //get row and column value of this position
          swapA = row;
          swapB = column;

          //call appropriate movement method depending on users move choice
          if(move=='w') moveUp(grid, moveNumber, swapA, swapB);
          if(move=='s') moveDown(grid, moveNumber, swapA, swapB);
          if(move=='a') moveLeft(grid, moveNumber, swapA, swapB);
          if(move=='d') moveRight(grid, moveNumber, swapA, swapB);

          //after (1) has been swapped, set current cell to 0 to allow rest of snake to be swapped
          grid[swapA][swapB] = 0;

          //stop all looping
          break outerloop;
        }
      }
    }

    //get the length of the snake (i.e. number at "tail"), has a return value so store in int variable
    int len = getSnakeLen(grid);

    //move the rest of the snake
    moveSnakeBody(grid,len,swapA,swapB,moveNumber);

  }

  //method to move the rest of the snake after the head/pointer has been moved
  /* Limit in for loops now relate to grid size instead of simple <9 */
  public static void moveSnakeBody(int[][] grid, int len, int swapA, int swapB, int moveNumber) {

    //the value of the cell that is about to be swapped, initially 2 as 1 has been swapped already
    int index = 2;

    /*nested for loop to iterate through each cell. if the value of the cell is equal to
    the value of the snake that is about to be swapped, then set the cell that previously
    held the snake head/pointer (1) to index, and make the current cell value 0, then stop
    looping. The loop will start again except the value to be swapped has incremeneted by 1,
    and will stop when the tail is reached */

    //loop while the value of the cell about to be swapped is less than or equal to the tail number
    while(index<=len) {
      //label for outer loop
      outerloop:
      /* Note that grid.length = grid height, and grid[0].lenth is the grid width (in row 0, which
      is the same for row 1, 2 etc...) */
      for(int row=0;row<grid.length;row++) {
        for(int column=0;column<grid[0].length;column++) {
          //if the current cell value is the value to be swapped
          if(grid[row][column]==index) {
            //swap the values and set current cell to 0, so can be swapped in next loop
            grid[swapA][swapB] = index;
            swapA = row;
            swapB = column;
            grid[swapA][swapB] = 0;
            break outerloop;
          }
        }
      }
      //sets value to be swapped to next value along the snake
      index += 1;
    }

    //every 5 moves, add a number greater than the last number in snake to the snakes tail
    if(CHANGED!=TOTAL_SCORE) {
      grid[swapA][swapB] = len+1;
      CHANGED += 1;
    }

  }

  //method to return the current length of the snake
  /* Limit in for loops now relate to grid size instead of simple <9 */
  public static int getSnakeLen(int[][] grid) {

    //initial length of snake is custom, minimum length snake can be is 1
    int max = 1;

    /* finding maximum algorithm, if the current cell value is greater than the current
    maximum value in grid then replace with this value */
    /* Note that grid.length = grid height, and grid[0].lenth is the grid width (in row 0, which
    is the same for row 1, 2 etc...) */
    for(int row=0;row<grid.length;row++) {
      for(int column=0;column<grid[0].length;column++) {
        if(grid[row][column] > max) max = grid[row][column];
      }
    }

    //returns the length of the snake
    return max;

  }

  //method to handle movement of the snakes head/pointer upwards
  /* If next position is a bonus cell add to score and add new bonus to the grid */
  public static void moveUp(int[][] grid, int moveNumber, int swapA, int swapB) {

    //try to swap snake head/pointer to next position
    try {

      //if the next position is a bonus add to total score, and add new bonus to board straight away
      if(grid[swapA-1][swapB]==-1) {
        TOTAL_SCORE += 1;
        System.out.println("Score: " + TOTAL_SCORE);
        addBonus(grid);
      }

      //if there is alreay part of snake in the position 1 rows above then game over
      if(grid[swapA-1][swapB]>1) gameOver(moveNumber);

      //if not the the swap is successful and the grid is updated
      else grid[swapA-1][swapB] = grid[swapA][swapB];

    //if the next position is a grid wall, it is out of bounds of the array
    /* teleport to row n times less than the current row, where n is the height of the grid */
    } catch(ArrayIndexOutOfBoundsException exception) {
      if(grid[swapA+grid.length-1][swapB]>1) gameOver(moveNumber);
      else grid[swapA+grid.length-1][swapB] = grid[swapA][swapB];
    }

  }

  //method to handle movement of the snakes head/pointer downwards
  /* If next position is a bonus cell add to score and add new bonus to the grid */
  public static void moveDown(int[][] grid, int moveNumber, int swapA, int swapB) {

    //try to swap snake head/pointer to next position
    try {

      //if the next position is a bonus add to total score
      if(grid[swapA+1][swapB]==-1) {
        TOTAL_SCORE += 1;
        System.out.println("Score: " + TOTAL_SCORE);
        addBonus(grid);
      }

      //if there is alreay part of snake in the position 1 rows below then game over
      if(grid[swapA+1][swapB]>1) gameOver(moveNumber);

      //if not the the swap is successful and the grid is updated
      else grid[swapA+1][swapB] = grid[swapA][swapB];

    //if the next position is a grid wall, it is out of bounds of the array so user loses
    /* teleport to row n times less than the current row, where n is the height of the grid */
    } catch(ArrayIndexOutOfBoundsException exception) {
      if(grid[grid.length-swapA-1][swapB]>1) gameOver(moveNumber);
      else grid[grid.length-swapA-1][swapB] = grid[swapA][swapB];
    }

  }

  //method to handle movement of the snakes head/pointer leftwards
  /* If next position is a bonus cell add to score and add new bonus to the grid */
  public static void moveLeft(int[][] grid, int moveNumber, int swapA, int swapB) {

    //try to swap snake head/pointer to next position
    try {

      //if the next position is a bonus add to total score
      if(grid[swapA][swapB-1]==-1) {
        TOTAL_SCORE += 1;
        System.out.println("Score: " + TOTAL_SCORE);
        addBonus(grid);
      }

      //if there is alreay part of snake in the position 1 column leftwards then game over
      if(grid[swapA][swapB-1]>1) gameOver(moveNumber);

      //if not the the swap is successful and the grid is updated
      else grid[swapA][swapB-1] = grid[swapA][swapB];

    //if the next position is a grid wall, it is out of bounds of the array so user loses
    /* teleport to row n times less than the current row, where n is the width of the grid */
    } catch(ArrayIndexOutOfBoundsException exception) {
      if(grid[swapA][swapB+grid[0].length-1]>1) gameOver(moveNumber);
      else grid[swapA][swapB+grid[0].length-1] = grid[swapA][swapB];
    }

  }

  //method to handle movement of the snakes head/pointer rightwards
  /* If next position is a bonus cell add to score and add new bonus to the grid */
  public static void moveRight(int[][] grid, int moveNumber, int swapA, int swapB) {

    //try to swap snake head/pointer to next position
    try {

      //if the next position is a bonus add to total score
      if(grid[swapA][swapB+1]==-1) {
        TOTAL_SCORE += 1;
        System.out.println("Score: " + TOTAL_SCORE);
        addBonus(grid);
      }

      //if there is alreay part of snake in the position 1 column rightwards then game over
      if(grid[swapA][swapB+1]>1) gameOver(moveNumber);

      //if not the the swap is successful and the grid is updated
      else grid[swapA][swapB+1] = grid[swapA][swapB];

    //if the next position is a grid wall, it is out of bounds of the array so user loses
    /* teleport to row n times more than the current row, where n is the width of the grid */
    } catch(ArrayIndexOutOfBoundsException exception) {
      if(grid[swapA][grid[0].length-swapB-1]>1) gameOver(moveNumber);
      else grid[swapA][grid[0].length-swapB-1] = grid[swapA][swapB];
    }

  }

  //method to output lose state and quit program
  /* Output score as well as move number */
  public static void gameOver(int moveNumber) {

    //print out lose state as required by spec
    System.out.println("Collision! Game over. Moves: " + moveNumber + ", Score: " + TOTAL_SCORE);

    //exit program
    System.exit(0);
  }

}
