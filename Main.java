import javax.swing.*;
import javax.swing.JOptionPane;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Scanner;
class FileChooser extends JFrame {
    private JFileChooser fileChooser;
    private JTextArea textArea;
    public FileChooser() {
        fileChooser = new JFileChooser();
        textArea = new JTextArea();
        textArea.setEditable(false);
        this.add(new JScrollPane(textArea), "Center");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
    }
    public File openFile() {
        FileChooser frame = new FileChooser();
        frame.setVisible(true);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            this.dispose();
            frame.setVisible(false);
            return selectedFile;
        }else {
            this.dispose();
            frame.setVisible(false);
           return null;
        }
    }
}
class BoardSetUp extends JFrame{
    JButton[][] boardButtons = new JButton[10][15];
    JOptionPane popupWindow = new JOptionPane("注意");
    private int[][][] MatrixEveryStep = new int[61][10][10];
    private int[][] LocationEveryStep = new int [61][3];
    private int order = 2;//2:black turn  1:white turn
    private int[][] Matrix = new int[10][15];
    private File FileOperate;
    FileChooser fileChooser = new FileChooser();
    private int[][] MatrixReset = {{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},{-1,0,0,0,0,0,0,0,0,-1}, {-1,0,0,0,0,0,0,0,0,-1}, {-1,0,0,0,3,0,0,0,0,-1}, {-1,0,0,3,1,2,0,0,0,-1}, {-1,0,0,0,2,1,3,0,0,-1}, {-1,0,0,0,0,3,0,0,0,-1}, {-1,0,0,0,0,0,0,0,0,-1}, {-1,0,0,0,0,0,0,0,0,-1},{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1}};
    private int totalSteps = 0;
    private int seconds = 59;
    int winner = 0;
    int GameEnd = 0;
    int Black = 0;
    int White = 0;
    public void PopUpWindowInitialization(){
        //popupWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        popupWindow.setSize(400,300);
    }
    public void GameTermination(){
        String win;
        if(winner == 1 || White > Black)win = "白棋胜利！";
        else if(winner == 2 || Black > White)win = "黑棋胜利！";
        else win = "平局！";
        int choice = JOptionPane.showConfirmDialog(
                popupWindow,
                win+"是否重开棋局？",
                "恭喜！",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        if(choice == JOptionPane.YES_OPTION){
                choice = JOptionPane.showConfirmDialog(
                        popupWindow,
                        "是否保存当前棋局？",
                        "恭喜！",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );
                if(choice == JOptionPane.YES_OPTION){
                    FileOperate = fileChooser.openFile();
                    FileWrite();
                }
            seconds = 59;
            boardButtons[0][0].setText(":59");
            GameEnd = 1;
            matrixReset();
            order = 2;
            totalSteps = 0;
            Refresh();
        }
        else return;
    }
    public void Restart(){//restart the game
        int choice = JOptionPane.showConfirmDialog(
                popupWindow,
                "是否重开棋局？",
                "警告",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        if(choice == JOptionPane.YES_OPTION){
            if(totalSteps != 0){
                choice = JOptionPane.showConfirmDialog(
                        popupWindow,
                        "是否保存当前棋局？",
                        "警告",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );
                if(choice == JOptionPane.YES_OPTION){
                    FileOperate = fileChooser.openFile();
                    FileWrite();
                }
            }
            seconds = 59;
            boardButtons[0][0].setText(":59");
            winner = 0;
            GameEnd = 1;
            matrixReset();
            Counting();
            order = 2;
            totalSteps = 0;
            Refresh();
        }
        else return;
    }
    public void Counting(){
        Black = 0;
        White = 0;
        for(int i = 1 ; i < 9 ; i++){
            for(int j = 1 ; j < 9 ; j++){
                if(Matrix[i][j] == 2)Black++;
                if(Matrix[i][j] == 1)White++;
            }
        }
        boardButtons[0][8].setText("白:"+White);
        boardButtons[0][9].setText("黑:"+Black);
        return;
    }
    public void winnerJudge(int n){
        if(n == 2)winner = 1;
        if(n == 1)winner = 2;
        GameTermination();
    }
    public void takeBackMoveRecord(int x,int y,int order){
        LocationEveryStep[totalSteps][0] = x;
        LocationEveryStep[totalSteps][1] = y;
        LocationEveryStep[totalSteps][2] = order;
        for(int i = 0 ; i < 10 ; i++){
            for(int j = 0 ; j < 10 ; j++){
                MatrixEveryStep[totalSteps][i][j] = Matrix[i][j];
            }
        }
    }
    public void FileRead(){
        if (FileOperate == null)return;
        try (Scanner scanner = new Scanner(FileOperate)) {

            totalSteps = scanner.nextInt();
            for(int i = 0 ; i <= totalSteps ; i ++){
                for(int j = 0 ; j < 10 ; j ++ ){
                    for(int k = 0 ; k < 10 ; k ++){
                        System.out.println(i+" "+j+" "+k);
                        MatrixEveryStep[i][j][k] = scanner.nextInt();
                        if (i == 0)Matrix[j][k] = MatrixEveryStep[i][j][k];
                    }
                }
                LocationEveryStep[i][0] = scanner.nextInt();
                LocationEveryStep[i][1] = scanner.nextInt();
                LocationEveryStep[i][2] = scanner.nextInt();
            }
            for(int i = 1 ; i <= totalSteps ; i ++){
                if(isValidMove(LocationEveryStep[i][0],LocationEveryStep[i][1]) == 0){
                    System.out.println("Illegal1");
                    return;
                }
                Matrix[LocationEveryStep[i][1]][LocationEveryStep[i][0]] = LocationEveryStep[i-1][2];
                chessFlip(LocationEveryStep[i][0],LocationEveryStep[i][1]);
                System.out.println(i);
                order = LocationEveryStep[i][2];
                ValidMoveMark();
                for(int j = 1 ; j <= 8 ; j++){
                    for(int k = 1 ; k <=8 ; k++){
                        if(Matrix[j][k] != MatrixEveryStep[i][j][k]){
                            System.out.println("illegal2");
                        }
                    }
                }
            }
            winner = 0;
            seconds = 59;
            boardButtons[0][0].setText(":59");
            Refresh();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void FileWrite(){
        if (FileOperate == null)return;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FileOperate))){
            writer.write(totalSteps+" ");
            for(int i = 0 ; i <= totalSteps ; i ++){
                for(int j = 0 ; j < 10 ; j ++){
                    for(int k = 0 ; k <10 ; k++ ){
                        writer.write(MatrixEveryStep[i][j][k]+" ");
                    }
                }
                writer.write(LocationEveryStep[i][0]+" "+LocationEveryStep[i][1]+" "+LocationEveryStep[i][2]+" ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void takeBackMove(){
        if(totalSteps == 0){
            System.out.println("First");
            return;
        }
        GameEnd = 0;
        totalSteps--;
        order = LocationEveryStep[totalSteps][2];
        for(int i = 1 ; i < 9 ; i++){
            for(int j = 1 ; j < 9 ; j++){
                Matrix[i][j] = MatrixEveryStep[totalSteps][i][j];
            }
        }
        winner = 0;
        seconds = 59;
        boardButtons[0][0].setText(":59");
        Refresh();
    }
    public void matrixReset(){
        for(int i = 0 ; i < 10 ; i ++){
            for(int j = 0 ; j < 10 ; j++ ){
                Matrix[i][j] = MatrixReset[i][j];
            }
        }
    }
    public int isValidMove(int x, int y){//method used to test validity
        if(Matrix[y][x] == 1||Matrix[y][x] == 2)return 0;
        return checkRecursion(x,y,0,1,2) + checkRecursion(x,y,1,0,2) + checkRecursion(x,y,-1,0,2) + checkRecursion(x,y,0,-1,2) + checkRecursion(x,y,-1,1,2) + checkRecursion(x,y,1,1,2) + checkRecursion(x,y,1,-1,2) + checkRecursion(x,y,-1,-1,2);
    }
    public void ValidMoveMark(){
        for(int i = 1 ; i < 9 ; i++){// mark valid next move
            for(int j = 1 ; j < 9 ; j++){
                if(isValidMove(i,j) != 0){
                    Matrix[j][i] = 3 ;
                    GameEnd = 1;
                }// 3 for following valid move
                else if(Matrix[j][i] == 3)Matrix[j][i] = 0;
            }
        }
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
        if(isValidMove(x,y) == 0 || x < 1 || x > 8 || y < 1 || y > 8) {
            System.out.println("Invalid");//test validity
            return;
        }
        GameEnd = 0;
        seconds = 59;
        boardButtons[0][0].setText(":59");
        totalSteps ++;
        Matrix[y][x] = order;//add chess
        chessFlip(x,y);
        Counting();
        if(order == 1){order = 2; System.out.println("black");}
        else {order = 1; System.out.println("white");}//turn shift
        ValidMoveMark();
        takeBackMoveRecord(x,y,order);
        if(GameEnd == 1){
            Refresh();
            return;
        }
        if(order == 1){order = 2; System.out.println("black");}
        else {order = 1; System.out.println("white");}
        for(int i = 1 ; i < 9 ; i++){// mark valid move
            for(int j = 1 ; j < 9 ; j++){
                if(isValidMove(i,j) != 0){
                    Matrix[j][i] = 3;
                    GameEnd = 1;
                }// 3 for following valid move
                else if(Matrix[i][j] == 3)Matrix[i][j] = 0;
            }
        }
        if(GameEnd == 1){
            Refresh();
            return;
        }
        GameTermination();
        Refresh();
        System.out.println("game end");
    }
    public void SetUp(BoardSetUp board){//set up board, buttons and listener
        matrixReset();
        takeBackMoveRecord(0,0,2);
        for(int i = 0 ; i < 10 ; i++ ){
            for(int j = 0 ; j < 15 ; j++ ){
                boardButtons[i][j] = new JButton();
                if(Matrix[i][j]==1)boardButtons[i][j].setBackground(Color.WHITE);
                else if(Matrix[i][j]==2)boardButtons[i][j].setBackground(Color.BLACK);
                else if(Matrix[i][j]==0)boardButtons[i][j].setBackground(Color.GRAY);
                else if(Matrix[i][j]==3)boardButtons[i][j].setBackground(Color.GREEN);
                else boardButtons[i][j].setBackground(Color.WHITE);
                int row = i;
                int col = j;
                if(!(i < 1 || i > 8 || j < 1 || j > 8)){
                    boardButtons[i][j].addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            BoardClick(col,row);
                        }
                    });
                }
                board.add(boardButtons[i][j]);
            }
        }
        Counting();
        boardButtons[1][13].setText("悔");
        boardButtons[1][13].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                takeBackMove();
            }
        });
        boardButtons[1][11].setText("重");
        boardButtons[1][11].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Restart();
            }
        });
        boardButtons[3][13].setText("存");
        boardButtons[3][13].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileOperate = fileChooser.openFile();
                FileWrite();
            }
        });
        boardButtons[3][11].setText("读");
        boardButtons[3][11].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileOperate = fileChooser.openFile();
                FileRead();
            }
        });
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seconds--;
                if (seconds == 0) {
                    ((Timer)e.getSource()).stop(); // 停止计时器
                    boardButtons[0][0].setText(":00"); // 显示20秒
                    winnerJudge(order);
                }
                boardButtons[0][0].setText(String.format(":%02d",seconds));
            }
        });
        timer.start();
        board.setVisible(true);
    }
    public void Refresh(){//Only update the  board
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
        board.setSize(1400,990);
        board.setLayout(new GridLayout(10,15));
        board.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        board.SetUp(board);
    }
}