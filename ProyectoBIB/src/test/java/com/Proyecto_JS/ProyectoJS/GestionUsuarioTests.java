package com.Proyecto_JS.ProyectoJS;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import com.Proyecto_JS.ProyectoJS.controller.admin.GestionUsuarioController;
import com.Proyecto_JS.ProyectoJS.entity.Usuario;
import com.Proyecto_JS.ProyectoJS.service.UsuarioService;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


@WebMvcTest(GestionUsuarioController.class)
@WithMockUser(username = "testAdmin", roles = {"ADMIN"}) 
public class GestionUsuarioTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;
    private final Usuario mockUsuarioBase = new Usuario(1L, "User Mock", Usuario.Rol.ADMIN); 

    @Test
    void mostrarGestionDeUsuarios_DebeRetonarVistaYUsuarios() throws Exception{
        Usuario user1=new Usuario(1L, "user 1", Usuario.Rol.ADMIN);
        Usuario user2=new Usuario(2L, "user 2", Usuario.Rol.CLIENTE);
        List<Usuario> usuarios=Arrays.asList(user1, user2);

        when(usuarioService.obtenerTodosLosUsuarios()).thenReturn(usuarios);

        mockMvc.perform(get("/admin/usuarios"))
        .andExpect(status().isOk())
        .andExpect(view().name("admin/gestionar-usuarios"))
        .andExpect(model().attributeExists("usuarios"))
        .andExpect(model().attribute("usuarios",usuarios));

        verify(usuarioService, times(1)).obtenerTodosLosUsuarios();
    }

    @Test
    void cambiarRol_Exito_DebeRedirigirYMostrarMensajeExito() throws Exception{
        Long userId=3L;
        Usuario.Rol newRol=Usuario.Rol.ADMIN;
 
        Usuario usuarioActualizado = new Usuario(userId, "Rol Change", newRol); 
        when(usuarioService.cambiarRol(userId, newRol)).thenReturn(usuarioActualizado);

        mockMvc.perform(post("/admin/usuarios/cambiar-rol")
            .param("usuarioId",String.valueOf(userId))
            .param("rol",newRol.name())
            .flashAttr("redirectAttributes",new Object())
            .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/admin/usuarios"))
            .andExpect(flash().attribute("successMessage","Rol del usuario actualizado con éxito."));

        verify(usuarioService, times(1)).cambiarRol(userId, newRol);
    }

    @Test
    void cambiarRol_Fallo_DebeRedirigirYMostrarMensajeError() throws Exception{
        Long userId=4L;
        Usuario.Rol newRol=Usuario.Rol.ADMIN;

        doThrow(new RuntimeException()).when(usuarioService).cambiarRol(userId,newRol);
        
        mockMvc.perform(post("/admin/usuarios/cambiar-rol")
            .param("usuarioId",String.valueOf(userId))
            .param("rol",newRol.name())
            .flashAttr("redirectAttributes",new Object())
            .with(csrf())) // Aseguramos el token CSRF
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/admin/usuarios"))
            .andExpect(flash().attribute("errorMessage","Error al cambiar el rol."));

        verify(usuarioService, times(1)).cambiarRol(userId, newRol);
    }

    @Test
    void cambiarEstado_Exito_DebeRedirigirYMostrarMensajeExito() throws Exception{
        Long userId=5L;
        Usuario.EstadoUsuario newEstado=Usuario.EstadoUsuario.SUSPENDIDO;
        
        Usuario usuarioActualizado = new Usuario(userId, "Estado Change", Usuario.Rol.CLIENTE);
        when(usuarioService.cambiarEstado(userId, newEstado)).thenReturn(usuarioActualizado);

        mockMvc.perform(post("/admin/usuarios/cambiar-estado")
        .param("usuarioId",String.valueOf(userId))
        .param("estado",newEstado.name())
        .flashAttr("redirectAttributes",new Object())
        .with(csrf())) 
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/admin/usuarios"))
        .andExpect(flash().attribute("successMessage","Estado del usuario actualizado con éxito."));

        verify(usuarioService, times(1)).cambiarEstado(userId, newEstado);
    }
}