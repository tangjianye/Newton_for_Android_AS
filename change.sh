#!/bin/bash

#改变APP名称和包名，快速创建新项目
OLD_APP_NAME='Newton_for_Android'
OLD_PACKAGE_NAME='com.fpliu.newton'

ROOT_DIR=$PWD

if [ $# = 2 ] ; then
	if [ $2 = ${OLD_PACKAGE_NAME} ] ; then
		echo 'your new packageName equals to OLD_PACKAGE_NAME'
		exit
	fi
	
	#替换旧的应用名称和包名为新的
	sed -i s/${OLD_APP_NAME}/$1/g `grep ${OLD_APP_NAME} -rl ./`
	sed -i s/${OLD_PACKAGE_NAME}/$2/g `grep ${OLD_PACKAGE_NAME} -rl ./`
	
	oldStr=${OLD_PACKAGE_NAME//./_}
	newStr=$2
	newStr=${newStr//./_}
	echo $oldStr
	echo $newStr
	
	sed -i s/${oldStr}/${newStr}/g `grep ${oldStr} -rl ./`

	
	oldPath=${OLD_PACKAGE_NAME//.//}
	newPath=$2
	newPath=${newPath//.//}
	echo $oldpath
        echo $newpath

	sed -i s/${oldStr}/${newStr}/g `grep ${oldStr} -rl ./`
	
	cd src/main/java
	
	var=$2
	#这里是将var中的.替换为空格
        var=${var//./ }
        for dir in $var
        do
                echo $dir
		if [ ! -d "$dir" ] ; then
			mkdir "$dir"
		fi
		cd "$dir"
        done

	cd $ROOT_DIR

	mv src/main/java/${oldPath}/* src/main/java/${newPath}
else
	echo 'please input new APP_NAME and PACKAGE_NAME'
fi
