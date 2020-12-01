package model;

import DAO.ArtistaDAO;
import controller.AppController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Alberto343
 */
@Entity
@Table(name = "ARTISTA")
public class Artista implements Serializable {

    private static AppController controlador = AppController.getInstance();

    @Id
    @Column(name = "ID")
    protected int ID;

    @Column(name = "NOMBRE")
    protected String nombre;

    @Column(name = "NACIONALIDAD")
    protected String nacionalidad;

    @Column(name = "FOTO")
    protected String foto;

    @OneToMany(mappedBy = "creador", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    protected List<Disco> repertorio;

    public Artista(int ID, String nombre, String nacionalidad, String foto, List<Disco> repertorio) {
        this.ID = ID;
        this.nombre = nombre;
        this.nacionalidad = nacionalidad;
        this.foto = foto;
        this.repertorio = repertorio;
    }

    public Artista(int ID, String nombre, String nacionalidad, String foto) {
        this.ID = ID;
        this.nombre = nombre;
        this.nacionalidad = nacionalidad;
        this.foto = foto;
    }

    public Artista(String nombre, String nacionalidad, String foto) {
        this.ID = -1;
        this.nombre = nombre;
        this.nacionalidad = nacionalidad;
        this.foto = foto;
    }

    public Artista() {
        this(-1, "", "", null, null);
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public List<Disco> getRepertorio() {
        List<Disco> discos = controlador.getRepertorio(this.ID);
        if (!discos.isEmpty()) {
            this.setRepertorio(discos);
        } else {
            discos = new ArrayList<>();
            this.setRepertorio(discos);
        }
        return repertorio;
    }

    public void setRepertorio(List<Disco> repertorio) {
        this.repertorio = repertorio;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        boolean igual = false;
        if (obj != null) {
            if (this == obj) {
                igual = true;
            } else {
                if (obj instanceof Artista) {
                    Artista n = (Artista) obj;
                    if (this.ID == n.getID()) {
                        igual = true;
                    }
                }
            }
        }
        return igual;
    }

    @Override
    public String toString() {
        return "Artista{" + "ID=" + ID + ", nombre=" + nombre + ", nacionalidad=" + nacionalidad + ", foto=" + foto + '}';
    }

}
