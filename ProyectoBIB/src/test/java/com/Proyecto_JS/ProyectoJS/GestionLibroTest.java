package com.Proyecto_JS.ProyectoJS;

import com.Proyecto_JS.ProyectoJS.controller.admin.GestionLibroController;
import com.Proyecto_JS.ProyectoJS.entity.Categoria;
import com.Proyecto_JS.ProyectoJS.entity.Inventario;
import com.Proyecto_JS.ProyectoJS.entity.Libro;
import com.Proyecto_JS.ProyectoJS.entity.Sucursal;
import com.Proyecto_JS.ProyectoJS.service.CategoriaService;
import com.Proyecto_JS.ProyectoJS.service.InventarioService;
import com.Proyecto_JS.ProyectoJS.service.LibroService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(GestionLibroController.class)
@WithMockUser(username = "admin",roles={"ADMIN"})
public class GestionLibroTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LibroService libroService;

    @MockBean
    private CategoriaService categoriaService;

    @MockBean
    private InventarioService inventarioService;

    private Libro createMockLibro(Long id, String titulo){
        Libro libro=new Libro();
        libro.setId(id);
        libro.setTitulo(titulo);
        libro.setAutor("Mock Autor");
        libro.setIsbn("1234567890");
        libro.setPrecioVenta(new BigDecimal("19.90"));
        libro.setEstado(Libro.EstadoLibro.ACTIVO);
        libro.setCreatedAt(LocalDateTime.now());
        return libro;
    }
    

    private Inventario createMockInventario(Long id, Libro libro, int stock) {
        Inventario inventario = new Inventario();
        inventario.setId(id);
        inventario.setLibro(libro);
        inventario.setStockVenta(stock); 
        inventario.setActivo(true);
        inventario.setSucursal(new Sucursal());
        return inventario;
    }

    private Categoria createMockCategoria(Long id,String nombre){
        Categoria categoria= new Categoria();
        categoria.setId(id);
        return categoria;
    }

    

    @Test
    void mostrarInventario_DebeRetonarVistaYModelos() throws Exception{
    Libro libro1=createMockLibro(1L,"Cien años de soledad");
    Inventario inv1=createMockInventario(10L,libro1,5);

    List<Inventario> mockInventario=Arrays.asList(inv1);
    List<Categoria> mockCategorias=Arrays.asList(createMockCategoria(1L, "Ficción"));

    when(inventarioService.obtenerTodoElInventario()).thenReturn(mockInventario);
    when(categoriaService.obtenerTodasLasCategorias()).thenReturn(mockCategorias);

    mockMvc.perform(get("/admin/libros"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/gestionar-libros"))
                .andExpect(model().attributeExists("inventario", "categorias", "libroNuevo"))
                .andExpect(model().attribute("inventario", mockInventario))
                .andExpect(model().attribute("categorias", mockCategorias));

        verify(inventarioService, times(1)).obtenerTodoElInventario();
        verify(categoriaService, times(1)).obtenerTodasLasCategorias();
    }
}
