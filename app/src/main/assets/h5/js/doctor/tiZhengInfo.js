
window.onload = function(){
	SetValue();
}

var tiWenBtn = "";
var maiBoBtn = "";
var huXiBtn = "";
var xueYaBtn = "";
var xueTangBtn = "";
var tiWenDiv = "";
var maiBoDiv = "";
var huXiDiv = "";
var xueYaDiv = "";
var xueTangDiv = "";
function SetValue(){
	tiWenBtn = document.getElementById("tiWenBtn");
	maiBoBtn = document.getElementById("maiBoBtn");
	huXiBtn = document.getElementById("huXiBtn");
	xueYaBtn = document.getElementById("xueYaBtn");
	xueTangBtn = document.getElementById("xueTangBtn");
	
	tiWenDiv = document.getElementById("tiWenDiv");
	maiBoDiv = document.getElementById("maiBoDiv");
	huXiDiv = document.getElementById("huXiDiv");
	xueYaDiv = document.getElementById("xueYaDiv");
	xueTangDiv = document.getElementById("xueTangDiv");

}


function changeBox(a){
	
	removeAllClass();
	if(a == 1){
		tiWenBtn.setAttribute('class','selectDiv act');
		tiWenDiv.style.display = 'block';
	}else if(a == 2){
		maiBoBtn.setAttribute('class','selectDiv act');
		maiBoDiv.style.display = 'block';
	}else if(a == 3){
		huXiBtn.setAttribute('class','selectDiv act');
		huXiDiv.style.display = 'block';
	}else if(a == 4){
		xueYaBtn.setAttribute('class','selectDiv act');
		xueYaDiv.style.display = 'block';
	}else if(a == 5){
		xueTangBtn.setAttribute('class','selectDiv act');
		xueTangDiv.style.display = 'block';
	}
}

function removeAllClass(){
	

	tiWenBtn.setAttribute("class",'selectDiv');
	maiBoBtn.setAttribute("class",'selectDiv');
	huXiBtn.setAttribute("class",'selectDiv');
	xueYaBtn.setAttribute("class",'selectDiv');
	xueTangBtn.setAttribute("class",'selectDiv');
	
	tiWenDiv.style.display="none";
	maiBoDiv.style.display = "none";
	huXiDiv.style.display = "none";
	xueYaDiv.style.display = "none";
	xueTangDiv.style.display = "none";
}

function getDraw(){
	
	
	$.ajax({
		type:"post",
		url:"",
		async:true
	});
	
}











