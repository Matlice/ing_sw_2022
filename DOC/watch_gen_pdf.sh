chsum1=""

while [[ true ]]
do
    chsum2=`find ./ -name "*.tex" -type f -exec md5sum {} \;`
    if [[ $chsum1 != $chsum2 ]] ; then           
        if [ -n "$chsum1" ]; then
            sh ./gen_pdf.sh 1
        fi
        chsum1=$chsum2
    fi
    sleep 2
done
