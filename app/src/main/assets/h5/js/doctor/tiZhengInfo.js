window.onload = function() {
	id = bridge.getUserId();
	token = bridge.getToken();
	patient = bridge.getPatientId();
	getTemp()
	getMaiBo();
	getHuXi();
	getXueYang();
	getXueYa();
	getXueTang();
	getTiZhong();
}
var id = "";
var token = "";
var patient = "";

var tempHtml = ""; //体温异常数据1
var maiBoHtml = ""; //脉搏异常数据2
var huXiHtml = ""; //呼吸异常数据3
var xueYaHtml = ""; //血压异常数据6
var xueTangHtml = ""; //血糖异常数据7
var xueYangHtml = ""; //血氧异常数据4
var tiZhongHtml = ""; //体重异常数据5



var tempLineArr = []; //体温数据
var tempDate = []; //体温日期
var maiBoLineArr = []; //脉搏数据
var maiBoDate = []; //脉搏日期
var huXiLineArr = []; //呼吸数据
var huXiDate = []; //呼吸日期
var xueYaLineArr = []; //血压数据
var xueYaDate = []; //血压日期
var xueTangLineArr = []; //血糖数据
var xueTangDate = []; //血糖日期
var xueYangLineArr = []; //血氧数据
var xueYangDate = []; //血氧日期
var tiZhongLineArr = []; //体重数据
var tiZhongDate = []; //体重日期

var tempYC = document.getElementById("tempYC");
var maiBoYC = document.getElementById("maiBoYC");
var huXiYC = document.getElementById("huXiYC");
var xueYaYC = document.getElementById("xueYaYC");
var xueTangYC = document.getElementById("xueTangYC");
var xueYangYC = document.getElementById("xueYangYC");
var tiZhongYC = document.getElementById("tiZhongYC");

//调用安卓弹出提示信息方法
function getPopInfo(msg) {
	bridge.alert(msg);
}
//调用安卓返回方法
function AndroidBack() {
	bridge.back();
}


function getHisPage(a){
	if(a == 1){
		pId = "tiwen";
	}else if(a == 2){
		pId = 'maibo'
	}else if(a == 3){
		pId = 'huxi'
	}else if(a == 4){
		pId = 'xueya'
	}else if(a == 5){
		pId = 'xuetang'
	}else if(a == 6){
		pId = 'xueyang'
	}else if(a == 7){
		pId = 'tizhong'
	}
	
	window.location.href = "tiZhengXQ.html?id="+pId;
}


