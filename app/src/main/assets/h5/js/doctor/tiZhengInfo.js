window.onload = function() {
	id = bridge.getUserId();
	token = bridge.getToken();
	patient = bridge.getPatientId();

//	id=48;
//	patient=41;
//	token = "";

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



var currentPageTW = 1;	//提问页码
var currentPageMB = 1;	//脉搏页码
var currentPageHX = 1;	//呼吸页码
var currentPageXYa = 1;	//血压页码
var currentPageXT = 1;	//血糖页码
var currentPageXYn = 1;	//血氧页码
var currentPageTZ = 1;	//体重页码

var tiwenFlag = 0;		//0:加   1：减
var maiboFlag = 0;		//0:加   1：减
var huxiFlag = 0;		//0:加   1：减
var xueyaFlag = 0;		//0:加   1：减
var xuetangFlag = 0;		//0:加   1：减
var xueyangFlag = 0;		//0:加   1：减
var tizhongFlag = 0;		//0:加   1：减


//体温分页
function tiwenPage(a){
	if(a == 1){
		tiwenFlag = 0;
		currentPageTW++;
		getTemp()
	}else{
		tiwenFlag = 1;
		if(currentPageTW == 1){
			
		}else{
			currentPageTW--;
			getTemp()
		}
		
	}
}

//脉搏分页
function maiboPage(a){
	if(a == 1){
		maiboFlag = 0;
		currentPageMB++;
		getMaiBo();
	}else{
		maiboFlag = 1;
		if(currentPageMB == 1){
			
		}else{
			currentPageMB--;
			getMaiBo()
		}
		
	}
}

//呼吸分页
function huxiPage(a){
	if(a == 1){
		huxiFlag = 0;
		currentPageHX++;
		getHuXi();
	}else{
		huxiFlag = 1;
		if(currentPageHX == 1){
			
		}else{
			currentPageHX--;
			getHuXi();
		}
		
	}
}
//血压分页
function xueyaPage(a){
	if(a == 1){
		xueyaFlag = 0;
		currentPageXYa++;
		getXueYa();
	}else{
		xueyaFlag = 1;
		if(currentPageXYa == 1){
			
		}else{
			currentPageXYa--;
			getXueYa();
		}
		
	}
}
//血糖分页
function xuetangPage(a){
	if(a == 1){
		xuetangFlag = 0;
		currentPageXT++;
		getXueTang();
	}else{
		xuetangFlag = 1;
		if(currentPageXT == 1){
			
		}else{
			currentPageXT--;
			getXueTang();
		}
	}
}
//血氧分页
function xueyangPage(a){
	if(a == 1){
		xueyangFlag = 0;
		currentPageXYn++;
		getXueYang();
	}else{
		xueyangFlag = 1;
		if(currentPageXYn == 1){
			
		}else{
			currentPageXYn--;
			getXueYang();
		}
		
	}
}
//体重分页
function tizhongPage(a){
	if(a == 1){
		tizhongFlag = 0;
		currentPageTZ++;
		getTiZhong();
	}else{
		tizhongFlag = 1;
		if(currentPageTZ == 1){
			
		}else{
			currentPageTZ--;
			getTiZhong();
		}
		
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
			'num': currentPageTW,
			'userid':id
		},
		success: function(m) {
		console.log("体温="+m)
			var result = eval("(" + m + ")");
			if (result.code == 1) {

				var date = ["","","","","","","",""];
				var data = {};
				var mvalue = [];
				var val1 = [];
				var val2 = [];
				var dataArr = result.data.line;
				var c = 0;
				var d = 0;			 
				
				var Ndate = "";
				$.each(dataArr, function(i,k) {
					if(c == 0){
						date[5] = i;
						c++;
					}else{
						date[1] = i;
					}
					$.each(k, function(j,l) { 
						if(d == 0){
							if(l == null){
								val2.push(null)
							}else{
								val2.push(Number(l))
							}
						}else{
							if(l == null){
								val1.push(null)
							}else{
								val1.push(Number(l))
							}
						}
					});
					d++;
				}); 
				tempLineArr = val1.concat(val2);
				tempDate = date;
				var j = 1;
				tempHtml = "";
				result.data.abnormal.forEach(function(e) {
					if(j == 1){
						tempHtml += '<div class="histLists_tiwen">'
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
				if(tiwenFlag == 0){
					currentPageTW --;
				}else{
					currentPageTW ++;
				}
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
			'num': currentPageHX,
			'history': 0,
			'userid':id
		},
		success: function(m) {
			console.log("呼吸="+m)
			var result = eval("(" + m + ")");
			if (result.code == 1) {


				var date = ["","","","","","","",""];
				var data = {};
				var mvalue = [];
				var val1 = [];
				var val2 = [];
				var dataArr = result.data.line;
				var c = 0;
				var d = 0;			 
	
				var Ndate = "";
				$.each(dataArr, function(i,k) {
					if(c == 0){
						date[5] = i;
						c++;
					}else{
						date[1] = i;
					}
					$.each(k, function(j,l) { 
						if(d == 0){
							if(l == null){
								val2.push(null)
							}else{
								val2.push(Number(l))
							}
						}else{
							if(l == null){
								val1.push(null)
							}else{
								val1.push(Number(l))
							}
						}
					});
					d++;
				}); 
				
				huXiLineArr = val1.concat(val2);
				huXiDate = date;
				var j = 1;
				huXiHtml = "";
				result.data.abnormal.forEach(function(e) {
					if(j == 1){
						huXiHtml += '<div>'
						huXiHtml += '<div class="tizhengInfoText tzLeft">'
						huXiHtml += '<div  class="tzInfoDate"><span>' + e.date + '</span></div>'
						huXiHtml += '<div class="tzInfoTemp"><span>每分呼吸：' + e.value + '次</span></div>'
						huXiHtml += '<div class="tzInfoState"><span>' + e.abnormal + '</span></div>'
						huXiHtml += '</div>'
						j = 2;
					}else{
						huXiHtml += '<div class="tizhengInfoText tzRight">'
						huXiHtml += '<div  class="tzInfoDate"><span>' + e.date + '</span></div>'
						huXiHtml += '<div class="tzInfoTemp"><span>每分呼吸：' + e.value + '次</span></div>'
						huXiHtml += '<div class="tzInfoState"><span>' + e.abnormal + '</span></div>'
						huXiHtml += '</div>'
						huXiHtml += '<div style="clear: both;"></div></div>'
						j = 1;
					}
				})

				drawData(3)
			} else {
				if(huxiFlag == 0){
					currentPageHX --;
				}else{
					currentPageHX ++;
				}
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
			'num': currentPageMB,
			'history': 0,
			'userid':id

		},
		success: function(m) {
		console.log("脉搏="+m)
			var result = eval("(" + m + ")");
			if (result.code == 1) {

				var date = ["","","","","","","",""];
				var data = {};
				var mvalue = [];
				var val1 = [];
				var val2 = [];
				var dataArr = result.data.line;
				var c = 0;
				var d = 0;			 
	
				var Ndate = "";
				$.each(dataArr, function(i,k) {
					if(c == 0){
						date[5] = i;
						c++;
					}else{
						date[1] = i;
					}
					$.each(k, function(j,l) { 
						if(d == 0){
							if(l == null){
								val2.push(null)
							}else{
								val2.push(Number(l))
							}
						}else{
							if(l == null){
								val1.push(null)
							}else{
								val1.push(Number(l))
							}
						}
					});
					d++;
				}); 
				maiBoLineArr = val1.concat(val2);
				maiBoDate = date;
				var j = 1;
				maiBoHtml = "";
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
				if(maiboFlag == 0){
					currentPageMB --;
				}else{
					currentPageMB ++;
				}
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
			'num': currentPageXYn,
			'userid':id
		},
		success: function(m) {
		console.log("血氧="+m)
			var result = eval("(" + m + ")");
			if (result.code == 1) {
				var date = ["","","","","","","",""];
				var data = {};
				var mvalue = [];
				var val1 = [];
				var val2 = [];
				var dataArr = result.data.line;
				var c = 0;
				var d = 0;			 
	
				var Ndate = "";
				$.each(dataArr, function(i,k) {
					if(c == 0){
						date[5] = i;
						c++;
					}else{
						date[1] = i;
					}
					$.each(k, function(j,l) { 
						if(d == 0){
							if(l == null){
								val2.push(null)
							}else{
								val2.push(Number(l))
							}
						}else{
							if(l == null){
								val1.push(null)
							}else{
								val1.push(Number(l))
							}
						}
					});
					d++;
				}); 
				xueYangLineArr = val1.concat(val2);
				xueYangDate = date; //血氧日期
				var j = 1;
				xueYangHtml = "";
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
				if(xueyangFlag == 0){
					currentPageXYn --;
				}else{
					currentPageXYn ++;
				}
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
			'num': currentPageTZ, 
			'userid':id

		},
		success: function(m) {
		console.log("体重="+m)
			var result = eval("(" + m + ")");
			tiZhongLineArr = [];
			tiZhongDate = [];
			if (result.code == 1) {
				result.data.line.reverse().forEach(function(e) {
					tiZhongDate.push(e.date);
					if(e.valueone == null){
						tiZhongLineArr.push(null);	
					}else{
						tiZhongLineArr.push(Number(e.valueone));	
					}
				})

				var j = 1;
				tiZhongHtml = "";
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
				if(tizhongFlag == 0){
					currentPageTZ --;
				}else{
					currentPageTZ ++;
				}
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
			'num': currentPageXYa,
			'userid':id
		},
		success: function(m) {
		console.log("血压="+m)
			var result = eval("(" + m + ")");
			if (result.code == 1) {
				
				
				var date = ["","","","","","","",""];
				var data = {};
				var mval1 = [];
				var mval2 = [];
				var nval1 = [];
				var nval2 = [];
				var dataArr = result.data.line;
				var c = 0;
				var d = 0;
				 
				mvalue = [];
				nvalue = [];
				var Ndate = "";
				$.each(dataArr, function(i,k) {
					if(c == 0){
						date[5] = i;
						c++;
					}else{
						date[1] = i;
					}
					$.each(k, function(j,l) { 
						var o = 0;
						
						$.each(l, function(f,h) {
							if(o == 0){
								if(d == 0){
									if(h == null){
										mval2.push(null);
									}else{
										mval2.push(Number(h));
									}
								}else{
									if(h == null){
										mval1.push(null);
									}else{
										mval1.push(Number(h));
									}
								}
								o++;
							}else{
								if(d == 0){
									if(h == null){
										nval2.push(null);
									}else{
										nval2.push(Number(h));
									}
								}else{
									if(h == null){
										nval1.push(null);
									}else{
										nval1.push(Number(h));
									}
								}
								
							}
						});					
					});
					d++;
	
				}); 
				
				mvalue = mval1.concat(mval2);
				nvalue = nval1.concat(nval2);
				

				xueYaDate = date;
				
				var j = 1;
				xueYaHtml = "";
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
				if(xueyaFlag == 0){
					currentPageXYa --;
				}else{
					currentPageXYa ++;
				}
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
			'num': currentPageXT,
			'userid':id
		},
		success: function(m) {
			
		console.log("血糖="+m)
			var result = eval("(" + m + ")");
			
			avalue = [];
			bvalue = [];
			cvalue = [];
			dvalue = [];
			evalue = [];
			fvalue = [];
			gvalue = [];
			if (result.code == '1') {
				result.data.line.forEach(function(e) {
					xueTangDate.push(e.date);
					if(e.value1 == null){
						avalue.push(null);
					}else{
						avalue.push(Number(e.value1));
					}
					if(e.value2 == null){
						bvalue.push(null);
					}else{
						bvalue.push(Number(e.value2));
					}
					
					if(e.value3 == null){
						cvalue.push(null);
					}else{
						cvalue.push(Number(e.value3));
					}
					if(e.value4 == null){
						dvalue.push(null);
					}else{
						dvalue.push(Number(e.value4));
					}
					if(e.value5 == null){
						evalue.push(null);
					}else{
						evalue.push(Number(e.value5));
					}
					if(e.value6 == null){
						fvalue.push(null);
					}else{
						fvalue.push(Number(e.value6));
					}
					if(e.value7 == null){
						gvalue.push(null);
					}else{
						gvalue.push(Number(e.value7));
					}
				})

				
				var j = 1;
				xueTangHtml = "";
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
				if(xuetangFlag == 0){
					currentPageXT --;
				}else{
					currentPageXT ++;
				}
				getPopInfo(result.msg)
			}
		},
		error: function(e) {
			getPopInfo("网络错误");
		}
	});
}




function drawData(a) {
	alert("dd")
	if (a == 1) {
		drawId = 'tempDraw';
		drawDate = tempDate;
		dData.name = '体温';
		dData.data = tempLineArr;
		dData.color = "#c36f2e";
		dInnId = 'tempYC';
		dHtml = tempHtml;
		DrawOneVData();
	} else if (a == 2) {
		drawId = 'maiBoDraw';
		drawDate = maiBoDate;
		dData.name = '脉搏';
		dData.data = maiBoLineArr;
		dData.color = "#c36f2e";
		dInnId = 'maiBoYC';
		dHtml = maiBoHtml;
		DrawOneVData();
	} else if (a == 3) {
		drawId = 'huXiDraw';
		drawDate = huXiDate;
		dData.name = '呼吸';
		dData.data = huXiLineArr;
		dData.color = "#c36f2e";
		dInnId = 'huXiYC';
		dHtml = huXiHtml;
		DrawOneVData();
	} else if (a == 4) {
		drawId = 'xueYangDraw';
		drawDate = xueYangDate;
		dData.name = '血氧';
		dData.data = xueYangLineArr;
		dData.color = "#c36f2e";
		dInnId = 'xueYangYC';
		dHtml = xueYangHtml;
		DrawOneVData();
	} else if (a == 5) {
		alert("ff")
		drawId = 'tiZhongDraw';
		drawDate = tiZhongDate;
		dData.name = '体重';
		dData.data = tiZhongLineArr;
		dData.color = "#c36f2e";
		dInnId = 'tiZhongYC';
		dHtml = tiZhongHtml;
		DrawTiZhong();
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
			categories: drawDate,
			lineColor: '#000000',
			lineWidth: 2,
			tickColor: "#000000",
			labels: {
				style: {
					color: 'black',
					fontSize: 11
				}
			},
			plotLines:[{
				        color:'black',            //线的颜色，定义为红色
				        dashStyle:'longdashdot',//标示线的样式，默认是solid（实线），这里定义为长虚线
				        value:3.5,                //定义在哪个值上显示标示线，这里是在x轴上刻度为3的值处垂直化一条线
				        width:1                 //标示线的宽度，2px
				    }]


		},
		yAxis: {
			allowDecimals: false,
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
					fontSize: 12
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
				},
				connectNulls: true
			}
		},
		series: [dData]
	});


	var InnId = document.getElementById(''+dInnId+'');
	InnId.innerHTML = dHtml;



}

function DrawTiZhong(){
	alert("dd")
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
			categories: drawDate,
			lineColor: '#000000',
			lineWidth: 2,
			tickColor: "#000000",
			labels: {
				style: {
					color: 'black',
					fontSize: 11
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
					fontSize: 12
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
				},
				connectNulls: true
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
			categories: drawDate,
			lineColor: '#000000',
			lineWidth: 2,
			tickColor: "#000000",
			labels: {
				style: {
					color: '#000000',
					fontSize: 11
				}
			},
			plotLines:[{
				        color:'black',            //线的颜色，定义为红色
				        dashStyle:'longdashdot',//标示线的样式，默认是solid（实线），这里定义为长虚线
				        value:3.5,                //定义在哪个值上显示标示线，这里是在x轴上刻度为3的值处垂直化一条线
				        width:1                 //标示线的宽度，2px
				    }]


		},
		yAxis: {
			title: {
				text: ''
			},
			gridLineColor: '#000000',
			gridLineWidth: 2,
			lineColor: '#000000',
			lineWidth: 2,
			tickColor: "#000000",
			labels: {
				style: {
					color: '#000000',
					fontSize: 12
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
				},
				connectNulls: true
			}
		},
		series: [{name:"高血压",data:mvalue,color:'#c36f2e'},{name:"低血压",data:nvalue,color:'#f23c13'}]
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
					fontSize: 11
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
					fontSize: 12
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
				},
				connectNulls: true
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