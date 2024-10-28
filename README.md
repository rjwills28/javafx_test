JAVAFX Test
===========

Requirements
------------
- Requires Java and `JAVA_HOME` to be set, e.g. for Fedora 40:
  ```
  JAVA_HOME=/usr/lib/jvm/java-21-openjdk-21.0.4.0.7-2.fc40.x86_64/
  ```
- Requires JavaFX:
  ```
  export PATH_TO_FX=/path/to/javafx-sdk-XX.XX.XX/lib
  ```
- Requires JCA:
  ```
  git clone git@github.com:epics-base/jca.git
  cd jca
  mvn install -DskipTests
  export PATH_TO_JCA=/home/rjaw/javafx/jca/target/jca-2.4.11-SNAPSHOT.jar
  ```

Compile
-------
```
javac --module-path $PATH_TO_FX --add-modules javafx.controls TestWindowApp.java
javac -cp .:$PATH_TO_JCA -p $PATH_TO_FX --add-modules javafx.controls TestJcaWindowApp.java
```
Run
---
```
java --module-path $PATH_TO_FX --add-modules javafx.controls TestWindowApp
java -cp .:$PATH_TO_JCA -p $PATH_TO_FX --add-modules javafx.controls TestJcaWindowApp
```
