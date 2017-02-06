#! /bin/sh

#Runs the program in tracing mode
#set -x

logFile=dirList.txt;
patchFile=patches.txt;
workingDirectory=$(pwd);

#Checks whether the parameter passed is Directory or Not 
function isDir
{
	if [ -d $1 ];
	then
		echo $1 | tee -a ./$patchFile;
	fi		
}

#Function that reads the file and checks for directory existence
function readFile
{
	while IFS= read line
	do
		isDir $line;
	done < "$1"	
}

function apply
{
	echo $1;
	cd $1/;
	. $workingDirectory/$1/patch.properties;
	jar=$(echo $jars);
	echo $jar;
	echo $$jar;
	pwd;
	#cp $jar $($jar);
	exit;
}

#Installs the Patch
function install
{
	ls $workingDirectory > $logFile;
	readFile $logFile;
	echo -n "Enter the Patch name to be applied: ";
	read patchName;
	while IFS= read line
	do
		if [ $patchName = $line ]
		then
			echo "Patch found. Applying the Patch";
			apply $patchName;
		else
			echo -n;
		fi
	done < "$patchFile"
	echo "The Patch name you have entered is not valid. Please check above and apply";
}

$1
rm -f $logFile;
rm -f $patchFile;

#echo -n "Enter the directory: "
#read -s input