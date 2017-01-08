#!/bin/bash

#------------------------------------------------------#

appName='newton_for_Android';
packageName='com.fpliu.newton';

keystorePath=keystore.jks
keystoreAlias=androiddebugkey
keystorePassword=android

svnApkRepoUrl=
svnTagRepoUrl=
svnBranchRepoUrl=

#------------------------------------------------------#

currentDate=`date +%Y%m%d`;
echo "currentDate=$currentDate"

osType=`uname -s`
echo "osType=$osType"

#更新Android SDK
function updateAndroidSDK() {
    echo y | android update sdk -u -a -t android-23
    echo y | android update sdk -u -a -t platform-tools
    echo y | android update sdk -u -a -t build-tools-23.0.2
    echo y | android update sdk -u -a -t extra-android-m2repository 
}

#执行单元测试
function testWithUnit() {
    adb shell am instrument -w ${packageName}.test/android.test.InstrumentationTestRunner
}

#执行Monkey测试
function testWithMonkey() {
    adb shell monkey -p ${packageName} -vvv 20000
}

#执行SonarQube代码扫描
function runSonarScanner() {
    if [ $osType = 'Darwin' ] ; then
    	sed -i ""  "s#[0-9]\{8\}#${currentDate}#g" sonar-project.properties
    else
    	sed -i "s#[0-9]\{8\}#${currentDate}#g" sonar-project.properties
    fi

    sonar-runner
}

#安装编译好的应用
function installApk() {
    adb install -r *.apk
}

#卸载当前设备上的应用
function uninstallApk {
    adb uninstall ${packageName}
}

#显示签名的MD5
function showMD5() {
keytool -list -v -keystore ${keystorePath} -storepass ${keystorePassword} -alias ${keystoreAlias}
}

#给Apk签名
function signApk {
    apkName=`ls *.apk`
    apkName2=$(basename ${apkName} .apk)
    echo "apkName=${apkName}"
    if [ $? -eq 0 ] ; then
    	jarsigner -verbose -keystore ${keystorePath} -storepass ${keystorePassword} -digestalg SHA1 -sigalg MD5withRSA -sigfile CERT -signedjar ${apkName2}_signed.apk ${apkName} ${keystoreAlias}
    fi
}

#对Apk进行字节对齐优化
function alignApk() {
    apkName=`ls *_signed.apk`
    zipalign -v 4 ${apkName} $(basename ${apkName} .apk)_aligned.apk
}

#显示Apk的版本名称
function showApkVersion() {
    aapt dump badging *.apk | grep "version"
}

#打开Genymotion模拟器
function openGenymotion() {
    player --vm-name "Google Nexus 5 - 4.4.4 - API 19 - 1080x1920" &
}

#SVN操作
function runSVN() {
    if [ $1 = 'getApkRepoUrl' ] ; then
    	echo "svnApkRepoUrl=$svnApkRepoUrl"
    elif [ $1 = 'getTagRepoUrl' ] ; then
        echo "svnTagRepoUrl=$svnTagRepoUrl"
    elif [ $1 = 'getBranchRepoUrl' ] ; then
        echo "svnBranchRepoUrl=$svnBranchRepoUrl"
    elif [ $1 = 'listApkRepo' ] ; then
        svn ls $svnApkRepoUrl
    elif [ $1 = 'listTagRepo' ] ; then
        svn ls $svnTagRepoUrl
    elif [ $1 = 'listBranchRepo' ] ; then
        svn ls $svnBranchRepoUrl
    elif [ $1 = 'uploadApk' ] ; then
        for apkFile in `ls *.apk`
	do
            remoteApk=${svnApkRepoUrl}/${apkFile}
    	    echo "remoteApk=$remoteApk"

    	    svn info ${remoteApk} > /dev/null 2> /dev/null
    	    if [ $? -eq 0 ] ; then
    		svn delete ${remoteApk} -m "re commit, so delete"
    		echo ${apkFile}" exsit at remote, re commit, so delete"
    	    fi

    	    svn import ${apkFile} ${remoteApk} -m "commit"
    	    echo "import success"
	done
    elif [ $1 = 'makeTag' ] ; then
        echo "-------------------clean begin---------------------"
        rm -f `find ./ -name "*.iml"`
        rm -f `find ./ -name "build"`
        rm -rf .gradle/
        rm -f *.apk
        echo "-------------------clean end---------------------"

        read -p "please input tagName:" tagName

        echo "-------------------upload begin---------------------"
        svn import . ${svnTagRepoUrl}/${tagName} -m "commit" --no-ignore
        echo "-------------------upload end---------------------"
    else
    	echo "$1 not support"
    fi
}

