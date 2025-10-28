package com.Proyecto_JS.ProyectoJS;
import com.Proyecto_JS.ProyectoJS.controller.admin.GestionPedidoController;
import com.Proyecto_JS.ProyectoJS.entity.Pedido;
import com.Proyecto_JS.ProyectoJS.service.PedidoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GestionPedidoController.class)
@WithMockUser(username = "admin",roles={"ADMIN"})
public class GestionPedidoTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoService pedidoService;

    private Pedido createMockPedido(Long id,Pedido.EstadoPedido estado){
        Pedido pedido=new Pedido();
        return pedido;
    }

    @Test
    void mostrarPedidosParaGestionar_DebeCargarPedidosPendientesYRetornarVista() throws Exception{
        Pedido pedido1=createMockPedido(1L,Pedido.EstadoPedido.PAGO_EN_REVISION);
        Pedido pedido2=createMockPedido(2L,Pedido.EstadoPedido.PAGO_EN_REVISION);
        List<Pedido> pedidosPendientes=Arrays.asList(pedido1,pedido2);

        when(pedidoService.obtenerPedidosPorEstado(Pedido.EstadoPedido.PAGO_EN_REVISION)).thenReturn(pedidosPendientes);

        mockMvc.perform(get("/admin/pedidos"))
        .andExpect(status().isOk())
        .andExpect(view().name("admin/gestionar-pedidos"))
        .andExpect(model().attributeExists("pedidos"))
        .andExpect(model().attribute("pedidos", pedidosPendientes));

        verify(pedidoService,times(1)).obtenerPedidosPorEstado(Pedido.EstadoPedido.PAGO_EN_REVISION);

    }

}