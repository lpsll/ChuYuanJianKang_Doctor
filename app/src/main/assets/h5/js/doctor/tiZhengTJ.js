window.onload = function() {
		id = bridge.getUserId();
		token = bridge.getToken();
		console.log("id=" + id);
}

var id = "";
var token = "";


var mecList = ""; //药品列表
var sexFlag = ""; //性别标志：1男 2女 3男女
var age = ""; //年龄
var zhiye = ""; //职业
var diqu = ""; //地区

var TZmecList = document.getElementById("TZmecList");
var TZage = document.getElementById("age");
var TZzhiYe = document.getElementById("TZzhiYe");
var TZdiQu = document.getElementById("TZdiQu");


var HxueYa = [];
var DxueYa = [];
var xueYaDate = [];
var xueTangDate = [];
var BeWuFan = [];
var AfWuFan = [];
var BeWanFan = [];
var AfWanFan = [];


function getData() {

HxueYa = [];
DxueYa = [];
xueYaDate = [];
xueTangDate = [];
BeWuFan = [];
AfWuFan = [];
BeWanFan = [];
AfWanFan = [];

	getCheckBox(); //获取性别
	getAge(); //获取年龄
	getSetVal(); //获得药品、职业、地区

	if (mecList.length == 0) {
		getPopInfo("请选择药品");
	} else {

		$.ajax({
			type: "post",
			url: myUrl + "inquire_searchtwo",
			data: {
				'token':token,
				'userid': id,
				'medicine': mecList,
				'sex': sexFlag,
				'age': age,
				'job': zhiye,
				'region': diqu

			},
			success: function(m) {
				console.log("drug："+mecList+"；sex："+sexFlag+"；age："+age+"；job："+zhiye+"；region："+diqu);
				console.log(m);
				var result = eval("(" + m + ")");
				var i = 1;
				var j = 1;
				result.data.blood.forEach(function(e) {
					if (i == 1) {
						e.forEach(function(g) {
							xueYaDate.push(g.name);
							HxueYa.push(Number(g.num));
						})
						i = 2;
					} else {
						e.forEach(function(g) {
							DxueYa.push(Number(g.num));
						})
					}
				})

				result.data.glucose.forEach(function(e) {
					if (j == 1) {
						e.forEach(function(g) {
							xueTangDate.push(g.name);
							BeWuFan.push(Number(g.num));
						})
						j = 2;
					} else if (j == 2) {
						e.forEach(function(g) {
							AfWuFan.push(Number(g.num));
						})
						j = 3;
					} else if (j == 3) {
						e.forEach(function(g) {
							BeWanFan.push(Number(g.num));
						})
						j = 4;
					} else if (j == 4) {
						e.forEach(function(g) {
							AfWanFan.push(Number(g.num));
						})
					}
				})
				
				var xueYaDiv = document.getElementById("XueYaDiv");
				var xueTangDiv = document.getElementById("XueTangDiv");
				
				xueYaDiv.style.display = 'block';
				xueTangDiv.style.display = 'block';

				DrawXueYa();
				DrawXueTang();
			},
			error: function(e) {
				getPopInfo("网络错误")
			}
		});
	}
}


//获取性别
function getCheckBox() {
	var str = document.getElementsByName("man");
	var objarray = str.length;
	var chestr = "";
	var sexArr = [];
	for (i = 0; i < objarray; i++) {
		if (str[i].checked == true) {
			sexArr.push(str[i].value);
		}
	}
	if (sexArr.length == 1) {
		sexFlag = sexArr[0];
	} else {
		sexFlag = 3;
	}

}
//获取年龄
function getAge() {
	var Temp = document.getElementById("age");
	var TempValue = Temp.value;
	var reg = /^[1-9]+[0-9]+()?$/g;
	if (TempValue.length == 0) {

	} else {

		if (!reg.test(TempValue)) {
			Temp.value = "";
			Temp.setAttribute('placeholder', '请输入正确的年龄')
		} else {
			age = TempValue;
		}
	}

}
//获取写入的药品、职业、地区
function getSetVal() {
	var TZmecList = document.getElementById("TZmecList");
	var TZage = document.getElementById("age");
	var TZzhiYe = document.getElementById("TZzhiYe");
	var TZdiQu = document.getElementById("TZdiQu");

	var mecListVal = TZmecList.getAttribute("val");
	var zhiYeVal = TZzhiYe.getAttribute("val");
	var diQuVal = TZdiQu.getAttribute('val');
	if (mecListVal.length == 0) {

	} else {
		mecList = mecListVal;
	}
	if (zhiYeVal.length == 0) {

	} else {
		zhiye = zhiYeVal;
	}
	if (diQuVal.length == 0) {

	} else {
		diqu = diQuVal;
	}

}



