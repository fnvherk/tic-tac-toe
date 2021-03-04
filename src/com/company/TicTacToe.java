package com.company;

import java.util.*;
import static java.util.Arrays.asList;

class TicTacToe {
    // The current player on turn.
    private Players _playerOnTurn = Players.None;

    // The values of the grid.
    // Key(Integer): number of a cell in the grid.
    // Value(String): the actual value as displayed visually in the grid.
    private Map<Integer, Players> _gridValues;

    TicTacToe(){
        SetupGrid();
        PlayGame();
    }

    // The values of the grid (what you see visually) is stored as value inside a HashMap.
    // Key: Number to place.
    // Value: Value placed for according key. This can either be a number, or an x or an o.
    private void SetupGrid(){
        _gridValues = new HashMap<>();

        for (int i = 1; i < 10; i++){
            _gridValues.put(i, Players.None);
        }
    }

    // The main game's logic.
    private void PlayGame(){
        System.out.println("Tic-tac-toe started. Good luck!");

        // Winner is unknown at the start.
        Players winner = Players.None;

        // Keep playing while no one has won.
        while(winner == Players.None){
            if(!_gridValues.containsValue(Players.None)){
                // No more moves possible, so we stop.
                break;
            }

            MakeTurn();
            winner = GetWinner();
        }

        PrintGrid();

        if(winner == Players.None){
            System.out.println("Game is a tie!");
        } else{
            System.out.println(winner.toString() + " wins!");
        }

        // End of game!
    }

    // Choose who's turn it is.
    // If no player has been on turn, a random player will be selected.
    // Otherwise, it will choose the opposing player.
    private void SetNextPlayer(){
        if(_playerOnTurn == Players.None){
            List<Players> players = new ArrayList<>();
            players.add(Players.Human);
            players.add(Players.Computer);
            Collections.shuffle(players);
            _playerOnTurn = players.get(0);
            System.out.println(_playerOnTurn.toString() + " begins.");
        } else{
            _playerOnTurn = _playerOnTurn == Players.Human
                    ? Players.Computer
                    : Players.Human;
        }
    }

    // Displays the grid visually on the screen.
    private void PrintGrid(){
        if(!_gridValues.isEmpty()){
            String separator = "\n-------------";
            System.out.println(separator);

            for(int i = 1; i <= _gridValues.values().size(); i++){
                String mark =
                        _gridValues.get(i) == Players.Human
                        ? "X"
                                : _gridValues.get(i) == Players.Computer
                                    ? "O"
                                    : String.valueOf(i);

                System.out.print("| " +  mark + " ");

                // After three cells, a new separator must be printed.
                if(i % 3 == 0){
                    System.out.println("| " + separator);
                }
            }
            System.out.println();
        }
    }

    private void MakeTurn(){
        PrintGrid();
        // Next player to play.
        SetNextPlayer();

        if(_playerOnTurn == Players.Human){
            MakeHumanTurn();
        }else if(_playerOnTurn == Players.Computer){
            MakeComputerTurn();
        }
    }

    private void MakeHumanTurn(){
        System.out.println("Human's turn.");
        boolean madeTurn = false;

        while(!madeTurn){
            try{
                System.out.print("Your choice: ");
                Scanner scanner = new Scanner(System.in);
                int playersDecision = scanner.nextInt();

                if(_gridValues.get(playersDecision) == Players.None){
                    SetDecision(playersDecision, Players.Human);
                    madeTurn = true;
                } else {
                    throw new InputMismatchException();
                }
            } catch(InputMismatchException e){
                System.out.println("Please make a valid choice.");
            }
        }
    }

    // Randomly chooses one of the remaining options.
    private void MakeComputerTurn(){
        var remainingMoves = new ArrayList<Integer>();
        for (Map.Entry<Integer, Players> entry : _gridValues.entrySet()){
            if(entry.getValue() == Players.None){
                remainingMoves.add(entry.getKey());
            }
        }

        Collections.shuffle(remainingMoves);
        int decision = remainingMoves.get(0);
        SetDecision(decision, Players.Computer);
    }

    // Writes decision to the HashMap containing the grid information.
    private void SetDecision(int decision, Players player){
        _gridValues.replace(decision, player);
        System.out.println(player.toString() + " chose: " + decision + ".");
    }

    // See if someone has won already.
    private Players GetWinner(){
        List<Integer> humanMarks = new ArrayList<>();
        List<Integer> computerMarks = new ArrayList<>();

        // Collect all checks per player.
        for (int i = 1; i <= _gridValues.size(); i++){
            if(_gridValues.get(i).equals(Players.Human)){
                humanMarks.add(i);
            } else if(_gridValues.get(i).equals(Players.Computer)){
                computerMarks.add(i);
            }
        }

        // The winning conditions.
        // A player must have at least one of these subsets fully checked on the grid, in order to win.
        List<List<Integer>> winningConditions = asList(
                asList(1, 2, 3),
                asList(4, 5, 6),
                asList(7, 8, 9),
                asList(1, 4, 7),
                asList(2, 5, 8),
                asList(3, 6, 9),
                asList(1, 5, 9),
                asList(3, 5, 7)
        );

        for (List<Integer> winningCondition : winningConditions) {
            if(humanMarks.containsAll(winningCondition)){
                // Human won.
                return Players.Human;
            }

            if(computerMarks.containsAll(winningCondition)){
                // Computer won.
                return Players.Computer;
            }
        }

        // No one has won.
        return Players.None;
    }

    // The kinds of players there are in this game.
    // None is essentially used to prevent having to use "null" in certain scenarios.
    private enum Players{
        None,
        Human,
        Computer
    }
}