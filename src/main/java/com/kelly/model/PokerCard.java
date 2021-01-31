package com.kelly.model;

public class PokerCard {
	private int type;		// 花色
	private int number;		// 數字

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	/**
	 * 得到數字的代表文字
	 */
	public String getNumberText() {
		switch(this.number) {
			case 1: return "A";
			case 11: return "J";
			case 12: return "Q";
			case 13: return "K";
			default: return String.valueOf(this.number);
		}
	}
	
	/**
	 * 得到花色的代表文字
	 */
	public String getTypeText() {
		switch(this.type) {
			case 1: return "♠";
			case 2: return "♥";
			case 3: return "♣";
			case 4: return "♦";
		}
		return null;
	}
	
	/**
	 * 得到牌型的顏色
	 */
	public String getTypeColor() {
		switch(this.type) {
			case 2: 
			case 4: return "red";
			case 1: 
			case 3: return "black";
		}
		return null;
	}

	/**
	 * 得到相鄰的牌，若是13則回傳1
	 */
	public int getLoopNumber() {
		if(this.number == 13) 
			return 1;
		else 
			return this.number + 1;
	}
}
