$(function(){
  $("#logo").click(function(){
    document.location = "/control/list.do"
  })

  $("#menuBtn01").click(function(){
    document.location = "/control/list.do"
  })
  $("#menuBtn02").click(function(){
    document.location = "/control/flist.do"
  })
  
  $("#menuBtn04").click(function(){
    document.location = "/control/config.do"
  })
  $("#menuBtn05").click(function(){
    document.location = "/control/userlist.do"
  })

  $("#join").click(function(){
    document.location = "/control/join.do"
  })
  $("#login").click(function(){
    document.location = "/control/login.do"
  })
  $("#logout").click(function(){
    if(confirm("로그아웃 하시겠습니까?"))	document.location = "/control/logout.do";
  })

	$("#profileConfig").click(function(){
		document.location = "/control/config.do";
	})

})