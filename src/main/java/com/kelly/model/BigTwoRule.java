package com.kelly.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

import com.kelly.base.Rule;
import com.kelly.util.Constants;

public class BigTwoRule implements Rule {
	final static Logger logger = Logger.getLogger(BigTwoRule.class);
	public static String typeName = "Big Two";			// 規則名稱
	private Status currentStatus = null;				// 目前牌面上的牌型
	private List<PokerCard> cardsOnBoard;				// 目前牌面上的牌
	private List<Integer> passedPlayerList;				// 已經PASS的玩家列表
	private int startPassPlayer;						// 第一個PASS的玩家ID

	// 大老二的所有牌型
	enum Status {
		Single, Pair, ThreeOfAKind, Straight, Flush, FullHouse, FourOfAKind, StraightFlush
	}

	/**
	 * 初始化設定，依規則將牌堆中的牌發給玩家
	 *
	 * @param cardPile 牌堆中的牌
	 * @param players 玩家清單
	 */
	@Override
	public void initialDealingCards(List<PokerCard> cardPile, List<Player> players) {
		// 先計算是否會有剩餘的牌，並移除這些牌
		int removeCount = Constants.MAX_CARD_NUMBER * Constants.TYPE_COUNT % players.size();
		for (int i = 0; i < removeCount; i++) {
			cardPile.remove(removeCount - 1 - i);
		}

		// 將牌堆中的牌發給玩家
		for (int i = 0; i < cardPile.size(); i++) {
			int index = i % players.size();
			players.get(index).addNewCard(cardPile.get(i));
		}
		passedPlayerList = new ArrayList<Integer>();
		cardPile.clear();
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
		// 檢查玩家是否PASS，且是否已輪完一輪
		if (processPassRound(totalPlayerCount, currentPlayerIndex, playerCards))
			return true;

		boolean isValid = false;
		// 確認玩家的所出牌型
		Status s = validatePlayerCards(playerCards);
		if (s == null) {
			// 玩家出的是無效牌型
			isValid = false;
		}
		else {
			// 玩家出的是有效牌型
			
			// 比較玩家與桌面上的牌型
			int statusCompare = compareStatus(s, currentStatus);
			if (statusCompare < 0) {
				// 若牌型無法比較或是較小牌型，則無效
				isValid = false;
			}
			else if (statusCompare > 0) {
				// 若牌型較大則為有效牌
				isValid = true;
			} else if (statusCompare == 0) {
				// 若牌型相同，則繼續比較數字和花色
				if (playerCards.size() >= 1 || playerCards.size() <= 3) {
					// 若是單張、一對、三條，比較數字再比較花色
					sortByType(playerCards);
					sortByType(cardsOnBoard);
					if (compareNumber(playerCards.get(0).getNumber(), cardsOnBoard.get(0).getNumber()) > 0)
						isValid = true;
					else if (playerCards.get(0).getNumber() == cardsOnBoard.get(0).getNumber()
							&& playerCards.get(0).getType() < cardsOnBoard.get(0).getType())
						isValid = true;
					else
						isValid = false;
				} else if (playerCards.size() == 5) {
					// 若是五張牌
					if (s == Status.Straight) {
						if (compareNumber(playerCards.get(4).getNumber(), cardsOnBoard.get(4).getNumber()) > 0)
							isValid = true;
						else if (compareNumber(playerCards.get(4).getNumber(), cardsOnBoard.get(4).getNumber()) == 0
								&& compareNumber(playerCards.get(3).getNumber(), cardsOnBoard.get(3).getNumber()) == 0)
							isValid = true;
						else
							isValid = false;
					}
					// To do

					isValid = true;
				}
			}
		}
		// 若是有效牌，更新目前桌上的牌以及牌型
		if (isValid) {
			setCurrentStatus(s);
			setCardsOnBoard(playerCards);
			return true;
		}
		return false;
	}
	/**
	 * 確認玩家所出的牌型
	 *
	 * @param playerCards 玩家所出的牌
	 * @return 牌型
	 */
	private Status validatePlayerCards(List<PokerCard> playerCards) {
		if (playerCards.size() > 5) {
			// 超過五張為無效
			return null;
		} else if (playerCards.size() == 1) {
			// 單張
			return Status.Single;
		} else if (playerCards.size() == 2) { 
			// 一對
			PokerCard firstCard = playerCards.get(0);
			if (firstCard.getNumber() == playerCards.get(1).getNumber())
				return Status.Pair;
			else
				return null;
		} else if (playerCards.size() == 3) { 
			// 三條
			PokerCard firstCard = playerCards.get(0);
			if (firstCard.getNumber() == playerCards.get(1).getNumber()
					&& firstCard.getNumber() == playerCards.get(2).getNumber())
				return Status.ThreeOfAKind;
			else
				return null;
		} else if (playerCards.size() == 4) {
			// 四張牌無效
			return null;
		} else if (playerCards.size() == 5) {
			// 順子或同花或同花順或葫蘆或四條
			List<PokerCard> tempList = playerCards;

			sortByNumber(tempList);

			boolean isStraight = isStraight(tempList);
			boolean isFlush = isFulsh(tempList);
			boolean isFullHouse = isFullHouse(tempList);
			boolean isFourOfAKind = isFourOfAKind(tempList);
			boolean isStraightFlush = isStraight && isFlush ? true : false;

			if (isStraightFlush)
				return Status.StraightFlush;
			else if (isFourOfAKind)
				return Status.FourOfAKind;
			else if (isFullHouse)
				return Status.FullHouse;
			else if (isFlush)
				return Status.Flush;
			else if (isStraight)
				return Status.Straight;
			else
				return null;
		}
		return null;

	}
	/**
	 * 檢查是否為順子：五張數字相連
	 * 
	 * @param tempList 欲檢查的牌
	 */
	private boolean isStraight(List<PokerCard> tempList) { // To do: KA2不成順
		boolean isStraight = true;
		for (int i = 0; i < tempList.size(); i++) {
			if (i != tempList.size() - 1 && tempList.get(i).getLoopNumber() != tempList.get(i + 1).getNumber())
				isStraight = false;
		}
		return isStraight;
	}
	/**
	 * 檢查是否為同花：五張花色都一樣
	 * 
	 * @param tempList 欲檢查的牌
	 */
	private boolean isFulsh(List<PokerCard> tempList) {
		boolean isFlush = true;
		for (int i = 0; i < tempList.size(); i++) {
			if (i != tempList.size() - 1 && tempList.get(i).getType() != tempList.get(i + 1).getType())
				isFlush = false;
		}
		return isFlush;
	}
	/**
	 * 檢查是否為葫蘆：三張數字相同+兩張數字相同
	 * 
	 * @param tempList 欲檢查的牌
	 */
	private boolean isFullHouse(List<PokerCard> tempList) {
		boolean isFullHouse = false;
		PokerCard firstCard = tempList.get(0);
		PokerCard thirdCard = tempList.get(2);
		if (firstCard.getNumber() == tempList.get(1).getNumber()
				&& firstCard.getNumber() == tempList.get(2).getNumber()) {
			if (tempList.get(3).getNumber() == tempList.get(4).getNumber())
				isFullHouse = true;
		} else if (thirdCard.getNumber() == tempList.get(3).getNumber()
				&& thirdCard.getNumber() == tempList.get(4).getNumber()) {
			if (tempList.get(0).getNumber() == tempList.get(1).getNumber())
				isFullHouse = true;
		}
		return isFullHouse;
	}
	/**
	 * 檢查是否為四條：四張數字相同，一張不同
	 * 
	 * @param tempList 欲檢查的牌
	 */
	private boolean isFourOfAKind(List<PokerCard> tempList) {
		boolean isFourOfAKind = false;
		PokerCard firstCard = tempList.get(0);
		PokerCard secondCard = tempList.get(1);
		if (firstCard.getNumber() == tempList.get(1).getNumber() && firstCard.getNumber() == tempList.get(2).getNumber()
				&& firstCard.getNumber() == tempList.get(3).getNumber()) {
			isFourOfAKind = true;
		} else if (secondCard.getNumber() == tempList.get(2).getNumber()
				&& secondCard.getNumber() == tempList.get(3).getNumber()
				&& secondCard.getNumber() == tempList.get(4).getNumber()) {
			isFourOfAKind = true;
		}
		return isFourOfAKind;
	}
	/**
	 * 依數字排序，從最小 3 排到最大 2
	 * 
	 * @param tempList 欲排序的牌
	 */
	private void sortByNumber(List<PokerCard> tempList) {
		Collections.sort(tempList, new Comparator<PokerCard>() {
			public int compare(PokerCard o1, PokerCard o2) {
				return compareNumber(o1.getNumber(), o2.getNumber());
			}
		});
	}
	/**
	 * 依花色排序
	 * 
	 * @param tempList 欲排序的牌
	 */
	private void sortByType(List<PokerCard> tempList) {
		Collections.sort(tempList, new Comparator<PokerCard>() {
			public int compare(PokerCard o1, PokerCard o2) {
				return o1.getType() - o2.getType();
			}
		});
	}
	/**
	 * 比較牌型a是否比牌型b大，同花順及四條可壓任意牌型，但若是其他牌型需a、b相同才可互比，
	 * 
	 * @param a 牌型a
	 * @param b 牌型b
	 * @return 兩種牌型的差值
	 */
	private int compareStatus(Status a, Status b) {
		if (a != null && b != null) {
			if(a.ordinal() == b.ordinal()) {
				return 0;
			}
			else if(a.ordinal() == Status.StraightFlush.ordinal() || a.ordinal() == Status.FourOfAKind.ordinal()) {
				return a.ordinal() - b.ordinal();
			}
			else 
				return -1;
		}
		else if(b == null)
			return 1;
		else 
			return -1;
	}
	/**
	 * 比較a是否大於b，2 > 1 > 13 > 12 > ... > 3
	 * 
	 * @param a 數字a
	 * @param b 數字b
	 * @return 兩個數字的差值
	 */
	private int compareNumber(int a, int b) {
		if (a == 2)
			a = 15;
		if (a == 1)
			a = 14;
		if (b == 2)
			b = 15;
		if (b == 1)
			b = 14;
		return a - b;
	}
	/**
	 * 得到目前桌面上的牌
	 */
	public List<PokerCard> getCardsOnBoard() {
		return cardsOnBoard;
	}
	/**
	 * 設定目前桌面上的牌
	 */
	public void setCardsOnBoard(List<PokerCard> cardsOnBoard) {
		this.cardsOnBoard = cardsOnBoard;
	}
	/**
	 * 將PASS玩家的ID加入PASS玩家清單
	 *
	 * @param passPlayer 玩家ID
	 */
	private void addPassedPlayerList(int passPlayer) {
		if (!passedPlayerList.contains(Integer.valueOf(passPlayer)))
			passedPlayerList.add(Integer.valueOf(passPlayer));
	}
	/**
	 * 檢查是否已經PASS一輪
	 *
	 * @param totalPlayerCount 總玩家數
	 * @return 是否已PASS一輪
	 */
	private boolean checkPassRound(int totalPlayerCount) {
		if (passedPlayerList.size() == totalPlayerCount - 1)
			return true;
		else
			return false;
	}
	/**
	 * 檢查玩家是否PASS，且是否已輪完一輪
	 *
	 * @param totalPlayerCount 總玩家數
	 * @param currentPlayerIndex 目前出牌的玩家ID
	 * @param playerCards 玩家所出的牌
	 * @return 玩家是否PASS
	 */
	private boolean processPassRound(int totalPlayerCount, int currentPlayerIndex, List<PokerCard> playerCards) {
		if (playerCards.size() == 0) {
			// 如果之前沒有玩家PASS，設定此玩家為第一位PASS玩家
			if (passedPlayerList.size() == 0) {
				startPassPlayer = currentPlayerIndex;
			}
			// 加入至PASS玩家清單
			addPassedPlayerList(currentPlayerIndex);
			// 檢查是否已經PASS一輪，是的話清空桌上的牌，重新設定PASS資料
			if (checkPassRound(totalPlayerCount)) {
				cardsOnBoard.clear();
				startPassPlayer = 0;
				passedPlayerList.clear();
				setCurrentStatus(null);
			}
			return true;
		} else
			return false;
	}

	public int getStartPassPlayer() {
		return startPassPlayer;
	}

	public void setStartPassPlayer(int startPassPlayer) {
		this.startPassPlayer = startPassPlayer;
	}
	
	public Status getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(Status currentStatus) {
		this.currentStatus = currentStatus;
	}
}
