#!/bin/sh
set -eu

classes="${TMPDIR:-/tmp}/games-classes-$$"
sources="${TMPDIR:-/tmp}/games-sources-$$.txt"
trap 'rm -rf "$classes" "$sources"' EXIT
mkdir -p "$classes"

gradle_cache="$HOME/.gradle/caches/modules-2/files-2.1"
jars='
org.junit.jupiter/junit-jupiter-api/6.0.0
org.junit.jupiter/junit-jupiter-engine/6.0.0
org.junit.platform/junit-platform-commons/6.0.0
org.junit.platform/junit-platform-engine/6.0.0
org.junit.platform/junit-platform-launcher/6.0.0
org.apiguardian/apiguardian-api/1.1.2
org.opentest4j/opentest4j/1.3.0
'

classes_cp=$(cygpath -w "$classes")
classpath="$classes_cp"
for artifact in $jars
do
    jar=$(find "$gradle_cache/$artifact" -name '*.jar' ! -name '*sources.jar' | head -n 1)
    if [ -z "$jar" ]
    then
        echo "Missing dependency jar: $artifact" >&2
        exit 1
    fi
    classpath="$classpath;$(cygpath -w "$jar")"
done

find src tst tools -name '*.java' | sort > "$sources"
javac -cp "$classpath" -d "$classes" @"$sources"

java -cp "$classpath" games.JUnitRunner
