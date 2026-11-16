module ni.edu.uam.hibernateorm {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires java.naming;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;

    opens ni.edu.uam.hibernateorm to javafx.fxml;
    opens ni.edu.uam.hibernateorm.controller to javafx.fxml;
    opens ni.edu.uam.hibernateorm.service to javafx.fxml;
    opens ni.edu.uam.hibernateorm.model to org.hibernate.orm.core, javafx.base;

    exports ni.edu.uam.hibernateorm;
    exports ni.edu.uam.hibernateorm.controller;
    exports ni.edu.uam.hibernateorm.service;
    exports ni.edu.uam.hibernateorm.model;
    exports ni.edu.uam.hibernateorm.repository;
    exports ni.edu.uam.hibernateorm.config;
}
