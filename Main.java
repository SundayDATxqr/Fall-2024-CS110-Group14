import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class BoardSetUp extends JFrame{
    JButton[][] boardButtons = new JButton[10][10];
    private int order = 2;//2:black turn  1:white turn
    private int[][] Matrix = {{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},{-1,0,0,0,0,0,0,0,0,-1}, {-1,0,0,0,0,0,0,0,0,-1}, {-1,0,0,0,3,0,0,0,0,-1}, {-1,0,0,3,1,2,0,0,0,-1}, {-1,0,0,0,2,1,3,0,0,-1}, {-1,0,0,0,0,3,0,0,0,-1}, {-1,0,0,0,0,0,0,0,0,-1}, {-1,0,0,0,0,0,0,0,0,-1},{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1}};
    private int totalSteps = 0;
    public int isValidMove(int x, int y){//method used to test validity
        if(Matrix[y][x] == 1||Matrix[y][x] == 2)return 0;
        return checkRecursion(x,y,0,1,2) + checkRecursion(x,y,1,0,2) + checkRecursion(x,y,-1,0,2) + checkRecursion(x,y,0,-1,2) + checkRecursion(x,y,-1,1,2) + checkRecursion(x,y,1,1,2) + checkRecursion(x,y,1,-1,2) + checkRecursion(x,y,-1,-1,2);
    }
    public void chessFlip(int x, int y){// method used to flip chess
        reverseRecursion(x,y,0,1,2);// use recursion
        reverseRecursion(x,y,1,0,2);
        reverseRecursion(x,y,-1,0,2);
        reverseRecursion(x,y,0,-1,2);
        reverseRecursion(x,y,-1,1,2);
        reverseRecursion(x,y,1,1,2);
        reverseRecursion(x,y,1,-1,2);
        reverseRecursion(x,y,-1,-1,2);
    }
    public int checkRecursion(int x,int y,int dx,int dy,int n){
            if(Matrix[y+dy][x+dx] == order && n != 2) return 1;
            else if(Matrix[y+dy][x+dx] == order && n == 2) return 0;
            else if(Matrix[y+dy][x+dx] == 0 || Matrix[y+dy][x+dx] == -1 || Matrix[y+dy][x+dx] == 3) return 0;
            else return checkRecursion(x+dx, y+dy, dx, dy, n+1);//recursion
    }
    public int reverseRecursion(int x,int y,int dx,int dy,int n){
        if(Matrix[y+dy][x+dx] == order && n != 2) return 1;
        else if(Matrix[y+dy][x+dx] == order && n == 2) return 0;
        else if(Matrix[y+dy][x+dx] == 0 || Matrix[y+dy][x+dx] == -1 || Matrix[y+dy][x+dx] == 3) return 0;
        else if(reverseRecursion(x + dx, y + dy, dx, dy, n + 1) == 1){
            Matrix[y+dy][x+dx] = order;//flip the chess
            return 1;
        }
        else return 0;
    }
    public void BoardClick(int x, int y){// handle click
        if(isValidMove(x,y) == 0) {
            System.out.println("Invalid");
            return;
        }//test validity
        int GameEnd = 0;
        totalSteps ++;
        Matrix[y][x] = order;//add chess
        chessFlip(x,y);
        if(order == 1){order = 2; System.out.println("black");}
        else {order = 1; System.out.println("white");}//turn shift
        for(int i = 1 ; i < 9 ; i++){// mark valid next move
            for(int j = 1 ; j < 9 ; j++){
                if(isValidMove(i,j) != 0){
                    Matrix[j][i] = 3 ;
                    GameEnd = 1;
                }// 3 for following valid move
                else if(Matrix[i][j] == 3)Matrix[i][j] = 0;
            }
        }
        if(GameEnd == 1){
            Refresh();
            return;
        }
        if(order == 1){order = 2; System.out.println("black");}
        else {order = 1; System.out.println("white");}
        for(int i = 1 ; i < 9 ; i++){// mark valid move
            for(int j = 1 ; j < 9 ; j++){
                if(isValidMove(i,j) != 0){
                    Matrix[j][i] = 3 ;
                    GameEnd = 1;
                }// 3 for following valid move
                else if(Matrix[i][j] == 3)Matrix[i][j] = 0;
            }
        }
        if(GameEnd == 1){
            Refresh();
            return;
        }
        Refresh();
        System.out.println("game end");
    }
    public void SetUp(BoardSetUp board){//set up board, buttons and listener

        for(int i = 0 ; i < 10 ; i++ ){
            for(int j = 0 ; j < 10 ; j++ ){
                boardButtons[i][j] = new JButton();
                if(Matrix[i][j]==1)boardButtons[i][j].setBackground(Color.WHITE);
                else if(Matrix[i][j]==2)boardButtons[i][j].setBackground(Color.BLACK);
                else if(Matrix[i][j]==0)boardButtons[i][j].setBackground(Color.GRAY);
                else if(Matrix[i][j]==3)boardButtons[i][j].setBackground(Color.GREEN);
                else boardButtons[i][j].setBackground(Color.WHITE);
                int row = i;
                int col = j;
                boardButtons[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        BoardClick(col,row);
                    }
                });
                board.add(boardButtons[i][j]);
            }
        }
        board.setVisible(true);
    }
    public void Refresh(){// Update the  board
        for(int i = 0 ; i < 10 ; i++ ){
            for(int j = 0 ; j < 10 ; j++ ){
                if(Matrix[i][j]==1)boardButtons[i][j].setBackground(Color.WHITE);
                else if(Matrix[i][j]==2)boardButtons[i][j].setBackground(Color.BLACK);
                else if(Matrix[i][j]==0)boardButtons[i][j].setBackground(Color.GRAY);
                else if(Matrix[i][j]==3)boardButtons[i][j].setBackground(Color.GREEN);
                else boardButtons[i][j].setBackground(Color.WHITE);
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        BoardSetUp board = new BoardSetUp();
        board.setSize(600,600);
        board.setLayout(new GridLayout(10,10));
        board.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        board.SetUp(board);
    }
}