//获取体温
function getTemp() {
	$.ajax({
		type: "post",
		url: myUrl + "doctor_getabnormaldata",
		data: {
			'token':token,
			'patient': patient,
			'type': 1,
			'history': 0,
			'num': 0,
			'userid':id
		},
		success: function(m) {
		console.log("体温="+m)
			var result = eval("(" + m + ")");
			if (result.code == 1) {


				result.data.line.forEach(function(e) {
					tempDate.push(e.date);
					tempLineArr.push(Number(e.valueone));
				})
				
				var j = 1;
				result.data.abnormal.forEach(function(e) {
					if(j == 1){
						tempHtml += '<div>'
						tempHtml += '<div class="tizhengInfoText tzLeft">'
						tempHtml += '<div  class="tzInfoDate"><span>' + e.date + '</span></div>'
						tempHtml += '<div class="tzInfoTemp"><span>体温：' + e.value + '度</span></div>'
						tempHtml += '<div class="tzInfoState"><span>' + e.abnormal + '</span></div>'
						tempHtml += '</div>'
						j = 2;
					}else{
						tempHtml += '<div class="tizhengInfoText tzRight ">'
						tempHtml += '<div  class="tzInfoDate"><span>' + e.date + '</span></div>'
						tempHtml += '<div class="tzInfoTemp"><span>体温：' + e.value + '度</span></div>'
						tempHtml += '<div class="tzInfoState"><span>' + e.abnormal + '</span></div>'
						tempHtml += '</div>'
						tempHtml += '<div style="clear: both;"></div></div>'
						j = 1;
					}
					

					
				})

				drawData(1)
			} else {
				getPopInfo(result.msg)
			}
		},
		error: function(e) {
			getPopInfo("网络错误");
		}
	});
}
//获取呼吸
function getHuXi() {
	$.ajax({
		type: "post",
		url: myUrl + "doctor_getabnormaldata",
		data: {
			'token':token,
			'patient': patient,
			'type': 3,
			'num': 0,
			'history': 0,
			'userid':id
		},
		success: function(m) {
			console.log("呼吸="+m)
			var result = eval("(" + m + ")");
			if (result.code == 1) {


				result.data.line.forEach(function(e) {
					huXiDate.push(e.date);
					huXiLineArr.push(Number(e.valueone));
				})

				var j = 1;
				result.data.abnormal.forEach(function(e) {
					if(j == 1){
						huXiHtml += '<div>'
						huXiHtml += '<div class="tizhengInfoText tzLeft">'
						huXiHtml += '<div  class="tzInfoDate"><span>' + e.date + '</span></div>'
						huXiHtml += '<div class="tzInfoTemp"><span>呼吸一次：' + e.value + '秒</span></div>'
						huXiHtml += '<div class="tzInfoState"><span>' + e.abnormal + '</span></div>'
						huXiHtml += '</div>'
						j = 2;
					}else{
						huXiHtml += '<div class="tizhengInfoText tzRight">'
						huXiHtml += '<div  class="tzInfoDate"><span>' + e.date + '</span></div>'
						huXiHtml += '<div class="tzInfoTemp"><span>呼吸一次：' + e.value + '秒</span></div>'
						huXiHtml += '<div class="tzInfoState"><span>' + e.abnormal + '</span></div>'
						huXiHtml += '</div>'
						huXiHtml += '<div style="clear: both;"></div></div>'
						j = 1;
					}
				})

				drawData(3)
			} else {
				getPopInfo(result.msg)
			}
		},
		error: function(e) {
			getPopInfo("网络错误");
		}
	});
}

//获取脉搏
function getMaiBo() {
	$.ajax({
		type: "post",
		url: myUrl + "doctor_getabnormaldata",
		data: {
			'token':token,
			'patient': patient,
			'type': 2,
			'num': 0,
			'history': 0,
			'userid':id

		},
		success: function(m) {
		console.log("脉搏="+m)
			var result = eval("(" + m + ")");
			if (result.code == 1) {


				result.data.line.forEach(function(e) {
					maiBoDate.push(e.date);
					maiBoLineArr.push(Number(e.valueone));
				})
					
				var j = 1;
				result.data.abnormal.forEach(function(e) {
					if(j == 1){
						maiBoHtml += '<div>'
						maiBoHtml += '<div class="tizhengInfoText tzLeft">'
						maiBoHtml += '<div  class="tzInfoDate"><span>' + e.date + '</span></div>'
						maiBoHtml += '<div class="tzInfoTemp"><span>脉搏：' + e.value + '次/分</span></div>'
						maiBoHtml += '<div class="tzInfoState"><span>' + e.abnormal + '</span></div>'
						maiBoHtml += '</div>'
						j = 2;
					}else{
						maiBoHtml += '<div class="tizhengInfoText tzRight">'
						maiBoHtml += '<div  class="tzInfoDate"><span>' + e.date + '</span></div>'
						maiBoHtml += '<div class="tzInfoTemp"><span>脉搏：' + e.value + '次/分</span></div>'
						maiBoHtml += '<div class="tzInfoState"><span>' + e.abnormal + '</span></div>'
						maiBoHtml += '</div>'
						maiBoHtml += '<div style="clear: both;"></div></div>'
						j = 1;
					}
				})

				drawData(2)
			} else {
				getPopInfo(result.msg)
			}
		},
		error: function(e) {
			getPopInfo("网络错误");
		}
	});
}


