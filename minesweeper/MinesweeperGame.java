package com.codegym.games.minesweeper;

import com.codegym.engine.cell.*;
import com.codegym.games.*;
import org.w3c.dom.ls.LSOutput;

import java.util.*;
public class MinesweeperGame extends Game{
    final private static int SIDE = 9;
    private GameObject[][] gameField = new GameObject[SIDE][SIDE];
    private int countMinesOnField = 0;
    private static final String MINE = "\uD83D\uDCA3";
    private static final String FLAG = "\uD83D\uDEA9";
    private  int countFlags;
    private boolean isGameStopped;
    private int countClosedTiles = SIDE * SIDE;
    private int score = 0;



    public static void main(String[] args){

    }

    public void initialize(){
        setScreenSize(SIDE,SIDE);
        createGame();
    }

    private void createGame(){
        for(int i = 0 ; i < SIDE;i++){
            for(int j = 0; j < SIDE;j++){
                boolean isMine = getRandomNumber(10) == SIDE;
                if(isMine) countMinesOnField ++;
                gameField[i][j] = new GameObject(j,i,isMine);

                setCellColor(i,j,Color.ORANGE);
                setCellValue(i,j,"");
            }
        }
        countMineNeighbors();
        countFlags = countMinesOnField;

    }

    private List<GameObject> getNeighbors(GameObject gameObject){
            List<GameObject> neighbors = new ArrayList<>();

            for(int i = gameObject.x -1 ; i <= gameObject.x+1;i++){
                for(int j = gameObject.y-1; j<=gameObject.y+1;j++){
                    if(i<0 || i>=SIDE){
                        continue;
                    }
                    if(j<0|| j >=SIDE){
                        continue;
                    }
                    if(i == gameObject.x && j == gameObject.y){
                        continue;
                    }
                    neighbors.add(gameField[j][i]);
                }
            }


            return neighbors;
    }

    private void countMineNeighbors(){
        int count = 0;
        for(int i = 0 ; i < SIDE;i++){
            for(int j = 0 ; j < SIDE; j++){
                GameObject cur = gameField[i][j];
                if(!cur.isMine){
                    List<GameObject> neighbors = getNeighbors(cur);
                    for(GameObject go: neighbors){
                        if(go.isMine) cur.countMineNeighbors = cur.countMineNeighbors + 1;
                    }
                }
            }
        }
    }

    private void openTile(int x, int y){
        if(isGameStopped) return;

        GameObject tile = gameField[y][x];
        if(tile.isOpen || tile.isFlag)return;

        tile.isOpen = true;
        setCellColor(x,y,Color.GREEN);

        if(tile.isMine) {
            setCellValueEx(x,y,Color.RED,MINE);
            gameOver();
        }
        else {
            setScore(score += 5);
            if (tile.countMineNeighbors == 0) {
                setCellValue(x, y, "");
                for (GameObject g : getNeighbors(tile)) {
                    if (!g.isOpen) openTile(g.x, g.y);
                }
            } else {
                setCellNumber(x, y, tile.countMineNeighbors);
            }
            countClosedTiles -= 1;

            if(countClosedTiles == countMinesOnField) win();;
        }
    }

    private void markTile(int x,int y){
        if(isGameStopped) return;
        GameObject tile = gameField[y][x];
        if(tile.isOpen) return;
        if(countFlags == 0 && !tile.isFlag ) return;

        // Add a flag to the tile;
        if(!tile.isFlag){
            tile.isFlag = true;
            countFlags -= 1;
            setCellValue(x,y,FLAG);
            setCellColor(x,y,Color.YELLOW);
        }
        // remove a flag from the tile
        else{
            tile.isFlag = false;
            countFlags += 1;
            setCellValue(x,y,"");
            setCellColor(x,y,Color.ORANGE);
        }

    }

    private void gameOver(){
        isGameStopped = true;
        showMessageDialog(Color.GRAY,"Game Over!", Color.BLACK,1);
    }

    private void win(){
        isGameStopped = true;
        showMessageDialog(Color.GRAY,"You Win!", Color.BLACK,1);
    }

    private void restart(){
        isGameStopped = false;
        score = 0;
        countClosedTiles = SIDE * SIDE;
        countMinesOnField = 0;

        setScore(score);
        createGame();
    }

    @Override
    public void onMouseLeftClick(int x, int y) {
        if(isGameStopped) {
            restart();
            return;
        }
        openTile(x,y);

    }

    @Override
    public void onMouseRightClick(int x,int y){
        markTile(x,y);
    }


}
