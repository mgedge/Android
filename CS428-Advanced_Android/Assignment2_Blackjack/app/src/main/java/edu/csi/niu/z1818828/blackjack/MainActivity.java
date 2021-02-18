package edu.csi.niu.z1818828.blackjack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    //UI Elements
    Button buttonHit;
    Button buttonStand;
    Button buttonDeal;
    Button buttonNewRound;
    Button buttonAddMoney;
    Button buttonSurrender;
    Button buttonDoubleDown;
    Button blueChip;
    Button greenChip;
    Button redChip;
    Button blackChip;
    TextView textViewNoMoney;
    TextView textViewPlaceBets;
    TextView textViewPlayerScore;
    TextView textViewDealerScore;
    TextView textViewPlayerLabel;
    TextView textViewDealerLabel;
    TextView textViewCashLabel;
    TextView textViewCash;
    TextView textViewBetLabel;
    TextView textViewBet;
    TextView textViewBlueChip;
    TextView textViewGreenChip;
    TextView textViewRedChip;
    TextView textViewBlackChip;
    ImageView imageViewGameStatus;
    RecyclerView dealerList;
    RecyclerView playerList;
    ConstraintLayout screen;

    //Simple data types
    int gameStatus = 0; //-1 - out of money, 0 - not active, 1 - active game, 2 - win/lose scenario
    int counter = 0;
    int talonStack = 0;
    int playerScore;
    int dealerScore;
    int hiddenCard;
    int playerCash = 1000;
    int playerBet = 0;
    boolean bAllowBets = true;
    boolean firstTurn = true;

    //Game settings
    boolean bAllowSurrender;
    boolean bAllowDouble;
    int numDecks;

    //Complex data types
    Card[] cards;
    List<Card> playerStack = new ArrayList<>();
    List<Card> dealerStack = new ArrayList<>();
    CardArrayAdapter playerAdapter;
    CardArrayAdapter dealerAdapter;

    //Set the overlapping card decoration
    RecyclerView.ItemDecoration decoration = new OverlapDecoration();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get the UI elements
        buttonHit = findViewById(R.id.buttonHit);
        buttonStand = findViewById(R.id.buttonStand);
        buttonDeal = findViewById(R.id.buttonDeal);
        buttonDoubleDown = findViewById(R.id.buttonDoubledown);
        buttonSurrender = findViewById(R.id.buttonSurrender);
        buttonNewRound = findViewById(R.id.buttonNewGame);
        buttonAddMoney = findViewById(R.id.buttonAddMoney);
        blueChip = findViewById(R.id.buttonBlueChip);
        blackChip = findViewById(R.id.buttonBlackChip);
        greenChip = findViewById(R.id.buttonGreenChip);
        redChip = findViewById(R.id.buttonRedChip);
        textViewBlueChip = findViewById(R.id.textViewBlueChip);
        textViewBlackChip = findViewById(R.id.textViewBlackChip);
        textViewGreenChip = findViewById(R.id.textViewGreenChip);
        textViewRedChip = findViewById(R.id.textViewRedChip);
        textViewNoMoney = findViewById(R.id.textViewNoMoney);
        textViewPlaceBets = findViewById(R.id.textViewPlaceBets);
        textViewPlayerScore = findViewById(R.id.textViewPlayerScore);
        textViewPlayerLabel = findViewById(R.id.textViewPlayer);
        textViewDealerScore = findViewById(R.id.textViewDealerScore);
        textViewDealerLabel = findViewById(R.id.textViewDealer);
        textViewBet = findViewById(R.id.textViewBetAmount);
        textViewBetLabel = findViewById(R.id.textViewBet);
        textViewCash = findViewById(R.id.textViewCashAmount);
        textViewCashLabel = findViewById(R.id.textViewCash);
        imageViewGameStatus = findViewById(R.id.imageViewGameStatus);
        screen = findViewById(R.id.gameScreen);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu_options);

        //Get game settings
        getGameSettings();

        //Set bets
        updateCash();

        //Get the recyclerViews
        playerList = findViewById(R.id.listViewPlayer);
        dealerList = findViewById(R.id.listViewDealer);

        //Set fixed size
        playerList.setHasFixedSize(true);
        dealerList.setHasFixedSize(true);

        //Setup layout managers
        playerList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        playerList.addItemDecoration(decoration);
        dealerList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        dealerList.addItemDecoration(decoration);

        //Create the adapters for the listViews
        playerAdapter = new CardArrayAdapter(this, playerStack);
        dealerAdapter = new CardArrayAdapter(this, dealerStack);

        //loadData();

        //Set the adapters for the listViews
        playerList.setAdapter(playerAdapter);
        dealerList.setAdapter(dealerAdapter);

        playerAdapter.notifyDataSetChanged();
        dealerAdapter.notifyDataSetChanged();

        //Game screen created, change visibility of relevant components
        switchUIGameStatusView();

        //Change visibility of extra game buttons
        switchSettingButtonsViews();

        //Create the deck of cards
        cards = new Card[52 * numDecks];
        cards = initialize(cards, numDecks);
        cards = shuffleDeck(cards, numDecks);


        //Look for when the screen is touched
        screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gameStatus == 2) {
                    newRound();
                }
            }
        });

        //Deal button listener
        buttonDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gameStatus == 2)
                    newRound();

                //Get the game settings
                getGameSettings();

                //Start the game status
                gameStatus = 1;
                firstTurn = true;
                bAllowBets = false;
                switchUIGameStatusView();

                //Show extra buttons
                switchSettingButtonsViews();

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

        buttonNewRound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newRound();
            }
        });

        //Hit button listener
        buttonHit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If the game is not over, handle the logic for the button
                if (gameStatus != 2) {
                    //Debug text line to see last hit card
                    //textViewDebug.setText("Card num: " + deckStack + " Suit: " + cards[deckStack].suit + " Rank: " + cards[deckStack].rank + " Card: " + cards[deckStack].cardPicture);

                    //End the first turn status
                    firstTurn = false;

                    //Disable extra buttons
                    switchSettingButtonsViews();

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

                        if (playerCash <= 0) {
                            restartGame();
                        } else {
                            setUIGameStatus(-1);
                            switchUIGameStatusView();
                        }
                    }
                    //If the player hit blackjack, the dealer takes the turn.
                    else if (playerScore == 21) {
                        dealerTurn();

                        //Tie
                        if (dealerScore == 21) {
                            setUIGameStatus(0);
                            playerCash += playerBet;
                            updateCash();
                        }
                        //Win
                        else {
                            setUIGameStatus(1);
                            playerCash += (playerBet * 2);
                            updateCash();
                        }

                        switchUIGameStatusView();
                    }
                } else {
                    newRound();
                }
            }
        });

        //Stand button listener
        buttonStand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gameStatus != 2) {
                    //End the first turn status
                    firstTurn = false;

                    //Show extra buttons
                    switchSettingButtonsViews();

                    //Play dealer
                    dealerTurn();

                    //Determine the winner
                    evaluateWinner();
                } else {
                    newRound();
                }
            }
        });

        if (bAllowSurrender) {
            buttonSurrender.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (firstTurn) {
                        //End the first turn status
                        firstTurn = false;

                        //Show extra buttons
                        switchSettingButtonsViews();

                        //Give the player back half their bet
                        playerCash += ((int) (playerBet / 2));

                        //Play dealer's turn
                        dealerTurn();

                        //End the game and change the views
                        restartGame();
                        setUIGameStatus(-1);
                        switchUIGameStatusView();
                    }
                }
            });
        }

        if (bAllowDouble) {
            buttonDoubleDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (firstTurn) {
                        //Make sure player has more cash than the bet
                        if (playerCash >= playerBet) {
                            //End the first turn status
                            firstTurn = false;

                            //Show extra buttons
                            switchSettingButtonsViews();

                            //Double the bet, decrement another bet from cash
                            playerCash -= playerBet;
                            playerBet = playerBet * 2;

                            //Deal the card to the player
                            playerStack.add(cards[talonStack]);
                            playerScore = evaluateDeckScore(playerStack);
                            talonStack++;

                            //Update the view
                            playerAdapter.notifyDataSetChanged();
                            updateGameScore();

                            //Play dealer's turn
                            dealerTurn();

                            //Evaluate the winner of the game
                            evaluateWinner();
                        }
                    }
                }
            });
        }

        buttonAddMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newRound();
                playerCash = 1000;
                updateCash();
            }
        });

        //blueChip
        blueChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((playerCash >= 1) && (bAllowBets)) {
                    playerCash--;
                    playerBet++;
                    updateCash();
                }
            }
        });

        greenChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((playerCash >= 10) && (bAllowBets)) {
                    playerCash -= 10;
                    playerBet += 10;
                    updateCash();
                }
            }
        });

        redChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((playerCash >= 100) && (bAllowBets)) {
                    playerCash -= 100;
                    playerBet += 100;
                    updateCash();
                }
            }
        });

        blackChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((playerCash >= 1000) && (bAllowBets)) {
                    playerCash -= 1000;
                    playerBet += 1000;
                    updateCash();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
                saveData();
                Toast.makeText(this, "Game saved!", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_options:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Load the player's cash
        loadData();

        //Give money if ended broke
        if ((playerCash == 0) && (gameStatus == 0)) {
            playerCash = 1000;
        }

        //Start new round
        newRound();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }

    /**
     * This method will save player cash and refund outstanding bets IF the game has not yet started
     */
    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("SharedPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //If the game hasn't started yet, "refund" the player
        if (gameStatus == 0)
            playerCash += playerBet;

        //Save the cash
        editor.putInt("cash", playerCash);
        editor.apply();
    }

    /**
     * This method will load player cash from save and update the cash UI
     */
    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("SharedPref", MODE_PRIVATE);
        playerCash = sharedPreferences.getInt("cash", 1000);
        updateCash();
    }

    /**
     * This method sets the visibility for UI elements if the game is running or not
     */
    public void switchUIGameStatusView() {
        //Game in play
        if (gameStatus == 1) {
            textViewNoMoney.setVisibility(View.INVISIBLE);
            buttonAddMoney.setVisibility(View.INVISIBLE);
            buttonHit.setVisibility(View.VISIBLE);
            buttonStand.setVisibility(View.VISIBLE);
            buttonNewRound.setVisibility(View.GONE);
            buttonDeal.setVisibility(View.GONE);
            textViewPlaceBets.setVisibility(View.INVISIBLE);
            textViewPlayerLabel.setVisibility(View.VISIBLE);
            textViewDealerLabel.setVisibility(View.VISIBLE);
            textViewPlayerScore.setVisibility(View.VISIBLE);
            textViewDealerScore.setVisibility(View.VISIBLE);
        }
        //Lose/Win active
        else if (gameStatus == 2) {
            textViewNoMoney.setVisibility(View.INVISIBLE);
            buttonAddMoney.setVisibility(View.INVISIBLE);
            buttonHit.setVisibility(View.INVISIBLE);
            buttonStand.setVisibility(View.INVISIBLE);
            buttonNewRound.setVisibility(View.VISIBLE);
        }
        //Out of money
        else if (gameStatus == -1) {
            textViewNoMoney.setVisibility(View.VISIBLE);
            buttonAddMoney.setVisibility(View.VISIBLE);
            buttonHit.setVisibility(View.INVISIBLE);
            buttonStand.setVisibility(View.INVISIBLE);
            buttonNewRound.setVisibility(View.INVISIBLE);
            buttonDeal.setVisibility(View.INVISIBLE);
            textViewPlaceBets.setVisibility(View.INVISIBLE);
            textViewPlayerLabel.setVisibility(View.INVISIBLE);
            textViewDealerLabel.setVisibility(View.INVISIBLE);
            textViewPlayerScore.setVisibility(View.INVISIBLE);
            textViewDealerScore.setVisibility(View.INVISIBLE);
            textViewCashLabel.setVisibility(View.INVISIBLE);
            textViewCash.setVisibility(View.INVISIBLE);
            textViewBetLabel.setVisibility(View.INVISIBLE);
            textViewBet.setVisibility(View.INVISIBLE);
            blueChip.setVisibility(View.GONE);
            greenChip.setVisibility(View.GONE);
            redChip.setVisibility(View.GONE);
            blackChip.setVisibility(View.GONE);
            textViewBlueChip.setVisibility(View.GONE);
            textViewGreenChip.setVisibility(View.GONE);
            textViewRedChip.setVisibility(View.GONE);
            textViewBlackChip.setVisibility(View.GONE);
        }
        //No game active
        else {
            textViewNoMoney.setVisibility(View.INVISIBLE);
            buttonAddMoney.setVisibility(View.INVISIBLE);
            buttonHit.setVisibility(View.INVISIBLE);
            buttonStand.setVisibility(View.INVISIBLE);
            buttonNewRound.setVisibility(View.GONE);
            buttonDeal.setVisibility(View.VISIBLE);
            textViewPlaceBets.setVisibility(View.VISIBLE);
            textViewPlayerLabel.setVisibility(View.INVISIBLE);
            textViewDealerLabel.setVisibility(View.INVISIBLE);
            textViewPlayerScore.setVisibility(View.INVISIBLE);
            textViewDealerScore.setVisibility(View.INVISIBLE);
            textViewCashLabel.setVisibility(View.VISIBLE);
            textViewCash.setVisibility(View.VISIBLE);
            textViewBetLabel.setVisibility(View.VISIBLE);
            textViewBet.setVisibility(View.VISIBLE);
            blueChip.setVisibility(View.VISIBLE);
            greenChip.setVisibility(View.VISIBLE);
            redChip.setVisibility(View.VISIBLE);
            blackChip.setVisibility(View.VISIBLE);
            textViewBlueChip.setVisibility(View.VISIBLE);
            textViewGreenChip.setVisibility(View.VISIBLE);
            textViewRedChip.setVisibility(View.VISIBLE);
            textViewBlackChip.setVisibility(View.VISIBLE);
        }
    }

    /**
     * This method will switch the visibility of the extra buttons
     */
    public void switchSettingButtonsViews() {
        if (firstTurn) {
            //Game in play
            if (gameStatus == 1) {
                if (bAllowDouble)
                    buttonDoubleDown.setVisibility(View.VISIBLE);
                if (bAllowSurrender)
                    buttonSurrender.setVisibility(View.VISIBLE);
            }
            //No game in play
            else {
                buttonDoubleDown.setVisibility(View.GONE);
                buttonSurrender.setVisibility(View.GONE);
            }
        } else {
            buttonDoubleDown.setVisibility(View.GONE);
            buttonSurrender.setVisibility(View.GONE);
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
     * This function simply updates the variables from settings
     */
    public void getGameSettings() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        bAllowDouble = prefs.getBoolean("double_down", false);
        bAllowSurrender = prefs.getBoolean("surrender", false);
        try {
            numDecks = Integer.parseInt(prefs.getString("numDecks", "1"));
        } catch (Exception e) {
            numDecks = 1;
        }
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
     * This method sets the UI cash and bet values to their corresponding integer values
     */
    public void updateCash() {
        try {
            textViewCash.setText("$" + String.valueOf(playerCash));
            textViewBet.setText("$" + String.valueOf(playerBet));
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
     * This method will compare the scores of the dealer and the player. UI views and game status
     * will be updated. Bets and player cash will be managed.
     */
    public void evaluateWinner() {
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
                setUIGameStatus(-1);
        }
        //Tie
        else if (winner == 0) {
            playerCash += playerBet;
            updateCash();
            setUIGameStatus(0);
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

            updateCash();
            setUIGameStatus(1);
        }

        switchUIGameStatusView();
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
        while ((dealerScore < playerScore) && (dealerScore < 17) && (playerScore <= 21)) {
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
    public void newRound() {
        //Reset the score
        dealerScore = 0;
        playerScore = 0;

        //Take the player's losses
        playerBet = 0;

        //Update the cash balance
        updateCash();

        //Allow bets again
        bAllowBets = true;

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

            //Clear the recycler views
            playerAdapter.notifyDataSetChanged();
            dealerAdapter.notifyDataSetChanged();

            //Change the game status to new game UI
            gameStatus = -1;
            switchUIGameStatusView();
        }
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