package com.kelly.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Player {
	private String userId;				// 玩家的ID
	private List<PokerCard> cardList;	// 玩家的手牌
	private int score;					// 玩家的分數

	public Player() {
		cardList = new ArrayList<PokerCard>();
		this.score = 0;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<PokerCard> getCardList() {
		return cardList;
	}

	public void setCardList(List<PokerCard> cardList) {
		this.cardList = cardList;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void addNewCard(PokerCard card) {
		cardList.add(card);
	}

	/**
	 * 排序玩家的手牌，先依花色再依數字排序
	 */
	public void rearrangeCards() {
		Collections.sort(cardList, new Comparator<PokerCard>() {
			public int compare(PokerCard o1, PokerCard o2) {
				if (o1.getType() == o2.getType())
					return o1.getNumber() - o2.getNumber();
				else
					return o1.getType() - o2.getType();
			}
		});
	}

	/**
	 * 尋找玩家是否有某張牌
	 * 
	 * @param type 花色
	 * @param number 數字
	 */
	public PokerCard findCard(String type, String number) {
		for (PokerCard c : cardList) {
			if (c.getTypeText().equals(type) && c.getNumberText().equals(number))
				return c;
		}
		return null;
	}
}
