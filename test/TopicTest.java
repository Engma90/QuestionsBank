import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import models.Topic;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.testfx.api.FxAssert;
import org.testfx.matcher.control.ListViewMatchers;
import org.testfx.util.WaitForAsyncUtils;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.hasText;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TopicTest extends TestBaseSkipLogin {
    
    private final String CONTAINER = "#topics";
    private final String TABLE = "#topics_list_view";
    private final String BUTTON_ADD = "#btn_add";
    private final String BUTTON_EDIT = "#btn_edit";
    private final String BUTTON_DELETE = "#btn_delete";


    private final String TOPIC_NAME = "Topic1";

    private final String TOPIC_NAME_EDITED = "Topic2";
    private final String TOPIC_NAME_FIELD = "#topic_name";
    private final String TOPIC_WINDOW_ADD_BUTTON = "#add_topic";
    private final String TOPIC_WINDOW_EDIT_BUTTON = "#edit_topic";


    private ListView<Topic> listView;

    @Test
    public void Test1AddTopic(){
        new ChapterTest().Test1AddChapter();
        clickOn(CONTAINER + " " + BUTTON_ADD);
        clickOn(TOPIC_NAME_FIELD).push(KeyCode.CONTROL, KeyCode.A).push(KeyCode.DELETE);
        clickOn(TOPIC_NAME_FIELD).write(TOPIC_NAME);
        clickOn(TOPIC_WINDOW_ADD_BUTTON);
        listView = lookup(CONTAINER).lookup(TABLE).query();
        Topic last = listView.getItems().get(listView.getItems().size() - 1);
        verifyThat(last.name, hasText(TOPIC_NAME));
    }

    @Test
    public void Test2EditTopic(){
        new ChapterTest().selectLast();
        listView = lookup(CONTAINER).lookup(TABLE).query();
        WaitForAsyncUtils.waitForFxEvents();
        listView.getSelectionModel().selectLast();
        clickOn(CONTAINER + " " + BUTTON_EDIT);


        clickOn(TOPIC_NAME_FIELD).push(KeyCode.CONTROL, KeyCode.A).push(KeyCode.DELETE);
        clickOn(TOPIC_NAME_FIELD).write(TOPIC_NAME_EDITED);
        clickOn(TOPIC_WINDOW_EDIT_BUTTON);

        listView = lookup(CONTAINER).lookup(TABLE).query();
        String last = listView.getItems().get(listView.getItems().size() - 1).name;
        verifyThat(last, hasText(TOPIC_NAME_EDITED));
    }


    @Test
    public void Test3DeleteTopic(){
        new ChapterTest().selectLast();
        WaitForAsyncUtils.waitForFxEvents(1);
        listView = lookup(CONTAINER).lookup(TABLE).query();
        listView.getSelectionModel().selectLast();
        Topic last = listView.getItems().get(listView.getItems().size() - 1);
        int size = listView.getItems().size();
        clickOn(CONTAINER + " " + BUTTON_DELETE);
        clickOn("Yes");
        WaitForAsyncUtils.waitForFxEvents(1);
        FxAssert.verifyThat(listView, ListViewMatchers.hasItems(size - 1));

        new ChapterTest().Test3DeleteChapter();
    }



}
