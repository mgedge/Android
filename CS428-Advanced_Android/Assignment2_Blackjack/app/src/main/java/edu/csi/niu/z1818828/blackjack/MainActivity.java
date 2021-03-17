/************************************************************************
 *                                                                      *
 * CSCI 428       			  Assignment 2               		 SP2021 *
 *                                                            		    *
 * 	Class Name: MainActivity.java		    							*
 * 																		*
 *  Developer: Matthew Gedge											*
 *  Due Date: 18 February 2021							    			*
 *   																	*
 *  Purpose: This activity manages the interaction of the game's UI.    *
 *  When an element of the UI is interacted with, this class will       *
 *  update corresponding UI elements and update game logic values via   *
 *  the Game class, which contains game logic methods.                  *
 *																		*
 * *********************************************************************/
package edu.csi.niu.z1818828.blackjack;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

    //Create game instance
    Game game = new Game();

    //Simple data types
    int gameStatus = 0; //-1 - out of money, 0 - not active, 1 - active game, 2 - win/lose scenario
    boolean bAllowBets = true;
    boolean firstTurn = true;

    //Constants
    static final int blueChipValue = 1;
    static final int greenChipValue = 10;
    static final int redChipValue = 100;
    static final int blackChipValue = 1000;

    //Game settings
    boolean bAllowSurrender;
    boolean bAllowDouble;

    //Complex data types
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

        //Set the game status
        imageViewGameStatus.setVisibility(View.GONE);

        //Set bets
        updateUICash();

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
        playerAdapter = new CardArrayAdapter(this, game.playerStack);
        dealerAdapter = new CardArrayAdapter(this, game.dealerStack);

        //Set the adapters for the listViews
        playerList.setAdapter(playerAdapter);
        dealerList.setAdapter(dealerAdapter);

        playerAdapter.notifyDataSetChanged();
        dealerAdapter.notifyDataSetChanged();

        //Game screen created, change visibility of relevant components
        switchUIGameStatusView();

        //Change visibility of extra game buttons
        switchSettingButtonsViews();

        //Look for when the screen is touched
        screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gameStatus == 2) {
                    game.newRound();

                    //Update the cash balance
                    updateUICash();

                    //Allow bets again
                    bAllowBets = true;
                }
            }
        });

        //Deal button listener
        buttonDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gameStatus == 2)
                    game.newRound();

                //Get the game settings
                getGameSettings();

                //Start the game status
                gameStatus = 1;
                firstTurn = true;
                bAllowBets = false;
                switchUIGameStatusView();

                //Show extra buttons
                switchSettingButtonsViews();

                //Deal the cards
                game.dealPlayer();
                game.dealHouse();

                //Update the UI
                playerAdapter.notifyDataSetChanged();
                dealerAdapter.notifyDataSetChanged();
                updateUIGameScore();
            }
        });

        buttonNewRound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.newRound();
                newRoundUI();
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

                    //Hit the player
                    setUIGameStatus(game.hit());

                    //Update the cards
                    playerAdapter.notifyDataSetChanged();

                    //Update the score
                    updateUIGameScore();

                    //Update the cash
                    updateUICash();

                    //Retrieve new cash
                    if (game.playerCash <= 0)
                        gameStatus = -1;

                    //Update the views
                    switchUIGameStatusView();
                } else {
                    game.newRound();
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
                    game.dealerTurn();

                    //Update dealer cards
                    dealerAdapter.notifyDataSetChanged();

                    //Update the score
                    updateUIGameScore();

                    //Determine the winner
                    setUIGameStatus(game.evaluateWinner());

                    //Update cash
                    updateUICash();

                    //Update UI
                    switchUIGameStatusView();
                } else {
                    game.newRound();
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

                        //Surrender the player
                        game.surrender();

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
                        if (game.playerCash >= game.playerBet) {
                            //End the first turn status
                            firstTurn = false;

                            //Show extra buttons
                            switchSettingButtonsViews();

                            //Double the player's bet
                            game.doubleDown();

                            //Update the view
                            playerAdapter.notifyDataSetChanged();
                            updateUIGameScore();

                            //Play dealer's turn
                            game.dealerTurn();
                            dealerAdapter.notifyDataSetChanged();
                            updateUIGameScore();

                            //Evaluate the winner of the game
                            setUIGameStatus(game.evaluateWinner());

                            updateUICash();
                            switchUIGameStatusView();
                        }
                    }
                }
            });
        }

        buttonAddMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.newRound();
                game.playerCash = 1000;
                updateUICash();
                gameStatus = 0;

                updateUIGameScore();
                newRoundUI();
            }
        });

        //blueChip
        blueChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bAllowBets) {
                    game.addBet(blueChipValue);
                    updateUICash();
                }
            }
        });

        greenChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bAllowBets) {
                    game.addBet(greenChipValue);
                    updateUICash();
                }
            }
        });

        redChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bAllowBets) {
                    game.addBet(redChipValue);
                    updateUICash();
                }
            }
        });

        blackChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bAllowBets) {
                    game.addBet(blackChipValue);
                    updateUICash();
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
            case R.id.menu_help:
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(R.string.blackjack_rules)
                        .setMessage(R.string.blackjack_rules_description)
                        .setCancelable(false)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        }).show();
                return true;
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
        if ((game.playerCash == 0) && (gameStatus == 0)) {
            game.playerCash = 1000;
        }

        //Start new round
        game.newRound();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }

    /**
     * This function simply updates the variables from settings
     */
    public void getGameSettings() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        bAllowDouble = prefs.getBoolean("double_down", false);
        bAllowSurrender = prefs.getBoolean("surrender", false);

        try {
            game.numDecks = Integer.parseInt(prefs.getString("numDecks", "1"));
        } catch (Exception e) {
            game.numDecks = 1;
        }
    }

    /**
     * This method will save player cash and refund outstanding bets IF the game has not yet started
     */
    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("SharedPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //If the game hasn't started yet, "refund" the player
        if (gameStatus == 0)
            game.playerCash += game.playerBet;

        //Save the cash
        editor.putInt("cash", game.playerCash);
        editor.apply();
    }

    /**
     * This method will load player cash from save and update the cash UI
     */
    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("SharedPref", MODE_PRIVATE);
        game.playerCash = sharedPreferences.getInt("cash", 1000);
        updateUICash();
    }

    /**
     * This method sets the visibility for UI elements if the game is running or not
     */
    public void switchUIGameStatusView() {
        //Game in play
        if (gameStatus == 1) {
            dealerList.setVisibility(View.VISIBLE);
            playerList.setVisibility(View.VISIBLE);
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
            blueChip.setVisibility(View.GONE);
            greenChip.setVisibility(View.GONE);
            redChip.setVisibility(View.GONE);
            blackChip.setVisibility(View.GONE);
            textViewBlueChip.setVisibility(View.GONE);
            textViewGreenChip.setVisibility(View.GONE);
            textViewRedChip.setVisibility(View.GONE);
            textViewBlackChip.setVisibility(View.GONE);
        }
        //Lose/Win active
        else if (gameStatus == 2) {
            dealerList.setVisibility(View.INVISIBLE);
            playerList.setVisibility(View.INVISIBLE);
            textViewPlayerLabel.setVisibility(View.INVISIBLE);
            textViewDealerLabel.setVisibility(View.INVISIBLE);
            textViewPlayerScore.setVisibility(View.INVISIBLE);
            textViewDealerScore.setVisibility(View.INVISIBLE);
            textViewNoMoney.setVisibility(View.INVISIBLE);
            buttonAddMoney.setVisibility(View.INVISIBLE);
            buttonHit.setVisibility(View.INVISIBLE);
            buttonStand.setVisibility(View.INVISIBLE);
            buttonNewRound.setVisibility(View.VISIBLE);
        }
        //Out of money
        else if (gameStatus == -1) {
            dealerList.setVisibility(View.INVISIBLE);
            playerList.setVisibility(View.INVISIBLE);
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
            dealerList.setVisibility(View.INVISIBLE);
            playerList.setVisibility(View.INVISIBLE);
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
     * @param status an integer for lose (-1), tie (0), and win(1) of the player, do nothing otherwise
     */
    public void setUIGameStatus(int status) {
        //Player lost
        if (status == -1) {
            if (game.playerCash <= 0)
                gameStatus = -1;
            else
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
        else if (status == 1) {
            gameStatus = 2;
            imageViewGameStatus.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.win, null));
            imageViewGameStatus.setVisibility(View.VISIBLE);
        }
    }

    /**
     * This method sets the UI scores to their corresponding integer values
     */
    public void updateUIGameScore() {
        try {
            textViewDealerScore.setText(String.valueOf(game.dealerScore));
            textViewPlayerScore.setText(String.valueOf(game.playerScore));
        } catch (Exception e) {
            System.out.println("Failed to update game score - " + e);
        }
    }

    /**
     * This method sets the UI cash and bet values to their corresponding integer values
     */
    public void updateUICash() {
        try {
            textViewCash.setText("$" + String.valueOf(game.playerCash));
            textViewBet.setText("$" + String.valueOf(game.playerBet));
        } catch (Exception e) {
            System.out.println("Failed to update game score - " + e);
        }
    }

    /**
     * This method resets the UI for a new round
     */
    public void newRoundUI() {
        //Reset the gameStatus code
        gameStatus = 0;

        //Update the cash balance
        updateUICash();

        //Allow bets again
        bAllowBets = true;

        //Reset UI elements to 'inactive'
        switchUIGameStatusView();
        imageViewGameStatus.setVisibility(View.INVISIBLE);

        //Clear the recycler views
        playerAdapter.notifyDataSetChanged();
        dealerAdapter.notifyDataSetChanged();
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