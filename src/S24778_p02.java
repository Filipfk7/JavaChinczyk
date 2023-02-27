import java.util.Scanner;


    public class S24778_p02 {

        static int maxFieldAmount = 39;

        static int playerAmount = -1;

        static final char[] playersNames = {'a', 'b', 'c', 'd'};

        static char[] podium = {'-', '-', '-', '-'};

        static boolean gameFinished = false;


        static int[][] playersPawnsLocations = new int[][] {
                {'a', -6},//0
                {'a', -6},//1
                {'a', -6},//2
                {'a', -6},//3

                {'b', -6},//4
                {'b', -6},//5
                {'b', -6},//6
                {'b', -6},//7

                {'c', -6},//8
                {'c', -6},//9
                {'c', -6},//10
                {'c', -6},//11

                {'d', -6},//12
                {'d', -6},//13
                {'d', -6},//14
                {'d', -6},//15
        };

        public static void main(String[] args) {
            start();
            System.out.println("Koniec rozgrywki");
        }

        public static void start(int playersAmount){
            System.out.println("metoda 2");
        }

        public static int getPawnsAmountOnBoardForPlayer(char player, boolean countHomePawns) {
            int startingIndex = getStartingIndex(player);
            int pawnsOnBoard = 0;

            for(int i = startingIndex; i < startingIndex+4; i++) {
                int[] tab = playersPawnsLocations[i];
                int pawnLocation = tab[1];

                if(countHomePawns) {
                    if(pawnLocation > maxFieldAmount){
                        pawnsOnBoard += 1;
                    }
                }
                else {
                    if(pawnLocation >= 0 && pawnLocation <= maxFieldAmount) {
                        pawnsOnBoard += 1;
                    }
                }
            }
            return pawnsOnBoard;
        }

        public static int getCorrectPawnIdOnBoardFromPlayer(char playerName){
            boolean foundPawn = false;
            int pawnId = 0;

            while(!foundPawn) {
                System.out.println("Which pawn do you want to move?");
                pawnId = getNumberFromPlayer();

                if(pawnId >= 0 && pawnId <= 3) {
                    int startingIndex = getStartingIndex(playerName);
                    int[] tab = playersPawnsLocations[startingIndex + pawnId];
                    int pawnLocation = tab[1];

                    //Tryb: chcemy znaelzc pionek na planszy
                    if (pawnLocation >= 0 && pawnLocation <= maxFieldAmount) {
                        foundPawn = true;
                    }
                }

                if(!foundPawn) {
                    System.out.println("You have chosen invalid pawn.. Please try again");
                }
            }

            return pawnId;
        }

        public static void start(){
            //1. Wybranie ilości graczy
            askForPlayersAmount();

            //2. Start gry
            startGame();

            //3. Game in progress
            int round = 0;
            while(!gameFinished) {
                round += 1;
                System.out.println("ROUND NUMBER: " + round);

                for (int j = 0; j < playerAmount ; j++) {
                    char playerName = playersNames[j];

                    boolean playerFinished = false;
                    for (int k = 0 ; k < podium.length ; k++) {
                        if(podium[k] == playerName ){
                            playerFinished = true;
                        }
                    }

                    // Pomijamy gracza, który jest na podium
                    if(playerFinished) {
                        continue;
                    }

                    playerRollDice(playerName);
                }
            }
        }

        public static void playerRollDice(char playerName) {
            System.out.println("Player with number - " + playerName + " throwing a dice");

            int rollAmount = 0;

            if(getPawnsAmountOnBoardForPlayer(playerName, false) > 0) {
                rollAmount = 1;
            }
            else {
                rollAmount = 3;
            }

            for(int roll = 0; roll < rollAmount; roll++) {
                int dice = rollDice();

                if(dice == 6) {
                    // Co chcesz zrobic? pojechac tym pionkiem czy wyjechac na plansze nowym?
                    System.out.println("You get 6, choose what you want to do: ");
                    int pawnsOnBoard = getPawnsAmountOnBoardForPlayer(playerName, false);
                    int pawnsInHome = getPawnsAmountOnBoardForPlayer(playerName, true);
                    int pawnsOnBoardAndHome = pawnsInHome + pawnsOnBoard;

                    if(pawnsOnBoard > 0) {
                        System.out.println("1 - Move your pawn by 6 squares");
                    }

                    int decision = 1;

                    if(pawnsOnBoardAndHome < 4 ) {
                        System.out.println("2 - Take your pawn from the base");
                    }
                    Scanner sc = new Scanner(System.in);
                    decision = sc.nextInt();

                    int pawnId = 0;
                    if(decision == 1 && pawnsOnBoard > 0) {
                        pawnId = getCorrectPawnIdOnBoardFromPlayer(playerName);
                        movePawn(playerName, pawnId, 6);
                        pawnMoved(playerName, pawnId);

                        if(gameFinished) {
                            return;
                        }
                    }
                    else if(decision == 2 && pawnsOnBoardAndHome < 4){
                        takePawnOnBoard(playerName);
                    }

                    rollAmount += 1;
                }
                else {
                    if(getPawnsAmountOnBoardForPlayer(playerName, false) > 0) {
                        int pawnId = getCorrectPawnIdOnBoardFromPlayer(playerName);
                        movePawn(playerName, pawnId, dice);
                        pawnMoved(playerName, pawnId);
                    }
                    else {
                        System.out.println("You dont have pawns on board.. try again to roll 6");
                    }
                }
            }
        }

        public static void pawnMoved(char player, int pawnId) {
            int startingIndex = getStartingIndex(player);
            int tabId = startingIndex + pawnId;
            int pawnPosition = playersPawnsLocations[tabId][1];

            if(pawnPosition > maxFieldAmount) {
                System.out.println("You reached home! congratulations");

                if(getPawnsAmountOnBoardForPlayer(player, true) == 4) {
                    System.out.println("Congratulations! You won!");

                    int i = 0;
                    while(podium[i] != '-') {
                        i++;
                    }
                    podium[i] = player;

                    int freePlaces = 3 - i;

                    // Koniec gry
                    if(5-playerAmount == freePlaces) {
                        System.out.println("Koniec gry!!");
                        gameFinished = true;
                    }
                }
            }

            for(int i = 0; i < playersPawnsLocations.length; i++) {
                int[] tab = playersPawnsLocations[i];
                int anotherPlayer = tab[0];
                int anotherPlayerPawnPosition = tab[1];

                if(anotherPlayer != player && anotherPlayerPawnPosition == pawnPosition) {
                    System.out.println("You have taken your opponent ("+anotherPlayer+") pawn to base!");
                    tab[1] = -6;
                }
            }
        }

        public static int takePawnOnBoard(char player) {
            int startingIndex = getStartingIndex(player);

            for(int i = startingIndex; i < startingIndex+4; i++) {
                int[] tab = playersPawnsLocations[i];
                int pawnLocation = tab[1];

                // -6 -> poza plansza
                if(pawnLocation == -6) {
                    int pawnId = i - startingIndex;
                    if(player == 'a') {
                        movePawn(player, pawnId,6);
                        return pawnId;
                    }
                    if(player == 'b'){
                        movePawn(player, pawnId,16);
                        return pawnId;
                    }
                    if(player == 'c'){
                        movePawn(player, pawnId,26);
                        return pawnId;
                    }
                    if(player == 'd'){
                        movePawn(player, pawnId,36);
                        return pawnId;
                    }
                }
            }
            return -1;
        }

        public static void startGame() {
            for (int i = 0; i < playerAmount ; i++) {
                char playerName = playersNames[i];

                System.out.println("Now player '"+playerName+"' turn");

                for (int j = 0; j < 3 ; j++) {
                    int dice = rollDice();

                    if(dice == 6) {
                        System.out.println("Congratulations, you rolled 6. We have just put a pawn on your board.");
                        int idPawn = takePawnOnBoard(playerName);

                        while(dice == 6) {
                            System.out.println("You have second chance to roll a dice.");
                            dice = rollDice();
                            System.out.println("You rolled "+dice);
                            System.out.println("Your pawn has been moved "+dice+" places");
                            movePawn(playerName, idPawn, dice);
                        }
                        break;
                    }
                    else {
                        System.out.println("You rolled = "+dice+".. try again");
                    }
                }
            }
        }

        public static int getNumberFromPlayer() {
            Scanner sc = new Scanner(System.in);
            int number = sc.nextInt();
            return number;
        }


        public static void movePawn(char player, int idPawn, int placesToMove) {
            int startingIndex = getStartingIndex(player);
            int tabId = startingIndex + idPawn; //5

            int start = 0;
            if(player == 'a'){
                start = 40;
            }else if(player == 'b'){
                start = 9;
            }else if(player == 'c'){
                start = 19;
            }else {
                start = 29;
            }



            if(player != 'a' && playersPawnsLocations[tabId][1] + placesToMove > 39 ){
                playersPawnsLocations[tabId][1] = playersPawnsLocations[tabId][1] + placesToMove - 40;
            } else if(playersPawnsLocations[tabId][1]>0 && playersPawnsLocations[tabId][1]< start &&  playersPawnsLocations[tabId][1] + placesToMove >= start){
                playersPawnsLocations[tabId][1] = 41+start;
            }
            else{
                playersPawnsLocations[tabId][1] += placesToMove;
            }
            drawBoard();
        }



        public static void askForPlayersAmount() {
            System.out.println("If you want to start game type 'Start'");
            Scanner sc = new Scanner(System.in);
            String startGame = sc.next();
            if (startGame.equals("Start")) {
                int howManyPlayers = -1;

                for(int i = 0; i < 10000; i++)
                {
                    System.out.println("How many players?");
                    howManyPlayers = sc.nextInt();

                    if (howManyPlayers == 1) {
                        System.out.println("It doesn't make sense to play alone, but choose your pawn");
                        break;
                    } else if (howManyPlayers == 2) {
                        System.out.println("2 players - choose your pawns");
                        break;
                    } else if (howManyPlayers == 3) {
                        System.out.println("3 players - choose your pawns");
                        break;
                    } else if (howManyPlayers == 4) {
                        System.out.println("4 players - choose your pawns");
                        System.out.println();
                        break;

                    } else {
                        System.out.println("There's no place for more than 4 players");
                    }
                }
                playerAmount = howManyPlayers;
            }
        }

        // Ta metoda zwraca tylko liczbę od 1-6
        public static int rollDice() {
            int diceNumber = (int)(Math.random() * 6) + 1;
            System.out.println("Dice number is: " +diceNumber);
            return diceNumber;

        }

        public static int getStartingIndex(char player){
            int startingIndex = 0;

            if(player == 'a') {
                startingIndex = 0;
            }
            else if (player == 'b') {
                startingIndex = 4;
            }
            else if (player == 'c') {
                startingIndex = 8;
            }
            else if (player == 'd') {
                startingIndex = 12;
            }
            return startingIndex;
        }

        final static int[][] positionToCords = new int[][] {
                {1, 7}, //0
                {2, 7}, //1
                {3, 7}, //2
                {4, 7},
                {5, 7},
                {5, 8},
                {5, 9},
                {5, 10},
                {5, 11}, //8
                {6, 11},
                {7, 11},
                {7, 10},
                {7, 9},
                {7, 8},
                {7, 7}, //14
                {8, 7},
                {9, 7},
                {10, 7},
                {11, 7},
                {11, 6},
                {11, 5}, //20
                {10, 5},
                {9, 5},
                {8, 5},
                {7, 5},
                {7, 4},
                {7, 3},
                {7, 2},
                {7, 1},
                {6, 1},
                {5, 1}, //30
                {5, 2},
                {5, 3},
                {5, 4},
                {5, 5},
                {4, 5},
                {3, 5},
                {2, 5},
                {1, 5},
                {1, 6},
        };

        public static void drawBoard() {
            String[][] board = new String[13][13];

            // put cross in board[][] (x)
            for (int row = 0; row < 13; row++) {
                for (int col = 0; col < 13; col++) {
                    boolean found = false; //TODO - cleanup
                    for (int i = 0; i < positionToCords.length; i++) {
                        int[] cors = positionToCords[i];

                        if (cors[0] == row && cors[1] == col) {
                            found = true;
                            board[row][col] = " x ";
                            break;
                        }
                    }
                    if (!found) {
                        board[row][col] = "   ";
                    }
                }
            }

            board[0][7] = "0 ";
            board[7][12] = "10";
            board[12][5] = "20";
            board[5][0] = "30 ";

            // update board with player positions
            for (int i = 0; i < playerAmount * 4; i++) {
                int[] playerPosition = playersPawnsLocations[i]; // {'a','6'}
                char playerName = (char) (playerPosition[0]);
                int pawnLocation = playerPosition[1]; //42
                int pawnId = i % 4;

                if (pawnLocation >= 0 && pawnLocation <= 39) {
                    int row = positionToCords[pawnLocation][0];
                    int col = positionToCords[pawnLocation][1];

                    board[row][col] = " " + playerName + " ";
                } else if (pawnLocation < 0){
                    //pionek jest poza plansza

                    int[] cords = getPawnStartCord(playerName, pawnId);
                    int row = cords[0];
                    int col = cords[1];

                    board[row][col] = " " + playerName + " ";
                }
            }


            // draw board
            for (int i = 0; i < 13; i++) {
                for (int j = 0; j < 13; j++) {
                    System.out.print(board[i][j]);
                }
                System.out.println("");
            }
        }

        public static int[] getPawnStartCord(char playerName, int pawnId) {
            int[][] playerAPawns = {
                    {1,10},
                    {1,11},
                    {2,10},
                    {2,11}
            };

            int[][] playerBPawns = {
                    {11,11},
                    {10,11},
                    {11,10},
                    {10,10}
            };

            int[][] playerCPawns = {
                    {11,1},
                    {10,2},
                    {10,1},
                    {11,2}
            };

            int[][] playerDPawns = {
                    {1,1},
                    {1,2},
                    {2,1},
                    {2,2}
            };

            switch (playerName) {
                case 'a':
                    return playerAPawns[pawnId];
                case 'b':
                    return playerBPawns[pawnId];
                case 'c':
                    return playerCPawns[pawnId];
                case 'd':
                    return playerDPawns[pawnId];
                default:
                    return null;
            }
        }

    }

