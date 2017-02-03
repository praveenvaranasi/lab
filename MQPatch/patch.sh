#! /bin/sh

#Checking directory existence

workingDirectory=$(pwd);
echo $workingDirectory;
checkDir=$1;
if [ ! -d $1];
then
	echo "not a directory";
else
	echo "directory";
fi

