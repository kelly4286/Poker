package com.kelly.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.kelly.base.Rule;
import com.kelly.util.Constants;

public class GameSet {
	private List<PokerCard> cardPile; 		// 桌上尚未使用的牌堆
	private List<Player> totalPlayerList;	// 所有玩家
	private int index;						// 第幾局
	private Rule gameRule; 					// 此賽局所使用的規則
	private int currentPlayerIndex; 		// 目前輪到哪位玩家

	/**
	 * 初始化一場新賽局
	 *
	 * @param numOfPlayers 總玩家數
	 * @param ruleTypeName 賽局所使用的規則
	 */
	public GameSet(int numOfPlayers, String ruleTypeName) {
		cardPile = new ArrayList<PokerCard>();
		totalPlayerList = new ArrayList<Player>();
		currentPlayerIndex = 0;

		// 建立一組撲克牌，共52張，4種花色，13種數字，放在牌堆中
		for (int i = 1; i <= Constants.TYPE_COUNT; i++) {
			for (int j = 1; j <= Constants.MAX_CARD_NUMBER; j++) {
				PokerCard card = new PokerCard();
				card.setType(i);
				card.setNumber(j);
				cardPile.add(card);
			}
		}
		// 初始化玩家資料
		for (int i = 0; i < numOfPlayers; i++) {
			Player player = new Player();
			player.setUserId(String.valueOf(i));
			totalPlayerList.add(player);
		}
		// 初始化規則資料
		if (ruleTypeName.equalsIgnoreCase(BigTwoRule.typeName)) {
			gameRule = new BigTwoRule();
		} else if (ruleTypeName.equalsIgnoreCase(PickRedRule.typeName)) {
			gameRule = new PickRedRule();
		}
	}

	/**
	 * 發牌
	 */
	public void dealCardPile() {
		gameRule.initialDealingCards(cardPile, totalPlayerList);
	}

	/**
	 * 處理一次玩家出牌
	 *
	 * @param playerDealCard 玩家所出的牌
	 */
	public boolean playerDeal(List<PokerCard> playerDealCard) {
		// 檢查是否為有效出牌
		boolean valid = gameRule.validatePlayerDeal(totalPlayerList.size(), currentPlayerIndex, playerDealCard);
		if (valid) {
			// 若有效，移除玩家的牌
			Player currentPlayer = totalPlayerList.get(currentPlayerIndex);
			for (PokerCard c : playerDealCard) {
				PokerCard tmp = currentPlayer.findCard(c.getTypeText(), c.getNumberText());
				currentPlayer.getCardList().remove(tmp);
			}
			nextPlayer();
		}
		return valid;
	}

	/**
	 * 洗牌
	 */
	public void suffleCardPile() {
		Collections.shuffle(cardPile);
	}

	/**
	 * 取得牌堆中的牌
	 */
	public List<PokerCard> getCardPile() {
		return cardPile;
	}

	/**
	 * 設定牌堆中的牌
	 */
	public void setCardPile(List<PokerCard> cardPile) {
		this.cardPile = cardPile;
	}

	/**
	 * 取得所有玩家
	 */
	public List<Player> getTotalPlayerList() {
		return totalPlayerList;
	}

	/**
	 * 設定所有玩家
	 */
	public void setTotalPlayerList(List<Player> totalPlayerList) {
		this.totalPlayerList = totalPlayerList;
	}

	/**
	 * 換下一位玩家
	 */
	public int nextPlayer() {
		currentPlayerIndex += 1;
		if (currentPlayerIndex >= totalPlayerList.size())
			currentPlayerIndex = 0;
		return currentPlayerIndex;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Rule getGameRule() {
		return gameRule;
	}

	public void setGameRule(Rule gameRule) {
		this.gameRule = gameRule;
	}

	public int getCurrentPlayerIndex() {
		return currentPlayerIndex;
	}

	public void setCurrentPlayerIndex(int currentPlayerIndex) {
		this.currentPlayerIndex = currentPlayerIndex;
	}
}
