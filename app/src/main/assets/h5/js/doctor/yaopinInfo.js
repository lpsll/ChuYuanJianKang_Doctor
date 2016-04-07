

window.onload = function() {
	id = bridge.getUserId();
	token = bridge.getToken();
	patient = bridge.getPatientId();
	console.log('id='+id+";token="+token+";patient="+patient);
	getyaopinInfo(id);
	act() ;
}

function act() {
	var NDloadBtn = document.getElementById("NDload");
	var NDloading = document.getElementById("NDloading");
	NDloadBtn.style.display = 'none';
	NDloading.style.display = 'block';
	NDloadBtn.addEventListener('click', function() {
		getyaopinInfoAppend(id);
	})
}


var token = "";
var id = "";
var patient = "";
function getyaopinInfo(id) {
	console.log("进入获得信息方法")
	$.ajax({
		type: "post",
		url: myUrl + "infowrite_medicinehistory",
		data: {
			'token':token,
			'num': 0,
			'flag': 1,
			'userid': id,
			'patient':patient
		},
		success: function(m) {
			console.log(m)
			var result = eval("(" + m + ")");
			var html = "";
			result.dataArray.forEach(function(e) {
				html += '<div class="mui-card yaopinContent">';
				html += '<ul class="mui-table-view">';
				html += '<li class="mui-table-view-cell mui-collapse">';
				html += '<a class="mui-navigate-right ypTitle" href="#">';
				html += e.date;
				html += '</a>';
				html += '<div class="mui-collapse-content ypCon">';
				html += '<ul id="yaopinul">';
				e.drug.forEach(function(f) {
					html += '<li><span class="ypconleft">';
					html += f.medicine;
					html += '</span><span class="ypconright"> ';
					html += f.num;
					html += f.unit;
					html += '</span>';
					html += '</li>';
				})
				html += '</ul>';
				html += '</div>';
				html += '</li>';
				html += '</ul>';
				html += '</div>';
			})
			var yaopinInfo = document.getElementById("yaopinInfo");
			yaopinInfo.innerHTML = html;
               
            var NDloadBtn = document.getElementById("NDload");
			var NDloading = document.getElementById("NDloading");

			NDloading.style.display = 'none';
			NDloadBtn.style.display = 'block';
		},
		error: function(e) {
			getPopInfo("网络错误");
		}

	});
}

var num = 1;

function getyaopinInfoAppend(id) {
	
	$.ajax({
		type: "post",
		url: myUrl + "infowrite_medicinehistory",
		data: {
			'token':token,
			'num': num,
			'flag': 1,
			'userid': id
		},
		success: function(m) {
			var result = eval("(" + m + ")");
			if (result.code == 0) {

				var NDloadBtn = document.getElementById("NDload");
				var NDloading = document.getElementById("NDloading");
				var NDout = document.getElementById("NDout");

				NDloadBtn.style.display = 'none';
				NDloading.style.display = 'none';
				NDout.style.display = 'block';
			} else {
				var html = "";
				result.dataArray.forEach(function(e) {
					html += '<div class="mui-card yaopinContent">';
					html += '<ul class="mui-table-view">';
					html += '<li class="mui-table-view-cell mui-collapse">';
					html += '<a class="mui-navigate-right ypTitle" href="#">';
					html += e.date;
					html += '</a>';
					html += '<div class="mui-collapse-content ypCon">';
					html += '<ul id="yaopinul">';
					e.drug.forEach(function(e) {
						html += '<li><span class="ypconleft">';
						html += e.medicine;
						html += '</span><span class="ypconright"> ';
						html += e.num;
						html += e.unit;
						html += '</span>';
						html += '</li>';
					})
					html += '</ul>';
					html += '</div>';
					html += '</li>';
					html += '</ul>';
					html += '</div>';
				})

				$("#yaopinInfo").append(html);
				num++;
				
				var NDloadBtn = document.getElementById("NDload");
				var NDloading = document.getElementById("NDloading");
				NDloadBtn.style.display = 'block';
				NDloading.style.display = 'none';
			}

		},
		error: function(e) {
			getPopInfo("网络错误");
		}

	});
}



//调用安卓弹出提示信息方法
function getPopInfo(msg) {
	bridge.alert(msg);
}
//调用安卓返回方法
function AndroidBack() {
	bridge.back();
}