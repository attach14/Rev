import java.util.*;

class MenuPanel {
    void Work() {
        while (true) {
            skip = false;
            step = 0;
            Mode = false;
            BotDiff = false;
            Moves.clear();
            Field.SetToBegin();
            FirstQuestion();
            if (TheEnd) {
                return;
            }
            if(skip){
                if(Bot_Leaderboard.size() == 0){
                    System.out.println("Ещё не было игр");
                    continue;
                }
                Collections.sort(Bot_Leaderboard);
                System.out.printf("Наибольшее число фишек после партии у игрока - %d\n", Bot_Leaderboard.lastElement());
                continue;
            }
            if (!Mode) {
                DiffChoice();
                if (TheEnd) {
                    return;
                }
                BotMoves Robot = new BotMoves();
                Robot.BotGame(this);
                ResultsCount();
                continue;
            }
            RealPlayer HumanGame = new RealPlayer();
            HumanGame.PvP(this);
            ResultsCount();
        }
    }
  void ResultsCount(){
        int first_score = 0;
        int second_score = 0;
        for(int i = 0; i < 8; i++){
            for(int y = 0; y < 8; y++) {
                if(Field.CurState[i][y] == Cell.B){
                    first_score++;
                }
                if(Field.CurState[i][y] == Cell.W){
                    second_score++;
                }
            }
        }
        if(!Mode){
            Bot_Leaderboard.add(first_score);
        }
        String first = "Игрок 1";
        String second = "Компьютер";
        if(Mode){
            second = "Игрок 2";
        }
        if(first_score == second_score){
            System.out.println("Игра завершилась ничьей!");
        }
        if(first_score > second_score){
            System.out.println("Победил игрок 1!");
        }
        if(first_score < second_score){
            System.out.printf("Победил %s!\n", second);
        }
        System.out.println("Счет в партии:");
        System.out.printf("%s - %d\n", first, first_score);
        System.out.printf("%s - %d\n", second, second_score);

        Field.PrintState();
    }
    void FirstQuestion() {
        while (true) {
            System.out.println("Для игры с компьютером введите 1");
            System.out.println("Для игры с другим человеком введите 2");
            System.out.println("Чтобы увидеть наилучший результат игры против компьютера введите 3");
            System.out.println("Для завершения сессии введите 4");
            answer = scanner.nextLine();
            if (answer.equals("1")) {
                break;
            }
            if (answer.equals("2")) {
                Mode = true;
                break;
            }
            if (answer.equals("3")) {
                skip = true;
                break;
            }
            if (answer.equals("4")) {
                TheEnd = true;
                return;
            }
            System.out.println("Введен некорректный ответ");
        }
    }
    void DiffChoice() {
        while (true) {
            System.out.println("Для игры в легком режиме введите 1");
            System.out.println("Для игры в сложном режиме введите 2");
            System.out.println("Для завершения сессии нажмите 3");
            answer = scanner.nextLine();
            if (answer.equals("1")) {
                break;
            }
            if (answer.equals("2")) {
                BotDiff = true;
                break;
            }
            if (answer.equals("3")) {
                TheEnd = true;
                return;
            }
            System.out.println("Введен некорректный ответ");
        }
    }

    void PosPrint(boolean Color){
        Field.PrintState();
        if(Color) {
            System.out.println("Ход игрока 2 белыми фишками, возможные позиции помечены знаком x");
        }
        else {
            System.out.println("Ход игрока 1 черными фишками, возможные позиции помечены знаком x");
        }
        for(int i = 0; i < 8; i++){
            char c = 'A';
            for(int y = 0; y < 8; y++) {
                if (Field.Possible[i][y]) {
                    System.out.print("Можно поставить фишку на клетку ");
                    System.out.print(c);
                    System.out.print(' ');
                    System.out.println(8 - i);
                }
                c += 1;
            }
        }
        System.out.println("Введите букву столбца и номер строки без пробела между ними");
        if(Mode && step >= 1){
            System.out.println("Для отмены предыдущего хода введите Undo");
        }
        if(!Mode && step >= 2){
            System.out.println("Для отмены своего предыдущего хода и хода компьютера введите Undo");
        }
        System.out.println("Для досрочного завершения партии введите Exit");
    }
    void PosUpdate(boolean Color){
        for(int i  = 0; i < 8; i++){
            for(int y = 0; y < 8; y++){
                Field.Possible[i][y] = false;
                Field.Changed[step][i][y] = false;
                if(Field.CurState[i][y] == Cell.W || Field.CurState[i][y] == Cell.B){
                    continue;
                }
                Field.CurState[i][y] = Cell.o;
                Check(i, y, Color);
                if(Field.Possible[i][y]){
                    Field.CurState[i][y] = Cell.x;
                }
            }
        }
    }
    void Check(int row_index, int column_index, boolean Color){
        Cell Enemy = Cell.W;
        Cell Us = Cell.B;
        if(Color) {
            Enemy = Cell.B;
            Us = Cell.W;
        }
        for(int d_x = -1; d_x <= 1; d_x++){
            for(int d_y = -1; d_y <= 1; d_y++){
                if(d_x == 0 && d_y == 0){
                    continue;
                }
                int new_row = d_y + row_index;
                int new_column = d_x + column_index;
                if(new_row < 0 || new_row > 7){
                    continue;
                }
                if(new_column < 0 || new_column > 7){
                    continue;
                }
                if(Field.CurState[new_row][new_column] != Enemy){
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
                    if(Field.CurState[new_row][new_column] == Us){
                        Around = true;
                        break;
                    }
                    if(Field.CurState[new_row][new_column] == Enemy){
                        continue;
                    }
                    break;
                }
                if(Around){
                    Field.Possible[row_index][column_index] = true;
                    return;
                }
            }
        }
    }
    BattleField Field = new BattleField();
    boolean Mode = false;
    boolean BotDiff = false;
    String answer;
    final Scanner scanner = new Scanner(System.in);
    private boolean TheEnd = false;
    Stack<Integer> Moves = new Stack<Integer>();

    Vector<Integer> Bot_Leaderboard = new Vector<Integer>();
    int step = 0;

    boolean skip = false;
}
