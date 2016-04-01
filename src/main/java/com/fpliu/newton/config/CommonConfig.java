package com.fpliu.newton.config;

/**
 * 公共的配置
 *
 * @author 792793182@qq.com 2015-09-11.
 */
abstract class CommonConfig implements IConfig {

    /**
     * 获取客服中心的URL
     */
    @Override
    public String getURLCustomerServiceCenter() {
        return "http://www.xinleju.cn/new/customservice/";
    }

    @Override
    public String getURLAbout() {
        return "http://www.xinleju.cn/new/html/2015/Relatedprotocol_0717/142.html";
    }

    /**
     * 获取首页的欢迎语
     */
    @Override
    public String getIndexWelcomeText() {
        return "您好，欢迎来到鑫乐居！";
    }

}
