package com.pabcubcru.bookdeal.controller;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pabcubcru.bookdeal.models.Authorities;
import com.pabcubcru.bookdeal.models.User;
import com.pabcubcru.bookdeal.services.AuthoritiesService;
import com.pabcubcru.bookdeal.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private AuthoritiesService authoritiesService;

    @Autowired
    private MockMvc mockMvc;

    private User user1;

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
    public static final Charset UTF_8 = Charset.forName("UTF-8");

    @BeforeEach
    public void setup() {
        user1 = new User();

        user1.setName("Test");
        user1.setUsername("test001");
        user1.setEmail("email@email.com");
        user1.setPhone("+34654987321");
        user1.setBirthDate(LocalDate.of(1998, 11, 23));
        user1.setProvince("Province");
        user1.setPostCode("41012");
        user1.setUsername("username");
        user1.setPassword(new BCryptPasswordEncoder().encode("password123"));
        user1.setEnabled(true);
        user1.setGenres("Religión,Gastronomía,Cocina");

        List<Authorities> auths = new ArrayList<>();
        Authorities auth = new Authorities();
        auth.setAuthority("admin");
        auths.add(auth);

        given(this.userService.findByUsername("test001")).willReturn(user1);
        given(this.userService.existUserWithSameEmail("email@email.com")).willReturn(false);
        given(this.userService.existUserWithSameEmail("emailExist@email.com")).willReturn(true);
        given(this.userService.existUserWithSameUsername("username")).willReturn(false);
        given(this.userService.existUserWithSameUsername("usernameExist")).willReturn(true);
        given(this.authoritiesService.findByUsername("test001")).willReturn(auths);
    }

    @Test
    @WithAnonymousUser
    public void testProcessRegisterFormSuccess() throws Exception {

        User u = new User();
        u.setId("");
        u.setName("Test");
        u.setEmail("email@email.com");
        u.setPhone("+34654987321");
        u.setProvince("Province");
        u.setBirthDate(null);
        u.setPostCode("41012");
        u.setUsername("username");
        u.setPassword("password123");
        u.setEnabled(true);
        u.setAccept(true);
        u.setConfirmPassword("password123");
        u.setGenres("Religión,Gastronomía,Cocina");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(u);
        requestJson = requestJson.replace("null", "\"1998-01-01\"");

        mockMvc.perform(post("/register").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true));

    }

    private String convertToUTF8(String content) {
        byte[] ptext = content.getBytes(ISO_8859_1);
        String res = new String(ptext, UTF_8);

        return res;
    }

    @Test
    @WithAnonymousUser
    public void testProcessRegisterFormHasErrors() throws Exception {

        User u = new User();
        u.setId("");
        u.setName("Test");
        u.setEmail("emailExist@email.com");
        u.setPhone("+34654987321");
        u.setProvince("Province");
        u.setBirthDate(null);
        u.setPostCode("41012");
        u.setUsername("usernameExist");
        u.setPassword("pass");
        u.setEnabled(true);
        u.setAccept(false);
        u.setConfirmPassword("p");
        u.setGenres("Religión");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(u);
        requestJson = requestJson.replace("null", "\"2020-01-01\"");

        MockHttpServletResponse response = mockMvc
                .perform(post("/register").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errors").isNotEmpty()).andReturn().getResponse();

        String content = response.getContentAsString();

        String res = this.convertToUTF8(content);

        assertThat(res).contains("Este correo electrónico ya está registrado.");
        assertThat(res).contains("El nombre de usuario no está disponible.");
        assertThat(res).contains("Debe aceptar las condiciones.");
        assertThat(res).contains("La contraseña debe contener entre 8 y 20 carácteres.");
        assertThat(res).contains("Debe seleccionar al menos 3 géneros.");
        assertThat(res).contains("Las contraseñas no coinciden.");
        assertThat(res).contains("Debe ser mayor de 18 años.");
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testProcessEditFormSuccess() throws Exception {

        User u = new User();
        u.setId("test001");
        u.setName("Test Edit");
        u.setEmail("email@email.com");
        u.setPhone("+34654987321");
        u.setProvince("Province");
        u.setBirthDate(null);
        u.setPostCode("41012");
        u.setUsername("username");
        u.setPassword("password123");
        u.setEnabled(true);
        u.setAccept(true);
        u.setConfirmPassword("password123");
        u.setGenres("Religión,Gastronomía,Cocina");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(u);
        requestJson = requestJson.replace("null", "\"1998-01-01\"");

        mockMvc.perform(put("/user/test001/edit").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testProcessEditFormHasErrors() throws Exception {

        User u = new User();
        u.setId("test001");
        u.setName("Test Edit");
        u.setEmail("emailExist@email.com");
        u.setPhone("+34654987321");
        u.setProvince("Province");
        u.setBirthDate(null);
        u.setPostCode("41012");
        u.setUsername("username");
        u.setPassword("pass");
        u.setEnabled(true);
        u.setAccept(true);
        u.setConfirmPassword("pa");
        u.setGenres("Religión,Gastronomía");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(u);
        requestJson = requestJson.replace("null", "\"2020-01-01\"");

        MockHttpServletResponse response = this.mockMvc
                .perform(put("/user/test001/edit").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errors").isNotEmpty()).andReturn().getResponse();

        String content = response.getContentAsString();

        String res = this.convertToUTF8(content);

        assertThat(res).contains("Este correo electrónico ya está registrado.");
        assertThat(res).contains("La contraseña debe contener entre 8 y 20 carácteres.");
        assertThat(res).contains("Debe seleccionar al menos 3 géneros.");
        assertThat(res).contains("Las contraseñas no coinciden.");
        assertThat(res).contains("Debe ser mayor de 18 años.");
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testMain() throws Exception {
        this.mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("Main"));
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testGetPrincipal() throws Exception {
        this.mockMvc.perform(get("/user/principal")).andExpect(status().isOk())
                .andExpect(jsonPath("$.isLogged").value(true))
                .andExpect(jsonPath("$.isAdmin").value(true));
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testGetProvinces() throws Exception {
        MockHttpServletResponse response = this.mockMvc.perform(get("/provinces")).andExpect(status().isOk())
                .andReturn().getResponse();

        String content = response.getContentAsString();

        byte[] ptext = content.getBytes(ISO_8859_1);
        String res = new String(ptext, UTF_8);

        assertThat(res).contains("\"Álava\",\"Albacete\",\"Alicante\",\"Almería\",\"Asturias\",\"Ávila\",\"Badajoz\","
                + "\"Barcelona\",\"Burgos\",\"Cáceres\",\"Cádiz\",\"Cantabria\",\"Castellón\",\"CiudadReal\",\"Córdoba\",\"La_Coruña\",\"Cuenca\",\"Gerona\","
                + "\"Granada\",\"Guadalajara\",\"Guipúzcoa\",\"Huelva\",\"Huesca\",\"Islas_Baleares\",\"Jaén\",\"León\",\"Lérida\",\"Lugo\",\"Madrid\",\"Málaga\","
                + "\"Murcia\",\"Navarra\",\"Orense\",\"Palencia\",\"Las_Palmas\",\"Pontevedra\",\"LaRioja\",\"Salamanca\",\"Segovia\",\"Sevilla\",\"Soria\",\"Tarragona\","
                + "\"Tenerife\",\"Teruel\",\"Toledo\",\"Valencia\",\"Valladolid\",\"Vizcaya\",\"Zamora\",\"Zaragoza\"");
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testGetUsername() throws Exception {
        this.mockMvc.perform(get("/user/get-username")).andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("test001"));
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testGetUser() throws Exception {
        this.mockMvc.perform(get("/user/test001")).andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.user.username").value(user1.getUsername()))
                .andExpect(jsonPath("$.user.email").value(user1.getEmail()));
    }
}
