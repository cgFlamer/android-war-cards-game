package com.gcioropina.jocrazboi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gcioropina on 1/13/15.
 */
public class EntityUser {
    private String username;
    
    private Integer cardIndex = 0;

    private List<EntityCard> cards;

    public EntityUser(String username) {
        cards = new ArrayList<EntityCard>();
        setUsername(username);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getCardIndex() {
        return cardIndex;
    }

    public void setCardIndex(Integer cardIndex) {
        this.cardIndex = cardIndex;
    }

    public List<EntityCard> getCards() {
        return cards;
    }

    public void setCards(List<EntityCard> cards) {
        this.cards = cards;
    }

    public void addCard(EntityCard card) {
        cards.add(card);
    }

    public void removeCard(Integer index) {
        cards.remove(index);
    }

    public boolean canPlay() {
        return !getCards().isEmpty();
    }
}