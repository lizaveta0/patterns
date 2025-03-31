import data.DataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pages.CardPage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
        city = generateAddress(locale);
        date = generateDate();
        name = generateName(locale);
        phone = generatePhone(locale);
        accept = true;
    }


    @Test
    public void testOrderFlowPositive() {
        cardPage.fillOrderForm(city, date, name, phone, accept);
        cardPage.checkOrderSuccessMessage("Встреча успешно запланирована на " + date);

        date = LocalDate.now().plusDays(5).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
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
    public void testOrderFlowNegativePhoneIncorrect() {
        String phoneLocale = "en";
        phone = generatePhone(phoneLocale);

        cardPage.fillOrderForm(city, date, name, phone, accept);
        cardPage.checkErrorMessageForPhone("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.");
    }

    @Test
    public void testOrderFlowNegativeAgreement() {
        accept = false;

        cardPage.fillOrderForm(city, date, name, phone, accept);
        cardPage.agreementIsInvalid();
    }
}
