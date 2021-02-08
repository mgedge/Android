package edu.csi.niu.z1818828.blackjack;

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
