# XX_for_Android_AS
这是一个使用Gradle进行构建的以Android Studio为IDE的Android快速开发原型框架

## 1、下载源码
```
git clone https://github.com/leleliu008/Newton_for_Android_AS.git
```
## 2、进入源码目录
```
cd Newton_for_Android_AS
```
## 3、替换成自己的应用名和包名
```
./change.sh XXX_for_Android com.xxx.yyy
```
## 4、修改local.properties中的AndroidSDK和NDK的配置
```
ndk.dir=/your/android/ndk/path
sdk.dir=/your/android/sdk/path
```
## 5、构建
```
./tool.sh build debug    //构建测试环境的包
./tool.sh build release  //构建生产环境的包
```
## 注意
版本名请在build.gradle中修改，不要在AndroidManifest.xml中修改。<br/>

tool.sh这个脚本比较强大，提供了很多实用的功能，具体使用请查看帮助：tool.sh -h或者tool.sh --help
