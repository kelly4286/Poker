package com.kelly.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.kelly.model.GameSet;
import com.kelly.model.Player;
import com.kelly.model.PokerCard;

public class GameStart extends HttpServlet {
	private static final long serialVersionUID = 4179545353414298791L;
	final static Logger logger = Logger.getLogger(GameStart.class);
	GameSet gameSet = null;
	List<Player> playerList = null;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String command = request.getParameter("post");
		StringBuilder errorMsg = new StringBuilder();

		if (command.equalsIgnoreCase("遊戲開始")) {
			gameStart(request, errorMsg);

			request.setAttribute("playerList", playerList);
			request.setAttribute("currentPlayer", gameSet.getCurrentPlayerIndex());
		} else if (command.equalsIgnoreCase("出牌") || command.equalsIgnoreCase("PASS")) {
			List<PokerCard> cardsOnBoard = playerDeal(request, errorMsg);

			request.setAttribute("playerList", playerList);
			request.setAttribute("currentPlayer", gameSet.getCurrentPlayerIndex());
			request.setAttribute("cardsOnBoard", cardsOnBoard);
			request.setAttribute("errorMsg", errorMsg);
		} 
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/game.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * 初始化遊戲資料
	 *
	 * @param request Http Request
	 * @param errorMsg 若出現錯誤，將錯誤訊息傳回前端
	 */
	private void gameStart(HttpServletRequest request, StringBuilder errorMsg) throws ServletException, IOException {
		int playerCount = Integer.valueOf(request.getParameter("playerCount"));
		String ruleName = request.getParameter("ruleName");

		gameSet = new GameSet(playerCount, ruleName);
		// 洗牌
		gameSet.suffleCardPile();
		// 發牌
		gameSet.dealCardPile();
		playerList = gameSet.getTotalPlayerList();
		// 排序玩家手中的牌
		for (Player p : playerList)
			p.rearrangeCards();
	}

	/**
	 * 處理一位玩家出牌
	 *
	 * @param request Http Request
	 * @param errorMsg 若出現錯誤，將錯誤訊息傳回前端
	 * @return 玩家出牌後，桌面上的牌
	 */
	private List<PokerCard> playerDeal(HttpServletRequest request, StringBuilder errorMsg) {
		String[] cardStrings = request.getParameterValues("checkedCards");
		List<PokerCard> playerCards = new ArrayList<PokerCard>();
		if (cardStrings != null) {
			// 整理前端傳來的撲克牌資料
			for (String s : cardStrings) {
				String[] card = s.split(",");
				int currentPlayer = gameSet.getCurrentPlayerIndex();
				playerCards.add(gameSet.getTotalPlayerList().get(currentPlayer).findCard(card[0], card[1]));
			}
		}
		// 確認出牌是否有效
		boolean deal = gameSet.playerDeal(playerCards);
		if (!deal) {
			errorMsg.append("出牌無效");
		}

		playerList = gameSet.getTotalPlayerList();
		List<PokerCard> cardsOnBoard = gameSet.getGameRule().getCardsOnBoard();
		return cardsOnBoard;
	}
}
