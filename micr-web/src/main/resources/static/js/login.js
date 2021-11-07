$(function(){
    //手机号
    $("#phone").on("blur",function(){

        let tip = $("#showId");
        let phone = $.trim($("#phone").val() );
        if( phone === undefined || phone === null || phone === ""){
            tip.text("请输入手机号！！！");
        } else if( phone.length != 11){
            tip.text("手机号格式不正确！！！")
        } else if( !/^1[1-9]\d{9}$/.test(phone) ) {
            tip.text("手机号格式不正确");
        } else {
            tip.text("")
        }
    })

    //密码
    $("#loginPassword").on("blur",function(){
        let tip = $("#showId");
        let pwd = $.trim( $("#loginPassword").val() );

        if( pwd === undefined || pwd === null || pwd === ""){
            tip.text("必须输入密码");
        } else if( pwd.length < 6 || pwd.length > 20){
            tip.text("密码是6-20位字符");
        } else if( !/^[0-9a-zA-Z]+$/.test(pwd) ){
            tip.text("密码只能使用数字和大小写英文字母");
        } else if( !/^(([a-zA-Z]+[0-9]+)|([0-9]+[a-zA-Z]+))[a-zA-Z0-9]*/.test(pwd)){
            tip.text("密码必须数字和英文字母的组合");
        } else {
            tip.text("");
        }
    })

    //登录按钮
    $("#loginId").on("click",function(){
        //验证数据
        $("#phone").blur();
        $("#loginPassword").blur();
        //获取showId的text
        let err = $("#showId").text();
        if( err === ""){
            let phone = $.trim( $("#phone").val() );
            let passwd  = $.trim( $("#loginPassword").val() );

            //准备ajax的请求
            $.ajax({
                url: globalContextPath +"/user/login",
                type: "post",
                data:{
                    phone:phone,
                    passwd: $.md5(passwd)
                },
                dataType:"json",
                success:function(resp){
                    if(resp.succ ){
                        //返回到来源位置
                        window.location.href = $("#returnUrl").val();
                    } else {
                        alert(resp.msg);
                    }
                },
                error:function(){
                    alert("请求失败，稍后重试");
                }

            })
        }

    })
})