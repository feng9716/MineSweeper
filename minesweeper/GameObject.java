package com.codegym.games.minesweeper;


public class GameObject{
        public int x;
        public int y;
        public boolean isMine;
        public int countMineNeighbors = 0;
        public boolean isOpen;
        public boolean isFlag;

        public GameObject(int x, int y, boolean m){
            this.x = x;
            this.y = y;
            isMine = m;
        }



}
