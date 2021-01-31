<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css" integrity="sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r" crossorigin="anonymous">
<script src="https://code.jquery.com/jquery-1.12.4.min.js" integrity="sha256-ZosEbRLbNQzLpnKIkEdrPv7lOy9C27hHQ+Xp8a4MxAQ=" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js" integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS" crossorigin="anonymous"></script>
<style>
.errorString {
	color: red;
}
.red {
	color: red;
}
.black {
	color: black;
}
.card {
	border: 1px solid;
	padding: 1px;
   	margin: 3px;
}
.toHome {
    float: right;
    font-size: 16px;
}
</style>

<script>
function validateForm() {
	var rule = $("input:checked").val();
	var count = $("input[name='playerCount']").val();
	console.log(rule + ", " + count);
	if (count == "" || count < 2) {
    	$(".errorString").html("請輸入大於1的玩家數量");
        return false;
    }
    if (rule == "Pick Red" && (count < 2 || count > 4)) {
    	$(".errorString").html("撿紅點僅支援2、3、4位玩家");
        return false;
    }
    else {
    	return true;
    }
}
</script>