#SCRIPT=$(readlink -f "$0")
#SCRIPT_PATH=$(dirname "$SCRIPT")
#echo $SCRIPT_PATH

if [ "$UID" -ne 0 ]; then
    echo "You must be root to run this script"
    exit 1
fi

# If we're here, we're supposed to have access. Copy the files over

apt-get install zlib1g-dev

if [ ! -d "/usr/lib/bwa" ]; then
	# Control will enter here if $DIRECTORY doesn't exist.
	mkdir /usr/lib/bwa
fi

if [ -d "/usr/lib/bwa/bwa-0.7.5a.tar.bz2" ]; then
	rm /usr/lib/bwa/bwa-0.7.5a.tar.bz2
fi

cp bwa-0.7.5a.tar.bz2 /usr/lib/bwa/
cd /usr/lib/bwa
tar -jxf bwa-0.7.5a.tar.bz2

cd bwa-0.7.5a
make clean
make

update-alternatives --install /usr/bin/bwa bwa /usr/lib/bwa/bwa-0.7.5a/bwa 1


echo 'BWA program got installed'

exit 0

#/usr/sbin/alternatives --install /usr/bin/bwa 
