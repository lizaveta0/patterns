import data.DataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pages.CardPage;

import static com.codeborne.selenide.Selenide.open;
import static data.DataGenerator.*;

public class CardDeliveryTest {

    private CardPage cardPage;
    private DataGenerator dataGenerator;
    String locale;
    String city;
    String date;
    String name;
    String phone;
    Boolean accept;

    @BeforeEach
    public void setUp() {
        open("http://localhost:9999/");
        cardPage = new CardPage();

        locale = "ru";
        UserInfo userInfo = DataGenerator.DeliveryCard.generateUser(locale);
        city = userInfo.getCity();
        date = DataGenerator.generateDate(3);
        name = userInfo.getName();
        phone = userInfo.getPhone();
        accept = userInfo.getAccept();

    }


    @Test
    public void testOrderFlowPositive() {
        cardPage.fillOrderForm(city, date, name, phone, accept);
        cardPage.checkOrderSuccessMessage("Встреча успешно запланирована на " + date);

        date = DataGenerator.generateDate(3);
        cardPage.replanAndGetMessage(date, "У вас уже запланирована встреча на другую дату. Перепланировать?");

        cardPage.submitReplan();
        cardPage.checkOrderSuccessMessage("Встреча успешно запланирована на " + date);
    }

    @Test
    public void testOrderFlowNegative() {
        cardPage.fillOrderForm(city, date, name, phone, accept);
        cardPage.checkOrderSuccessMessage("Встреча успешно запланирована на " + date);

        name = "Тестовый Покупатель";
        cardPage.fillOrderForm(city, date, name, phone, accept);
        cardPage.checkOrderSuccessMessage("Встреча успешно запланирована на " + date);
    }

    @Test
    public void testOrderFlowNegativeCity() {
        city = "";

        cardPage.fillOrderForm(city, date, name, phone, accept);
        cardPage.checkErrorMessageForCity("Поле обязательно для заполнения");
    }

    @Test
    public void testOrderFlowNegativeCityIncorrect() {
        String localeCity = "en";
        city = generateAddress(localeCity);

        cardPage.fillOrderForm(city, date, name, phone, accept);
        cardPage.checkErrorMessageForCity("Доставка в выбранный город недоступна");
    }

    @Test
    public void testOrderFlowNegativeDate() {
        date = "1";

        cardPage.fillOrderForm(city, date, name, phone, accept);
        cardPage.checkErrorMessageForDate("Неверно введена дата");
    }

    @Test
    public void testOrderFlowNegativeDateEmpty() {
        date = " ";

        cardPage.fillOrderForm(city, date, name, phone, accept);
        cardPage.checkErrorMessageForDate("Неверно введена дата");
    }

    @Test
    public void testOrderFlowNegativeNameEmpty() {
        name = "";

        cardPage.fillOrderForm(city, date, name, phone, accept);
        cardPage.checkErrorMessageForName("Поле обязательно для заполнения");
    }

    @Test
    public void testOrderFlowNegativeNameIncorrect() {
        String nameLocale = "en";
        name = generateName(nameLocale);

        cardPage.fillOrderForm(city, date, name, phone, accept);
        cardPage.checkErrorMessageForName("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.");
    }

    @Test
    public void testOrderFlowNegativePhoneEmpty() {
        phone = "";

        cardPage.fillOrderForm(city, date, name, phone, accept);
        cardPage.checkErrorMessageForPhone("Поле обязательно для заполнения");
    }

    @Test
    public void testOrderFlowNegativeAgreement() {
        accept = false;

        cardPage.fillOrderForm(city, date, name, phone, accept);
        cardPage.agreementIsInvalid();
    }
}
