SVNCL="svn2cl-0.12/svn2cl.sh"
SVNURL="http://toxiclibs.googlecode.com/svn/trunk/toxiclibs/src."

while [ $# -gt 0 ]
do
	CMD="$SVNCL -i -a -o ../readme1st/$1/CHANGELOG.txt $SVNURL$1"
	echo "processing $1..."
	eval "$CMD"
	shift
done