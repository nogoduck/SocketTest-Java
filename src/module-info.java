module Practice {
	requires javafx.controls;
	requires javafx.fxml;
	
	opens application to javafx.graphics, javafx.fxml;
	opens client to javafx.graphics, javafx.fxml;
}
