great now this command runs successuflly :

java --module-path ".\lib\javafx\lib" --add-modules javafx.controls,javafx.fxml,javafx.web,javafx.swing -jar target/qu iz-project-1.0-SNAPSHOT-jar-with-dependencies.jar

now I want to copy all files in this directory to a linux/ubuntu based image with openjdk installed, and install mvn, then run mvn package and run the command above make sure to properly format the path since on windows we use \ instead /