#SCRIPT=$(readlink -f "$0")
#SCRIPT_PATH=$(dirname "$SCRIPT")
#echo $SCRIPT_PATH

if [ "$UID" -ne 0 ]; then
    echo "You must be root to run this script"
    exit 1
fi

# If we're here, we're supposed to have access. Copy the files over

if [ ! -d "/usr/lib/samtools" ]; then
	# Control will enter here if $DIRECTORY doesn't exist.
	mkdir /usr/lib/samtools
fi

if [ -d "/usr/lib/samtools/samtools-0.1.19.tar.bz2" ]; then
	rm /usr/lib/samtools/samtools-0.1.19.tar.bz2
fi

cp samtools-0.1.19.tar.bz2 /usr/lib/samtools/
cd /usr/lib/samtools
tar -jxf samtools-0.1.19.tar.bz2

cd samtools-0.1.19
make clean
make

/usr/sbin/alternatives --install /usr/bin/samtools samtools /usr/lib/samtools/samtools-0.1.19/samtools 20000


echo 'SamTools program got installed'

exit 0
