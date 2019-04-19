import controllers.WindowLoader;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import models.DBHandler;
import models.LoginHandler;
import org.junit.After;
import org.junit.BeforeClass;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.util.ResourceBundle;
import java.util.concurrent.TimeoutException;

public abstract class TestBaseSkipLogin extends ApplicationTest {
    private Stage primaryStage;
    protected static ResourceBundle bundle;
    private final static boolean HEADLESS = false;

    @BeforeClass
    public static void setupHeadlessMode() {

        //if (Boolean.getBoolean("headless")) {
        if(HEADLESS) {
            System.setProperty("testfx.robot", "glass");
            System.setProperty("testfx.headless", "true");
            System.setProperty("prism.order", "sw");
            System.setProperty("prism.text", "t2k");
            System.setProperty("java.awt.headless", "true");
        }
        //}

        bundle = ResourceBundle.getBundle("Bundle");
    }

//    @Before
//    public  void beforeEachTest() throws Exception {
//        /*
//         *  The following FxToolkit lines allow for indirectly performing
//         *  ApplicationTest.launch(Main.class); and registering the primary stage
//         *  in order to allow running multiple @Test in a single file
//         */
//        FxToolkit.registerPrimaryStage();
//        FxToolkit.setupApplication(Main.class);
//    }

    @After
    public void atferEachTest() throws TimeoutException {
        FxToolkit.cleanupStages();
        FxToolkit.hideStage();
        release(new KeyCode[] {});
        release(new MouseButton[] {});
    }

    @Override
    public void start(Stage stage) throws Exception {
        if (DBHandler.getInstance().createSchema()) {
            LoginHandler.getInstance().login(Vars.TEST_ACCOUNT_EMAIL, Vars.TEST_ACCOUNT_PASSWORD);
            new WindowLoader().load(null, "/views/Dashboard.fxml", null, null, false, false, null);
            WaitForAsyncUtils.waitForFxEvents(1);
        }else {
            throw new Exception("Connection error");
        }
    }
}
