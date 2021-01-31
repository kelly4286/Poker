package com.kelly.model;

import java.util.List;

import com.kelly.base.Rule;

public class PickRedRule implements Rule{
	public static String typeName = "Pick Red";			// 規則名稱
	private static int totalDeliverCard = 24;			// 剛開始總共發給玩家24張牌

	/**
	 * 初始化設定
	 *
	 * @param cardPile 牌堆中的牌，依規則將牌堆中的牌發給玩家
	 * @param players 玩家清單
	 */
	@Override
	public void initialDealingCards(List<PokerCard> cardPile, List<Player> players) {
		// 每位玩家的手牌數，僅支援 2、3、4位玩家
		int cardPerPeople = 6;
		if(players.size() == 2 || players.size() == 3 || players.size() == 4) {
			cardPerPeople = totalDeliverCard / players.size();
		}
		
		// 將牌堆中的牌發給玩家
		for(int i = 0; i < cardPerPeople; i++) {
			for(int j = 0; j < players.size(); j++) {
				int delieverIndex = totalDeliverCard - 1 - i * players.size() - j;
				players.get(j).addNewCard(cardPile.get(delieverIndex));
				cardPile.remove(delieverIndex);
			}
		}
	}

	/**
	 * 確認是否為有效出牌
	 *
	 * @param totalPlayerCount 總玩家數
	 * @param currentPlayerIndex 目前出牌的玩家ID
	 * @param playerCards 玩家所出的牌
	 */
	@Override
	public boolean validatePlayerDeal(int totalPlayerCount, int currentPlayerIndex, List<PokerCard> playerCards) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 得到目前桌面上的牌
	 */
	@Override
	public List<PokerCard> getCardsOnBoard() {
		// TODO Auto-generated method stub
		return null;
	}

}
