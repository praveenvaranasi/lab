create table MIGWCN (CNSVID varchar(40),CNTIME datetime,CNMSG_ decimal,CNSRCA varchar(30),CNFLOC decimal,CNCTID varchar(20),CNTLC_ decimal)
create table Download843Messages(MessageSequence Integer,Packet varchar(50),DownloadTime DATE,Flag TINYINT)
create table Download841Messages(MessageSequence Integer,Packet varchar(50),DownloadTime DATE,Flag TINYINT)
ALTER TABLE PUBLIC.MIGWCN ADD TIF_RECORDID IDENTITY 
ALTER TABLE PUBLIC.MIGWCN ADD TIF_STATUS CHAR(1)