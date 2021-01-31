package com.kelly.base;

import java.util.List;

import com.kelly.model.Player;
import com.kelly.model.PokerCard;

public interface Rule {
	/**
	 * 初始化設定，依規則將牌堆中的牌發給玩家
	 *
	 * @param cardPile 牌堆中的牌
	 * @param players 玩家清單
	 */
	void initialDealingCards(List<PokerCard> cardPile, List<Player> players);
	/**
	 * 確認是否為有效出牌
	 *
	 * @param totalPlayerCount 總玩家數
	 * @param currentPlayerIndex 目前出牌的玩家ID
	 * @param playerCards 玩家所出的牌
	 */
	boolean validatePlayerDeal(int totalPlayerCount, int currentPlayerIndex, List<PokerCard> playerCards);
	/**
	 * 得到目前桌面上的牌
	 */
	List<PokerCard> getCardsOnBoard();
}