//获取血氧
function getXueYang() {
	$.ajax({
		type: "post",
		url: myUrl + "doctor_getabnormaldata",
		data: {
			'token':token,
			'patient': patient,
			'type': 4,
			'history': 0,
			'num': 0,
			'userid':id
		},
		success: function(m) {
		console.log("血氧="+m)
			var result = eval("(" + m + ")");
			if (result.code == 1) {
				result.data.line.forEach(function(e) {
					xueYangDate.push(e.date);
					xueYangLineArr.push(Number(e.valueone));
				})

				
				var j = 1;
				result.data.abnormal.forEach(function(e) {
					if(j == 1){
						xueYangHtml += '<div>'
						xueYangHtml += '<div class="tizhengInfoText tzLeft">'
						xueYangHtml += '<div  class="tzInfoDate"><span>' + e.date + '</span></div>'
						xueYangHtml += '<div class="tzInfoTemp"><span>血氧：' + e.value + '%</span></div>'
						xueYangHtml += '<div class="tzInfoState"><span>' + e.abnormal + '</span></div>'
						xueYangHtml += '</div>'
						j = 2;
					}else{
						xueYangHtml += '<div class="tizhengInfoText tzRight">'
						xueYangHtml += '<div  class="tzInfoDate"><span>' + e.date + '</span></div>'
						xueYangHtml += '<div class="tzInfoTemp"><span>血氧：' + e.value + '%</span></div>'
						xueYangHtml += '<div class="tzInfoState"><span>' + e.abnormal + '</span></div>'
						xueYangHtml += '</div>'
						xueYangHtml += '<div style="clear: both;"></div></div>'
						j = 1;
					}
				})

				drawData(4)
			} else {
				getPopInfo(result.msg)
			}
		},
		error: function(e) {
			getPopInfo("网络错误");
		}
	});
}



//获取体重
function getTiZhong() {
	$.ajax({
		type: "post",
		url: myUrl + "doctor_getabnormaldata",
		data: {
			'token':token,
			'patient':patient,
			'type': 5,
			'history': 0,
			'num': 0,
			'userid':id

		},
		success: function(m) {
		console.log("体重="+m)
			var result = eval("(" + m + ")");
			if (result.code == 1) {
				result.data.line.forEach(function(e) {
					tiZhongDate.push(e.date);
					tiZhongLineArr.push(Number(e.valueone));
				})

				var j = 1;
				
				result.data.abnormal.forEach(function(e) {
					if(j == 1){
						tiZhongHtml += '<div>'
						tiZhongHtml += '<div class="tizhengInfoText tzLeft">'
						tiZhongHtml += '<div  class="tzInfoDate"><span>' + e.date + '</span></div>'
						tiZhongHtml += '<div class="tzInfoTemp"><span>体重：' + e.value + 'Kg</span></div>'
						tiZhongHtml += '<div class="tzInfoState"><span>' + e.abnormal + '</span></div>'
						tiZhongHtml += '</div>'
						j = 2;
					}else{
						tiZhongHtml += '<div class="tizhengInfoText tzRight">'
						tiZhongHtml += '<div  class="tzInfoDate"><span>' + e.date + '</span></div>'
						tiZhongHtml += '<div class="tzInfoTemp"><span>体重：' + e.value + 'Kg</span></div>'
						tiZhongHtml += '<div class="tzInfoState"><span>' + e.abnormal + '</span></div>'
						tiZhongHtml += '</div>'
						tiZhongHtml += '<div style="clear: both;"></div></div>'
						j = 1;
					}
				})

				drawData(5)
			} else {
				getPopInfo(result.msg)
			}
		},
		error: function(e) {
			getPopInfo("网络错误");
		}
	});
}


//获取血压
function getXueYa() {
	$.ajax({
		type: "post",
		url: myUrl + "doctor_getabnormaldata",
		data: {
			'token':token,
			'patient':patient,
			'type': 6,
			'history': 0,
			'num': 0,
			'userid':id
		},
		success: function(m) {
		console.log("血压="+m)
			var result = eval("(" + m + ")");
			if (result.code == 1) {
				result.data.line.forEach(function(e) {
					xueYaDate.push(e.date);
					mvalue.push(Number(e.valueone));
					nvalue.push(Number(e.valuetwo));
				})

				var j = 1;
				
				result.data.abnormal.forEach(function(e) {
					if(j == 1){
						xueYaHtml += '<div>'
						xueYaHtml += '<div class="tizhengInfoText tzLeft">'
						xueYaHtml += '<div  class="tzInfoDate"><span>' + e.date + '</span></div>'
						xueYaHtml += '<div class="tzInfoTemp"><span>血压：' + e.value + 'mmhg</span></div>'
						xueYaHtml += '<div class="tzInfoState"><span>' + e.abnormal + '</span></div>'
						xueYaHtml += '</div>'
						j = 2;
					}else{
						xueYaHtml += '<div class="tizhengInfoText tzRight">'
						xueYaHtml += '<div  class="tzInfoDate"><span>' + e.date + '</span></div>'
						xueYaHtml += '<div class="tzInfoTemp"><span>血压：' + e.value + 'mmhg</span></div>'
						xueYaHtml += '<div class="tzInfoState"><span>' + e.abnormal + '</span></div>'
						xueYaHtml += '</div>'
						xueYaHtml += '<div style="clear: both;"></div></div>'
						j = 1;
					}
				})

				drawData(6)
			} else {
				getPopInfo(result.msg)
			}
		},
		error: function(e) {
			getPopInfo("网络错误");
		}
	});
}

