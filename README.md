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

Compile
-------
```
javac --module-path $PATH_TO_FX --add-modules javafx.controls TestWindowApp.java
```
Run
---
```
java --module-path $PATH_TO_FX --add-modules javafx.controls TestWindowApp
```
