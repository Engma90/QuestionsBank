import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.input.KeyCode;
import models.Course;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.testfx.api.FxAssert;
import org.testfx.matcher.control.TableViewMatchers;
import org.testfx.util.WaitForAsyncUtils;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.hasText;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CourseTest extends TestBaseSkipLogin {

    private final String DR_ID = "1";
    private final String CONTAINER = "#courses";
    private final String TABLE = "#courses_table_view";
    private final String BUTTON_ADD = "#btn_add";
    private final String BUTTON_EDIT = "#btn_edit";
    private final String BUTTON_DELETE = "#btn_delete";

    private final String ADD_WINDOWCONTAINER = "#container";
    private final String COURSE_CODE = "EEP";
    private final String COURSE_NAME = "Programming";
    private final String PREFERRED_EXAM_LAYOUT_COMBO = "#preferredExamLayout";

    private final String COURSE_CODE_EDITED = "EEP2";
    private final String COURSE_NAME_EDITED = "ProgrammingEdited";
    private final String COURSE_CODE_FIELD = "#course_code";
    private final String COURSE_NAME_FIELD = "#course_name";
    private final String COURSE_WINDOW_ADD_BUTTON = "#add_course";
    private final String COURSE_WINDOW_EDIT_BUTTON = "Edit";
    private ComboBox preferredExamLayout;



    private TableView<Course> tableView;

//    @Override
//    public void start(Stage stage) throws Exception {
//        DashboardController.doctor = new Doctor();
//        DashboardController.doctor.setId(DR_ID);
//        new WindowLoader().load(null, "/views/Dashboard.fxml", null, null, false, false, null);
//        WaitForAsyncUtils.waitForFxEvents(1);
//    }
//
//    @After
//    public void atferEachTest() throws TimeoutException {
//        //
//        FxToolkit.cleanupStages();
//        FxToolkit.hideStage();
//        release(new KeyCode[] {});
//        release(new MouseButton[] {});
//    }

    @Test
    public void Test1AddCourse(){
        clickOn(CONTAINER + " " + BUTTON_ADD);

        clickOn(COURSE_CODE_FIELD).write(COURSE_CODE);
        clickOn(COURSE_NAME_FIELD).write(COURSE_NAME);

        preferredExamLayout = lookup(ADD_WINDOWCONTAINER).lookup(PREFERRED_EXAM_LAYOUT_COMBO).query();
        clickOn(preferredExamLayout);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);

        clickOn(COURSE_WINDOW_ADD_BUTTON);
        tableView = lookup(CONTAINER).lookup(TABLE).query();
        Course last = tableView.getItems().get(tableView.getItems().size() - 1);
        verifyThat(last.name, hasText(COURSE_NAME));
    }

    @Test
    public void Test2EditCourse(){
        tableView = lookup(CONTAINER).lookup(TABLE).query();
        WaitForAsyncUtils.waitForFxEvents();
        tableView.getSelectionModel().selectLast();
        clickOn(CONTAINER + " " + BUTTON_EDIT);


        clickOn(COURSE_CODE_FIELD).push(KeyCode.CONTROL, KeyCode.A).push(KeyCode.DELETE);
        clickOn(COURSE_NAME_FIELD).push(KeyCode.CONTROL, KeyCode.A).push(KeyCode.DELETE);

        clickOn(COURSE_CODE_FIELD).write(COURSE_CODE_EDITED);
        clickOn(COURSE_NAME_FIELD).write(COURSE_NAME_EDITED);

        preferredExamLayout = lookup(ADD_WINDOWCONTAINER).lookup(PREFERRED_EXAM_LAYOUT_COMBO).query();
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);

        clickOn(COURSE_WINDOW_EDIT_BUTTON);

        tableView = lookup(CONTAINER).lookup(TABLE).query();
        String last = tableView.getItems().get(tableView.getItems().size() - 1).name;
        verifyThat(last, hasText(COURSE_NAME_EDITED));
    }


    @Test
    public void Test3DeleteCourse(){
        WaitForAsyncUtils.waitForFxEvents(1);
        tableView = lookup(CONTAINER).lookup(TABLE).query();
        tableView.getSelectionModel().selectLast();
        Course last = tableView.getItems().get(tableView.getItems().size() - 1);
        int size = tableView.getItems().size();
        clickOn(CONTAINER + " " + BUTTON_DELETE);
        clickOn("Yes");
        WaitForAsyncUtils.waitForFxEvents(1);
        FxAssert.verifyThat(tableView, TableViewMatchers.hasNumRows(size - 1));
    }

    public void selectLast(){
        tableView = lookup(CONTAINER).lookup(TABLE).query();
        WaitForAsyncUtils.waitForFxEvents();
        tableView.getSelectionModel().selectLast();
    }


}
