package data;

import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class DataGenerator {

    private DataGenerator() {
    }

    public static String generatePhone(String locale) {
        return new Faker(new Locale(locale)).phoneNumber().phoneNumber();
    }

    public static String generateAddress(String locale) {
        return new Faker(new Locale(locale)).address().city();
    }

    public static String generateName(String locale) {
        return new Faker(new Locale(locale)).name().name();
    }

    public static String generateDate() {
        LocalDate futureDate = LocalDate.now().plusDays(3);
        Date date = Date.from(futureDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        date = new Faker().date().future(365, TimeUnit.DAYS, date);
        futureDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return futureDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }
}