#!/bin/bash

set -x

[ -d ./bin ] || mkdir ./bin
[ -f ./bin/plantuml.jar ] || wget https://github.com/plantuml/plantuml/releases/download/v1.2022.1/plantuml-1.2022.1.jar -O ./bin/plantuml.jar

export PLANTUML_JAR=./bin/plantuml.jar

TIMES=1
if [ $# -eq 1 ]; then
    if [ $1 == 'clean' ]; then
        rm -r compiled-puml
        find . -name '*.aux' -exec rm -v {} \;
        find . -name '*.toc' -exec rm -v {} \;
        find . -name '*.pdf' -exec rm -v {} \;
        find . -name '*.lof' -exec rm -v {} \;
        find . -name '*.lot' -exec rm -v {} \;
        find . -name '_markdown_main/' -exec rm -v {} \;
        find . -name '*.md.tex' -exec rm -v {} \;
        find . -name '*.markdown.lua' -exec rm -v {} \;
        find . -name '*.fdb_latexmk' -exec rm -v {} \;
        find . -name '*.fls' -exec rm -v {} \;
        find . -name '*.pytxcode' -exec rm -v {} \;
        exit
    fi
    TIMES=$1
fi

for i in $(seq $TIMES); do
    lualatex -etex -parse-first-line -shell-escape -halt-on-error --interaction=nonstopmode main.tex 
done
