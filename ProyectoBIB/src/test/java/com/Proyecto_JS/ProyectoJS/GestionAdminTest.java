package com.Proyecto_JS.ProyectoJS;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import com.Proyecto_JS.ProyectoJS.controller.admin.AdminDashboardController;

import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(AdminDashboardController.class)
@WithMockUser(username="testAdmin", roles={"ADMIN"})

public class GestionAdminTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void mostrarDashboard_DebeRetonarVistaDashbord() throws Exception{
        mockMvc.perform(get("/admin/dashboard"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(view().name("admin/dashboard"));
    }
    
}
