import controllers.WindowLoader;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import java.util.concurrent.TimeoutException;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.hasText;


public class LoginTest extends ApplicationTest {

    private final String VALID_USERNAME = "a";
    private final String VALID_PASSWORD = "a";
    private final String BAD_USERNAME = "badUser";
    private final String BAD_PASSWORD = "badPassword";

    @Override
    public void start(Stage stage) throws Exception {
        new WindowLoader().load(null, "/views/Home.fxml", null, null, false, false, null);
    }

    @After
    public void atferEachTest() throws TimeoutException {
        //
        FxToolkit.cleanupStages();
        FxToolkit.hideStage();
        release(new KeyCode[] {});
        release(new MouseButton[] {});
    }


    @Test
    public void loginValid(){
        clickOn(Vars.PRE_LOGIN_BUTTON);
        clickOn(Vars.USERNAME_FIELD).write(VALID_USERNAME);
        clickOn(Vars.PASSWORD_FIELD).write(VALID_PASSWORD);
        clickOn(Vars.LOGIN_BUTTON);
        verifyThat(Vars.LOGIN_STATUS_LABEL,hasText("Dr. TestAccount"));
    }

    @Test
    public void loginInvalid(){
        clickOn(Vars.PRE_LOGIN_BUTTON);
        for (String[] data: parametersForIncorrectLogins()) {
            clickOn(Vars.USERNAME_FIELD).write(data[0]);
            clickOn(Vars.PASSWORD_FIELD).write(data[1]);
            clickOn(Vars.LOGIN_BUTTON);
            verifyThat(Vars.LOGIN_STATUS_LABEL,hasText("Error"));
            clickOn("OK");

            TextField username = (TextField) lookup(Vars.USERNAME_FIELD).queryAll().iterator().next();
            username.clear();
            TextField password = (TextField) lookup(Vars.PASSWORD_FIELD).queryAll().iterator().next();
            password.clear();
        }
    }

    private String[][] parametersForIncorrectLogins() {
        return new String[][]{
                new String[]{BAD_USERNAME, BAD_PASSWORD},
                new String[]{BAD_USERNAME, VALID_PASSWORD},
                new String[]{VALID_USERNAME, BAD_PASSWORD},
                new String[]{"", ""}
        };
    }




}
