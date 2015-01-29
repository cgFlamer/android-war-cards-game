package com.gcioropina.jocrazboi;

import java.util.ArrayList;
import java.util.List;

/**
 * User entity
 * @author gcioropina
 * @crated 1/13/15
 */
public class EntityUser {
	
    private String username;
    
    private Integer cardIndex = 0;

    private List<EntityCard> cards;

    /**
     * Constructor method.
     * @param username
     */
    public EntityUser(String username) {
        cards = new ArrayList<EntityCard>();
        setUsername(username);
    }

    /**
     * Get username
     * @return String
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set username
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get card index.
     * @return
     */
    public Integer getCardIndex() {
        return cardIndex;
    }

    /**
     * Set card index
     * @param cardIndex
     */
    public void setCardIndex(Integer cardIndex) {
        this.cardIndex = cardIndex;
    }

    /**
     * Get cards list.
     * @return
     */
    public List<EntityCard> getCards() {
        return cards;
    }

    /**
     * Set cards list
     * @param cards
     */
    public void setCards(List<EntityCard> cards) {
        this.cards = cards;
    }

    /**
     * Add card in list.
     * @param card
     */
    public void addCard(EntityCard card) {
        cards.add(card);
    }

    /**
     * Remove card from list.
     * @param index
     */
    public void removeCard(Integer index) {
        cards.remove(index);
    }

    /**
     * Check if user can play.
     * @return
     */
    public boolean canPlay() {
        return !getCards().isEmpty();
    }
}