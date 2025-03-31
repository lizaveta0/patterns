import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static data.DataGeneratorAPI.*;

class AuthTest {
    private String login;
    private String password;
    private String status;

    @BeforeEach
    void setUpAll() {
        login = generateLogin();
        password = generatePassword();
        status = "active";
    }

    @Test
    public void testRegistrationPositive() {
        createRequest(login, password, status).then().statusCode(200);
    }

    @Test
    public void testRegistrationUserBlocked() {
        createRequest(login, password, status = "blocked").then().statusCode(200);
    }

    @Test
    public void testRegistrationLoginEmpty() {
        createRequest(login = "", password, status).then().statusCode(500);
    }

    @Test
    public void testRegistrationPasswordEmpty() {
        createRequest(login, password = "", status).then().statusCode(500);
    }

    @Test
    public void testRegistrationStatusEmpty() {
        createRequest(login, password, status = "").then().statusCode(500);
    }
}