/************************************************************************
 *                                                                      *
 * CSCI 428       			  Assignment 2               		 SP2021 *
 *                                                            		    *
 * 	Class Name: Game.java		    		        					*
 * 																		*
 *  Developer: Matthew Gedge											*
 *  Due Date: 18 February 2021							    			*
 *   																	*
 *  Purpose: This class is used to drive the game logic. When the UI is *
 *  manipulated by the player, this class will be called to manage the  *
 *  gameplay variables.                                                 *
 *																		*
 * *********************************************************************/

package edu.csi.niu.z1818828.blackjack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    //Simple data types
    int counter = 0;
    int talonStack = 0;
    int playerScore;
    int dealerScore;
    int hiddenCard;
    int playerCash = 1000;
    int playerBet = 0;
    int numDecks = 1;

    Card[] cards;
    List<Card> playerStack = new ArrayList<>();
    List<Card> dealerStack = new ArrayList<>();

    /**
     * Game constructor creates and shuffles a deck
     */
    public Game() {
        //Create the deck of cards
        cards = new Card[52 * numDecks];
        cards = initialize(cards, numDecks);
        cards = shuffleDeck(cards, numDecks);
    }

    /**
     * This method iterates through the deck of cards and sets its values to its rank and suit
     *
     * @param deck the deck of null cards to initialize
     * @return the deck of cards with all 4 suits and 13 ranks
     */
    public Card[] initialize(Card[] deck, int decks) {
        counter = 0;

        //Initialize the deck
        for (int i = 0; i < decks; i++) {
            for (int suit = 0; suit < 4; suit++) {
                for (int rank = 0; rank < 13; rank++) {
                    cards[counter] = new Card(suit, rank, resolvePNG(suit, rank));
                    counter++;
                }
            }
        }

        return deck;
    }

    /**
     * This method determines the name of the card as a char/int pair
     *
     * @param suit the integer value of the suit
     * @param rank the integer value of the rank
     * @return a string for the suit/rank pair (i.e. "12c" which is queen of clubs)
     */
    public String resolvePNG(int suit, int rank) {
        String cardName = null;
        String sut = null;
        int rnk = 1;

        //Search through each suit and rank
        for (int s = 0; s < 4; s++) {
            for (int r = 0; r < 13; r++) {
                //When the rank and suit are found, set the corresponding variables
                if ((suit == s) && (rank == r)) {
                    switch (s) {
                        case 0:
                            sut = "c";
                            break;
                        case 1:
                            sut = "d";
                            break;
                        case 2:
                            sut = "h";
                            break;
                        case 3:
                            sut = "s";
                            break;
                    }

                    rnk = r + 1;
                }
            }
        }

        //Assuming the suit is not empty, set it to the int, char pair
        if (sut != null)
            cardName = rnk + sut;

        //Return the final char/int string pair
        return cardName;
    }

    /**
     * This method "shuffles" the deck by iterating through each element of the Card array then randomly
     * selects the replacement position to swap it to.
     *
     * @param deck the initialized deck to shuffle
     * @return the shuffled deck
     */
    public Card[] shuffleDeck(Card[] deck, int decks) {
        Random rnd = new Random();
        Card temp;

        int numCards = 52 * decks;

        if (decks < 1)
            numCards = 52;

        //Iterate through the deck
        for (int i = 0; i < numCards; i++) {
            //Get a random index
            int random = rnd.nextInt(52);

            //Swap the current card with the random index
            temp = deck[random];
            deck[random] = deck[i];
            deck[i] = temp;
        }

        //Return the shuffled deck
        return deck;
    }

    /**
     * This method evaluates the score of the card
     *
     * @param score the integer value of the players score
     * @param rank  the integer value for the rank of the card
     * @return integer value of the card's score
     */
    public int evaluateCardScore(int score, int rank) {
        //If the card is an ace
        if (rank == 0) {
            //If the score is 11 or greater, 11 value is bad
            if (score > 10)
                return 1;
                //if the score is 10 or less, 11 will raise score close to 21
            else
                return 11;
        }
        //Card is a court it is worth 10
        else if (rank > 9)
            return 10;
            //Any other card is worth its rank+1
        else
            return rank + 1;
    }

    /**
     * This method iterates through the deck param and evaluates the deck's total value
     *
     * @param deck the List of cards to evaluate
     * @return the integer value of the deck
     */
    public int evaluateDeckScore(List<Card> deck) {
        int score = 0;
        int currentCard;
        List<Integer> aces = new ArrayList<>();

        //Iterate through the deck
        for (int i = 0; i < deck.size(); i++) {
            currentCard = deck.get(i).rank;

            //If the card is an ace, add it to a temp list
            if (currentCard == 0) {
                aces.add(1);
            } else if (currentCard > 9)
                score += 10;
            else {
                score += (currentCard + 1);
            }
        }

        //Re-evaluate the aces
        if (aces.size() > 0) {
            //If there's more than one, you cannot get blackjack
            if (aces.size() > 1) {
                score += aces.size();
            } else {
                if (score > 10)
                    score++;
                else
                    score += 11;
            }
        }

        //Return final score
        return score;
    }

    /**
     * This method will compare the scores of the dealer and the player. UI views and game status
     * will be updated. Bets and player cash will be managed.
     */
    public int evaluateWinner() {
        int winner = -1; //-1 house, 0 tie, 1 player
        boolean playerBlackjack = false;

        //Player got winnable score
        if (playerScore <= 21) {

            //Dealer got a winnable score
            if (dealerScore <= 21) {

                //Dealer got blackjack
                if (dealerScore == 21) {
                    //Player also got blackjack, tie
                    if (playerScore == 21) {
                        winner = 0;
                    }
                    //Player did not have blackjack, loses
                    else {
                        winner = -1;
                    }
                }
                //Player got blackjack, but dealer did not
                else if (playerScore == 21) {
                    playerBlackjack = true;
                    winner = 1;
                }
                //Tie
                else if (dealerScore == playerScore) {
                    winner = 0;
                }
                //Player scored higher than dealer, but did not get blackjack
                else if (playerScore > dealerScore) {
                    winner = 1;
                }
            }
            //Dealer went over 21
            else {
                //If player got blackjack
                if (playerScore == 21) {
                    playerBlackjack = true;
                }

                winner = 1;
            }
        }

        //House won
        if (winner == -1) {
            if (playerCash <= 0)
                restartGame();
            else
                return winner;
        }
        //Tie
        else if (winner == 0) {
            playerCash += playerBet;
            return winner;
        }
        //Player won
        else {
            if (playerBlackjack) {
                //Player won, give back original bet AND blackjack earnings
                playerCash += (playerBet + (playerBet * 1.5));
            } else {
                //Player won, give back bet AND earnings
                playerCash += (playerBet + playerBet);
            }

            return winner;
        }

        return winner;
    }

    /**
     * This method changes the players bet and cash amount by the bet if the bet
     * does not exceed player's available cash
     *
     * @param betAmount the amount of money to add to the bet
     */
    public void addBet(int betAmount) {
        if (playerCash >= betAmount) {
            playerCash -= betAmount;
            playerBet += betAmount;
        }
    }

    /**
     * This method deals two cards to the player
     */
    public void dealPlayer() {
        //Deal two cards to the player
        playerStack.add(cards[talonStack]);
        playerScore += evaluateCardScore(playerScore, cards[talonStack].rank);
        talonStack++;

        playerStack.add(cards[talonStack]);
        playerScore += evaluateCardScore(playerScore, cards[talonStack].rank);
        talonStack++;
    }

    /**
     * This method deals one card to the house, and keeps one card hidden
     */
    public void dealHouse() {
        //Deal two cards to the dealer, but keep one hidden
        dealerStack.add(null);
        hiddenCard = talonStack;
        talonStack++;

        dealerStack.add(cards[talonStack]);
        dealerScore += evaluateCardScore(dealerScore, cards[talonStack].rank);
        talonStack++;
    }

    /**
     * This method adds a card to the players deck and returns a status code if the hit results
     * in a win/lose scenario
     *
     * @return an integer code for the
     */
    public int hit() {
        //Add the new card to the players deck
        playerStack.add(cards[talonStack]);

        //Increment the score based on the new card
        playerScore += evaluateCardScore(playerScore, cards[talonStack].rank);

        //Increment the counter for the un-played deck
        talonStack++;

        //If the player gets a score above 21, the game is over
        if (playerScore > 21) {
            dealerTurn();

            if (playerCash <= 0) {
                restartGame();
            } else {
                return -1;
            }
        }
        //If the player hit blackjack, the dealer takes the turn.
        else if (playerScore == 21) {
            dealerTurn();

            //Tie
            if (dealerScore == 21) {
                playerCash += playerBet;
                return 0;
            }
            //Win
            else {
                playerCash += (playerBet * 2);
                return 1;
            }
        }

        return 2;
    }

    public void doubleDown() {
        //Double the bet, decrement another bet from cash
        playerCash -= playerBet;
        playerBet = playerBet * 2;

        //Deal the card to the player
        playerStack.add(cards[talonStack]);
        playerScore = evaluateDeckScore(playerStack);
        talonStack++;
    }

    public void surrender() {
        //Give the player back half their bet
        playerCash += playerBet / 2;

        //Play dealer's turn
        dealerTurn();

        //End the game and change the views
        restartGame();
    }

    /**
     * This method handles the logic for the dealer.
     * First, the hidden card is uncovered.
     * Next, the dealer will hit until dealers score is more than player's, hits blackjack, or the dealer busts
     */
    public void dealerTurn() {
        //Uncover hidden card
        dealerStack.set(0, cards[hiddenCard]);
        dealerScore += evaluateCardScore(dealerScore, cards[hiddenCard].rank);

        //Deal cards until dealer blackjack, bust, or score is greater than players
        while ((dealerScore < playerScore) && (dealerScore < 17) && (playerScore <= 21)) {
            //Add a new card to the dealer deck
            dealerStack.add(cards[talonStack]);

            //Increment the score based on the new card
            dealerScore = evaluateDeckScore(dealerStack);

            //Increment the counter for the talon deck
            talonStack++;
        }

    }

    /**
     * This method simply resets all game variables to their starting values
     */
    public void newRound() {
        //Reset the score
        dealerScore = 0;
        playerScore = 0;

        //Take the player's losses
        playerBet = 0;

        //Clear the decks
        dealerStack.clear();
        playerStack.clear();

        //Clear the talon
        cards = null;

        //reset the talon counter
        talonStack = 0;

        //Create a new deck and shuffle for next game
        cards = new Card[52 * numDecks];
        cards = initialize(cards, numDecks);
        cards = shuffleDeck(cards, numDecks);
    }

    public void restartGame() {
        if (playerCash <= 0) {
            //Clear the decks
            dealerStack.clear();
            playerStack.clear();

            //Clear the talon
            cards = null;

            /*

            //Clear the recycler views
            playerAdapter.notifyDataSetChanged();
            dealerAdapter.notifyDataSetChanged();

            //Change the game status to new game UI
            gameStatus = -1;
            switchUIGameStatusView();

             */
        }
    }
}
