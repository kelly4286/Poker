<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="init.jsp" %>
<html>
<head>
<meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
<title>Poker Game</title>
</head>

<body>
	<h1>Poker Game</h1>
	<a class="toHome" href="./index.jsp">回到首頁</a>
	
	<h3>桌面上的牌</h3>
	<c:forEach var="card" items="${cardsOnBoard}">
		<label>
			<span class="card ${card.getTypeColor()}">
			${card.getTypeText()} ${card.getNumberText()}
			</span>
	    </label>	
	</c:forEach>
	<hr>
	<h3>玩家的手牌</h3>	
	<ul>
	<c:forEach var="player" items="${playerList}">
		<li><h4>Player-${player.getUserId()} has ${player.getCardList().size()} cards</h4></li>
		<c:if test="${player.getUserId()==currentPlayer}">
			<form class="form-inline" action="./gameStart" method="post">
			<c:forEach var="card" items="${player.getCardList()}">
				<div class="checkbox">
					<label>
				    	<input type="checkbox" name="checkedCards" value="${card.getTypeText().concat(',').concat(card.getNumberText())}">
						<span class="card ${card.getTypeColor()}">
						${card.getTypeText()} ${card.getNumberText()}
						</span>
				    </label>
				</div>			
			</c:forEach>
			<input type="submit" class="btn btn-primary" name="post" value="出牌" />
			<input type="submit" class="btn btn-default" name="post" value="PASS" />
			</form>
		</c:if>
		<c:if test="${player.getUserId()!=currentPlayer}">
			<c:forEach var="card" items="${player.getCardList()}">
				<span class="card ${card.getTypeColor()}">
				${card.getTypeText()} ${card.getNumberText()}
				</span>
			</c:forEach>
		</c:if>
	</c:forEach>
	</ul>
	
	<h4 class="errorString">${errorMsg}</h4>

</body>
</html>