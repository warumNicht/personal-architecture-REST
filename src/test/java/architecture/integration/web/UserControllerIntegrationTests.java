package architecture.integration.web;

import architecture.constants.AppConstants;
import architecture.constants.ViewNames;
import architecture.domain.models.bindingModels.users.UserCreateBindingModel;
import architecture.repositories.UserRepository;
import architecture.util.TestConstants;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import java.util.Locale;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class UserControllerIntegrationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void getUserRegister_returnsCorrectView() throws Exception {
        this.mockMvc.perform(get("/fr/users/register")
                .locale(Locale.FRANCE)
                .contextPath("/fr")
                .cookie(new Cookie(AppConstants.LOCALE_COOKIE_NAME, "fr")))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewNames.USER_REGISTER))
                .andDo(print());
    }

    @Test
    public void postUserRegister_validModel_redirectsCorrectAndSavesData() throws Exception {
        this.mockMvc.perform(post("/fr/users/register")
                .locale(Locale.FRANCE)
                .contextPath("/fr")
                .cookie(new Cookie(AppConstants.LOCALE_COOKIE_NAME, "fr"))
                .flashAttr(ViewNames.USER_REGISTER_binding_model, this.createValidUser())
                .with(csrf()))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/fr/"))
                .andDo(print());

        Assert.assertEquals(1, this.userRepository.count());
    }

    @Test
    public void postUserRegister_validModel_DuplicatedData_returnsErrroPage_throwsDataIntegrityError() throws Exception {
        this.mockMvc.perform(post("/fr/users/register")
                .locale(Locale.FRANCE)
                .contextPath("/fr")
                .cookie(new Cookie(AppConstants.LOCALE_COOKIE_NAME, "fr"))
                .flashAttr(ViewNames.USER_REGISTER_binding_model, this.createValidUser())
                .with(csrf()))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/fr/"));

        MvcResult mvcResult = this.mockMvc.perform(post("/fr/users/register")
                .locale(Locale.FRANCE)
                .contextPath("/fr")
                .cookie(new Cookie(AppConstants.LOCALE_COOKIE_NAME, "fr"))
                .flashAttr(ViewNames.USER_REGISTER_binding_model, this.createValidUser())
                .with(csrf()))
                .andExpect(status().is(200))
                .andExpect(view().name(ViewNames.DEFAULT_ERROR))
                .andDo(print()).andReturn();

        Exception resolvedException = mvcResult.getResolvedException();
        Throwable cause = resolvedException.getCause();

        Assert.assertTrue(resolvedException instanceof DataIntegrityViolationException);
        Assert.assertEquals(cause.getClass().getSimpleName(), "ConstraintViolationException");
    }

    @Test
    public void postUserRegister_invalidData_returnsForm_withoutFlushing() throws Exception {
        this.mockMvc.perform(post("/fr/users/register")
                .locale(Locale.FRANCE)
                .contextPath("/fr")
                .cookie(new Cookie(AppConstants.LOCALE_COOKIE_NAME, "fr"))
                .flashAttr(ViewNames.USER_REGISTER_binding_model, new UserCreateBindingModel())
                .with(csrf()))
                .andExpect(status().is(200))
                .andExpect(model().hasErrors())
                .andExpect(view().name(ViewNames.USER_REGISTER))
                .andDo(print());

        Assert.assertEquals(0, this.userRepository.count());
    }

    @Test
    public void postUserRegister_validDataNotMatchingPasswords_returnsForm_withoutFlushing() throws Exception {
        UserCreateBindingModel user = this.createValidUser();
        user.setConfirmPassword(TestConstants.USER_PASSWORD_NOT_MATCHING);
        this.mockMvc.perform(post("/fr/users/register")
                .locale(Locale.FRANCE)
                .contextPath("/fr")
                .cookie(new Cookie(AppConstants.LOCALE_COOKIE_NAME, "fr"))
                .flashAttr(ViewNames.USER_REGISTER_binding_model, user)
                .with(csrf()))
                .andExpect(status().is(200))
                .andExpect(model().errorCount(1))
                .andExpect(model()
                        .attributeHasFieldErrorCode(ViewNames.USER_REGISTER_binding_model,
                                "confirmPassword", "user.password.notMatch"))
                .andExpect(view().name(ViewNames.USER_REGISTER))
                .andDo(print());

        Assert.assertEquals(0, this.userRepository.count());
    }

    @Test
    public void getUserLogin_returnsCorrectView() throws Exception {
        this.mockMvc.perform(get("/fr/users/login")
                .locale(Locale.FRANCE)
                .contextPath("/fr")
                .cookie(new Cookie(AppConstants.LOCALE_COOKIE_NAME, "fr")))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewNames.USER_LOGIN))
                .andDo(print());
    }

    private UserCreateBindingModel createValidUser() {
        UserCreateBindingModel user = new UserCreateBindingModel();
        user.setUsername(TestConstants.USERNAME_VALID);
        user.setEmail(TestConstants.USER_EMAIL_VALID);
        user.setPassword(TestConstants.USER_PASSWORD_VALID);
        user.setConfirmPassword(TestConstants.USER_PASSWORD_VALID);
        return user;
    }
}
