package ni.edu.uam.hibernateorm.service;

import ni.edu.uam.hibernateorm.model.Usuario;
import ni.edu.uam.hibernateorm.repository.UsuarioRepository;
import ni.edu.uam.hibernateorm.repository.UsuarioRepositoryHibernate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService() {
        this(new UsuarioRepositoryHibernate());
    }

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> findAll() throws Exception {
        return usuarioRepository.findAll();
    }

    public void save(Usuario usuario) throws Exception {
        usuarioRepository.save(usuario);
    }

    public void update(Usuario usuario) throws Exception {
        usuarioRepository.update(usuario);
    }

    public void delete(UUID id) throws Exception {
        usuarioRepository.delete(id);
    }

    public Optional<Usuario> findByLogin(String usuario, String password) throws Exception {
        return usuarioRepository.findByLogin(usuario, password);
    }
}
