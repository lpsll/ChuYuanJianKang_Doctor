window.onload = function() {
	//	DrawData()
}


function DrawData() {

	$.ajax({
		url: "http://192.168.0.188/ifactory/getMachineTotal.action?callback?",
		dataType: "jsonp",
		success: function(result) {
			var date = [];
			var data = {};
			var totalstitch = [];
			var maxCount = 15;
			result.forEach(function(e) {
				if (maxCount >= 0) {
					date.push(e.date);
					totalstitch.push(e.machine);
				}
				maxCount--;
			});
			data.name = "开机";
			data.data = totalstitch;
			$('#DayStarts').highcharts({
				chart: {
					type: 'line'
				},
				title: {
					text: '每日开机'
				},
				xAxis: {
					categories: date,
				},
				yAxis: {
					title: {
						text: ''
					}
				},
				plotOptions: {
					line: {
						dataLabels: {
							enabled: true
						},
						enableMouseTracking: false
					},
					series: {
						events: {
							legendItemClick: function(e) {
								return false; // 直接 return false 即可禁用图例点击事件
							}
						}
					}
				},
				series: [data]
			});


		},
		error: function(e) {
			alert("wangluocuowu")
		}
	});

}

function saveData() {
	
	if(mvalue.length == 0){
		return false;
	}else if(nvalue.length == 0){
		return false;
	}else {alert("get")
		$.ajax({
			type: "post",
			url: "",
			data: {

			},
			seccess: function(m) {

			},
			error: function(e) {

			}
		});
	}
}
var flag = 0; //0关闭；1开启；
function showSelectList() {
	var selectIcon = document.getElementById("selectIcon");
	selectIcon.removeAttribute('class');
	if (flag == 0) {
		selectIcon.setAttribute('class', "HistBtnIcon mui-icon mui-icon-arrowup");
		openList();
		flag = 1;
	} else {
		selectIcon.setAttribute('class', "HistBtnIcon mui-icon mui-icon-arrowdown");
		flag = 0;
	}
}
var mflag = 0;//0关闭；1开启；
function showSelectList2() {
	var selectIcon = document.getElementById("selectIcon2");
	selectIcon.removeAttribute('class');
	if (mflag == 0) {
		selectIcon.setAttribute('class', "HistBtnIcon mui-icon mui-icon-arrowup");
		openList2();
		mflag = 1;
	} else {
		selectIcon.setAttribute('class', "HistBtnIcon mui-icon mui-icon-arrowdown");
		mflag = 0;
	}
}

var mvalue = "";
function getSelectValue(thisa) {
	var radionum = document.getElementsByName("gender");

	for (var i = 0; i < radionum.length; i++) {

		if (radionum[i].checked) {

			mvalue = radionum[i].value;
			var selectText = radionum[i].getAttribute("mText");
			
			var TitleText = document.getElementById("TitleText");
			
			TitleText.innerHTML = selectText;

		}
	}
	closeList();
}

var nvalue = "";
function getSelectValue2(thisa) {
	var radionum = document.getElementsByName("gender2");

	for (var i = 0; i < radionum.length; i++) {

		if (radionum[i].checked) {

			nvalue = radionum[i].value;
			var selectText = radionum[i].getAttribute("mText");
			
			var TitleText = document.getElementById("TitleText2");
			
			TitleText.innerHTML = selectText;

		}
	}
	closeList2();
}

function openList() {
	var selectMenu = document.getElementById("selectMenu");
	selectMenu.style.display = "block";
}

function closeList() {
	var selectMenu = document.getElementById("selectMenu");
	selectMenu.style.display = "none";
}

function openList2() {
	var selectMenu = document.getElementById("selectMenu2");
	selectMenu.style.display = "block";
}

function closeList2() {
	var selectMenu = document.getElementById("selectMenu2");
	selectMenu.style.display = "none";
}


function checkHistory() {
	window.location.href = "History.html";
}