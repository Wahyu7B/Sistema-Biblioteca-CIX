package com.Proyecto_JS.ProyectoJS;
import com.Proyecto_JS.ProyectoJS.controller.web.AuthController;
import com.Proyecto_JS.ProyectoJS.dto.UsuarioRegistroDTO;
import com.Proyecto_JS.ProyectoJS.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AuthController.class)

public class AuthTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Test
    void mostrarLogin_DebeRetornarVistaLogin() throws Exception{
        mockMvc.perform(get("/login"))
        .andExpect(status().isOk())
        .andExpect(view().name("auth/login"));
    }

    @Test
    void mostrarFormularioDeRegistro_DebeRetornarVistaYModeloVacio() throws Exception{
        mockMvc.perform(get("/registro"))
        .andExpect(status().isOk())
        .andExpect(view().name("auth/registro"))
        .andExpect(model().attributeExists("usuario"))
        .andExpect(model().attribute("usuario", new UsuarioRegistroDTO()));
    }
    

}
