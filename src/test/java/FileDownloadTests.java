import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.FileDownloadMode;
import com.codeborne.selenide.Selenide;
import com.codeborne.xlstest.XLS;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.*;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class FileDownloadTests extends TestBase {
    static String URL = "https://accounts.test.goodwix.com/login";
    static String URL_MES = "https://messenger.test.istock.link";

    @Test
    void loadTestInMessenger() throws FileNotFoundException {
        open(URL_MES);
        $("input[name='email']").setValue(login);
        $("input[name='password']").setValue(password);
        $("button[name='loginUserButton']").click();


        $(".cws-chat-item__desc").click();

        $(".cw-input-msg__btn-attach").click();
        $("input[type='file']").uploadFromClasspath("1.txt");
    }

    @Test
    void downloadTestInMessenger() throws FileNotFoundException, Exception {
        Configuration.proxyEnabled = true;
        Configuration.fileDownload = FileDownloadMode.PROXY;
        open(URL_MES);
        $("input[name='email']").setValue(login);
        $("input[name='password']").setValue(password);
        $("button[name='loginUserButton']").click();
        $(".cws-chat-item__desc").click();
        $(".file-preview_theme_main-chat")
                .$(byText("txt"))
                .parent()
                .$(".file-preview__dropdown-container").click();
        File download = $(".cws-dropdown-menu-item_color_gray").download();
        String fileContent = IOUtils.toString(new FileReader(download));
        assertTrue(fileContent.contains("Тома?т, или помид?р"));
    }


    @Test
    void loadXlsPriceLoadTest() {
        open(URL);
        $("input[name='email']").setValue(login);
        $("input[name='password']").setValue(password);
        $("button[name='loginUserButton']").click();
        $(".navigation__list-of-links").$(byText("Номенклатура")).click();
        $(".table-new__body").$(byText("Тестовый каталог")).click();
        $(".text-content-left").$(byText("импортом")).click();
        $("input[type='file']").uploadFromClasspath("example.xls");
    }

    @Test
    void downloadXlsPriceTest() throws FileNotFoundException, Exception {
        open(URL);
        $("input[name='email']").setValue(login);
        $("input[name='password']").setValue(password);
        $("button[name='loginUserButton']").click();
        $(".navigation__list-of-links").$(byText("Номенклатура")).click();
        Selenide.$$(".table-new__table-data_text-align_left").get(6).click();
        File file = $(".table-item-export__link")
                .download();
        XLS parsedXls = new XLS(file);
        parsedXls.excel.getActiveSheetIndex();
    }
}

