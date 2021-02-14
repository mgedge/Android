package edu.csi.niu.z1818828.blackjack;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    //UI Elements
    Button buttonHit;
    Button buttonStand;
    Button buttonDeal;
    TextView textViewDebug;
    TextView textViewPlayerScore;
    TextView textViewDealerScore;
    ImageView imageViewGameStatus;
    RecyclerView dealerList;
    RecyclerView playerList;
    ConstraintLayout screen;

    //Simple data types
    int gameStatus = 0; //0 - not active, 1 - active game, 2 - win/lose scenario
    int counter = 0;
    int talonStack = 0;
    int playerScore;
    int dealerScore;
    int hiddenCard;

    //Complex data types
    Card[] cards;
    List<Card> playerStack = new ArrayList<>();
    List<Card> dealerStack = new ArrayList<>();
    CardArrayAdapter playerAdapter;
    CardArrayAdapter dealerAdapter;
    Map<String, Bitmap> bitmapCards = new HashMap<>();

    RecyclerView.ItemDecoration decoration = new OverlapDecoration();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get the UI elements
        buttonHit = findViewById(R.id.buttonHit);
        buttonStand = findViewById(R.id.buttonStand);
        buttonDeal = findViewById(R.id.buttonDeal);
        textViewPlayerScore = findViewById(R.id.textViewPlayerScore);
        textViewDealerScore = findViewById(R.id.textViewDealerScore);
        imageViewGameStatus = findViewById(R.id.imageViewGameStatus);
        screen = findViewById(R.id.gameScreen);

        //Get the recyclerViews
        playerList = findViewById(R.id.listViewPlayer);
        dealerList = findViewById(R.id.listViewDealer);

        //Setup layout managers
        playerList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        playerList.addItemDecoration(decoration);
        dealerList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        dealerList.addItemDecoration(decoration);

        //Create the adapters for the listViews
        playerAdapter = new CardArrayAdapter(this, playerStack);
        dealerAdapter = new CardArrayAdapter(this, dealerStack);

        //Set the adapters for the listViews
        playerList.setAdapter(playerAdapter);
        dealerList.setAdapter(dealerAdapter);

        //Game screen created, change visibility of relevant components
        switchUIGameStatusView();

        //Create the deck of cards
        cards = new Card[52];
        cards = initialize(cards);
        cards = shuffleDeck(cards);

        //Look for when the screen is touched
        screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gameStatus == 2) {
                    resetGame();
                }
            }
        });

        //Deal button listener
        buttonDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start the game status
                gameStatus = 1;
                switchUIGameStatusView();

                //Deal two cards to the player
                playerStack.add(cards[talonStack]);
                playerScore += evaluateCardScore(playerScore, cards[talonStack].rank);
                talonStack++;

                playerStack.add(cards[talonStack]);
                playerScore += evaluateCardScore(playerScore, cards[talonStack].rank);
                talonStack++;

                //Deal two cards to the dealer, but keep one hidden
                dealerStack.add(null);
                hiddenCard = talonStack;
                talonStack++;

                dealerStack.add(cards[talonStack]);
                dealerScore += evaluateCardScore(dealerScore, cards[talonStack].rank);
                talonStack++;

                //Update the UI
                playerAdapter.notifyDataSetChanged();
                dealerAdapter.notifyDataSetChanged();
                updateGameScore();
            }
        });

        //Hit button listener
        buttonHit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If the game is not over, handle the logic for the button
                if(gameStatus != 2) {
                    //Debug text line to see last hit card
                    //textViewDebug.setText("Card num: " + deckStack + " Suit: " + cards[deckStack].suit + " Rank: " + cards[deckStack].rank + " Card: " + cards[deckStack].cardPicture);

                    //Add the new card to the players deck
                    playerStack.add(cards[talonStack]);
                    playerAdapter.notifyDataSetChanged();

                    //Increment the score based on the new card
                    playerScore += evaluateCardScore(playerScore, cards[talonStack].rank);

                    //Increment the counter for the un-played deck
                    talonStack++;

                    //Update score UI elements
                    updateGameScore();

                    //If the player gets a score above 21, the game is over
                    if (playerScore > 21) {
                        dealerTurn();
                        setUIGameStatus(-1);
                    }
                    //If the player hit blackjack, the dealer takes the turn.
                    else if (playerScore == 21) {
                        dealerTurn();

                        if (dealerScore == 21) {
                            setUIGameStatus(0);
                        } else
                            setUIGameStatus(1);
                    }
                }
                else {
                    resetGame();
                }
            }
        });

        //Stand button listener
        buttonStand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gameStatus != 2) {
                    dealerTurn();

                    //Dealer got blackjack
                    if (dealerScore == 21) {
                        //Tie
                        if (playerScore == 21) {
                            setUIGameStatus(0);
                        }
                        //Player did not have blackjack
                        else {
                            setUIGameStatus(-1);
                        }
                    }
                    //Tie
                    else if (dealerScore == playerScore)
                        setUIGameStatus(0);
                        //Neither player got blackjack
                    else if ((dealerScore > playerScore) && (dealerScore < 21)) {
                        if (dealerScore == playerScore)
                            setUIGameStatus(0);
                        else
                            setUIGameStatus(-1);
                    } else
                        setUIGameStatus(1);

                    switchUIGameStatusView();
                } else {
                    resetGame();
                }
            }
        });
    }

    /**
     * This method sets the visibility for UI elements if the game is running or not
     */
    public void switchUIGameStatusView() {
        if ((gameStatus == 1) || (gameStatus == 2)) {
            buttonHit.setVisibility(View.VISIBLE);
            buttonStand.setVisibility(View.VISIBLE);
            buttonDeal.setVisibility(View.INVISIBLE);
            textViewPlayerScore.setVisibility(View.VISIBLE);
            textViewDealerScore.setVisibility(View.VISIBLE);
        } else {
            buttonHit.setVisibility(View.INVISIBLE);
            buttonStand.setVisibility(View.INVISIBLE);
            buttonDeal.setVisibility(View.VISIBLE);
            textViewPlayerScore.setVisibility(View.INVISIBLE);
            textViewDealerScore.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * This method sets the visibility of the win/lose scenario image depending on game status
     *
     * @param status an integer for lose (-1), tie (0), and win(1) of the player
     */
    public void setUIGameStatus(int status) {
        //Player lost
        if (status == -1) {
            gameStatus = 2;
            imageViewGameStatus.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.lose, null));
            imageViewGameStatus.setVisibility(View.VISIBLE);
        }
        //Game tie
        else if (status == 0) {
            gameStatus = 2;
            imageViewGameStatus.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.tie, null));
            imageViewGameStatus.setVisibility(View.VISIBLE);
        }
        //Player win
        else {
            gameStatus = 2;
            imageViewGameStatus.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.win, null));
            imageViewGameStatus.setVisibility(View.VISIBLE);
        }
    }

    /**
     * This method iterates through the deck of cards and sets its values to its rank and suit
     *
     * @param deck the deck of null cards to initialize
     * @return the deck of cards with all 4 suits and 13 ranks
     */
    public Card[] initialize(Card[] deck) {
        counter = 0;

        //Initialize the deck
        for (int suit = 0; suit < 4; suit++) {
            for (int rank = 0; rank < 13; rank++) {
                cards[counter] = new Card(suit, rank, resolvePNG(suit, rank));
                counter++;
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
    public Card[] shuffleDeck(Card[] deck) {
        Random rnd = new Random();
        Card temp;

        //Iterate through the deck
        for (int i = 0; i < 52; i++) {
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
     * This method sets the UI scores to their corresponding integer values
     */
    public void updateGameScore() {
        try {
            textViewDealerScore.setText(String.valueOf(dealerScore));
            textViewPlayerScore.setText(String.valueOf(playerScore));
        } catch (Exception e) {
            System.out.println("Failed to update game score - " + e);
        }
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
     * This method handles the logic for the dealer.
     * First, the hidden card is uncovered.
     * Next, the dealer will hit until dealers score is more than player's, hits blackjack, or the dealer busts
     */
    public void dealerTurn() {
        //Uncover hidden card
        dealerStack.set(0, cards[hiddenCard]);
        dealerScore += evaluateCardScore(dealerScore, cards[hiddenCard].rank);
        dealerAdapter.notifyDataSetChanged();
        updateGameScore();

        //Deal cards until dealer blackjack, bust, or score is greater than players
        while ((dealerScore < playerScore) && (dealerScore < 21) && (playerScore <= 21)) {
            //Add a new card to the dealer deck
            dealerStack.add(cards[talonStack]);
            dealerAdapter.notifyDataSetChanged();

            //Increment the score based on the new card
            dealerScore = evaluateDeckScore(dealerStack);
            updateGameScore();

            //Increment the counter for the talon deck
            talonStack++;
        }

    }

    /**
     * This method simply resets all game variables to their starting values
     */
    public void resetGame() {
        //Reset the score
        dealerScore = 0;
        playerScore = 0;

        //Clear the decks
        dealerStack.clear();
        playerStack.clear();

        //Clear the talon
        cards = null;

        //Reset the gameStatus code
        gameStatus = 0;

        //reset the talon counter
        talonStack = 0;

        //Reset UI elements to 'inactive'
        switchUIGameStatusView();
        imageViewGameStatus.setVisibility(View.INVISIBLE);

        //Clear the recycler views
        playerAdapter.notifyDataSetChanged();
        dealerAdapter.notifyDataSetChanged();

        //Create a new deck and shuffle for next game
        cards = new Card[52];
        cards = initialize(cards);
        cards = shuffleDeck(cards);
    }

    /**
     * This supporting method layers the cards in the recyclerView to give the appearance of
     * stacked, layered cards in the recyclerView
     */
    private class OverlapDecoration extends RecyclerView.ItemDecoration {
        final int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        final static int overlap = -100; //-150 for 4XL

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);

            final int itemPos = parent.getChildAdapterPosition(view);
            if (itemPos == 0) {
                outRect.set(0, 0, 0, 0);
            } else
                outRect.set(overlap, 0, 0, 0);
        }
    }
}