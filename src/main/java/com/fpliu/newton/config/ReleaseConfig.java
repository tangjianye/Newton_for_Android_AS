package com.fpliu.newton.config;

/**
 * 生产环境的配置
 *
 * @author 792793182@qq.com 2015-09-11.
 */
final class ReleaseConfig extends CommonConfig {

    /**
     * 获取SSO服务器的地址
     */
    @Override
    public String getServerURLSSO() {
//        return "http://182.92.251.195";
        return "http://ssoapi.xinleju.cn";
    }

    /**
     * 获取全名经纪人服务器的地址
     */
    @Override
    public String getServerURLHouse() {
//        return "https://101.200.30.85";
        return "http://qmyxapi.xinleju.cn";
    }

    /**
     * 获取O2O服务器的地址
     */
    @Override
    public String getServerURLO2O() {
        return "http://o2oapi.xinleju.cn";
    }

    /**
     * 获取P2P服务器的地址
     */
    @Override
    public String getServerURLP2P() {
//        return "https://101.200.29.82";
        return "http://p2papi.xinleju.cn";
    }

    /**
     * 获取图片服务器的地址
     */
    @Override
    public String getServerURLImage() {
//        return "https://101.200.229.107";
        return "http://imgapi.xinleju.cn";
    }

    /**
     * 获取支付服务器的地址
     */
    @Override
    public String getServerURLPayment() {
//        return "https://101.200.96.36/";
        return "http://payment.xinleju.cn/";
    }

    @Override
    public String getURLShopBase() {
        //地址可能不对
        return "http://mall.xinleju.cn/";
    }

    @Override
    public String getURLShop() {
        //地址可能不对
        return "http://mall.xinleju.cn" + "/shop/index.html";
    }

    @Override
    public String getURLShoppingTrolley() {
        return "http://mall.xinleju.cn" + "/mall/shopH5/goodsCart.html?xljFrom=1";
    }

    @Override
    public String getURLShoppingOrderList(int index) {
        return "http://mall.xinleju.cn" + "/mall/shopH5/orderList.html?tab=" + index;
    }

    @Override
    public String getShopSecondBaseUrl() {
        return "http://mall.xinleju.cn" + "/mall/mallindex.do";
    }

    @Override
    public String getShopSecondHTML5Url() {
        return "http://mall.xinleju.cn" + "/mall/shopH5/goodsDetail.html?xljFrom=1&goodsId=";
    }

    @Override
    public String getShopSecondGoodsListHTML5Url() {
        return "http://mall.xinleju.cn" + "/mall/shopH5/goodsList.html?";
    }

    @Override
    public String getAppSecondIndexBaseUrl() {
        return "http://mall.xinleju.cn" + "/mall/appindex.do";
    }

    @Override
    public String getAppSecondIndexHTML5Url() {
        return "http://mall.xinleju.cn" + "/mall/shopH5/goodsDetail.html?goodsId=";
    }
}
