class RealPlayer {
    void PvP(MenuPanel Screen){
        boolean LastMove = true;
        boolean PreLastMove;
        for(int i = 0; i < 60; i++){
            PreLastMove = LastMove;
            if(i % 2 == 1){
                LastMove = MakeMove(true, Screen);
            }
            else{
                LastMove = MakeMove(false, Screen);
            }
            if(Screen.answer.equals("Exit")) {
                return;
            }
            if (!PreLastMove && !LastMove){
                break;
            }
        }
    }
    boolean MakeMove(boolean Color, MenuPanel Screen){// Color == 1 ходят белые
        Screen.PosUpdate(Color);
        boolean MoveExistence = false;
        for(int i = 0; i < 8; i++){
            for(int y = 0; y < 8; y++){
                if(Screen.Field.Possible[i][y]){
                    MoveExistence = true;
                }
            }
        }
        if(!MoveExistence){
            if(!Color) {
                System.out.println("У игрока 1 нет подходящих позиций, переход хода");
            }
            else{
                System.out.println("У игрока 2 нет подходящих позиций, переход хода");
            }
            return false;
        }
        while(true){
            Screen.PosPrint(Color);
            Screen.answer = Screen.scanner.nextLine();
            if(Screen.answer.equals("Exit")){
                return false;
            }
            if(Screen.answer.equals("Undo")){
                if(!Screen.Mode) {
                    Screen.Field.Undo(Screen);
                    Screen.Field.Undo(Screen);
                    Screen.PosUpdate(Color);
                    continue;
                }
                Screen.Field.Undo(Screen);
                Color ^= true;
                Screen.PosUpdate(Color);
            }
            if(Screen.answer.length() != 2){
                System.out.println("Введен некорректный ответ");
                continue;
            }
            int j = Screen.answer.charAt(0)- 'A';
            int i = Screen.answer.charAt(1) - '0';
            i = 8 - i;
            if(i < 0 || i > 7){
                System.out.println("Введен некорректный ответ");
                continue;
            }
            if(j < 0 || j > 7) {
                System.out.println("Введен некорректный ответ");
                continue;
            }
            if(!Screen.Field.Possible[i][j]){
                System.out.println("Введен некорректный ответ");
                continue;
            }
            if(!Color) {
                Screen.Field.CurState[i][j] = Cell.B;
            }
            else{
                Screen.Field.CurState[i][j] = Cell.W;
            }
            Screen.Moves.push(i*8 + j);
            Screen.Field.Paint(Screen);
            Screen.step++;
            return true;
        }
    }
}
