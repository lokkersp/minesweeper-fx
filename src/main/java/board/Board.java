package board;

import utils.TriFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Created by noctuam on 15.07.16.
 */
public class Board {
    int x,y;
    public BoardCell[][] boardCells;
    public List<BoardCell[][]> states;
    int bombCount;
    Random rnd = new Random();

    public Board(int x, int y) {
        this.x = x;
        this.y = y;
        this.boardCells = new BoardCell[x][y];
        this.bombCount = 15*(this.x*this.y/100);
        initBoard();
        this.states = new ArrayList<>();
        states.add(boardCells);
        plantBombs();
        List<String> bombList =bombsIn.apply(this.boardCells);
        bombList.forEach(System.out::println);
        states.add(boardCells);

    }

    public Board(int x, int y, int bombCount) {
        this.x = x;
        this.y = y;
        this.bombCount = bombCount;
    }

    /**
     * init board
     */
    void initBoard(){
        for (int i = 0; i < x; i++)
            for (int j = 0; j < y; j++)
                this.boardCells[i][j] = new BoardCell(i,j);
    }

    /**
     * Method plants bomb on the board
     */
    void plantBombs() {

        int planted=0;
        int dx,dy = -1;
        while (planted<=bombCount){
            dx = rnd.nextInt(x);
            dy = rnd.nextInt(y);
            if(!this.boardCells[dx][dy].isBomb()) {
                this.boardCells[dx][dy].setBomb(true);
                planted++;
            }
        }

    }
    BiFunction<Integer,Integer,String> buildString = (c1,c2)->String.format("Mine at: [%d,%d]",c1,c2);

    Function<BoardCell[][],List<String>> bombsIn = b->{
        List<String> mineList = new ArrayList<>();
        Stream<BoardCell> cellStream= Arrays.stream(b).flatMap(g->Arrays.stream(g));

        cellStream.filter(BoardCell::isBomb).forEach(t->mineList.add(buildString.apply(t.x,t.y)));
        return mineList;
    };
    void revealNeighbours(int x, int y){

    }

    void test(){
        BoardCell[][] tmp = this.boardCells;
        Stream<BoardCell> tmp1= (Arrays.stream(tmp)).flatMap(x->Arrays.stream(x));
    }

    public Stream<BoardCell> board2CellStream(BoardCell[][] board){
        return (Arrays.stream(board).flatMap(g->Arrays.stream(g)));
    }

    /**
     * Methods search mines around cell
     * @param x coordinate of a cell
     * @param y coordinate of a cell
     * @return count of mined cells around
     */
    int minesAroundCell(int x,int y){
        int count = 0;
        if(checkCoordinates.apply(this,x,y)) {
            for (int i = -1; i <= 1 ; i++) {
                int xValue = x + i;
                for (int j = -1; j <= 1 ; j++) {
                    int yValue = y +j;
                    if (this.boardCells[xValue][yValue].isBomb()) count++;
                }
            }
        } else System.out.println("Wrong coordinates: ["+ x +"," + y +"]");
        return count;
    }

    /**
     * Method opens cells around opened if they aren't bomb
     * @param x coordinate of a cell
     * @param y coordinate of a cell
     */
    void openAround(int x,int y){
        if(checkCoordinates.apply(this,x,y)) {
            for (int i = -1; i <= 1 ; i++) {
                int xValue = x + i;
                for (int j = -1; j <= 1 ; j++) {
                    int yValue = y +j;
                    if (!this.boardCells[xValue][yValue].isBomb() &&
                            !this.boardCells[xValue][yValue].isOpen())
                        this.boardCells[xValue][yValue].setOpen(true);
                }
            }
        }
    }
    public boolean openCellTestUI(int x,int y){
        if(!isOpenState(x,y)){
           this.boardCells[x][y].setOpen(true);
            states.add(this.boardCells);
            return true;
        }
    return false;
    }

    public boolean defuseCellTestUI(int x, int y){
        boolean state;
        if(!isDefuseState(x,y)) {
            this.boardCells[x][y].setDefused(true);
            this.states.add(this.boardCells);
            state =true;
        } else {
            this.boardCells[x][y].setDefused(false);
            this.states.add(this.boardCells);
            state = false;
        }
        return state;

    }

    /**
     *
     * @param x pos
     * @param y pos
     * @return true if open
     */
    boolean isOpenState(int x,int y) {
        return this.boardCells[x][y].isOpen();
    }

    /**
     *
     * @param x pos
     * @param y pos
     * @return true if defused
     */
    boolean isDefuseState(int x,int y){
        return this.boardCells[x][y].isDefused();
    }
    public TriFunction<Integer,Integer,Integer,Boolean>
            betweenExclusive = (p, l, r)-> (l<p && p<r);
    public TriFunction<Integer,Integer,Integer,Boolean>
            betweenInclusive = (p,l,r)-> (l<=p && p<=r);
    /**
     *
     */
    public TriFunction<Board,Integer,Integer,Boolean>
            checkCoordinates = (b,xC,yC)->{
                int rX = b.x; int rY = b.y;
            return
                    (betweenInclusive.apply(xC,0,rX) && betweenInclusive.apply(yC,0,rY));
    };

    public void persistState(){
        states.add(this.boardCells);
    }
}
