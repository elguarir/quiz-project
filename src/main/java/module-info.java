module org.quizproject.quizproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires mysql.connector.j;

    opens org.quizproject.quizproject.Authentification to javafx.fxml;

    exports org.quizproject.quizproject.Authentification;
}