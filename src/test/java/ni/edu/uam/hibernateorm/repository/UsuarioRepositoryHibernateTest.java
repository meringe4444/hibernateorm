package ni.edu.uam.hibernateorm.repository;

import ni.edu.uam.hibernateorm.config.HibernateUtil;
import ni.edu.uam.hibernateorm.model.Usuario;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioRepositoryHibernateTest {

    private static UsuarioRepository repository;

    @BeforeAll
    public static void setUp() {
        repository = new UsuarioRepositoryHibernate();
    }

    @Test
    public void testSessionFactoryCreation() {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        assertNotNull(sf);
        assertFalse(sf.isClosed());
    }

    @Test
    public void testFindByLoginAdmin() throws Exception {
        Optional<Usuario> adminOpt = repository.findByLogin("admin", "1234");
        assertTrue(adminOpt.isPresent());
        Usuario admin = adminOpt.get();
        assertEquals("admin", admin.getUsuario());
        assertEquals("Admin", admin.getNombre());
    }

    @Test
    public void testFindAll() throws Exception {
        List<Usuario> usuarios = repository.findAll();
        assertNotNull(usuarios);
        assertFalse(usuarios.isEmpty());
    }

    @Test
    public void testCrudCicloCompleto() throws Exception {
        String randomSuffix = UUID.randomUUID().toString().substring(0, 8);
        Usuario nuevo = new Usuario(
                "TestNombre",
                "TestApellido",
                "test" + randomSuffix + "@uam.edu.ni",
                "user_" + randomSuffix,
                "password123",
                ""
        );

        repository.save(nuevo);
        assertNotNull(nuevo.getId());

        Optional<Usuario> encontrado = repository.findById(nuevo.getId());
        assertTrue(encontrado.isPresent());
        assertEquals("TestNombre", encontrado.get().getNombre());

        nuevo.setNombre("NombreModificado");
        repository.update(nuevo);
        Optional<Usuario> modificado = repository.findById(nuevo.getId());
        assertTrue(modificado.isPresent());
        assertEquals("NombreModificado", modificado.get().getNombre());

        repository.delete(nuevo.getId());
        Optional<Usuario> eliminado = repository.findById(nuevo.getId());
        assertFalse(eliminado.isPresent());
    }
}
