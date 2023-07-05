window.eruptSiteConfig = {
    //erupt接口地址，在前后端分离时指定
    domain: "",
    //附件地址，一般情况下不需要指定，如果自定义对象存储空间，则需在此指定附件资源访问地址
    fileDomain: "http://rlsv4aala.hn-bkt.clouddn.com",
    //标题
    title: "后台管理系统",
    //描述
    desc: "用心做好每一件事",
    //是否展示版权信息
    copyright: false,
    //高德地图api key,使用地图组件须指定此属性，amapKey获取地址：https://lbs.amap.com (服务平台为：Web端(JS API))
    amapKey: "6ba79a8d",
    //logo路径
    logoPath: "https://android-class.oss-cn-hangzhou.aliyuncs.com/20221126030803.png",
    //logo文字
    logoText: "后台管理系统",
    //注册页地址(仅是一个链接，需要自定义实际样式)
    registerPage: "",
    //自定义导航栏按钮，配置后将会出现在页面右上角
    r_tools: [
        {
            text: "Github后台代码仓库",
            icon: "fa-github",
            mobileHidden: true,
            click: function (event) {
                // 打开新标签页
                window.open("https://github.com/yxr2333/cloud-knowledge");
            }
        },
        {
            text: "Github安卓APP代码仓库",
            icon: "fa-github-alt",
            mobileHidden: true,
            click: function (event) {
                // 打开新标签页
                window.open("https://github.com/yxr2333/second-sell-react-native");
            }
        }
    ],
    //登录成功事件
    login: function (user) {

    },
    //注销事件
    logout: function (user) {

    }
};

//路由回调函数
window.eruptRouterEvent = {
    //key表示要监听的路由切换地址，为url hash地址最后一段
    //例如：http://www.erupt.xyz:9999/#/build/table/demo中demo为回调key
    demo: {
        //路由载入事件
        load: function (e) {

        },
        //路由退出事件
        unload: function (e) {

        }
    },
    //$ 为全路径通配符，在任何路由切换时都会执行load与unload事件
    $: {
        load: function (e) {

        },
        unload: function (e) {
        }
    }
};

//erupt生命周期函数
window.eruptEvent = {
    //页面加载完成后回调
    startup: function () {

    }
}