/**
 * 
 */
/**
 * 
 */
module MemoryCatcher {
    requires javafx.controls;
    requires javafx.fxml;

    opens View to javafx.graphics;  
    opens Model to javafx.base;

    exports View;
    exports Model;
    exports Controller;
}
