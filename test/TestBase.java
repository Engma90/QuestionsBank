import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.util.ResourceBundle;
import java.util.concurrent.TimeoutException;

public abstract class TestBase extends ApplicationTest {
    private Stage primaryStage;
    protected static ResourceBundle bundle;

    @BeforeClass
    public static void setupHeadlessMode() {

        if (Boolean.getBoolean("headless")) {
            System.setProperty("testfx.robot", "glass");
            System.setProperty("testfx.headless", "true");
            System.setProperty("prism.order", "sw");
            System.setProperty("prism.text", "t2k");
            System.setProperty("java.awt.headless", "true");
        }

        bundle = ResourceBundle.getBundle("Bundle");
    }

    @Before
    public  void beforeEachTest() throws Exception {
        /*
         *  The following FxToolkit lines allow for indirectly performing
         *  ApplicationTest.launch(Main.class); and registering the primary stage
         *  in order to allow running multiple @Test in a single file
         */
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(Main.class);
    }

    @After
    public void atferEachTest() throws TimeoutException {
        /*
         *   used here to clear all the possible key or mouse events that are still
         * in progress at the end of each unit test. The hideStage operation
         * closes the GUI after each test.
         */
        FxToolkit.hideStage();
        release(new KeyCode[] {});
        release(new MouseButton[] {});
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        stage.show();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void ensureEventQueueComplete(){
        WaitForAsyncUtils.waitForFxEvents(1);
    }

    public static ResourceBundle getBundle() {
        return bundle;
    }

    /* Helper method to retrieve Java FX GUI components. */
    public <T extends Node> T find(final String query) {
        return (T) lookup(query).queryAll().iterator().next();
    }
}
