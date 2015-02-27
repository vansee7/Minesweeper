import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import de.bezier.guido.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Minesweeper extends PApplet {




public final static int NUM_ROWS = 20;
public final static int NUM_COLS = 20;
private MSButton[][] buttons; //2d array of minesweeper buttons
private ArrayList <MSButton> bombs = new ArrayList <MSButton> (); //ArrayList of just the minesweeper buttons that are mined

public void setup ()
{
    size(400, 400);
    textAlign(CENTER,CENTER);
    
    // make the manager
    Interactive.make( this );
    
    buttons = new MSButton [NUM_ROWS][NUM_COLS];
    for(int row = 0; row < 20; row++){
        for(int col = 0; col <20; col++)          
            buttons [row][col] = new MSButton(row,col);
    }
    setBombs();    
}

public void setBombs()
{
    for(int n=1; n<20; n++){
        int row = (int)(Math.random()*20);
        int col = (int)(Math.random()*20);
            if(!bombs.contains(buttons[row][col]));
                bombs.add(buttons[row][col]);
    }    
}

public void draw ()
{
    background( 0 );
    if(isWon())
        displayWinningMessage();
}
public boolean isWon()
{
    for(int i=0; i<bombs.size();i++){
        if(bombs.get(i).marked==true)
        return true;
    }    
    return false;
}
public void displayLosingMessage()
{
    for(int i=0; i<bombs.size();i++){
        bombs.get(i).clicked=true;
    } 
    buttons[10][6].setLabel("G");
    buttons[10][7].setLabel("A");
    buttons[10][8].setLabel("M");
    buttons[10][9].setLabel("E");
    buttons[10][10].setLabel(" ");
    buttons[10][11].setLabel("O");
    buttons[10][12].setLabel("V");
    buttons[10][13].setLabel("E");
    buttons[10][14].setLabel("R");
}

public void displayWinningMessage()
{
    buttons[10][7].setLabel("Y");
    buttons[10][8].setLabel("O");
    buttons[10][9].setLabel("U");
    buttons[10][10].setLabel(" ");
    buttons[10][11].setLabel("W");
    buttons[10][12].setLabel("I");
    buttons[10][13].setLabel("N");
}

public class MSButton
{
    private int r, c;
    private float x,y, width, height;
    private boolean clicked, marked;
    private String label;
    
    public MSButton ( int rr, int cc )
    {
        width = 400/NUM_COLS;
        height = 400/NUM_ROWS;
        r = rr;
        c = cc; 
        x = c*width;
        y = r*height;
        label = "";
        marked = clicked = false;
        Interactive.add( this ); // register it with the manager
    }
    public boolean isMarked()
    {
        return marked;
    }
    public boolean isClicked()
    {
        return clicked;
    }
    // called by manager
    
    public void mousePressed () 
    {
        clicked = true;
        if(mouseButton==RIGHT)
            marked=!marked;
        else if(bombs.contains(this))
            displayLosingMessage();
        else if(countBombs(r,c) > 0)
            setLabel(str(countBombs(r,c)));
        else {
            if(isValid(r,c-1) && buttons[r][c-1].isClicked()==false)
                buttons[r][c-1].mousePressed();
            if(isValid(r-1,c-1) && buttons[r-1][c-1].isClicked()==false)
                buttons[r-1][c-1].mousePressed();
            if(isValid(r-1,c) && buttons[r-1][c].isClicked()==false)
                buttons[r-1][c].mousePressed();
            if(isValid(r-1,c+1) && buttons[r-1][c+1].isClicked()==false)
                buttons[r-1][c+1].mousePressed();
            if(isValid(r,c+1) && buttons[r][c+1].isClicked()==false)
                buttons[r][c+1].mousePressed();
            if(isValid(r+1,c+1) && buttons[r+1][c+1].isClicked()==false)
                buttons[r+1][c+1].mousePressed();
            if(isValid(r+1,c) && buttons[r+1][c].isClicked()==false)
                buttons[r+1][c].mousePressed();
            if(isValid(r+1,c-1) && buttons[r+1][c-1].isClicked()==false)
                buttons[r+1][c-1].mousePressed();
        }
    }

    public void draw () 
    {    
        if (marked)
            fill(0);
         else if( clicked && bombs.contains(this) ) 
             fill(255,0,0);
        else if(clicked)
            fill( 200 );
        else 
            fill( 100 );
        rect(x, y, width, height);
        fill(0);
        text(label,x+width/2,y+height/2);
    }
    public void setLabel(String newLabel)
    {
        label = newLabel;
    }
    public boolean isValid(int r, int c)
    {
        if(r>=0 && r < 20 && c >= 0 && c < 20)
            return true;
        return false;
    }
    public int countBombs(int row, int col)
    {
        int numBombs = 0;
        //above
        if(isValid(r-1,c) && bombs.contains(buttons[row-1][col]))
            numBombs++;
        //aboveright
        if(isValid(r-1,c+1) && bombs.contains(buttons[row-1][col+1]))
            numBombs++;
        //right
        if(isValid(r,c+1) && bombs.contains(buttons[row][col+1]))
            numBombs++;
        //belowright
        if(isValid(r+1,c+1) && bombs.contains(buttons[row+1][col+1]))
            numBombs++;
        //below
        if(isValid(r+1,c) && bombs.contains(buttons[row+1][col]))
            numBombs++;
        //belowleft
        if(isValid(r+1,c-1) && bombs.contains(buttons[row+1][col-1]))
            numBombs++;
        //left
        if(isValid(r,c-1) && bombs.contains(buttons[row][col-1]))
            numBombs++;
        //aboveleft
        if(isValid(r-1,c-1) && bombs.contains(buttons[row-1][col-1]))
            numBombs++;
        return numBombs;
    }
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Minesweeper" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
