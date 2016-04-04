#!/bin/bash

APP_NAME='Newton_for_Android';
PACKAGE_NAME='com.fpliu.newton';

if [ $# = 1 ] ; then
	if [ $1 = "-h" ] || [ $1 = "--help" ] ; then
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
	fi
elif [ $# = 2 ] ; then
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
	elif [ $1 = 'check' ] ; then
		if [ $2 = 'version' ] ; then
			aapt dump badging *.apk | grep "version"
		fi
	elif [ $1 = 'install' ] ; then
		adb install -r *.apk
	elif [ $1 = 'uninstall' ] ; then
		adb uninstall com.fpliu.newton
	elif [ $1 = 'build' ] ; then
		MODE=$2;

		current_date=`date +%Y%m%d`;
		echo current_date=$current_date

		#替换版本号
		sed -i "s#versionCode [0-9]\{8\}#versionCode ${current_date}#g" build.gradle

		#
		if [ $MODE = 'debug' ] ; then
        		sed -i "s#android:debuggable=\"false\"#android:debuggable=\"true\"#g" src/main/AndroidManifest.xml
		else
        		sed -i "s#android:debuggable=\"true\"#android:debuggable=\"false\"#g" src/main/AndroidManifest.xml
		fi

		#替换环境配置
		sed -i "s#Environment\.[a-z]*#Environment.${MODE}#g" src/main/java/com/fpliu/newton/config/ConfigFactory.java

		#从主工程的AndroidManifest.xml中读取版本信息
		versionName=`cat build.gradle | grep 'versionName "[^"]*"' | sed 's/versionName "\([^"]*\)".*/\1/'`
		echo ${versionName}
		
		apkName=${APP_NAME}_`echo ${versionName}`_${current_date}_${MODE}.apk
		echo ${apkName}
		
		./gradlew clean
		if [ $MODE = 'release' ] ; then
        		./gradlew assembleRelease
        
        		if [ $? -eq 0 ] ; then
				rm -f *.apk
                		cp build/outputs/apk/$(basename `pwd`)-release.apk ${apkName}
        		else
                		exit
        		fi
		else
        		./gradlew assembleDebug
        
        		if [ $? -eq 0 ] ; then
				rm -f *.apk;
                		cp build/outputs/apk/$(basename `pwd`)-debug.apk ${apkName}
        		else
                		exit
        		fi
		fi
	fi
fi
