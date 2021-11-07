
//同意实名认证协议
$(function() {
	//姓名（中文）
	$("#realName").on("blur",function(){

		let name = $.trim( $("#realName").val() );
		if( name === undefined || name === null || name === ""){
			showError("realName","必须输入正确的姓名");
		} else if( name.length < 2){
			showError("realName","姓名不正确");
		} else if( !/^[\u4e00-\u9fa5]{0,}$/.test(name)){
			showError("realName","姓名必须是中文");
		} else {
			showSuccess("realName")
		}
	})


	//身份证号
	$("#idCard").on("blur",function(){
		let card = $.trim( $("#idCard").val() );
		if( card === undefined || card === null || card === ""){
			showError("idCard","必须输入身份证号");
		} else if( !/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/.test(card)){
			showError("idCard","身份证号格式不正确");
		} else {
			showSuccess("idCard");
		}
	});

	//按钮
	$("#btnRegist").on("click",function(){
		//检查输入的数据
		$("#realName").blur();
		$("#idCard").blur();

		//判断错误的文本
		let err = $("[id $= 'Err']").text();
		if( err === ""){
			let phone = $("#phone").val();
			let name = $.trim( $("#realName").val());
			let card = $.trim( $("#idCard").val() );
			$.ajax({
				url:globalContextPath+"/user/realname",
				type:"post",
				data:{
					phone:phone,
					name:name,
					card:card
				},
				dataType:"json",
				success:function(resp){
					if( resp.succ ){
						//跳转到用户中心
						window.location.href = globalContextPath +"/user/page/mycenter";
					} else {
						alert(resp.msg);
					}
				},
				error:function(){
					alert("请求错误，稍后重试")
				}
			})

		}
	})


	$("#agree").click(function(){
		var ischeck = document.getElementById("agree").checked;
		if (ischeck) {
			$("#btnRegist").attr("disabled", false);
			$("#btnRegist").removeClass("fail");
		} else {
			$("#btnRegist").attr("disabled","disabled");
			$("#btnRegist").addClass("fail");
		}
	});
});
//打开注册协议弹层
function alertBox(maskid,bosid){
	$("#"+maskid).show();
	$("#"+bosid).show();
}
//关闭注册协议弹层
function closeBox(maskid,bosid){
	$("#"+maskid).hide();
	$("#"+bosid).hide();
}

//错误提示
function showError(id,msg) {
	$("#"+id+"Ok").hide();
	$("#"+id+"Err").html("<i></i><p>"+msg+"</p>");
	$("#"+id+"Err").show();
	$("#"+id).addClass("input-red");
}
//错误隐藏
function hideError(id) {
	$("#"+id+"Err").hide();
	$("#"+id+"Err").html("");
	$("#"+id).removeClass("input-red");
}
//显示成功
function showSuccess(id) {
	$("#"+id+"Err").hide();
	$("#"+id+"Err").html("");
	$("#"+id+"Ok").show();
	$("#"+id).removeClass("input-red");
}