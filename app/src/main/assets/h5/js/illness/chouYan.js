window.onload = function(){
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
		error:function(e){
			alert("wangluocuowu")
		}
	});

}

function saveData(){
	var Freq = document.getElementById("Freq");
	var GetTime = document.getElementById("GetTime");
	var Fvalue = Freq.value;
	var Tvalue = GetTime.value;
	if(Fvalue.length == 0){
		Freq.setAttribute('placeholder','请输入抽烟根数');
		return false;
	}else if(Tvalue.length == 0){
		GetTime.setAttribute('placeholder','请选择时间');
		return false;
	}else{
		$.ajax({
			type:"post",
			url:"",
			data:{
				
			},
			seccess:function(m){
				
			},
			error:function(e){
				
			}
		});
	}
}

function checkHistory(){
	window.location.href = "History.html?id=11";
}