//获取安卓药品
function getAndroidMecList() {
	bridge.getMecList()
}
//h5页面写入药品
function setMec(AyaopinName,AyaopinId) {
	var TZmecList = document.getElementById("TZmecList");
	var TZage = document.getElementById("age");
	var TZzhiYe = document.getElementById("TZzhiYe");
	var TZdiQu = document.getElementById("TZdiQu");

	TZmecList.innerHTML = AyaopinName;
	TZmecList.setAttribute('val', AyaopinId);
}

//获取安卓职业
function getAndroidZhiYe() {
	bridge.getZhiYe()
}
//h5页面写入职业
function setZhiYe(Azhiye) {
	var TZmecList = document.getElementById("TZmecList");
	var TZage = document.getElementById("age");
	var TZzhiYe = document.getElementById("TZzhiYe");
	var TZdiQu = document.getElementById("TZdiQu");

	TZzhiYe.innerHTML = Azhiye;
	TZzhiYe.setAttribute('val', Azhiye);
}

//获取安卓地区
function getAndroidDiQu() {
	bridge.getDiQu()
}
//h5页面写入地区
function setDiQu(AdiquName,AdiquId) {
	var TZmecList = document.getElementById("TZmecList");
	var TZage = document.getElementById("age");
	var TZzhiYe = document.getElementById("TZzhiYe");
	var TZdiQu = document.getElementById("TZdiQu");

	TZdiQu.innerHTML = AdiquName;
	TZdiQu.setAttribute('val', AdiquId);
}



//调用安卓弹出提示信息方法
function getPopInfo(msg) {
	bridge.alert(msg);
}
//调用安卓返回方法
function AndroidBack() {
	console.log("AndroidBack")
	bridge.back();
}

$(function(){
	console.log("进入了体征统计")
})


function DrawXueTang(){

	
			$('#XueTangPic').highcharts({
				chart: {
					type: 'line',
//					margin: [20, 15,65, 40],
					fontSize: 10,
					fontColor: '#000000',
					backgroundColor: '#EFEFF4',
				},
				title: {
					text: ''
				},
				xAxis: {
					categories: xueYaDate,
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
					tickInterval: 40,
					endOnTick: false,
					maxPadding: 0.3
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
				series: [{
					name:'午饭前',
					data:BeWuFan,
					color:'#c36f2e'
				},{
					name:'午餐后',
					data:AfWuFan,
					color:'#f23c13'
				},{
					name:'晚餐前',
					data:BeWanFan,
					color:'#e7739a'
				},{
					name:'晚餐后',
					data:AfWanFan,
					color:'#fee554'
				}]
			});
}


function DrawXueYa() {

	
			$('#XueYaPic').highcharts({
				chart: {
					type: 'line',
//					margin: [20, 15,65, 40],
					fontSize: 10,
					fontColor: '#000000',
					backgroundColor: '#EFEFF4',
				},
				title: {
					text: ''
				},
				xAxis: {
					categories: xueYaDate,
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
					tickInterval: 40,
					endOnTick: false,
					maxPadding: 0.3
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
				series: [{
					name:'高血压',
					data:HxueYa,
					color:'#c36f2e'
				},{
					name:'低血压',
					data:DxueYa,
					color:'#f23c13'
				}]
			});
}