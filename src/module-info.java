/**
 * 
 */
/**
 * 
 */
module MemoryCatcher {
    requires javafx.controls;
    requires javafx.fxml;
	requires java.sql;

    opens view to javafx.graphics;  
    opens model to javafx.base;
    
    opens database;

    exports view;
    exports model;
    exports controller;
}
