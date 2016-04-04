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
	
	oldPackageName=${OLD_PACKAGE_NAME}

	#替换旧的应用名称和包名为新的
	sed -i s/${OLD_APP_NAME}/$1/g `grep ${OLD_APP_NAME} -rl ./`
	
	echo "1、-----------------------"
	
	#
        oldStr=${oldPackageName//./\\.}
	echo "oldStr = $oldStr"
	sed -i s/"${oldStr}"/"$2"/g `grep "${oldStr}" -rl ./`
	
	echo "2、-----------------------"
	
	#
	oldStr=${oldPackageName//./_}
 	newStr=$2
 	newStr=${newStr//./_}
	echo "oldStr = $oldStr"
        echo "newStr = $newStr"
	sed -i s/"${oldStr}"/"${newStr}"/g `grep "${oldStr}" -rl ./`
	
	echo "3、-----------------------"
	
	#	
	oldPath_=${oldPackageName//.//}
	oldPath=${oldPackageName//./\\/}
	newPath=$2
	newPath_=${newPath//.//}
	newPath=${newPath//./\\/}
	echo "oldPath = $oldPath"
        echo "newPath = $newPath"

	sed -i s/"${oldPath}"/"${newPath}"/g `grep "${oldPath}" -rl ./`
	
	echo "4、-----------------------"
	
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

	mv src/main/java/${oldPath_}/* src/main/java/${newPath_}
	
	echo "5、-----------------------"

        cd src/main
        cp -r libs libs.bak
        ndk-build
        rm -f `find libs/ -name "gdbserver"`
        rm -f `find libs/ -name "gdb.setup"`
        rm -rf obj/
        mv libs libs.bak2

        mv libs.bak libs
        cp -r libs.bak2/* libs
	rm -rf libs.bak2
else
	echo 'please input new APP_NAME and PACKAGE_NAME'
fi
