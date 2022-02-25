#!/bin/bash

set -x

[ -d ./bin ] || mkdir ./bin
[ -f ./bin/plantuml.jar ] || wget https://github.com/plantuml/plantuml/releases/download/v1.2022.1/plantuml-1.2022.1.jar -O ./bin/plantuml.jar

export PLANTUML_JAR=./bin/plantuml.jar

TIMES=1
if [ $# -eq 1 ]; then
    TIMES=$1
fi

for i in $(seq $TIMES); do
    lualatex -etex -parse-first-line -shell-escape -halt-on-error --interaction=nonstopmode main.tex 
done
