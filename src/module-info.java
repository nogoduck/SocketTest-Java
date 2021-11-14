module Practice {
	requires javafx.controls;
	requires javafx.fxml;
	
	opens application.client to javafx.graphics, javafx.fxml;
	opens application.client300 to javafx.graphics, javafx.fxml;
	opens application.server to javafx.graphics, javafx.fxml;
	opens application.server2 to javafx.graphics, javafx.fxml;


//	opens application to javafx.graphics, javafx.fxml;
}
