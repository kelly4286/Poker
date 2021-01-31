<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="init.jsp" %>
<html>
<head>
<meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
<title>Poker Game</title>
</head>

<body>
	<h1>Poker Game</h1>
	
	<form action="./gameStart" method="post" class="form-inline" onsubmit="return validateForm()" >
	<div class="form-group">
		<label class="control-label">玩家數量：</label>
   		<input type="number" class="form-control" name="playerCount">
		<br/>
		
		<label class="control-label">選擇玩法：</label>
		<div class="radio">
		<label><input type="radio" class="form-control" name="ruleName" value="Big Two" checked>大老二</label>  
		<label><input type="radio" class="form-control" name="ruleName" value="Pick Red">撿紅點</label>
		</div>
		<br/>
		<input id="startGame" type="submit" class="btn btn-primary" name="post" value="遊戲開始" />
	</div>
	</form>
	
	<h4 class="errorString">${errorString}</h4>
	
</body>
</html>