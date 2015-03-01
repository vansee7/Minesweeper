

import de.bezier.guido.*;
public final static int NUM_ROWS = 20;
public final static int NUM_COLS = 20;
private MSButton[][] buttons; //2d array of minesweeper buttons
private ArrayList <MSButton> bombs = new ArrayList <MSButton> (); //ArrayList of just the minesweeper buttons that are mined

void setup ()
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
    for(int n=0; n<15; n++){
        int row = (int)(Math.random()*20);
        int col = (int)(Math.random()*20);
            if(!bombs.contains(buttons[row][col]))
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
    int count=0;
    for(int i=0; i<bombs.size(); i++){
      if(bombs.get(i).marked==true)
        count++;
      if(count==15)
        return true;  
    }
    return false;
}

public void displayLosingMessage()
{
    for(int i=0; i<bombs.size();i++){
        bombs.get(i).clicked=true;
    }     
    for(int r=0; r<20; r++){
      buttons[r][5].setLabel(" ");
      buttons[r][6].setLabel("G");
      buttons[r][7].setLabel("A");
      buttons[r][8].setLabel("M");
      buttons[r][9].setLabel("E");
      buttons[r][10].setLabel(" ");
      buttons[r][11].setLabel("O");
      buttons[r][12].setLabel("V");
      buttons[r][13].setLabel("E");
      buttons[r][14].setLabel("R");
      buttons[r][15].setLabel(" ");
    }
}

public void displayWinningMessage()
{ 
      for(int r=0; r < 20; r++){
      buttons[r][7].setLabel("Y");
      buttons[r][8].setLabel("O");
      buttons[r][9].setLabel("U");
      buttons[r][10].setLabel(" ");
      buttons[r][11].setLabel("W");
      buttons[r][12].setLabel("I");
      buttons[r][13].setLabel("N");        
      }
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
