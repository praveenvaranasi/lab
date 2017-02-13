#! /bin/sh

# 
#
#
#
#
#

#Runs the program in tracing mode
#set -x;

logFile=dirList.txt;
patchFile=patches.txt;
jarList=jars.txt
workingDirectory=$(pwd);

function test
{
	echo "$1";
}

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
	echo "Applying the Patch: "$1;
	cd $1/;
	. ./patch.properties;
	echo $jars > $jarList;
	oldIFS=$IFS;
	IFS=',';
	readarray jarArray < $jarList;
	echo -n ${jarArray[0]} > $jarList;
	num=$(awk '{print NF}' jars.txt);
	echo $num;
	for (( i=1; i <= $num; i++ ))
	do		
		jar=$(echo ${jarArray[0]} | awk -v var="$i" '{print $var}' );
		test $jar;		
	done	
	IFS=$oldIFS;
	exit;
}

#Installs the Patch
function install
{
	ls $workingDirectory > $logFile;
	
	# Lists the directories in a file, filters the patch files among them-
	# -and keeps the filtered list in a file named 'patches.txt'.
	readFile $logFile;	
	echo -n "Enter the Patch name to be applied: ";
	read patchName;
	
	# Looks for the entered Patch name in the Patches.txt and then proceeds if its there.
	# If its not there, it 'll throw an error message and exits
	# while IFS= read line
	while read line
	do
		if [ $patchName = $line ]
		then
			echo -n "Patch found.";
			apply $patchName;
		else
			echo -n ;
		fi
	done < "$patchFile"
	echo "The Patch name you have entered is not valid. Please check above and apply";
}

$1
rm -f $logFile;
rm -f $patchFile;

#echo -n "Enter the directory: "
#read -s input
# read <Some Variable>: reads the value given in command line and saves the same in 'Some Variable' which can be used in future.
#cat -etv <<<"$IFS";
#read f1 f2 -u $jarList;
#read -u fd $jarList; 
#while read f1 f2 
#do
#	echo $f1;
#	echo $f2;
#done < $jarList;
#declare -a Array;
#Arr=( $(echo ${jarArray[0]}) );
#Array=( $Arr );
#Array=$(${jarArray[0]});
#echo ${Array[1]};
#echo ${Array[1]};
