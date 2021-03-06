/**
 * Created by admin on 2017/3/9.
 */
//javascript模块化
//存放主要交互逻辑js代码
var seckill = {
    //封装秒杀相关的ajax的url
    URL:{
        //获取服务器时间路径
        nowTime : function () {
            return seckill.URL.basePath()+'/seckill/now/time';
        },
        //获取工程路径
        basePath : function () {
            var localObj = window.location;

            var contextPath = localObj.pathname.split("/")[1];

            var basePath = localObj.protocol+"//"+localObj.host+"/"+contextPath;

            var server_context=basePath;
            return server_context;
        }
    },
    //验证手机号
    validatePhone : function (userPhone) {
      if (userPhone && userPhone.length==11 && !isNaN(userPhone)){
            return true;
      }else {
            return false;
      }
    },
    //详情页秒杀逻辑
    detail:{
        //初始化方法
        init : function (params) {

            //手机验证登录，计时交互
            //在cookie中查找手机号
            var userPhone = $.cookie('userPhone');
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            var seckillId = params['seckillId'];
            //验证手机号
            if (!seckill.validatePhone(userPhone)){
                //绑定phone
                //控制输出
                var killPhoneModal=$('#killPhoneModal');
                //显示弹出层
                killPhoneModal.modal({
                    show : true,//显示弹出层
                    backdrop : 'static',//禁止位置关闭
                    keyboard : false //关闭键盘事件，防止ESC关闭弹出层
                });
                //提交事件
                $("#killPhoneBtn").click(function () {
                    var userPhone = $('#killPhoneKey').val();
                    //验证手机号
                    if (seckill.validatePhone(userPhone)){
                        //手机号写入cookie    expires:保存时间 天数   path：/seckill  路径下cookie有效
                        $.cookie('userPhone',userPhone);
                        //刷新页面
                        window.location.reload();
                    }else {
                        $('#killPhoneMessage').hide().html('<label class="label label-danger">手机号码填写错误！</label>').show(300);
                    }
                });
            }
            //已经登录
            //计时交互
            $.get(seckill.URL.nowTime(),{},function (result) {
                console.info(result);
            });

        }
    }

}

