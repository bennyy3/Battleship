module battleShip {
	requires javafx.base;
	requires javafx.graphics;
	requires javafx.controls;
	
	opens battleShip to javafx.graphics;
	
}