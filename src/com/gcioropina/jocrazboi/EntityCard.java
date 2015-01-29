package com.gcioropina.jocrazboi;

/**
 * Card entity.
 * @author gcioropina
 * @created 1/13/15
 */
public class EntityCard implements Cloneable {

    public static Integer won = 1;

    public static Integer lost = 0;

    public static Integer draw = 2;

    private Integer rank;

    private Integer color;

    /**
     * Constructor method.
     * @param rank
     * @param color
     */
    public EntityCard(Integer rank, Integer color) {
        setRank(rank);
        setColor(color);
    }

    /**
     * Get card's rank.
     * @return
     */
    public Integer getRank() {
        return rank;
    }

    /**
     * Set rank.
     * @param rank
     */
    public void setRank(Integer rank) {
        this.rank = rank;
    }

    /**
     * Get card color.
     * @return
     */
    public Integer getColor() {
        return color;
    }

    /**
     * Set color.
     * @param color
     */
    public void setColor(Integer color) {
        this.color = color;
    }

    /**
     * Get display name of the card.
     *
     * @return
     */
    public String getDisplayName() {
        switch (getRank()) {
            case 15:
                return "A";

            case 14:
                return "K";

            case 13:
                return "Q";

            case 12:
                return "J";

            default:
                return getRank().toString();
        }
    }

    /**
     * Check if the card is greater than another card.
     *
     * @param other
     * @return
     */
    public Integer compare(EntityCard other) {
        if (getRank() < other.getRank()) {
            return lost;
        } else if (getRank() == other.getRank()) {
            return draw;
        } else {
            return won;
        }
    }

    /**
     * Clone card method.
     * @override
     */
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}