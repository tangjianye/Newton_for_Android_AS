# XX_for_Android_AS
这是一个使用Gradle进行构建的以Android Studio为IDE的Android快速开发原型框架

## 1、下载源码
git clone https://github.com/leleliu008/Newton_for_Android_AS.git

## 2、替换成自己的应用名和包名
<code>cd Newton_for_Android_AS<br/>
./change.sh XXX_for_Android com.xxx.yyy</code>

## 3、构建
<code>./tool.sh build debug    //构建测试环境的包<br/>
./tool.sh build release  //构建生产环境的包</code>

<br/>
tool.sh这个脚本比较强大，提供了很多实用的功能，具体使用请查看帮助：tool.sh -h或者tool.sh --help
