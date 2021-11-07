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

//注册协议确认
$(function() {
	//手机号
	$("#phone").on("blur",function(){
		let phone = $.trim($("#phone").val())
		if(phone === null || phone === undefined || phone === ""){
			showError("phone","必须填写手机号");
		} else if( phone.length != 11){
			showError("phone","手机号格式不正确");
		} else if( !/^1[1-9]\d{9}$/.test(phone) ){
			showError("phone","手机号格式不正确");
		} else {
			//手机号格式正确
			showSuccess("phone");
			//发起ajax，查询手机号能否注册
			$.ajax({
				url:globalContextPath+"/phone/registed",
				method:"get",
				async : false,
				data:{
				  phone: phone
				},
				dataType:"json",
				success:function(data){
					if(data.succ){
						showSuccess("phone");
					} else {
						alert("处理结果：错误码："+data.code + "-"+data.msg)
					}
				},
				error:function(){
					alert("注册请求失败，请稍后重试");
				}
			})
		}
	})

	//密码
	$("#loginPassword").on("blur",function(){
		let pwd = $.trim( $("#loginPassword").val() );

		if( pwd === undefined || pwd === null || pwd === ""){
			showError("loginPassword","必须输入密码");
		} else if( pwd.length < 6 || pwd.length > 20){
			showError("loginPassword","密码是6-20位字符");
		} else if( !/^[0-9a-zA-Z]+$/.test(pwd) ){
			showError("loginPassword","密码只能使用数字和大小写英文字母");
		} else if( !/^(([a-zA-Z]+[0-9]+)|([0-9]+[a-zA-Z]+))[a-zA-Z0-9]*/.test(pwd)){
			showError("loginPassword","密码必须数字和英文字母的组合");
		} else {
			showSuccess("loginPassword")
		}
	})

	//验证码文本框
	$("#messageCode").on("blur",function(){
		let code = $.trim( $("#messageCode").val() );
		if( code === undefined || code === null || code === ""){
			showError("messageCode","必须输入验证码")
		} else if( code.length !=6){
			showError("messageCode","验证码是6位字符")
		} else {
			showSuccess("messageCode");
		}

	})


	//发送验证的按钮
	$("#messageCodeBtn").on("click",function(){
		let btnObject = $("#messageCodeBtn");

		//控制不能重复倒计时
		if( btnObject.hasClass("on")){
			return;
		}

		 //验证手机号
		$("#phone").blur();
		let err = $("#phoneErr").text();
		if( err === ""){
			/*手机号格式正确,验证码处理
			  1.按钮的外观有改变
			  2.按钮上的文字有倒计时
			 */
			//给按钮加入样式 on
			btnObject.addClass("on");
			//倒计时的秒
			let second = 0;
			$.leftTime(5,function(d){
				second  = parseInt(d.s);
				if(second == 0){
					btnObject.text("获取验证码");
					btnObject.removeClass("on")
				} else {
					btnObject.text(second+"秒后获取");
				}
			})

			//ajax发送短信请求
			$.ajax({
				url:globalContextPath +"/sms/sendCode",
				type:"get",
				data:{
					phone: $.trim( $("#phone").val() )
				},
				dataType:"json",
				success:function(resp){
					if( resp.succ == false){
						alert(resp.msg)
					}
				},
				error:function(){
					alert("请求稍后重试");
				}
			})


		}
	})


	//注册按钮
	$("#btnRegist").on("click",function(){
		 //检查数据
		$("#phone").blur();
		$("#loginPassword").blur();
		$("#messageCode").blur();

		let err = $("[id $='Err']").text();
		if(err == ""){
			//实现注册功能
			let phone = $.trim( $("#phone").val() )
			let passwd = $.trim( $("#loginPassword").val() )
			let code = $.trim( $("#messageCode").val() )

			$.ajax({
				url:globalContextPath + "/user/regist",
				type:"post",
				data:{
					phone:phone,
					passwd: $.md5(passwd),
					code:code
				},
				dataType:"json",
				success:function(resp){
					if( resp.succ ){
						//跳转到实名认证的页面
						window.location.href = globalContextPath+"/user/page/realname";
					} else {
						alert(resp.msg)
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
