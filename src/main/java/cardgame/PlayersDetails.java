package cardgame;

import java.util.ArrayList;
import java.util.List;

public class PlayersDetails implements PlayerStrategy {
	private final static int NUMBER_OF_PLAYERS = 4;
    private int setPlayerId;
    private List<Integer> setOpponentIds;
    private List<Card> myCards = new ArrayList<>();
    private List<PlayerTurn> opponentMoves;
    private Card cardToPlay;
    private int totalScore = 0;
    /**
     * Gives the player their assigned id, as well as a list of the opponents' assigned ids.
     *
     * @param playerId The id for this player
     * @param opponentIds A list of ids for this player's opponents
     */
    public void init(int playerId, List<Integer> opponentIds) {
        setPlayerId = playerId;
        setOpponentIds = opponentIds;
    }
    /**
     * Called at the very beginning of the game to deal the player their initial cards.
     *
     * @param cards The initial list of cards dealt to this player
     */

    public void receiveInitialCards(List<Card> cards) {
        myCards.addAll(cards);
    }
    /**
     * Called to check whether the player wants to draw this turn. Gives this player the top card of
     * the discard pile at the beginning of their turn, as well as an optional suit for the pile in
     * case a "8" was played, and the suit was changed.
     *
     * By having this return true, the game engine will then call receiveCard() for this player.
     * Otherwise, playCard() will be called.
     *
     * @param topPileCard The card currently at the top of the pile
     * @param pileSuit The suit that the pile was changed to as the result of an "8" being played.
     * Will be null if no "8" was played.
     * @return whether or not the player wants to draw
     */

    public boolean shouldDrawCard(Card topPileCard, Card.Suit pileSuit) {
        for (int i = 0; i < myCards.size(); i++ ) {
            if (topPileCard.getRank().equals(myCards.get(i).getRank()) ||
                    pileSuit.equals(myCards.get(i).getSuit()) ||
                    myCards.get(i).getRank() == (Card.Rank.EIGHT)) {
                return false;
            }
        }
        return true;
    }
    /**
     * Called when this player has chosen to draw a card from the deck.
     *
     * @param drawnCard The card that this player has drawn
     */
    public void receiveCard(Card drawnCard) {
        myCards.add(drawnCard);
    }
    /**
     * Called when this player is ready to play a card (will not be called if this player drew on
     * their turn).
     *
     * This will end this player's turn.
     *
     * @return The card this player wishes to put on top of the pile
     */

    public Card playCard() {
        return myCards.get(0);
    }
    /**
     * Called if this player decided to play a "8" card. This player should then return the
     * Card.Suit enum that it wishes to set for the discard pile.
     */
    public Card.Suit declareSuit() {
        return getGreatestSuit();
    }
    /**
     * Called at the very beginning of this player's turn to give it context of what its opponents
     * chose to do on each of their turns.
     *
     * @param opponentActions A list of what the opponents did on each of their turns
     */

    public void processOpponentActions(List<PlayerTurn> opponentActions) {
        opponentMoves = opponentActions;
    }
    /**
     * Called when the game is being reset for the next round.
     */
    public void reset() {
        myCards.clear();
    }
    /**
     * Returns the player id of the given player.
     * @return the player id
     */

    public int getPlayerId() {
        return setPlayerId;
    }
    /**
     * Determines the card that should be played.
     * @return the card to play
     */
    public void getCardToPlay(Card topPileCard) {
        for (int i = 0; i < myCards.size(); i ++) {
            if (myCards.get(i).getSuit().equals(topPileCard.getSuit())) {
                cardToPlay = myCards.get(i);
            } else if (myCards.get(i).getRank().equals(topPileCard.getRank())) {
                cardToPlay = myCards.get(i);
            } else if (myCards.get(i).getRank().equals(Card.Rank.EIGHT)) {
                cardToPlay = myCards.get(i);
            }
        }
        myCards.remove(cardToPlay);
        myCards.add(0, cardToPlay);
    }
    /**
     * Counts the number of cards of each type of suit and finds the greatest suit.
     * @return the suit with the greatest amount of cards in hand
     */
    public Card.Suit getGreatestSuit() {
        int countDiamonds = 0;
        int countHearts = 0;
        int countSpades = 0;
        int countClubs = 0;

        for (int i = 0; i < myCards.size(); i++) {
            if (myCards.get(i).getSuit().equals(Card.Suit.DIAMONDS)) {
                countDiamonds++;
            }
            if (myCards.get(i).getSuit().equals(Card.Suit.HEARTS)) {
                countHearts++;
            }
            if (myCards.get(i).getSuit().equals(Card.Suit.SPADES)) {
                countSpades++;
            }
            if (myCards.get(i).getSuit().equals(Card.Suit.CLUBS)) {
                countClubs++;
            }
        }

        if ((countDiamonds >= countHearts) && (countDiamonds >= countSpades) && (countDiamonds >= countClubs)) {
            return Card.Suit.DIAMONDS;
        } else if ((countHearts >= countSpades) && (countHearts >= countClubs)) {
            return Card.Suit.HEARTS;
        } else if ((countSpades >= countClubs)) {
            return Card.Suit.SPADES;
        } else {
            return Card.Suit.CLUBS;
        }
    }
    /**
     * Get an array of players.
     * @return the players
     */
    public static PlayersDetails[] getPlayers() {
        PlayersDetails[] players = new PlayersDetails[NUMBER_OF_PLAYERS];
        for (int i = 0; i < players.length; i++) {
            players[i] = new PlayersDetails();
            players[i].init(i + 1, null);
        }
        return players;
    }
    /**
     * Return the hand of the player.
     * @return - the cards in the players hand
     */
    public List<Card> getMyCards() {
        return myCards;
    }
    /**
     * Add score of match to players total score.
     */
    public void addScore(int score) {
        totalScore += score;
    }
    /**
     * Get the player's current total score.
     * @return - the player's total score
     */
    public int getTotalScore() {
        return totalScore;
    }

    @Override
    public String toString() {
        return String.valueOf(getPlayerId());
    }
}
