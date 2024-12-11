module at.htlleonding.imagediscovery {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens at.htlleonding.imagediscovery to javafx.fxml;
    exports at.htlleonding.imagediscovery;
}