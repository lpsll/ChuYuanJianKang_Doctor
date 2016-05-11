window.onload = function() {
	id = bridge.getUserId();
	token = bridge.getToken();
	patient = bridge.getPatientId();
	
//	id=48;
//	patient=41;
//	token = "";
	
	getUrl();
}
var id = "";
var token = "";
var patient = "";
var mflag = "";

function getUrl() {
	var mUrl = location.search;
	var mArr = mUrl.split('?');
	var mValArr = mArr[1].split('=');
	mflag = mValArr[1];
	getOneVData()
}

//调用安卓弹出提示信息方法
function getPopInfo(msg) {
	bridge.alert(msg);
}
//调用安卓返回方法
function AndroidBack() {
	bridge.back();
}

var gType = 0;
var gNum = 0;

function getNextPage() {

	var reload = document.getElementById("reload");
	var loading = document.getElementById("loading");
	var outload = document.getElementById("outLoad");
	reload.style.display = 'none';
	loading.style.display = 'block';

	gNum++;
	getOneVData();

}

function getOneVData() {
	if (mflag == 'tiwen') {
		gType = 1;
	} else if (mflag == 'maibo') {
		gType = 2;
	} else if (mflag == 'huxi') {
		gType = 3;
	} else if (mflag == 'xueyang') {
		gType = 4;
	} else if (mflag == 'tizhong') {
		gType = 5;
	} else if (mflag == 'xueya') {
		gType = 6;
	} else if (mflag == 'xuetang') {
		gType = 7;
	}




	$.ajax({
		type: "post",
		url: myUrl + "doctor_getabnormaldata",
		data: {
			'token': token,
			'patient': patient,
			'type': gType,
			'history': 1,
			'num': gNum,
			'userid': id
			
		},
		success: function(m) {
			console.log(m);
			console.log(gNum)
			var result = eval("(" + m + ")");
			var html = "";
			if (result.code == 1) {
				if (result.dataArray.length == 0) {
					var reload = document.getElementById("reload");
					var loading = document.getElementById("loading");
					var outload = document.getElementById("outLoad");
					reload.style.display = 'none';
					loading.style.display = 'none';
					outload.style.display = 'block';
				} else {
					result.dataArray.forEach(function(e) {
						if (gType == 1) {
							html += '<div class="XQblock">'
							html += '<span class="XQtitle">测量时间：</span><span class="XQvalue">' + e.date + '</span><br />'
//							html += '<span class="XQtitle">测量次数：</span><span class="XQvalue">' + 3 + '次</span><br />'
							html += '<span class="XQtitle marleft">温度：</span><span class="XQvalue">' + e.numericOne + '度</span>'
							html += '</div>'
						} else if (gType == 2) {
							html += '<div class="XQblock">'
							html += '<span class="XQtitle">测量时间：</span><span class="XQvalue">' + e.date + '</span><br />'
							html += '<span class="XQtitle">脉搏频率：</span><span class="XQvalue">' + e.numericOne + '次/分</span><br />'
							html += '</div>'
						} else if (gType == 3) {
							html += '<div class="XQblock">'
							html += '<span class="XQtitle">测量时间：</span><span class="XQvalue">' + e.date + '</span><br />'
							html += '<span class="XQtitle">呼吸次数：</span><span class="XQvalue">' + e.numericOne + '次/分</span><br />'
//							html += '<span class="XQtitle">吸入/呼出气体量：</span><span class="XQvalue">' + '-------' + '</span><br />'
							html += '</div>'
						} else if (gType == 4) {
							html += '<div class="XQblock">'
							html += '<span class="XQtitle">测量时间：</span><span class="XQvalue">' + e.date + '</span><br />'
							html += '<span class="XQtitle">气氧含量：</span><span class="XQvalue">' + e.numericOne + '%</span><br />'
							html += '</div>'
						} else if (gType == 5) {
							html += '<div class="XQblock">'
							html += '<span class="XQtitle">测量时间：</span><span class="XQvalue">' + e.date + '</span><br />'
							html += '<span class="XQtitle marleft">体重：</span><span class="XQvalue">' + e.numericOne + 'Kg</span><br />'
							html += '</div>'
						} else if (gType == 6) {
							html += '<div class="XQblock">'
							html += '<span class="XQtitle">测量时间：</span><span class="XQvalue">' + e.date + '</span><br />'
//							html += '<span class="XQtitle">身体部位：</span><span class="XQvalue">' + '胳膊' + '</span><br />'
//							html += '<span class="XQtitle marleft">心率：</span><span class="XQvalue">' + '-------' + '</span><br />'
							html += '<span class="XQtitle">高压值/低压值：</span><span class="XQvalue">'
							html += e.numericOne+"/"+e.numericTwo
							html += '</span><br />'
							html += '</div>'
						} else if (gType == 7) {
							if(e.type == 1){
								html += '<div class="XQblock">'
								html += '<span class="XQtitle">测量时间：</span><span class="XQvalue">' + e.date + '</span><br />'
								html += '<span class="XQtitle">测量时段：</span><span class="XQvalue">早餐前</span><br />'
								html += '<span class="XQtitle marleftxt">血糖值：</span><span class="XQvalue">' + e.numericOne + '</span><br />'
								html += '</div>'
							}else if(e.type == 2){
								html += '<div class="XQblock">'
								html += '<span class="XQtitle">测量时间：</span><span class="XQvalue">' + e.date + '</span><br />'
								html += '<span class="XQtitle">测量时段：</span><span class="XQvalue">早餐后</span><br />'
								html += '<span class="XQtitle marleftxt">血糖值：</span><span class="XQvalue">' + e.numericOne + '</span><br />'
								html += '</div>'
							}else if(e.type == 3){
								html += '<div class="XQblock">'
								html += '<span class="XQtitle">测量时间：</span><span class="XQvalue">' + e.date + '</span><br />'
								html += '<span class="XQtitle">测量时段：</span><span class="XQvalue">午餐前</span><br />'
								html += '<span class="XQtitle marleftxt">血糖值：</span><span class="XQvalue">' + e.numericOne + '</span><br />'
								html += '</div>'
							}else if(e.type == 4){
								
								html += '<div class="XQblock">'
								html += '<span class="XQtitle">测量时间：</span><span class="XQvalue">' + e.date + '</span><br />'
								html += '<span class="XQtitle">测量时段：</span><span class="XQvalue">午餐后</span><br />'
								html += '<span class="XQtitle marleftxt">血糖值：</span><span class="XQvalue">' + e.numericOne + '</span><br />'
								html += '</div>'
							}else if(e.type == 5){
								html += '<div class="XQblock">'
								html += '<span class="XQtitle">测量时间：</span><span class="XQvalue">' + e.date + '</span><br />'
								html += '<span class="XQtitle">测量时段：</span><span class="XQvalue">晚餐前</span><br />'
								html += '<span class="XQtitle marleftxt">血糖值：</span><span class="XQvalue">' + e.numericOne + '</span><br />'
								html += '</div>'
							}else if(e.type == 6){
								html += '<div class="XQblock">'
								html += '<span class="XQtitle">测量时间：</span><span class="XQvalue">' + e.date + '</span><br />'
								html += '<span class="XQtitle">测量时段：</span><span class="XQvalue">晚餐后</span><br />'
								html += '<span class="XQtitle marleftxt">血糖值：</span><span class="XQvalue">' + e.numericOne + '</span><br />'
								html += '</div>'
							}else if(e.type == 7){
								html += '<div class="XQblock">'
								html += '<span class="XQtitle">测量时间：</span><span class="XQvalue">' + e.date + '</span><br />'
								html += '<span class="XQtitle">测量时段：</span><span class="XQvalue">晚上三点</span><br />'
								html += '<span class="XQtitle marleftxt">血糖值：</span><span class="XQvalue">' + e.numericOne + '</span><br />'
								html += '</div>'
							}
						}
					})
					$("#contInner").append(html);
					var reload = document.getElementById("reload");
					var loading = document.getElementById("loading");
					var outload = document.getElementById("outLoad");
					reload.style.display = 'block';
					loading.style.display = 'none';
				}
			} else {
				var reload = document.getElementById("reload");
				var loading = document.getElementById("loading");
				var outload = document.getElementById("outLoad");
				outload.style.display = 'block';
				loading.style.display = 'none';
				reload.style.display = 'none';
				getPopInfo(result.msg);
			}
		},
		error: function(e) {
			getPopInfo("网络错误");
		}
	});
}