#!/bin/sh

VERSION=$(grep version ../pom.xml | head -1 | cut -d'>' -f2 | cut -d'<' -f1)
echo "VERSION: $VERSION"

set -e

pushd ..
mvn package
popd

MVNJAR=../target/dommod-$VERSION-jar-with-dependencies.jar

if [ ! -f $MVNJAR ]; then
    echo "$MVNJAR not found. Exit ..."
    exit 1
fi

TMPDIR=$(mktemp -d)
echo "Building in $TMPDIR"

DIRNAME="dommod-$VERSION"
JARNAME="dommod-$VERSION.jar"

mkdir -p $TMPDIR/$DIRNAME
cp $MVNJAR $TMPDIR/$DIRNAME/$JARNAME
cp ../README.md ../LICENSE dommod.bat dommod $TMPDIR/$DIRNAME
pushd $TMPDIR/$DIRNAME
sed -i "s/JARNAME/$JARNAME/g" dommod dommod.bat
cd $TMPDIR
zip -r $DIRNAME.zip $DIRNAME
popd
mv $TMPDIR/$DIRNAME.zip ../target

