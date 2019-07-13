package com.company;

import java.util.*;
import static java.util.Arrays.asList;

class TicTacToe {
    private enum Players{
        Human,
        Computer,
        None
    }

    private enum PlayerChecks {
        X, // Human
        O  // Computer
    }

    private Players _playerOnTurn = Players.None;
    private Map<Integer, String> _gridValues;

    TicTacToe(){
        SetupGrid();
        PlayGame();
    }

    // Set who is playing first.
    private void SetNextPlayer(){
        if(_playerOnTurn == Players.None){
            _playerOnTurn = Players.Human;
        } else{
            if (_playerOnTurn == Players.Computer){
                _playerOnTurn = Players.Human;
            } else if (_playerOnTurn == Players.Human) {
                _playerOnTurn = Players.Computer;
            }
        }
    }

    // Sets the size of the grid.
    private void SetupGrid(){
        _gridValues = new HashMap<>();

        for (int i = 1; i < Math.pow(3, 2) + 1; i++){
            _gridValues.put(i, String.valueOf(i));
        }
    }

    private void PlayGame(){
       Players winner = Players.None;
       HashMap <Integer, String> availableChoices;

       while(winner == Players.None){
           availableChoices = GetAvailableChoices();
           if(availableChoices.isEmpty()){
               break;
           }
           SetNextPlayer();
           MakeTurn(availableChoices);
           winner = GetWinner();
       }

        PrintGrid();

       if(winner == Players.None){
           System.out.println("Game is a tie!");
       } else{
           System.out.println(winner.toString() + " wins!");
       }
    }

    private void PrintGrid(){
        if(!_gridValues.isEmpty()){
            String dashRow = "\n-------------";
            System.out.println(dashRow);

            for(int i = 1; i <= _gridValues.values().size(); i++){
                System.out.print("| " +  _gridValues.get(i) + " ");

                if(i % 3 == 0){
                    System.out.println("| " + dashRow);
                }
            }
            System.out.println();
        }
    }

    private HashMap<Integer, String> GetAvailableChoices(){
        HashMap<Integer, String> availableChoices = new HashMap<>(_gridValues);

        for (Map.Entry<Integer, String> choice : _gridValues.entrySet()) {
            if(choice.getValue().equals(PlayerChecks.X.toString()) || choice.getValue().equals(PlayerChecks.O.toString())){
                availableChoices.remove(choice.getKey());
            }
        }

        return availableChoices;
    }

    private void MakeTurn(HashMap<Integer, String> availableChoices){
        if(_playerOnTurn == Players.Human){
           MakePlayerTurn(new ArrayList<>(availableChoices.keySet()));
        }else if(_playerOnTurn == Players.Computer){
           MakeComputerTurn(new ArrayList<>(availableChoices.keySet()));
        }
    }

    private void MakePlayerTurn(List<Integer> availableChoices){
        PrintGrid();
        System.out.println("Player's turn.");
        boolean madeTurn = false;

        while(!madeTurn){
            try{
                System.out.print("Your choice: ");
                Scanner scanner = new Scanner(System.in);
                int playersDecision = scanner.nextInt();

                if(availableChoices.contains(playersDecision)){
                    _gridValues.replace(playersDecision, PlayerChecks.X.toString());
                    madeTurn = true;
                } else {
                    throw new InputMismatchException();
                }
            } catch(InputMismatchException e){
                System.out.println("Please make a valid choice.");
            }
        }
    }

    private void MakeComputerTurn(List<Integer> availableChoices){
        Collections.shuffle(availableChoices);
        int decision = availableChoices.get(0);
        _gridValues.replace(decision, PlayerChecks.O.toString());
        System.out.println("Computer chose: " + decision + ".");
    }

    private Players GetWinner(){
        List<Integer> humanMarks = new ArrayList<>();
        List<Integer> computerMarks = new ArrayList<>();

        for (int i = 1; i <= _gridValues.size(); i++){
            if(_gridValues.get(i).equals(PlayerChecks.X.toString())){
               humanMarks.add(i);
            } else if(_gridValues.get(i).equals(PlayerChecks.O.toString())){
                computerMarks.add(i);
            }
        }

        // These are all the valid winning conditions.
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
                return Players.Human;
            }

            if(computerMarks.containsAll(winningCondition)){
                return Players.Computer;
            }
        }

        return Players.None;
    }
}