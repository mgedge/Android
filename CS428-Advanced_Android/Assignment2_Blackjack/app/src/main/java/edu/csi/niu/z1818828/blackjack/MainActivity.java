package edu.csi.niu.z1818828.blackjack;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Handler mHandler;
    Runnable mUpdate;


    Card[] cards;
    int counter = 0;

    Map<String, Bitmap> bitmapCards = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cards = new Card[52];
        cards = initialize(cards);
        cards = shuffleDeck(cards);


        mHandler = new Handler();
        mHandler.post(mUpdate);

    }

    public Card[] initialize(Card[] deck) {
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
}

