package edu.csi.niu.z1818828.blackjack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Handler mHandler;
    Runnable mUpdate;
    Button buttonHit;
    Button buttonStand;
    Button buttonDeal;
    TextView textViewDebug;
    TextView textViewPlayerScore;
    TextView textViewDealerScore;
    ImageView imageViewGameStatus;
    ConstraintLayout screen;


    Card[] cards;
    int gameStatus = 0; //0 - not active, 1 - active game, 2 - win/lose scenario
    int counter = 0;
    int deckStack = 0;
    int playerScore;
    int dealerScore;

    Deque<Card> playerStack = new ArrayDeque<Card>();
    Deque<Card> dealerStack = new ArrayDeque<Card>();
    Map<String, Bitmap> bitmapCards = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonHit = findViewById(R.id.buttonHit);
        buttonStand = findViewById(R.id.buttonStand);
        buttonDeal = findViewById(R.id.buttonDeal);
        textViewDebug = findViewById(R.id.textViewDebugCard);
        textViewPlayerScore = findViewById(R.id.textViewPlayerScore);
        textViewDealerScore = findViewById(R.id.textViewDealerScore);
        imageViewGameStatus = findViewById(R.id.imageViewGameStatus);
        screen = findViewById(R.id.gameScreen);

        switchGameStatusView();

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
                playerScore += evaluateCardScore(cards[deckStack].rank);

                deckStack++;
                playerStack.add(cards[deckStack]);
                playerScore += evaluateCardScore(cards[deckStack].rank);
                deckStack++;

                //Deal two cards to the dealer
                dealerStack.add(cards[deckStack]);
                dealerScore += evaluateCardScore(cards[deckStack].rank);
                deckStack++;
                dealerStack.add(cards[deckStack]);
                dealerScore += evaluateCardScore(cards[deckStack].rank);
                deckStack++;
                updateGameScore();
            }
        });

        buttonHit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gameStatus != 2) {
                    textViewDebug.setText("Card num: " + deckStack + " Suit: " + cards[deckStack].suit + " Rank: " + cards[deckStack].rank + " Card: " + cards[deckStack].cardPicture);
                    playerStack.add(cards[deckStack]);
                    playerScore += evaluateCardScore(cards[deckStack].rank);
                    deckStack++;
                    updateGameScore();

                    if (playerScore > 21) {
                        gameStatus = 2;
                        imageViewGameStatus.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.lose, null));
                        imageViewGameStatus.setVisibility(View.VISIBLE);
                        cards = null;
                    }
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

    public int evaluateCardScore(int rank) {
        if(rank == 0) {
            if(playerScore > 10)
                return 1;
            else
                return 11;
        }
        else if(rank > 9)
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

        cards = new Card[52];
        cards = initialize(cards);
        cards = shuffleDeck(cards);

    }
}