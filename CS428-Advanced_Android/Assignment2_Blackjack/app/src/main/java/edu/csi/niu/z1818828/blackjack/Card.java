/************************************************************************
 *                                                                      *
 * CSCI 428       			  Assignment 2               		 SP2021 *
 *                                                            		    *
 * 	Class Name: Card.java		    		        					*
 * 																		*
 *  Developer: Matthew Gedge											*
 *  Due Date: 18 February 2021							    			*
 *   																	*
 *  Purpose: This class simply creates a Card object. This object has a *
 *  suit, rank, and an image that defines each card                     *
 *																		*
 * *********************************************************************/

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
