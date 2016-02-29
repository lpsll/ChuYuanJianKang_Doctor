window.onload = function() {
	getUrl()
}
//var id = localStorage.userid;
var mflag = 0;

function getUrl() {
	var mUrl = location.search;
	var mArr = mUrl.split('?');
	var mValArr = mArr[1].split('=');
	mflag = mValArr[1];
	getValType();
}

var ginfotype = 0;
var gtype = 0;
var gnum = 0;
var gflag = 0;

function getValType() {
	gnum = 0;
	gflag = 1;
	if (mflag == "1") {
		ginfotype = 0;
		gtype = 1;
	}else if(mflag == "2"){
		ginfotype = 0;
		gtype = 2;
	}else if(mflag == "3"){
		ginfotype = 0;
		gtype = 3;
	}else if(mflag == "4"){
		ginfotype = 0;
		gtype = 4;
	}else if(mflag == "5"){
		ginfotype = 0;
		gtype = 5;
	}else if(mflag == "6"){
		ginfotype = 0;
		gtype = 6;
	}else if(mflag == "7"){
		ginfotype = 0;
		gtype = 7;
	}else if(mflag == "8"){
		ginfotype = 1;
		gtype = 1;
	}else if(mflag == "9"){
		ginfotype = 1;
		gtype = 2;
	}else if(mflag == "10"){
		ginfotype = 1;
		gtype = 3;
	}else if(mflag == "11"){
		ginfotype = 1;
		gtype = 4;
	}else if(mflag == "12"){
		ginfotype = 1;
		gtype = 5;
	}

	getHisData();
}

var result = '';

function getHisData() {

	$.ajax({
		type: "post",
		url: myUrl + "/mysite/outhospital/index.php/home_infowrite_infohistory",
		data: {
			'userid': 4, 
			'infotype': ginfotype,
			'type': gtype,
			'num': gnum,
			'flag': gflag
		},
		success: function(m) {
			result = eval("(" + m + ")");


			getValueArray();

		},
		error: function(e) {
			alert("error")
		}




	});
}

var valueArr = new Array();
function getValueArray() {
	
	var ReLength = result.dataArray.length;
	var i = 0;
	var iFlag = 1;
	var j = 0;
	
	for (i = 0; i < ReLength; i++) {
		if (iFlag == 8) {  
			DrawPic();
			iFlag = 1;
			j = 0;
			i--;

		} else {
			
//			valueArr.push(result.dataArray[i]);
			
			valueArr[j] = result.dataArray[i];
			j++;
			iFlag++;
		}

	}

}

var dId = 0;
function DrawPic() {
	alert(valueArr[0])
	dId++;
	var html = "";
	html += '<div class="ImgBox">'
	html += '<div class="ImgTitle">'
	if(dId == 1){
		html += '最近一周'
	}else{
		html += valueArr[0].date;
	}
	
	html += '</div>'
	html += '<div class="ImgDataDraw" id='+dId+' >'

	html += '</div> '
	html += '</div>'
	$("#Content").append(html);
	
	
	var date = [];
	var data = {};
	var mvalue = [];
	var nvalue = [];
	var dataArr = valueArr;
	var OlDate = [];
	dataArr.forEach(function(e) {

		OlDate.push(e.date);
		mvalue.push(e.valueone);
		nvalue.push(e.valuetwo);


	});
	date = OlDate.reverse();
	data.name = "";
	data.data = mvalue.reverse();
	data.color = 'red';
	$('#'+dId).highcharts({
		chart: {
			type: 'line',
			margin: [6, 2, 22, 30],
			fontSize: 10,
			fontColor: '#ffffff',
			backgroundColor: '#ffffff',



		},
		legend: {
			enabled: false
		},
		title: {
			text: ''
		},
		xAxis: {
			categories: date,
			lineColor: '#000000',
			lineWidth: 2,
			tickColor: "#000000",
			labels: {
				style: {
					color: '#000000',
					fontSize: 8
				}
			}

		},
		yAxis: {
			title: {
				text: ''
			},
			gridLineColor: '#000000',
			gridLineWidth: 2,
			lineColor: '#000000',
			lineWidth: 2,
			labels: {
				style: {
					color: '#000000',
					fontSize: 8
				}
			},
			tickInterval: 3,
			endOnTick: false,
			maxPadding: 0.5
		},
		plotOptions: {
			line: {
				dataLabels: {
					//							enabled: true 
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



valueArr = new Array();
}