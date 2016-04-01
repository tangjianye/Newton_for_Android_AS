package com.fpliu.newton.config;

/**
 * 演示环境的配置
 *
 * @author 792793182@qq.com 2015-09-11.
 */
final class DemoConfig extends CommonConfig {

    /**
     * 获取SSO服务器的地址
     */
    @Override
    public String getServerURLSSO() {
        return "http://192.168.3.53:8006";
    }

    /**
     * 获取全名经纪人服务器的地址
     */
    @Override
    public String getServerURLHouse() {
        return "http://192.168.3.51";
    }

    /**
     * 获取O2O服务器的地址
     */
    @Override
    public String getServerURLO2O() {
        return "http://192.168.3.49";
    }

    /**
     * 获取P2P服务器的地址
     */
    @Override
    public String getServerURLP2P() {
        return "http://192.168.3.48:8080";
    }

    /**
     * 获取图片服务器的地址
     */
    @Override
    public String getServerURLImage() {
        return "http://192.168.3.59";
    }

    /**
     * 获取支付服务器的地址
     */
    @Override
    public String getServerURLPayment() {
        return null;
    }

    @Override
    public String getURLShop() {
        return "http://192.168.3.40/shop/index.html";
    }

    @Override
    public String getURLShopBase() {
        return "";
    }

    @Override
    public String getURLShoppingTrolley() {
        return "http://192.168.3.40:8080/mall/shopH5/orderConfirm.html";
    }

    @Override
    public String getURLShoppingOrderList(int index) {
        return "http://192.168.3.40:8080/mall/shopH5/orderList.html?tab=" + index;
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
    public String getAppSecondIndexBaseUrl() {
        return getServerURLO2O()+":8080/mall/appindex.do";
    }

    @Override
    public String getAppSecondIndexHTML5Url() {
        return getServerURLO2O()+":8080/mall/shopH5/goodsDetail.html?goodsId=";
    }

    @Override
    public String getShopSecondGoodsListHTML5Url() {
        return getServerURLO2O()+":8080/mall/shopH5/goodsList.html?";
    }
}
