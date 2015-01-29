package com.gcioropina.jocrazboi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Game Activity
 * @author gcioropina
 * @created 1/14/15
 */
public class GameActivity extends ActionBarActivity {

    private EntityUser systemUser, localUser;
    private String localUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        /** Write local user's username from current activity's extra data bag */
        localUsername = getIntent().getStringExtra("username");

        /** Call procedures */
        createUsers();
        assignCards();

        /** Display status */
        refreshStatus();
    }

    /**
     * Show screen notification.
     * @param message
     */
    private void showScreenNotification(String message) {
        Toast
            .makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
            .show();
    }

    /**
     * Notify user when the game has finished.
     * @param winner
     */
    private void showEndOfGameNotification(EntityUser winner) {
        /** Create end of game string */
        String message = String.format(
                getResources().getString(R.string.game_won),
                winner.getUsername()
        );
        /**
         * Show dialog.
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.game_won_title);
        builder.setMessage(message);
        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        GameActivity.this.finish();
                    }
                }
        );
        /**
         * Show dialog.
         */
        builder.show();
    }

    /**
     * Create users.
     */
    private void createUsers() {
        systemUser = new EntityUser(getResources().getString(R.string.system_username));
        localUser = new EntityUser(localUsername);
    }

    /**
     * Assign cards to our users.
     */
    private void assignCards() {
        /** Get values from resources for colors and card values */
        String[] cards = getResources().getStringArray(R.array.cards);
        String[] colors = getResources().getStringArray(R.array.card_colors);

        /** Create the pack of cards */
        ArrayList<EntityCard> pack = new ArrayList<>();
        for (String color : colors) {
            for (String value : cards) {
                EntityCard card = new EntityCard(Integer.parseInt(value), Integer.parseInt(color));
                pack.add(card);
            }
        }

        /** Randomize card order */
        Collections.shuffle(pack);

        /** Assign cards to users */
        int i = 0;
        for (EntityCard card : pack) {
            if (i % 2 == 0) {
                systemUser.addCard(card);
            } else {
                localUser.addCard(card);
            }
            i++;
        }

        /** Display a notification on screen */
        showScreenNotification(getResources().getString(R.string.game_start));
    }

    /**
     * Refresh status for users.
     */
    private void refreshStatus() {
        /** Populate info for system user */
        TextView username = (TextView) findViewById(R.id.systemUsername);
        username.setText(systemUser.getUsername());
        TextView cards = (TextView) findViewById(R.id.systemCards);
        cards.setText(String.format(getResources().getString(R.string.cards_status), systemUser.getCards().size()));

        /** Populate info for local user */
        username = (TextView) findViewById(R.id.localUsername);
        username.setText(localUser.getUsername());
        cards = (TextView) findViewById(R.id.localCards);
        cards.setText(String.format(getResources().getString(R.string.cards_status), localUser.getCards().size()));

        /**
         * Check if any of the users has no cards left.
         */
        if(systemUser.canPlay() == false) {
            showEndOfGameNotification(localUser);
        } else if(localUser.canPlay() == false) {
            showEndOfGameNotification(systemUser);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    /**
     * Issue behavior when pushing play button.
     * @param v
     */
    public void onGamePlayBtnClick(View v) {
        showCardsOnViews();
        compareCards();
        incrementCardIds();
        refreshStatus();
    }

    /**
     * Compare cards functionality.
     */
    private void compareCards() {
        EntityCard systemCard, userCard;

        systemCard = systemUser.getCards().get(systemUser.getCardIndex());
        userCard = localUser.getCards().get(localUser.getCardIndex());

        Integer status = systemCard.compare(userCard);

        /** Local user won */
        if (status.equals(EntityCard.lost)) {
            try {
                transferCard(localUser, systemUser, systemCard);
                showScreenNotification(String.format(getResources().getString(R.string.game_status), localUser.getUsername()));
            } catch (CloneNotSupportedException ex) {
                Log.e("clone_error", ex.getMessage());
            }
        }
        /** System won */
        else if (status.equals(EntityCard.won)) {
            try {
                transferCard(systemUser, localUser, userCard);
                showScreenNotification(String.format(getResources().getString(R.string.game_status), systemUser.getUsername()));
            } catch (CloneNotSupportedException ex) {
                Log.e("clone_error", ex.getMessage());
            }

        }
        /** Draw -> WAR! */
        else {
            war(systemCard.getRank());
        }
    }

    /**
     * Implement war functionality by rank.
     * @param rank
     */
    private void war(Integer rank) {
        if(rank > localUser.getCards().size() || rank > systemUser.getCards().size()) {
            rank = Math.min(localUser.getCards().size(), systemUser.getCards().size());
        }

        Integer systemCardIndex, localUserCardIndex;

        systemCardIndex = Math.abs(systemUser.getCards().size() - rank - 1);
        localUserCardIndex = Math.abs(localUser.getCards().size() - rank - 1);

        EntityCard systemCard, userCard;
        systemCard = systemUser.getCards().get(systemCardIndex);
        userCard = localUser.getCards().get(localUserCardIndex);

        Integer status = systemCard.compare(userCard);

        /** Local user won */
        if (status.equals(EntityCard.lost)) {
            processWarResult(localUser, systemUser, rank);
            showScreenNotification(String.format(getResources().getString(R.string.war_status), localUser.getUsername(), rank));
        }
        /** System user won */
        else {
            processWarResult(systemUser, localUser, rank);
            showScreenNotification(String.format(getResources().getString(R.string.war_status), systemUser.getUsername(), rank));
        }
    }

    /**
     * Get cards from the defeated user and assign them to the winner.
     * Consider the arrayList of cards as a circular queue.
     * @param winner
     * @param defeated
     * @param rank
     */
    private void processWarResult(EntityUser winner, EntityUser defeated, Integer rank) {
        Integer cardIndex, cardListSize;
        EntityCard targetCard;
        cardIndex = systemUser.getCardIndex() + 1;
        cardListSize =  systemUser.getCards().size();
        for(int i = 0; i < rank; i++) {
            if(cardIndex >= cardListSize) {
                cardIndex = 0;
            }
            try {
                targetCard = defeated.getCards().get(cardIndex);
                transferCard(winner, defeated, targetCard);
            } catch (CloneNotSupportedException ex) {
                Log.e("clone_error", ex.getMessage());
            }
        }
    }

    /**
     * Transfer card from loser to winner.
     * @param winner
     * @param defeated
     * @param targetCard
     * @throws CloneNotSupportedException
     */
    private void transferCard(EntityUser winner, EntityUser defeated, EntityCard targetCard)
            throws CloneNotSupportedException{
        EntityCard copyCard;
        copyCard = (EntityCard) targetCard.clone();
        winner.addCard(copyCard);
        defeated.getCards().remove(targetCard);
    }

    /**
     * Increment current card index position.
     * Implement over ArrayLists the feature of cycle lists.
     */
    private void incrementCardIds() {
        Integer cardIndex;

        /** Update for system user */
        cardIndex = systemUser.getCardIndex();
        if (cardIndex >= (systemUser.getCards().size() - 2)) {
            cardIndex = 0;
        } else {
            cardIndex++;
        }
        systemUser.setCardIndex(cardIndex);

        /** Update for local user */
        cardIndex = localUser.getCardIndex();
        if (cardIndex >= (localUser.getCards().size() - 2)) {
            cardIndex = 0;
        } else {
            cardIndex++;
        }
        localUser.setCardIndex(cardIndex);
    }

    /**
     * Display current card value and color.
     */
    private void showCardsOnViews() {

        EntityCard card;
        TextView cardText;

        /** Populate data for system user */
        card = systemUser.getCards().get(systemUser.getCardIndex());
        cardText = (TextView) findViewById(R.id.systemCardViewText);
        cardText.setText(card.getDisplayName());
        cardText.setTextColor(getCardTextColor(card));
//        getSystemCardView().setBackgroundResource(0);
//        getSystemCardView().setBackgroundColor(getResources().getColor(R.color.card_bg_color));
        cardText.setVisibility(TextView.VISIBLE);

        /** Populate data for local user */
        card = localUser.getCards().get(localUser.getCardIndex());
        cardText = (TextView) findViewById(R.id.userCardViewText);
        cardText.setText(card.getDisplayName());
        cardText.setTextColor(getCardTextColor(card));
//        getUserCardView().setBackgroundResource(0);
//        getUserCardView().setBackgroundColor(getResources().getColor(R.color.card_bg_color));
        cardText.setVisibility(TextView.VISIBLE);


    }

    /**
     * Get card text color.
     * @param card
     * @return
     */
    private Integer getCardTextColor(EntityCard card) {
        Integer colorCode = 0;
        switch (card.getColor()) {
            case 1 :
                colorCode = getResources().getColor(R.color.color_clubs);
                break;

            case 2 :
                colorCode = getResources().getColor(R.color.color_diamonds);
                break;

            case 3 :
                colorCode = getResources().getColor(R.color.color_spades);
                break;

            case 4 :
                colorCode = getResources().getColor(R.color.color_hearts);
                break;
        }

        return colorCode;
    }

    /**
     * Exit game menu item click handler.
     * @param v
     * @return
     */
    public boolean onGameExitBtnClick(MenuItem v) {
        finish();
        return true;
    }

    /**
     * New game menu item click handler.
     * @param v
     * @return
     */
    public boolean onNewGameBtnClick(MenuItem v) {
    	GameActivity.this.finish();
    	return true;
    }
}