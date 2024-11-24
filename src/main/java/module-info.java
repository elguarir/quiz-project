module org.quizproject.quizproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;

    opens org.quizproject.quizproject.Authentification to javafx.fxml;

    exports org.quizproject.quizproject.Authentification;
}