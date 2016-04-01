package com.fpliu.newton.config;

/**
 * 测试环境的配置
 *
 * @author 792793182@qq.com 2015-09-11.
 */
final class DebugConfig extends CommonConfig {

    /**
     * 获取SSO服务器的地址
     */
    @Override
    public String getServerURLSSO() {
//        return "http://192.168.3.46:8006";
        return "http://192.168.3.43:8006";
    }

    /**
     * 获取全名经纪人服务器的地址
     */
    @Override
    public String getServerURLHouse() {
//        return "http://192.168.3.88:8080";
        return "http://192.168.3.41";
    }

    /**
     * 获取O2O服务器的地址
     */
    @Override
    public String getServerURLO2O() {
//        return "http://192.168.3.86";
        return "http://192.168.3.40";
    }

    /**
     * 获取P2P服务器的地址
     */
    @Override
    public String getServerURLP2P() {
//        return "http://192.168.3.89:8080";
        return "http://192.168.3.35:8080";
    }

    /**
     * 获取图片服务器的地址
     */
    @Override
    public String getServerURLImage() {
//        return "http://192.168.3.87";
        return "http://192.168.3.43";
    }

    /**
     * 获取支付服务器的地址
     */
    @Override
    public String getServerURLPayment() {
//        return "http://192.168.3.35:8888/";
        return "http://192.168.3.35:9080/";
    }

    @Override
    public String getURLShop() {
        return getServerURLO2O()+":8080/shop/index.html";
    }

    @Override
    public String getURLShopBase() {
        return getServerURLO2O()+":8080/";
    }

    @Override
    public String getURLShoppingTrolley() {
        return getServerURLO2O()+":8080/mall/shopH5/goodsCart.html?xljFrom=1";
    }

    @Override
    public String getURLShoppingOrderList(int index) {
        return getServerURLO2O()+":8080/mall/shopH5/orderList.html?tab=" + index;
    }

    @Override
    public String getShopSecondBaseUrl() {
        return getServerURLO2O()+":8080/mall/mallindex.do";
    }

    @Override
    public String getShopSecondHTML5Url() {
        return getServerURLO2O()+":8080/mall/shopH5/goodsDetail.html?goodsId=";

    }

    @Override
    public String getShopSecondGoodsListHTML5Url() {
        return getServerURLO2O()+":8080/mall/shopH5/goodsList.html?";
    }

    @Override
    public String getAppSecondIndexBaseUrl() {
        return getServerURLO2O()+":8080/mall/appindex.do";
    }

    @Override
    public String getAppSecondIndexHTML5Url() {
        return getServerURLO2O()+":8080/mall/shopH5/goodsDetail.html?goodsId=";
    }
}
