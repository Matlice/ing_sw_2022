#!/bin/bash

set -x

[ -f ./plantuml.jar ] || wget https://github.com/plantuml/plantuml/releases/download/v1.2022.6/plantuml-1.2022.6.jar -O ./plantuml.jar


TIMES=1
if [ $# -eq 1 ]; then
    TIMES=$1
fi

for i in $(seq $TIMES); do
    for f in presentazione.tex presentazione_no_note.tex handout.tex; do
        latexmk -synctex=1 -interaction=nonstopmode -file-line-error -lualatex -outdir=texout/ -shell-escape $f
    done
done
