//调用安卓弹出提示信息方法
function getPopInfo(msg) {
	bridge.alert(msg);
}
//调用安卓返回方法
function AndroidBack() {
	bridge.back();
}

window.onload = function() {
		id = bridge.getUserId();
		token = bridge.getToken();
		patient = bridge.getPatientId();

//	getOutInfo();
	getPatInfo();
	getMecList();
	getUnormalInfo(1);
	DrawYunDong();
	DrawShuiMian();
	DrawChouYan();
	DrawHeJiu();
	DrawYinShi()
}
var token = "";
var id = "";
var patient = "";


function DrawYinShi(){
	

	$.ajax({
		url: myUrl+"infowrite_eathistory",
		type:"post",
		data:{
			'patient':patient,
			'token':token,
			'userid':id,
			'num':0,
			'flag':0
		},
		success: function(m) {
			console.log(m)
			var result = eval("("+m+")");
			var date = []; 
			var data = {};
			var mvalue = [];
			var nvalue = [];
			var dataArr = result.dataArray.reverse();
			dataArr.forEach(function(e) {
					date.push(e.date);
					if(e.numericOne == null){
						mvalue.push(null);
					}else{
						mvalue.push(parseInt(e.numericOne));
					}
					if(e.numericTwo == null){
						nvalue.push(null);
					}else{
						nvalue.push(parseInt(e.numericTwo));
					}
					
			});
//			data.name = "饮食";
			console.log(mvalue);
			console.log(nvalue);
			$('#YinShiPic').highcharts({
				chart: {
					type: 'line',
//					margin: [6, 2, 50, 30],
					fontSize:10,
					fontColor:'red',
					backgroundColor: '#ffffff',
//					spacingBottom: 100
					
				},
				legend: {
		            enabled: true,
		      	},
				title: {
					text: ''
				},
				xAxis: {
					categories: date,
					lineColor: '#000000',
        			lineWidth: 2,
					labels: {
		                style: {
		                    color: 'black',
		                    fontSize:8
		                }
		            }

				},
				yAxis: {
					title: {
						text: ''
					},
					gridLineColor: '#000000',
					gridLineWidth:2,
					lineColor: '#000000',
        			lineWidth: 2,
        			labels: {
		                style: {
		                    color: 'black',
		                    fontSize:8
		                }
		            },
		            tickInterval: 1,
		            endOnTick: false,
           			maxPadding: 0.8
				},
				plotOptions: {
					line: {
						dataLabels: {
							enabled: true ,
							color:"#000000",
							 formatter: function () {
//		                        return  '咸';
		                        if(this.y == 1){
		                        	return '无'
		                        }else if(this.y == 2){
		                        	return '油腻'
		                        }else if(this.y == 3){
		                        	return '一般'
		                        }else if(this.y == 4){
		                        	return '清淡'
		                        }
		                    }
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
					},
//				  	columnrange: {
//		                dataLabels: {
//		                    enabled: true,
//		                    formatter: function () {
//		                        return  '咸';
//		                    }
//		                }
//		            }
				},
				series: [{
					color:'#498EDB',
					name:'饮食',
					data:mvalue
				
				}]
			});


		},
		error:function(e){
			getPopInfo("网络错误");
		}
	});


}

function DrawHeJiu(){
	

	$.ajax({
		url: myUrl+"infowrite_infohistory",
		type:"post",
		data:{
			'patient':patient,
			'token':token,
			'userid':id,
			'type':8,//8:饮酒
			'num':0,
			'flag':0//0：一周记录
		},
		success: function(m) {
			console.log("success="+m);
			var result = eval("("+m+")");
			var date = [];
			var data = {};
			var mvalue = [];
			var dataArr = result.dataArray.reverse();
			dataArr.forEach(function(e) {
					date.push(e.date);
					if(e.valueone == null){
						mvalue.push(null);	
					}else{
						mvalue.push(parseInt(e.valueone));	
					}
								
			}); 
			data.name = "饮酒";
			data.data = mvalue;
			data.color='#498EDB'
			$('#HeJiuPic').highcharts({
				chart: {
					type: 'line',
//					margin: [6, 2, 22, 30],
					fontSize:10,
					fontColor:'#ffffff',
					backgroundColor: '#ffffff',
					
					
				},
				legend: {
		            enabled: true
		      	},
				title: {
					text: ''
				},
				xAxis: {
					categories: date,
					lineColor: '#000000',
        			lineWidth: 2,
					labels: {
		                style: {
		                    color: 'black',
		                    fontSize:8
		                }
		            }

				},
				yAxis: {
					title: {
						text: ''
					},
					gridLineColor: '#000000',
					gridLineWidth:2,
					lineColor: '#000000',
        			lineWidth: 2,
        			labels: {
		                style: {
		                    color: 'black',
		                    fontSize:8
		                }
		            },
		            tickInterval: 8,
		            endOnTick: false,
           			maxPadding: 0.3
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
				series: [data]
			});


		},
		error:function(e){
			getPopInfo("网络错误");
		}
	});


}

function DrawChouYan(){
	

	$.ajax({
		url: myUrl+"infowrite_infohistory",
		type:"post",
		data:{
			'userid':id,
			'type':7,//7:抽烟
			'num':0,
			'flag':0,//0：一周记录
			'token':token,
			'patient':patient

		},
		success: function(m) {
			console.log("success="+m);
			var result = eval("("+m+")");
			var date = [];
			var data = {};
			var mvalue = [];
			var dataArr = result.dataArray.reverse();
			dataArr.forEach(function(e) {
					date.push(e.date);
					if(e.valueone == null){
						mvalue.push(null);	
					}else{
						mvalue.push(parseInt(e.valueone));	
					}
								
			});
			data.name = "抽烟";
			data.data = mvalue;
			data.color='#498EDB'
			$('#ChouYanPic').highcharts({
				chart: {
					type: 'line',
//					margin: [6, 2, 22, 30],
					fontSize:10,
					fontColor:'#ffffff',
					backgroundColor: '#ffffff',
					
					
				},
				legend: {
		            enabled: true
		      	},
				title: {
					text: ''
				},
				xAxis: {
					categories: date,
					lineColor: '#000000',
        			lineWidth: 2,
					labels: {
		                style: {
		                    color: 'black',
		                    fontSize:8
		                }
		            }

				},
				yAxis: {
					title: {
						text: ''
					},
					gridLineColor: '#000000',
					gridLineWidth:2,
					lineColor: '#000000',
        			lineWidth: 2,
        			labels: {
		                style: {
		                    color: 'black',
		                    fontSize:8
		                }
		            },
		            tickInterval: 3,
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
				series: [data]
			});


		},
		error:function(e){
			getPopInfo("网络错误");
		}
	});


}


function DrawYunDong(){
	

	$.ajax({
		url: myUrl+"infowrite_infohistory",
		type:"post",
		data:{
			'patient':patient,
			'token':token,
			'userid':id,
			'type':6,//6:运动
			'num':0,
			'flag':0//0：一周记录
		},
		success: function(m) {
			var result = eval("("+m+")");
			var date = [];
			var data = {};
			var mvalue = [];
			var dataArr = result.dataArray.reverse();
			dataArr.forEach(function(e) {
					date.push(e.date);
					if(e.valueone == null){
						mvalue.push(null);
					}else{
						mvalue.push(parseInt(e.valueone));
					}
									
			});
			data.name = "运动";
			data.data = mvalue;
			data.color='#498EDB'
			$('#YunDongPic').highcharts({
				chart: {
					type: 'line',
//					margin: [6, 2, 22, 30],
					fontSize:10,
					fontColor:'#498EDB',
					backgroundColor: '#ffffff',
					
					
				},
				legend: {
		            enabled: true
		      	},
				title: {
					text: ''
				},
				xAxis: {
					categories: date,
					lineColor: '#000000',
        			lineWidth: 2,
					labels: {
		                style: {
		                    color: 'black',
		                    fontSize:8
		                }
		            }

				},
				yAxis: {
					title: {
						text: ''
					},
					gridLineColor: '#000000',
					gridLineWidth:2,
					lineColor: '#000000',
        			lineWidth: 2,
        			labels: {
		                style: {
		                    color: 'black',
		                    fontSize:8
		                }
		            },
		            tickInterval: 20,
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
				series: [data]
			});


		},
		error:function(e){
			getPopInfo("网络错误");
		}
	});


}

function DrawShuiMian(){
	

	$.ajax({
		url:  myUrl+"infowrite_sleephistory",
		type:"post",
		data:{
			'patient':id,
			'token':token,
			'userid':id,
			'num':0,
			'flag':0//0：一周记录
		},
		success: function(m) {
			console.log("success="+m);
			var result = eval("("+m+")");
			var date = [];
			var data = {};
			var mvalue = [];
			var dataArr = result.dataArray.reverse();
			dataArr.forEach(function(e) {
					date.push(e.date);
					if(e.valueone == null){
						mvalue.push(null);	
					}else{
						mvalue.push(parseInt(e.valueone));	
					}
								
			});
			data.name = "睡眠";
			data.data = mvalue;
			data.color='#498EDB'
			$('#ShuiMianPic').highcharts({
				chart: {
					type: 'line',
//					margin: [6, 2, 22, 30],
					fontSize:10,
					fontColor:'#ffffff',
					backgroundColor: '#ffffff',
					
					
				},
				legend: {
		            enabled: true
		      	},
				title: {
					text: ''
				},
				xAxis: {
					categories: date,
					lineColor: '#000000',
        			lineWidth: 2,
					labels: {
		                style: {
		                    color: 'black',
		                    fontSize:8
		                }
		            }

				},
				yAxis: {
					title: {
						text: ''
					},
					gridLineColor: '#000000',
					gridLineWidth:2,
					lineColor: '#000000',
        			lineWidth: 2,
        			labels: {
		                style: {
		                    color: 'black',
		                    fontSize:8
		                }
		            },
		            tickInterval: 2,
		            endOnTick: false,
           			maxPadding: 0.3
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
				series: [data]
			});


		},
		error:function(e){
			getPopInfo("网络错误");
		}
	});


}

function getUnormalInfo(a) {
	if(a == 1){
		abName = "体温";
		abUnit = "度";
	}else if( a == 2){
		abName = "脉搏";
		abUnit = "次/分";
	}else if( a == 3){
		abName = "呼吸";
		abUnit = "次/分";
	}else if( a == 4){
		abName = "血氧";
		abUnit = "%";
	}else if( a == 5){
		abName = "体重";
		abUnit = "Kg";
	}else if( a == 6){
		abName = "血压";
		abUnit = "mmhg";
	}else if( a == 7){
		abName = "血糖";
		abUnit = "毫克/分升";
	}
	$.ajax({
		type: "post",
		url: myUrl + "doctor_getabnormaldata",
		data: {
			'userid':id,
			'patient':patient,
			'type':a,
			'history':0,
			'num':0,
			'token':token
		},
		success: function(m) {
			var result = eval("(" + m + ")");
			if (result.code == 1) {
				var MobjLine = result.data.line; //图表数据
				var MobjAbnom = result.data.abnormal; //异常数据

				var html = "";
				MobjAbnom.forEach(function(e) {
					
					if(a == 6){
						html += '<div class="TabInfoList">'
						html += '<span class="BlockSpan">'+e.date+'</span>'
						html += '<span class="BlockSpan SpanFont">'+abName+'：'+e.value+abUnit+'</span>'
						html += '<span class="BlockSpan SpanRFont">'+e.abnormal+'</span>'
						html += '</div>'
					}else if(a == 7){
						html += '<div class="TabInfoList">'
						html += '<span class="BlockSpan">'+e.date+'</span>'
						html += '<span class="BlockSpan SpanFont">'+abName+'：'+e.numericOne+abUnit+'</span>'
						html += '<span class="BlockSpan SpanRFont">'+e.abnormal+'</span>'
						html += '</div>'
					}else{
						html += '<div class="TabInfoList">'
						html += '<span class="BlockSpan">'+e.date+'</span>'
						html += '<span class="BlockSpan SpanFont">'+abName+'：'+e.value+abUnit+'</span>'
						html += '<span class="BlockSpan SpanRFont">'+e.abnormal+'</span>'
						html += '</div>'
					}
				})
				$("#abNormal").append(html);
				
				if(a == 1){
					getUnormalInfo(2)
				}else if(a ==2 ){
					getUnormalInfo(3)
				}else if(a ==3 ){
					getUnormalInfo(4)
				}else if(a ==4 ){
					getUnormalInfo(5)
				}else if(a ==5 ){
					getUnormalInfo(6)
				}else if(a ==6 ){
					getUnormalInfo(7)
				}
				
			} else {
				getPopInfo(result.msg);
			}
		},
		error: function(e) {
			getPopInfo("网络错误");
		}
	});
}



function getMecList() {
	$.ajax({
		type: "post",
		url: myUrl + "infowrite_medicinehistory",
		data: {
			'userid':id,
			'patient':patient,
			'flag':0,
			'token':token
		},
		success: function(m) {
			var result = eval("(" + m + ")");
			if (result.code == 1) {
				var Mobj = result.dataArray;
				var html = "";
				Mobj.forEach(function(e) {
					html += '<div class="MadList" >'
					html += '<ul class="MadUl">'
					html += '<li class="MadUl"><span class="MadInfo">' + e.date + '</span></li>'

					e.drug.forEach(function(t) {
						html += '<li class="MadUl"><span class="MadInfo">' + t.medicine + '</span><span class="MadNum">' + t.num + t.unit + '</span></li>'
					})

					html += '</ul>'
					html += '</div>'
				})

				var PatMadBox = document.getElementById("PatMadBox");
				PatMadBox.innerHTML = html;
			} else {
				getPopInfo(result.msg)
			}
		},
		error: function(e) {
			getPopInfo("网络错误");
		}
	});
}


function getPatInfo() {
	$.ajax({
		type: "post",
		url: myUrl + "doctor_getdetails",
		data: {
			'userid':id,
			'patient':patient,
			'token':token
		},
		success: function(m) {

			var result = eval("(" + m + ")");
			if (result.code == 1) {
			console.log(m)
				var Mobj = result.data;
				var PSex = "";
				var PatName = document.getElementById("PatName");
				var PatAge = document.getElementById("PatAge");
				var PatSex = document.getElementById("PatSex");
				var PatBingZhuang = document.getElementById("PatBingZhuang");
				var PatDocotr = document.getElementById("PatDocotr");
				var telPhone = document.getElementById("telPhone");
				console.log(Mobj.phone)
				if (Mobj.sex == 0) {
					PSex = "男";
				} else {
					PSex = "女";
				}
				PatName.innerHTML = Mobj.name;
				PatAge.innerHTML = Mobj.age;
				PatSex.innerHTML = PSex;
				PatBingZhuang.innerHTML = Mobj.subjectName;
				PatDocotr.innerHTML = Mobj.doctor;
				telPhone.setAttribute('value',Mobj.phone);



				var html = "";

				html += '<li class="MadUl"><span class="SpanBlackFont">姓 名：</span><span class="SpanFont">' + Mobj.name + '</span></li>'
				html += '<li class="MadUl"><span class="SpanBlackFont">性 别：</span><span class="SpanFont">' + Mobj.sex + '</span></li>'
				html += '<li class="MadUl"><span class="SpanBlackFont">年 龄：</span><span class="SpanFont">' + Mobj.age + '</span></li>'
				html += '<li class="MadUl"><span class="SpanBlackFont">职 业：</span><span class="SpanFont">' + Mobj.profession + '</span></li>'
				html += '<li class="MadUl"><span class="SpanBlackFont">入院日期：</span><span class="SpanFont">' + Mobj.hospitalize + '</span></li>'
				html += '<li class="MadUl"><span class="SpanBlackFont">出院日期：</span><span class="SpanFont">' + Mobj.discharged + '</span></li>'
				html += '<li class="MadUl"><span class="SpanBlackFont">住院天数：</span><span class="SpanFont">' + Mobj.inDay + '天</span></li>'
				html += '<li class="MadUl"><span class="SpanBlackFont">入院诊断：</span><span class="SpanFont">' + Mobj.inDiagnose + '</span></li>'
				html += '<li class="MadUl"><span class="SpanBlackFont">出院诊断：</span><span class="SpanFont">' + Mobj.outDiagnose + '</span></li>'
				html += '<li class="MadUl"><span class="SpanBlackFont">治疗结果：</span><span class="SpanFont">' + Mobj.result + '</span></li>'
				html += '<li class="MadUl"><span class="SpanBlackFont">各种特殊检查号：</span><span class="SpanFont">' + Mobj.checkNum + '</span></li>'
				html += '<li class="MadUl BotmMag"><span class="SpanBlackFont">入院情况：</span><span class="SpanFont">'
				html += Mobj.inInfo
				html += '</span></li>'
				html += '<li class="MadUl BotmMag"><span class="SpanBlackFont">住院情况：</span><span class="SpanFont">'
				html += Mobj.onIfo
				html += '</span></li>'
				html += '<li class="MadUl BotmMag"><span class="SpanBlackFont">出院情况：</span><span class="SpanFont">'
				html += Mobj.outInfo
				html += '</span></li>'
				html += '<li class="MadUl BotmMag"><span class="SpanBlackFont">出院医嘱：</span><span class="SpanFont">'
				html += Mobj.advice
				html += '</span></li>'




				var OutHosUl = document.getElementById("OutHosUl");
				OutHosUl.innerHTML = html;
			} else {
				getPopInfo(result.msg);
			}
		},
		error: function(e) {
			getPopInfo("网络错误");
		}
	});
}

//function getOutInfo() {
//	$.ajax({
//		type: "post",
//		url: myUrl + "doctor_getdetails",
//		data: {
//			'userid':id,
//			'patient':patient,
//			'token':token
//		},
//		success: function(m) {
//			var result = eval("(" + m + ")");
//			if (result.code == 1) {
//				var Mobj = result.data;
//
//
//			} else {
//				getPopInfo(result.msg)
//			}
//
//		},
//		error: function(e) {
//			getPopInfo("网络错误")
//		}
//	});
//}
function tel(){
	var telPhoneNum = document.getElementById("telPhone").getAttribute("value");
	console.log(telPhoneNum);
	bridge.callTel(telPhoneNum);
}
