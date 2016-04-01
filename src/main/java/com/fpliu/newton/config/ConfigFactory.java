package com.fpliu.newton.config;

/**
 * Config工厂类
 *
 * @author 792793182@qq.com 2015-09-11.
 */
public final class ConfigFactory {

    /**
     * 环境常量，打包的时候根据是哪种环境选择不同的配置
     */
    public enum Environment {
        debug,   //测试环境
        release, //生产环境
        develop, //开发环境
        demo;    //演示环境
    }

    /**
     * 此常量在打包的时候进行修改
     */
    private static final Environment environment = Environment.debug;

    private ConfigFactory() {

    }

    /**
     * 创建IConfig的实例
     */
    public static IConfig newInstance() {
        switch (environment) {
            case debug:
                return new DebugConfig();
            case release:
                return new ReleaseConfig();
            case develop:
                return new DevelopConfig();
            case demo:
                return new DemoConfig();
            default:
                return new DebugConfig();
        }
    }
}
