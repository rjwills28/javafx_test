# JavaFX Tests


## Prerequisites
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
  export PATH_TO_JCA=/path/to/jca/target/jca-2.X.XX-SNAPSHOT.jar
  ```

## Usage
- Clone.

### JavaFX with internal updating widgets
The following applciation will create 10 windows each with 20 text widgets that are updating their value at 10Hz with an internal thread.

- Compile:
  ```
  javac --module-path $PATH_TO_FX --add-modules javafx.controls TestWindowApp.java
  ```
- Run:
  ```
  java --module-path $PATH_TO_FX --add-modules javafx.controls TestWindowApp
  ```
- Monitor the CPU with the following helper script. Obtain the process ID (`<PID>`) for the Java app and run (in a separate terminal):
  ```
  ./cpu_monitor.sh -p <PID>
  ```
  
### JavaFX App with updates from EPICS
The following applciation will create 10 windows each with 20 text widgets each attached to a PV monitor. These widgets will update as soon as the value
of the PV changes. For this test that will be at a rate of 10Hz.

- First create a DB file to run with EPICS that contains 200 PVs updating at 10Hz between the values 0 and 1. This script creates the file `performanceTestDb.db`.
  ```
  ./create_db.sh -n 200
  ```
- Run this IOC:
  ```
  softIoc -d performanceTestDb.db
  ```
- Compile the JavaFX (in a separate terminal)
  ```
  javac -cp .:$PATH_TO_JCA -p $PATH_TO_FX --add-modules javafx.controls TestJcaWindowApp.java
  ```
- Run:
  ```
  java -cp .:$PATH_TO_JCA -p $PATH_TO_FX --add-modules javafx.controls TestJcaWindowApp
  ```
- Monitor the CPU with the following helper script. Obtain the process ID (`<PID>`) for the Java app and run (in a separate terminal):
  ```
  ./cpu_monitor.sh -p <PID>
  ```

### JavaFX App with an intensive CPU task
This is a sanity check. The following applciation will create 10 windows each with 20 text widgets that are updating their value at 10Hz with an internal thread.
There is also a CPU intensive task running on a separate thread. This is just to test that this task does not interfere with the JavaFX updates.

- Compile:
  ```
  javac --module-path $PATH_TO_FX --add-modules javafx.controls TestHighCPUWindowApp.java
  ```
- Run:
  ```
  java --module-path $PATH_TO_FX --add-modules javafx.controls TestHighCPUWindowApp
  ```
