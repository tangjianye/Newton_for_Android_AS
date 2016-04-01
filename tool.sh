#!/bin/sh

APP_NAME='Newton_for_Android';
PACKAGE_NAME='com.fpliu.newton';

if [ $# = 2 ] ; then
	if [ $1 = 'test' ] ; then
		if [ $2 = 'unit' ] ; then
        		adb shell am instrument -w ${PACKAGE_NAME}.test/android.test.InstrumentationTestRunner
		elif [ $2 = 'monkey' ] ; then
			adb shell monkey -p ${PACKAGE_NAME} -vvv 20000
		fi
	elif [ $1 = 'sonar' ] ; then
		current_date=`date +%Y%m%d`;
		echo current_date=$current_date

		sed -i "s#[0-9]\{8\}#${current_date}#g" sonar-project.properties

		sonar-runner
	elif [ $1 = 'show-md5' ] ; then
		keytool -list -v -keystore ~/.android/debug.keystore -storepass android -alias androiddebugkey
	elif [ $1 = 'build' ] ; then
		MODE=$2;

		current_date=`date +%Y%m%d`;
		echo current_date=$current_date

		cd src/main

		#替换版本号
		sed -i "s#android:versionCode=\"[0-9]\{8\}\"#android:versionCode=\"${current_date}\"#g" AndroidManifest.xml

		#
		if [ $MODE = 'debug' ] ; then
        		sed -i "s#android:debuggable=\"false\"#android:debuggable=\"true\"#g" AndroidManifest.xml
		else
        		sed -i "s#android:debuggable=\"true\"#android:debuggable=\"false\"#g" AndroidManifest.xml
		fi

		#替换环境配置
		sed -i "s#Environment\.[a-z]*#Environment.${MODE}#g" java/com/unionx/yilingdoctor/config/ConfigFactory.java

		#从主工程的AndroidManifest.xml中读取版本信息
		versionName=`cat AndroidManifest.xml | grep 'versionName="[^"]*"' | sed 's/.*versionName="\([^"]*\)".*/\1/'`
		echo $versionName

		cd ../../
		
		apkName=${APP_NAME}_${versionName}_${current_date}_${MODE}.apk;

		./gradlew clean
		if [ $MODE = 'release' ] ; then
        		./gradlew assembleRelease
        
        		if [ $? -eq 0 ] ; then
                		cp build/outputs/apk/main-release.apk ${apkName}
        		else
                		exit
        		fi
		else
        		./gradlew assembleDebug
        
        		if [ $? -eq 0 ] ; then
                		cp build/outputs/apk/main-debug.apk ${apkName}
        		else
                		exit
        		fi
		fi
	fi
fi
