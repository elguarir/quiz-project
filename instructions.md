## Installation Instructions

### Step 1: packaging the application into a JAR file using Maven

1. Open a terminal window and navigate to the root directory of the project.
2. Run the following command to package the application into a JAR file:
```bash
mvn clean package
```
3. The JAR file will be created in the `target` directory.


### Step 2: running the application

1. Open a terminal window and run the following command to start the application:
```bash
java --module-path ".\lib\javafx\lib" --add-modules javafx.controls,javafx.fxml,javafx.web,javafx.swing -jar out/artifacts/quiz_project_jar/quiz-project.jar
```