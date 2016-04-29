window.onload = function() {
	id = bridge.getUserId();
	token = bridge.getToken();
	console.log("id=" + id);
}

var id = "";
var token = "";


var sTime = ""; //开始时间
var eTime = ""; //结束时间
var sexFlag = "";//性别标志：0男 1女 3男女
var age = "";//年龄
var jibing = "";//疾病
var zhiye = "";//职业
var diqu = "";//地区

var BQJiBing = document.getElementById("BQJiBing");
var BQZhiYe = document.getElementById("BQZhiYe");
var BQDiQu = document.getElementById("BQDiQu");
var startTime = document.getElementById("startTime");
var endTime = document.getElementById("endTime");


var BQname = [];
var BQvalue = [];

function getData() {
	 BQname = [];
	 BQvalue = [];


	getDate();	//获取时间
	getCheckBox();   //获取性别
	getAge();	//获取年龄
	getSetVal();	//获得疾病、职业、地区

	if(sTime.length == 0){
		getPopInfo("请选择开始时间");
	}else if(eTime.length == 0){
		getPopInfo("请选择结束时间");
	}else{
	
		$.ajax({
			type:"post",
			url:myUrl+"inquire_searchone",
			data:{
				'token':token,
				'userid':id,
				'startday':sTime,
				'endday':eTime,
				'sex':sexFlag,
				'age':age,
				'job':zhiye,
				'region':diqu,
				'disease':jibing

			},
			success:function(m){
				console.log(m)
				var result = eval("("+m+")");
				if(result.code == 1){
					result.dataArray.forEach(function(e){
						BQname.push(e.name);
						BQvalue.push(Number(e.value)*100);
					});
					
					if(BQvalue.length >7){
						var DrawBox = document.getElementById("DrawBox");
						var mwid = BQvalue.length - 7;
						var wind = ((mwid+10)*10);
						var BQDrawInfo = document.getElementById("BQDrawInfo");
						DrawBox.style.overflowX = 'scroll';
						BQDrawInfo.style.width = (wind)+'%';
					}
					DrawData(); 
				}else{
					getPopInfo(result.msg)
				}
			},
			error:function(e){
				getPopInfo("网络错误")
			}
		});
	}
}

//获取起止时间
function getDate(){
	var startTime = document.getElementById("startTime");
	var endTime = document.getElementById("endTime");
	sTime = startTime.getAttribute('val');
	eTime = endTime.getAttribute('val');
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
	if(sexArr.length == 1){
		sexFlag = sexArr[0];
	}else{
		sexFlag = 3;
	}

}
//获取年龄
function getAge(){
	var Temp = document.getElementById("Temp");
	var TempValue = Temp.value;
	var reg = /^[1-9]+[0-9]+()?$/g;
	if(TempValue.length == 0){
		
	}else{
		
		if(!reg.test(TempValue)){
			Temp.value = "";
			Temp.setAttribute('placeholder','请输入正确的年龄')
		}else{
			age = TempValue;
		}
	}
	
}
//获取写入的疾病、职业、地区
function getSetVal(){
var BQJiBing = document.getElementById("BQJiBing");
var BQZhiYe = document.getElementById("BQZhiYe");
var BQDiQu = document.getElementById("BQDiQu");
var startTime = document.getElementById("startTime");
var endTime = document.getElementById("endTime");

	var jiBingVal = BQJiBing.getAttribute("val");
	var zhiYeVal = BQZhiYe.getAttribute("val");
	var diQuVal = BQDiQu.getAttribute('val');
	if(jiBingVal.length == 0){
		
	}else{
		jibing = jiBingVal;
	}
	if(zhiYeVal.length == 0){
		
	}else{
		zhiye = zhiYeVal;
	}
	if(diQuVal.length == 0){
		
	}else{
		diqu = diQuVal;
	}
	
}

//获取安卓疾病
function getAndroidJiBing(){
	bridge.getJiBing()
}
//h5页面写入疾病
function setJiBing(jibingName,jibingId){
	console.log("写入疾病"+jibingName+";id"+jibingId)
	var BQJiBing = document.getElementById("BQJiBing");

	BQJiBing.innerHTML = jibingName;
	BQJiBing.setAttribute('val',jibingId);
}

//获取安卓职业
function getAndroidZhiYe(){
	bridge.getZhiYe()
}
//h5页面写入职业
function setZhiYe(zhiye){

var BQZhiYe = document.getElementById("BQZhiYe");
	BQZhiYe.innerHTML = zhiye;
	BQZhiYe.setAttribute('val',zhiye);
}

//获取安卓地区
function getAndroidDiQu(){
	bridge.getDiQu()
}
//h5页面写入地区
function setDiQu(diquName,diquId){
	var BQDiQu = document.getElementById("BQDiQu");
	BQDiQu.innerHTML = diquName;
	BQDiQu.setAttribute('val',diquId);
}

var dataFlag = 0;
//调用安卓选择日期方法
function getAndroidDate(a) {
	dataFlag = a;
	bridge.jsInvokeAndroid();
	bridge.textTime();
}
//获取安卓选择日期写入h5测量时间输入框
function SetDateValue(AndrDate) {
	if (dataFlag == 1) {
		var startTime = document.getElementById("startTime");
		startTime.innerHTML = AndrDate;
		startTime.setAttribute('val', AndrDate);
		
	} else {
		var endTime = document.getElementById("endTime");
		endTime.innerHTML = AndrDate;
		endTime.setAttribute('val', AndrDate);
	}

}

//调用安卓弹出提示信息方法
function getPopInfo(msg) {
	bridge.alert(msg);
}
//调用安卓返回方法
function AndroidBack() {
	bridge.back();
}



function DrawData() {
	
	$('#BQDrawInfo').highcharts({
        chart: {
            type: 'column',
            fontSize: 10,
			fontColor: '#000000',
			backgroundColor: '#EFEFF4',
        },
        title: {
            text: ''
        },
        xAxis: {
            categories: BQname,
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
        	allowDecimals: false,
            min: 0,
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
					tickInterval: 15,
					endOnTick: false,
					maxPadding: 0.3
        },
      
        plotOptions: {
        	
        },
        series: [{
            name: '药量（%）',
            data: BQvalue

        }]
    });

}