//获取血糖
function getXueTang() {
	$.ajax({
		type: "post",
		url:myUrl+'doctor_getabnormaldata',
		data: {
			'token':token,
			'patient':patient,
			'type': 7,
			'history': 0,
			'num': 0,
			'userid':id
		},
		success: function(m) {
		console.log("血糖="+m)
			var result = eval("(" + m + ")");
			if (result.code == '1') {
				result.data.line.forEach(function(e) {
					xueTangDate.push(e.date);
					avalue.push(Number(e.value1));
					bvalue.push(Number(e.value2));
					cvalue.push(Number(e.value3));
					dvalue.push(Number(e.value4));
					evalue.push(Number(e.value5));
					fvalue.push(Number(e.value6));
					gvalue.push(Number(e.value7));
				})

				
				var j = 1;
				
				result.data.abnormal.forEach(function(e) {
					if(j == 1){
						xueTangHtml += '<div>'
						xueTangHtml += '<div class="tizhengInfoText tzLeft">'
						xueTangHtml += '<div  class="tzInfoDate"><span>' + e.date + '</span></div>'
						xueTangHtml += '<div class="tzInfoTemp"><span>血糖：' + e.numericOne + '毫克/分升</span></div>'
						xueTangHtml += '<div class="tzInfoState"><span>' + e.abnormal + '</span></div>'
						xueTangHtml += '</div>'
						j = 2;
					}else{
						xueTangHtml += '<div class="tizhengInfoText tzRight">'
						xueTangHtml += '<div  class="tzInfoDate"><span>' + e.date + '</span></div>'
						xueTangHtml += '<div class="tzInfoTemp"><span>血糖：' + e.numericOne + '毫克/分升</span></div>'
						xueTangHtml += '<div class="tzInfoState"><span>' + e.abnormal + '</span></div>'
						xueTangHtml += '</div>'
						xueTangHtml += '<div style="clear: both;"></div></div>'
						j = 1;
					}
				})

				drawData(7)
			} else {
				getPopInfo(result.msg)
			}
		},
		error: function(e) {
			getPopInfo("网络错误");
		}
	});
}


var drawId = "";
var drawDate = "";
var drawValue = "";
var dData = {};
var dInnId = '';
var dHtml = '';
var mvalue = [];
var nvalue = [];

var avalue = [];
var bvalue = [];
var cvalue = [];
var dvalue = [];
var evalue = [];
var fvalue = [];
var gvalue = [];

