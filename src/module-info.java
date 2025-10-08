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

    opens View to javafx.graphics;  
    opens Model to javafx.base;
    
    opens Database;

    exports View;
    exports Model;
    exports Controller;
}
