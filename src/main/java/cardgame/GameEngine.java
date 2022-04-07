package cardgame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class GameEngine {

	private static final int INITIAL_DISPATCHED_CARD = 5;
	private static final int NUMBER_OF_PLAYERS = 4;
	private static final int WIN_SCORE= 200;
	private static final int INDEX_OF_TOP_CARD_OF_DECK = 0;
	private static List<PlayersDetails> playerList;
	private static Card topPileCard;
	private static boolean gameOver = false;
	private static List<Card> deckOfCards;
	public static void main(String[] arguments) {
		//welcome message
        System.out.println("\n========================================================\n");
		System.out.println("\n..........WELCOME TO CRAZY8 CARD GAME..........\n");
		showOptions();
	}
	/**
	 * function to get the options from the user
	 */
	public static void showOptions() {
        System.out.println("\n========================================================\n");
		System.out.println("1.START GAME");
		System.out.println("2.GUIDE");
		System.out.println("3.EXIT");
		int choice;
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter Your Choice"); 
		choice = sc.nextInt();
		switch(choice) {
			case 1:
				startGame();
				break;
			case 2:
				guideForPlayGame();
				break;
			case 3:
				return;
		}
	}
	/**
	 * to get the names of the four players
	 * player list is created to store the name of the players
	 */
	public static void startGame() {
		String[] playersName = new String[NUMBER_OF_PLAYERS];
		System.out.println("PLEASE ENTER THE PLAYER NAMES TO BEGIN THE GAME...");
		for (int i = 1; i <= NUMBER_OF_PLAYERS; i++) {
			Scanner sc = new Scanner(System.in);
			System.out.println("Enter Player " + i + " Name :");  
			playersName[i-1]= sc.nextLine();
		}  
		playerList = createPlayerList();
		gameEngine(playersName);
		winnerOfGame(playersName);
	}
	/**
	 * if user chooses guide, the the instruction is deleted
	 */
	public static void guideForPlayGame() {
        System.out.println("\n========================================================\n");
		System.out.println("Start Game");
		System.out.println("Enter the players name");
		System.out.println("Which player first reach 200 points,he is the winner");
		System.out.println("points detail");
		System.out.println("1. heart (♥), spade (♠) diamond (♦), club(♣)  containes 10 points");
		System.out.println("Other cards contains the points based on the number");
		showOptions();
	}
	/**
	 * game engine 
	 * @param playersName stores the player name and displays the score of the indiviual player
	 *@param matchNumber stores the number of match played
	 */
	public static void gameEngine(String[] playersName) {
		int matchNumber = 0;
		while (!gameOver) {
			playGame();
			matchNumber++;
			System.out.println("\n" + "Player scores for Match " + matchNumber + ":");
			for (int i = 0; i < NUMBER_OF_PLAYERS; i++) {
				System.out.println(playersName[i] +" 's score is " + playerList.get(i).getTotalScore());
				if (playerList.get(i).getTotalScore() >= 200) {
					gameOver = true;
				}
			}
		}
	}
	/** 
	 * plays a match and determines the winner of the match
	 */
    public static void playGame() {
        boolean matchEnded = false;
        giveInitialCardsToPlayers();
        topPileCard = getStartingTopPileCard();
        while (!matchEnded) {
            if (deckOfCards.size() > 0) {
                for (int i = 0; i < NUMBER_OF_PLAYERS; i++) {
                		if (playerList.get(i).getMyCards().size() == 0) {
                        	addMatchScore(playerList.get(i));
                        	matchEnded = true;
                        	deckOfCards.clear();
                        	break;
                    	}
                    	if (!(playerList.get(i).shouldDrawCard(topPileCard, topPileCard.getSuit()))) {
                        	playerList.get(i).getCardToPlay(topPileCard);
                        	topPileCard = playerList.get(i).playCard();
                        	playerList.get(i).getMyCards().remove(0);
                        	if (playerList.get(i).getMyCards().size() == 0) {
                            	addMatchScore(playerList.get(i));
                            	matchEnded = true;
                            	deckOfCards.clear();
                            	break;
                        	}
                    	} else {
                        	if (deckOfCards.size() != 0) {
                        		playerList.get(i).receiveCard(deckOfCards.get(INDEX_OF_TOP_CARD_OF_DECK));
                            	deckOfCards.remove(INDEX_OF_TOP_CARD_OF_DECK);
                            	if (deckOfCards.size() == 0) {
                                	addTiedMatchScore();
                                	matchEnded = true;
                                	deckOfCards.clear();
                                	break;
                            	}
                        	}
                    	}
                }  
            } else {
                addTiedMatchScore();
            }
        }
        for (int i = 0; i < NUMBER_OF_PLAYERS; i++) {
            playerList.get(i).reset();
        }
    }
	/**
	 * creates a player list
	 * @return a list of players
	 */
	public static List<PlayersDetails> createPlayerList() {
		return new ArrayList<>(Arrays.asList(PlayersDetails.getPlayers()));
	}
	/**
	 * gives player their starting hand
	 */
	public static void giveInitialCardsToPlayers() {
		deckOfCards = Card.getDeck();
		Collections.shuffle(deckOfCards);

		for (int i = 0; i < playerList.size(); i++) {
			List<Card> temp = new ArrayList<>();
			for (int j = 0; j < INITIAL_DISPATCHED_CARD; j++) {
				temp.add(deckOfCards.get(INDEX_OF_TOP_CARD_OF_DECK));
				deckOfCards.remove(INDEX_OF_TOP_CARD_OF_DECK);
			}
			playerList.get(i).receiveInitialCards(temp);
			temp.clear();

		}
	}
	/**
	 * gets the starting top pile card
	 * @return the starting card for the game
	 */
	public static Card getStartingTopPileCard() {
		if (deckOfCards.get(INDEX_OF_TOP_CARD_OF_DECK).getRank() == Card.Rank.EIGHT) {
			deckOfCards.add(deckOfCards.get(INDEX_OF_TOP_CARD_OF_DECK));
			deckOfCards.remove(INDEX_OF_TOP_CARD_OF_DECK);
		}
		topPileCard = deckOfCards.get(INDEX_OF_TOP_CARD_OF_DECK);
		deckOfCards.remove(INDEX_OF_TOP_CARD_OF_DECK);
		return topPileCard;
	}
	public static void addTiedMatchScore() {
		for (int i = 0; i < NUMBER_OF_PLAYERS; i++) {
			for (int j = 0; j < NUMBER_OF_PLAYERS; j++) {
				for (int k = 0; k < playerList.get(j).getMyCards().size(); k++) {
					if (i != j) {
						playerList.get(i).addScore(playerList.get(j).getMyCards().get(k).getPointValue());
					}
				}
			}
		}
	}
	/**
	 * add the score for each player in the case of tie
	 */
	public static void addMatchScore(PlayersDetails winnerOfMatch) {
		for (int i = 0; i < NUMBER_OF_PLAYERS; i++) {
			if (winnerOfMatch != playerList.get(i)) {
				for (int j = 0; j < playerList.get(i).getMyCards().size(); j++) {
					winnerOfMatch.addScore(playerList.get(i).getMyCards().get(j).getPointValue());
				}
			}
		}
	}
	/**
	 * adds score to the player who won the match
	 * @return the winner of the game
	 */
	public static void winnerOfGame(String[] playersName) {

		for (int i = 0; i < NUMBER_OF_PLAYERS; i++) {
			if (playerList.get(i).getTotalScore() >=WIN_SCORE) {
		        System.out.println("\n========================================================\n");
				System.out.print("\n"+playersName[i] + ".....IS WINNER....\n");
		        System.out.println("\n========================================================\n");
				
			}
		
		}
	}
}