package com.fpliu.newton.config;

/**
 * 配置接口
 * @author 792793182@qq.com 2015-09-11.
 */
public interface IConfig {

    /**
     * 获取SSO服务器的地址
     */
    String getServerURLSSO();

    /**
     * 获取全名经纪人服务器的地址
     */
    String getServerURLHouse();

    /**
     * 获取O2O服务器的地址
     */
    String getServerURLO2O();

    /**
     * 获取P2P服务器的地址
     */
    String getServerURLP2P();

    /**
     * 获取图片服务器的地址
     */
    String getServerURLImage();

    /**
     * 获取支付服务器的地址
     */
    String getServerURLPayment();

    /**
     * 获取客服中心的URL
     */
    String getURLCustomerServiceCenter();

    /**
     * 获取商城的首页的URL
     */
    String getURLShop();

    /**
     * 获取商城的URL
     */
    String getURLShopBase();

    /**
     * 获取购物车的URL
     */
    String getURLShoppingTrolley();

    /**
     * 获取订单列表的URL
     */
    String getURLShoppingOrderList(int index);

    /**
     * 获取关于页面
     */
    String getURLAbout();

    /**
     * 获取首页的欢迎语
     */
    String getIndexWelcomeText();

    /**
     * 获取App第二版首页URL
     * @return
     */
    String getAppSecondIndexBaseUrl();

    /**
     * 获取App第二版首页H5的URL
     * @return
     */
    String getAppSecondIndexHTML5Url();
    /**
     * 获取第二版商城首页URL
     *
     */
    String getShopSecondBaseUrl();

    /**
     * 获取第二版商城商品详情HTML5页面的URL*
     */
    String getShopSecondHTML5Url();

    /**
     * 获取第二版商城某种商品列表的HTML5页面的URL*
     * @return
     */
    String getShopSecondGoodsListHTML5Url();

    /** */
    public static final String WEIXIN_AppID = "wxa92b8b98594c4f29";

    public static final String WEIXIN_AppSecret = "5a04044a6806ad13566942bc634e91b5";
}
