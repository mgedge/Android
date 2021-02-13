package edu.csi.niu.z1818828.blackjack;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
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
    Handler mHandler;
    Runnable mUpdate;
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
    int deckStack = 0;
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
        textViewDebug = findViewById(R.id.textViewDebugCard);
        textViewPlayerScore = findViewById(R.id.textViewPlayerScore);
        textViewDealerScore = findViewById(R.id.textViewDealerScore);
        imageViewGameStatus = findViewById(R.id.imageViewGameStatus);
        screen = findViewById(R.id.gameScreen);

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
        switchGameStatusView();

        //Create the deck of cards
        cards = new Card[52];
        cards = initialize(cards);
        cards = shuffleDeck(cards);

        //mHandler = new Handler();
        //mHandler.post(mUpdate);

        screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gameStatus == 2) {
                    resetGame();
                }
            }
        });

        buttonDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start the game status
                gameStatus = 1;
                switchGameStatusView();

                //Deal two cards to the player
                playerStack.add(cards[deckStack]);
                playerScore += evaluateCardScore(playerScore, cards[deckStack].rank);
                deckStack++;

                playerStack.add(cards[deckStack]);
                playerScore += evaluateCardScore(playerScore, cards[deckStack].rank);
                deckStack++;

                //Deal two cards to the dealer, but keep one hidden
                dealerStack.add(null);
                hiddenCard = deckStack;
                //dealerScore += evaluateCardScore(cards[deckStack].rank);
                deckStack++;

                dealerStack.add(cards[deckStack]);
                dealerScore += evaluateCardScore(dealerScore, cards[deckStack].rank);
                deckStack++;

                playerAdapter.notifyDataSetChanged();
                dealerAdapter.notifyDataSetChanged();
                updateGameScore();
            }
        });

        buttonHit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If the game is not over, handle the logic for the button
                if(gameStatus != 2) {
                    textViewDebug.setText("Card num: " + deckStack + " Suit: " + cards[deckStack].suit + " Rank: " + cards[deckStack].rank + " Card: " + cards[deckStack].cardPicture);
                    //Add the new card to the players deck
                    playerStack.add(cards[deckStack]);
                    playerAdapter.notifyDataSetChanged();

                    //Increment the score based on the new card
                    playerScore += evaluateCardScore(playerScore, cards[deckStack].rank);

                    //Increment the counter for the un-played deck
                    deckStack++;

                    //Update score UI elements
                    updateGameScore();

                    //If the player gets a score above 21, the game is over
                    if (playerScore > 21) {
                        gameStatus = 2;
                        imageViewGameStatus.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.lose, null));
                        imageViewGameStatus.setVisibility(View.VISIBLE);
                        cards = null;
                    }
                    //If the player hit blackjack, the dealer takes the turn.
                    else if (playerScore == 21) {
                        gameStatus = 2;
                        imageViewGameStatus.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.win, null));
                        imageViewGameStatus.setVisibility(View.VISIBLE);
                        cards = null;
                    }
                }
                else {
                    resetGame();
                }
            }
        });

        buttonStand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gameStatus != 2) {
                    //Uncover hidden card
                    dealerStack.set(0, cards[hiddenCard]);
                    dealerScore += evaluateCardScore(dealerScore, cards[hiddenCard].rank);
                    dealerAdapter.notifyDataSetChanged();
                    updateGameScore();

                }
                else {
                    resetGame();
                }
            }
        });
    }

    public void switchGameStatusView() {
        if((gameStatus == 1) || (gameStatus == 2)){
            buttonHit.setVisibility(View.VISIBLE);
            buttonStand.setVisibility(View.VISIBLE);
            buttonDeal.setVisibility(View.INVISIBLE);
            textViewPlayerScore.setVisibility(View.VISIBLE);
            textViewDealerScore.setVisibility(View.VISIBLE);
        }
        else {
            buttonHit.setVisibility(View.INVISIBLE);
            buttonStand.setVisibility(View.INVISIBLE);
            buttonDeal.setVisibility(View.VISIBLE);
            textViewPlayerScore.setVisibility(View.INVISIBLE);
            textViewDealerScore.setVisibility(View.INVISIBLE);
        }
    }

    public Card[] initialize(Card[] deck) {
        counter = 0;

        //Initialize the deck
        for(int suit = 0; suit < 4; suit++) {
            for(int rank = 0; rank < 13; rank++) {
                cards[counter] = new Card(suit, rank, resolvePNG(suit, rank));
                counter++;
            }
        }

        return deck;
    }

    public String resolvePNG(int suit, int rank) {
        String cardName = null;
        String sut = null;
        int rnk = 1;

        //Search through each suit and rank
        for(int s = 0; s < 4; s++) {
            for(int r = 0; r < 13; r++) {
                //When the rank and suit are found, set the corresponding variables
                if((suit == s) && (rank == r)) {
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
        if(sut != null)
            cardName = rnk + sut;

        return cardName;
    }

    public Card[] shuffleDeck(Card[] deck) {
        Random rnd = new Random();
        Card temp = new Card(0,0, null);

        //Iterate through the deck
        for(int i = 0; i < 52; i++) {
            //Get a random index
            int random = rnd.nextInt(52);

            //Swap the cards
            temp = deck[random];
            deck[random] = deck[i];
            deck[i] = temp;
        }

        return deck;
    }

    public void updateGameScore() {
        try {
            textViewDealerScore.setText(String.valueOf(dealerScore));
            textViewPlayerScore.setText(String.valueOf(playerScore));
        }
        catch (Exception e) {
            System.out.println("Failed to update game score - " + e);
        }
    }

    public int evaluateCardScore(int score, int rank) {
        if (rank == 0) {
            if (score > 10)
                return 1;
            else
                return 11;
        } else if (rank > 9)
            return 10;
        else
            return rank + 1;
    }

    public void resetGame() {
        dealerScore = 0;
        playerScore = 0;

        dealerStack.clear();
        playerStack.clear();

        gameStatus = 0;
        deckStack = 0;
        switchGameStatusView();
        imageViewGameStatus.setVisibility(View.INVISIBLE);

        playerAdapter.notifyDataSetChanged();
        dealerAdapter.notifyDataSetChanged();

        cards = new Card[52];
        cards = initialize(cards);
        cards = shuffleDeck(cards);

    }

    private class OverlapDecoration extends RecyclerView.ItemDecoration {
        final static int overlap = -150;

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