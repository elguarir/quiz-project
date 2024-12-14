module org.quizproject.quizproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;

    opens org.quizproject.quizproject to javafx.fxml;
    opens org.quizproject.quizproject.Controllers to javafx.fxml;
    opens org.quizproject.quizproject.Models to javafx.base;
    
    exports org.quizproject.quizproject;
    exports org.quizproject.quizproject.Controllers;
    exports org.quizproject.quizproject.Models;
    exports org.quizproject.quizproject.Dao;
    exports org.quizproject.quizproject.Sockets;
}