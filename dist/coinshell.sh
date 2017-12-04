#!/bin/sh

COINSHELL_JAR=`find . -name 'coinshell-*.jar' -print | head -1`
echo "Using CoinShell jar $COINSHELL_JAR"
java -jar $COINSHELL_JAR $*
