package board;

/**
 * Created by noctuam on 15.07.16.
 */
public class BoardCell {

    int x,y;

    boolean isBomb;
    boolean isOpen;
    boolean isDefused;

    int minesAround;

    /**
     * Default constructor for cell
     * @param x pos
     * @param y pos
     */
    public BoardCell(int x, int y) {
        this.x = x;
        this.y = y;
        this.isBomb = false;
        this.isOpen = false;
        this.isDefused = false;
        this.minesAround = -1;
    }

    /**
     * Checking cell state (Generated by IDEA)
     * @return true if cell mined
     */
    public boolean isBomb() {
        return isBomb;
    }

    /**
     * Setting bomb to cell (Generated by IDEA)
     * @param bomb
     */
    public void setBomb(boolean bomb) {
        isBomb = bomb;
    }

    /**
     * Checking cell state (Generated by IDEA)
     * @return return true if cell opened
     */
    public boolean isOpen() {
        return isOpen;
    }


    public boolean hidedCell(){
        return !isOpen;
    }
    /**
     * Set cell opened. Wright workaround this action implemented in game process (Generated by IDEA)
     * @param open
     */
    public void setOpen(boolean open) {
        isOpen = open;
    }

    /**
     * Checking cell state (Generated by IDEA)
     * @return
     */
    public boolean isDefused() {
        return isDefused;
    }

    /**
     * Set cell defused. Wright workaround this action implemented in game process (Generated by IDEA)
     * @param defused
     */
    public void setDefused(boolean defused) {
        isDefused = defused;
    }
}