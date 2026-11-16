package ni.edu.uam.hibernateorm.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
@Table(name = "usuario")
public class Usuario extends BaseEntity {

    @Column(name = "primer_nombre", length = 100)
    private String nombre;

    @Column(name = "primer_apellido", length = 100)
    private String apellido;

    @Column(name = "correo", length = 150)
    private String correo;

    @Column(name = "password", length = 255)
    private String password;

    @Column(name = "usuario", unique = true, length = 100)
    private String usuario;

    @Column(name = "ruta_foto", length = 255)
    private String rutafoto;

    public Usuario() {
        super();
    }

    public Usuario(String nombre, String apellido, String email, String usuario, String password, String rutafoto) {
        super();
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = email;
        this.usuario = usuario;
        this.password = password;
        this.rutafoto = rutafoto;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public StringProperty usuarioProperty() {
        return new SimpleStringProperty(usuario);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public StringProperty nombreProperty() {
        return new SimpleStringProperty(nombre);
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public StringProperty apellidoProperty() {
        return new SimpleStringProperty(apellido);
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public StringProperty correoProperty() {
        return new SimpleStringProperty(correo);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public StringProperty passwordProperty() {
        return new SimpleStringProperty(password);
    }

    public String getRutafoto() {
        return rutafoto;
    }

    public void setRutafoto(String rutafoto) {
        this.rutafoto = rutafoto;
    }

    public StringProperty rutafotoProperty() {
        return new SimpleStringProperty(rutafoto);
    }
}
