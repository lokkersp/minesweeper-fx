import board.Board;
import board.BoardCell;
import utils.TriFunction;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.lang.Math.max;

/**
 * Created by noctuam on 15.07.16.
 */
public class Game implements GameOps {
    Board board;

    int x, y;

    public boolean isActive() {
        return isActive;
    }

    boolean isActive;
    boolean isWin;
    boolean isLost;
    int bombs, defuseKitCount;

   // List<TurnType> turns;


    public Game(int x, int y) {
        this.x = x;
        this.y = y;
        initGame();
    }

    public Game(int x, int y, int bombsCount) {
        this.x = x;
        this.y = y;
        this.bombs = bombsCount;
        this.defuseKitCount = bombs;
        initGame();
    }

    public Game(int x, int y, BiFunction<Integer,Integer,Integer> f) {
        this.x = x;
        this.y = y;
        this.bombs = f.apply(x,y);
        this.defuseKitCount = bombs;
        initGame();
    }


    void initGame(){
        isWin = false;
        isLost = false;
        this.board = new Board(this.x,this.y);
    }

    public void doOpen(int xPos, int yPos) {
        BoardCell cell = board.boardCells[xPos][yPos];
        if (!cell.isOpen() && !cell.isBomb()) {
            board.boardCells[xPos][yPos].setOpen(true);
        }
        if (!cell.isOpen() && cell.isBomb()) {
            board.boardCells[xPos][yPos].setOpen(true);
            isLost = true;
            isActive = false;
        }
        board.persistState();
    }

    public void doDefuse(int xPos, int yPos) {
        BoardCell cell = board.boardCells[xPos][yPos];
        if (defuseKitCount > 0)
            if (!cell.isOpen() && !cell.isDefused()) {
                board.boardCells[xPos][yPos].setDefused(true);
                defuseKitCount--;
            }
        if (!cell.isOpen() && cell.isDefused()) {
            board.boardCells[xPos][yPos].setDefused(false);
            defuseKitCount++;
        }
        board.persistState();
    }

    public boolean isDefused(int xPos,int yPos){
        return this.board.boardCells[xPos][yPos].isDefused();

    }
    public boolean isCleanCell(int xPos, int yPos) {
        return !this.board.boardCells[xPos][yPos].isBomb();
    }

    void neighbours(int xPos,int yPos){
        if(!board.boardCells[xPos][yPos].isOpen()) {
            board.boardCells[xPos][yPos].setOpen(true); {
                if (!board.boardCells[xPos][yPos].isBomb())
                    for (int i = max(xPos-1,0); i < x && i <=(x+1); i++)
                        for (int j = max(yPos-1,0); j <y && j<=(y+1) ; j++)
                            neighbours(i,j);


            }
        }
    }
    void setLost(){
        this.isLost = true;
    }
    void winGame(){
        this.isWin = true;
    }

    boolean isLost() {
        return this.isLost;
    }
    boolean isWin() {
        return this.isWin;
    }
}

//TODO:Create method that check game state, at defuse check count of truly defused mines and so on
