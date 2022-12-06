import java.util.Arrays;

class BotMoves {
   BotMoves(){
        Values[0][0] = 0.8;
        Values[0][7] = 0.8;
        Values[7][0] = 0.8;
        Values[7][7] = 0.8;
        for(int[] row: ConquerProfit){
            Arrays.fill(row, 1);
        }
        for(int i = 1; i < 7; i++){
            Values[0][i] = 0.4;
            Values[7][i] = 0.4;
            Values[i][0] = 0.4;
            Values[i][7] = 0.4;
            ConquerProfit[0][i] = 2;
            ConquerProfit[7][i] = 2;
            ConquerProfit[i][0] = 2;
            ConquerProfit[i][7] = 2;
        }
    }
    void BotGame(MenuPanel Screen){
        RealPlayer Human = new RealPlayer();
        BotMoves Bot = new BotMoves();
        boolean LastMove = true;
        boolean PreLastMove;
        for(int i = 0; i < 60; i++){
            PreLastMove = LastMove;
            if(i%2 == 0){
                LastMove = Human.MakeMove(false, Screen);
                if(Screen.answer.equals("Exit")){
                    return;
                }
            }
            else{
                LastMove = Bot.Move(Screen);
            }
            if (!PreLastMove && !LastMove){
                break;
            }
        }
    }
    boolean Move(MenuPanel Screen) {
        Screen.PosUpdate(true);
        boolean MoveExistence = false;
        for (int i = 0; i < 8; i++) {
            for (int y = 0; y < 8; y++) {
                if (Screen.Field.Possible[i][y]) {
                    MoveExistence = true;
                }
            }
        }
        if (!MoveExistence) {
            System.out.println("У компьютера нет подходящих позиций, переход хода");
            return false;
        }
        if(!Screen.BotDiff){
            EasyMove(Screen);
            Screen.Field.CurState[best_row][best_column] = Cell.W;
            Screen.Moves.push(best_row*8 + best_column);
            Screen.Field.Paint(Screen);
            Screen.step++;
            return true;
        }
        HardMove(Screen);
        Screen.Field.CurState[best_row][best_column] = Cell.W;
        Screen.Moves.push(best_row*8 + best_column);
        Screen.Field.Paint(Screen);
        Screen.step++;
        return true;
    }
    void EasyMove(MenuPanel Screen){
        best = 0;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                double sum = NoDepCount(Screen, i, j);
                if(sum > best){
                    best = sum;
                    best_row = i;
                    best_column = j;
                }
            }
        }
    }

    void HardMove(MenuPanel Screen){
        best = -100;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(!Screen.Field.Possible[i][j]){
                    continue;
                }
                double sum = NoDepCount(Screen, i, j);
                Screen.Field.CurState[i][j] = Cell.W;
                Screen.Moves.push(i*8 + j);
                Screen.Field.Paint(Screen);
                Screen.step++;
                double worst = 0;
                for(int y = 0; y < 8; y++){
                    for(int k = 0; k < 8; k++){
                        double now = NoDepCount(Screen, y, k);
                        if(now > worst){
                            worst = now;
                        }
                    }
                }
                sum -= worst;
                if(sum > best){
                    best = sum;
                    best_row = i;
                    best_column = j;
                }
                Screen.Field.Undo(Screen);
            }
        }
    }
    double NoDepCount(MenuPanel Screen, int i, int j){
        if(!Screen.Field.Possible[i][j]){
            return 0;
        }
        double sum = Values[i][j];
        Cell Us = Cell.W;
        Cell Enemy = Cell.B;
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
                        sum += ConquerProfit[new_row][new_column];
                        Screen.Field.Changed[Screen.step][new_row][new_column] = true;
                        new_row += d_y;
                        new_column += d_x;
                    }
                }
            }
        }
        return sum;
    }
    double best = 0;
    int best_row = 0;
    int best_column = 0;
    double Values[][] = new double[8][8];
    int ConquerProfit[][] = new int[8][8];
}
