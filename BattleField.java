import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;

class BattleField {
     BattleField() {
       CurState = new Cell[8][8];
        for(Cell[] row: CurState){
            Arrays.fill(row, Cell.o);
        }
        CurState[3][3] = Cell.W;
        CurState[4][4] = Cell.W;
        CurState[3][4] = Cell.B;
        CurState[4][3] = Cell.B;
    }
     void SetToBegin(){
        for(Cell[] row: CurState){
            Arrays.fill(row, Cell.o);
        }
        CurState[3][3] = Cell.W;
        CurState[4][4] = Cell.W;
        CurState[3][4] = Cell.B;
        CurState[4][3] = Cell.B;
    }
    void PrintState(){
        for(int i = 0; i < 8; i++){
            System.out.print(8 - i);
            System.out.print(' ');
            for(int y = 0; y < 8; y++){
                System.out.print(CurState[i][y]);
                System.out.print(' ');
            }
            System.out.print('\n');
        }
        System.out.print("  ");
        char c = 'A';
        for(int i = 0; i < 8; i++){
            System.out.print(c);
            System.out.print(' ');
            c += 1;
        }
        System.out.print('\n');
    }
    void Undo(MenuPanel Screen){
        Screen.step--;
        int i = Screen.Moves.peek() / 8;
        int j = Screen.Moves.peek() % 8;
        Screen.Moves.pop();
        Cell Us = Screen.Field.CurState[i][j];
        Cell Enemy = Cell.W;
        if(Us == Cell.W){
            Enemy = Cell.B;
        }
        Screen.Field.CurState[i][j] = Cell.o;
        for(i = 0; i < 8; i++){
            for(j = 0; j < 8; j++){
                if(Screen.Field.Changed[Screen.step][i][j]){
                    Screen.Field.CurState[i][j] = Enemy;
                }
            }
        }
    }
    void Paint(MenuPanel Screen){
        int i = Screen.Moves.peek() / 8;
        int j = Screen.Moves.peek() % 8;
        Cell Us = Screen.Field.CurState[i][j];
        Cell Enemy = Cell.W;
        if(Us == Cell.W){
            Enemy = Cell.B;
        }
        for(int d_x = -1; d_x <= 1; d_x++) {
            for (int d_y = -1; d_y <= 1; d_y++) {
                if (d_x == 0 && d_y == 0) {
                    continue;
                }
                int new_row = d_y + i;
                int new_column = d_x + j;
                if(new_row < 0 || new_row > 7){
                    continue;
                }
                if(new_column < 0 || new_column > 7){
                    continue;
                }
                if(Screen.Field.CurState[new_row][new_column] != Enemy){
                    continue;
                }
                boolean Around = false;
                while(true){
                    new_row += d_y;
                    new_column += d_x;
                    if(new_row < 0 || new_row > 7){
                        break;
                    }
                    if(new_column < 0 || new_column > 7){
                        break;
                    }
                    if(Screen.Field.CurState[new_row][new_column] == Us){
                        Around = true;
                        break;
                    }
                    if(Screen.Field.CurState[new_row][new_column] == Enemy){
                        continue;
                    }
                    break;
                }
                if(Around){
                    new_row = d_y + i;
                    new_column = d_x + j;
                    while(Screen.Field.CurState[new_row][new_column] == Enemy){
                        Screen.Field.CurState[new_row][new_column] = Us;
                        Screen.Field.Changed[Screen.step][new_row][new_column] = true;
                        new_row += d_y;
                        new_column += d_x;
                    }
                }
            }
        }
    }
    Cell CurState[][];
    boolean Possible[][] = new boolean[8][8];

    boolean Changed[][][] = new boolean[64][8][8];
}