function drawData(a) {
	if (a == 1) {
		drawId = 'tempDraw';
		drawDate = tempDate;
		dData.name = '体温';
		dData.data = tempLineArr.reverse();
		dData.color = "#c36f2e";
		dInnId = 'tempYC';
		dHtml = tempHtml;
		DrawOneVData();
	} else if (a == 2) {
		drawId = 'maiBoDraw';
		drawDate = maiBoDate;
		dData.name = '脉搏';
		dData.data = maiBoLineArr.reverse();
		dData.color = "#c36f2e";
		dInnId = 'maiBoYC';
		dHtml = maiBoHtml;
		DrawOneVData();
	} else if (a == 3) {
		drawId = 'huXiDraw';
		drawDate = huXiDate;
		dData.name = '呼吸';
		dData.data = huXiLineArr.reverse();
		dData.color = "#c36f2e";
		dInnId = 'huXiYC';
		dHtml = huXiHtml;
		DrawOneVData();
	} else if (a == 4) {
		drawId = 'xueYangDraw';
		drawDate = xueYangDate;
		dData.name = '血氧';
		dData.data = xueYangLineArr.reverse();
		dData.color = "#c36f2e";
		dInnId = 'xueYangYC';
		dHtml = xueYangHtml;
		DrawOneVData();
	} else if (a == 5) {
		drawId = 'tiZhongDraw';
		drawDate = tiZhongDate;
		dData.name = '体重';
		dData.data = tiZhongLineArr.reverse();
		dData.color = "#c36f2e";
		dInnId = 'tiZhongYC';
		dHtml = tiZhongHtml;
		DrawOneVData();
	} else if (a == 6) {
	console.log('flag=6')
		drawId = 'xueYaDraw';
		drawDate = xueYaDate;
		dInnId = 'xueYaYC';
		dHtml = xueYaHtml;
		DrawXueYaVData();
	} else if (a == 7) {
	console.log('flag=7')
		drawId = 'xueTangDraw';
		drawDate = xueTangDate;
		dInnId = 'xueTangYC';
		dHtml = xueTangHtml;
		console.log("zhiqian")
		DrawXueTangVData();
	}

}


function DrawOneVData(){
	
	$('#' + drawId + '').highcharts({
		chart: {
			type: 'line',
//			margin: [6, 2, 22, 30],
			fontSize: 10,
			fontColor: '#000000',
			backgroundColor: 'white',
		},
		legend: {
			enabled: true
		},
		title: {
			text: ''
		},
		xAxis: {
			categories: drawDate.reverse(),
			lineColor: '#000000',
			lineWidth: 2,
			labels: {
				style: {
					color: 'black',
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
					color: 'black',
					fontSize: 8
				}
			},
			tickInterval: 8,
			endOnTick: false,
			maxPadding: 0.5
		},
		plotOptions: {
			line: {
				
				dataLabels: {
					color:'blue',
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
		series: [dData]
	});


	var InnId = document.getElementById(''+dInnId+'');
	InnId.innerHTML = dHtml;


}

function DrawXueYaVData(){
	
	$('#' + drawId + '').highcharts({
		chart: {
			type: 'line',
//			margin: [6, 2, 22, 30],
			fontSize: 10,
			fontColor: '#000000',
			backgroundColor: '#ffffff',
		},
		legend: {
			enabled: true
		},
		title: {
			text: ''
		},
		xAxis: {
			categories: drawDate.reverse(),
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
			tickInterval: 50,
			endOnTick: false,
			maxPadding: 0.5
		},
		plotOptions: {
			line: {
				dataLabels: {
					color:'blue',
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
		series: [{name:"高血压",data:mvalue.reverse(),color:'#c36f2e'},{name:"低血压",data:nvalue.reverse(),color:'#f23c13'}]
	});
	
	var InnId = document.getElementById(''+dInnId+'');
	InnId.innerHTML = dHtml;
}

function DrawXueTangVData(){
$('#' + drawId + '').highcharts({
		chart: {
			type: 'line',
//			margin: [6, 2, 22, 30],
			fontSize: 10,
			fontColor: '#000000',
			backgroundColor: '#ffffff',



		},
		legend: {
			enabled: true
		},
		title: {
			text: ''
		},
		xAxis: {
			categories: drawDate.reverse(),
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
			tickInterval: 40,
			endOnTick: false,
			maxPadding: 0.5
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
		series: [
		{name:"早餐前",data:avalue.reverse(),color:'#c36f2e'},
		{name:"早餐后",data:bvalue.reverse(),color:'#f23c13'},
		{name:"午餐前",data:cvalue.reverse(),color:'#e7739a'},
		{name:"午餐后",data:dvalue.reverse(),color:'#e8e5d2'},
		{name:"晚餐前",data:evalue.reverse(),color:'#fee554'},
		{name:"晚餐后",data:fvalue.reverse(),color:'#009714'},
		{name:"晚上三点",data:gvalue.reverse(),color:'#ff243d'},
		]
	});
	
	var InnId = document.getElementById(''+dInnId+'');
	InnId.innerHTML = dHtml;
}