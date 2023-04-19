package org.cis1200.TwentyFortyEight;

public class Tile {
    //Tile Value
    private int tileVal;

    public Tile(int tileVal) {
        this.tileVal = tileVal;
    }

    public int getTileVal() {
        return tileVal;
    }

    //Tile Combine
    private boolean combineStatus = false;
    public void setCombineStatus(boolean newCombineStatus) { //for resetting tile combine status
        combineStatus = newCombineStatus;
    }

    public boolean canCombineWith(Tile tile) { //not null + same can combine status + same value
        return tile != null && !combineStatus
                && !tile.combineStatus && tileVal == tile.getTileVal();
    }

    int combineWith(Tile t2) { //combine af
        if (canCombineWith(t2)) {
            tileVal *= 2;
            combineStatus = true;
            return tileVal;
        }
        return -1;
    }
}