#替换环境 取值为 debug | release | develop | demo
function changeEnvTo() {
    MODE=$1;

    echo "ANDROID_HOME="${ANDROID_HOME}

    #替换Android SDK路径
    if [ $osType = 'Darwin' ] ; then
        sed -i ""  "s#sdk\.dir=.*#sdk.dir=${ANDROID_HOME}#g" `find ./  -name "local.properties"`
    else
        sed -i "s#sdk\.dir=.*#sdk.dir=${ANDROID_HOME}#g" `find ./  -name "local.properties"`
    fi

    #替换版本号
    if [ $osType = 'Darwin' ] ; then
        sed -i  ""  "s#versionCode [0-9]\{8\}#versionCode ${current_date}#g" build.gradle
    else
        sed -i "s#versionCode [0-9]\{8\}#versionCode ${current_date}#g" build.gradle
    fi


    #
    if [ $MODE = 'debug' ] ; then
        if [ $osType = 'Darwin' ] ; then
            sed -i  ""  "s#android:debuggable=\"false\"#android:debuggable=\"true\"#g" src/main/AndroidManifest.xml
        else
            sed -i "s#android:debuggable=\"false\"#android:debuggable=\"true\"#g" src/main/AndroidManifest.xml
        fi
    else
        if [ $osType = 'Darwin' ] ; then
            sed -i  ""  "s#android:debuggable=\"true\"#android:debuggable=\"false\"#g" src/main/AndroidManifest.xml
        else
            sed -i "s#android:debuggable=\"true\"#android:debuggable=\"false\"#g" src/main/AndroidManifest.xml
        fi
    fi

    packagePath=${packageName//.//}
    echo "packagePath = $packagePath"

    #替换环境配置
    if [ $osType = 'Darwin' ] ; then
        sed -i  ""  "s#Environment\.[a-z]*#Environment.${MODE}#g" src/main/java/${packagePath}/config/ConfigFactory.java
    else
        sed -i "s#Environment\.[a-z]*#Environment.${MODE}#g" src/main/java/${packagePath}/config/ConfigFactory.java
    fi


    #从主工程的AndroidManifest.xml中读取版本信息
    versionName=`cat build.gradle | grep 'versionName "[^"]*"' | sed 's/versionName "\([^"]*\)".*/\1/'`
    echo ${versionName}

    apkName=${appName}_`echo ${versionName}`_${currentDate}_${MODE}.apk
    echo ${apkName}
}

#执行编译
function runBuild() {
    MODE=$1;

    changeEnvTo $MODE

    ./gradlew clean
    if [ $MODE = 'release' ] ; then
        ./gradlew assembleRelease

        if [ $? -eq 0 ] ; then
    		rm -f *.apk
            cp build/outputs/apk/$(basename `pwd`)-release.apk ${apkName}
        else
            exit 1
        fi
    else
        ./gradlew assembleDebug

        if [ $? -eq 0 ] ; then
    		rm -f *.apk;
            cp build/outputs/apk/$(basename `pwd`)-debug.apk ${apkName}
        else
            exit 1
        fi
    fi
}


function showHelp() {
    echo "Usage:"
    echo "tool.sh <sub-command> <action>"
    echo ""
    echo "sub-command:"
    echo "test build check install uninstall show-md5"
    echo ""
    echo "examples:"
    echo "tool.sh test unit      execute unit test"
    echo "tool.sh test monkey    execute monkey test"
    echo "tool.sh sonar sonar    execute sonar scanner"
    echo "tool.sh show-md5       show your keystore's md5"
    echo "tool.sh build debug    build debug environment apk"
    echo "tool.sh build release  build release environment apk"
    echo "tool.sh install .      install the builded apk"
    echo "tool.sh uninstall .    uninstall my apk"
    echo "tool.sh check version  get the apk versionCode and versionName"
}

#正文开始
if [ $# = 1 ] ; then
	if [ $1 = "-h" ] || [ $1 = "--help" ] ; then
        showHelp
	fi
elif [ $# = 2 ] ; then
	if [ $1 = 'test' ] ; then
		if [ $2 = 'unit' ] ; then
		    testWithUnit
		elif [ $2 = 'monkey' ] ; then
		    testWithMonkey
		fi
	elif [ $1 = 'sonar' ] ; then
		runSonarScanner
	elif [ $1 = 'show-md5' ] ; then
		showMD5
	elif [ $1 = 'check' ] ; then
		if [ $2 = 'version' ] ; then
			showApkVersion
		fi
	elif [ $1 = 'install' ] ; then
		installApk
	elif [ $1 = 'uninstall' ] ; then
		uninstallApk
	elif [ $1 = 'sign' ] ;then
		signApk
	elif [ $1 = 'align' ] ; then
		alignApk
	elif [ $1 = 'genymotion' ] ; then
		openGenymotion
	elif [ $1 = 'svn' ] ; then
        runSVN $2
    elif [ $1 = 'changeEnvTo' ] ; then
         changeEnvTo $2
	elif [ $1 = 'build' ] ; then
		runBuild $2
	fi
fi
