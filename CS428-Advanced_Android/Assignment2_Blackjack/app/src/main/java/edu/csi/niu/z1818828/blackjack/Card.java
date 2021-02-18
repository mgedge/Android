package edu.csi.niu.z1818828.blackjack;

/**
 * This class defines each card in the deck. It has a suit, rank, and a corresponding picture
 */
public class Card {
    int suit = 0;
    int rank = 0;
    String cardPicture = null;

    public Card(int suit, int rank, String cardPicture) {
        this.suit = suit;
        this.rank = rank;
        this.cardPicture = cardPicture;
    }
}
