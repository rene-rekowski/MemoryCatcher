
module MemoryCatcher {
	    requires javafx.controls;
	    requires javafx.fxml;
		requires javafx.graphics;
		requires java.sql;

	    opens view to javafx.fxml;
	    exports view;
	    exports controller;
	    exports model;
	    exports database;
}