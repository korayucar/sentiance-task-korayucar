BASEDIR=$(dirname "$0")
echo "going into "$BASEDIR
pushd $BASEDIR
./gradlew clean build fatJar -x test
java -cp build/libs/sentiance-assignment-koray-ucar.jar xyz.korayucar.sentiance.BackupMasterDataSet  $@
