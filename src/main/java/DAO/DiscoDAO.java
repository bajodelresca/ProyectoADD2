package DAO;

import Utils.ConnectionUtils;
import controller.AppController;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Artista;
import model.Disco;

/**
 *
 * @author Jorge SB
 */
public class DiscoDAO extends Disco implements DAO<Disco> {

    enum queries {
        INSERT("INSERT INTO disco (ID, Nombre, Nacionalidad, Foto, Fecha, IDArtista) VALUES (?,?,?,?)"),
        UPDATE("UPDATE disco SET Nombre=?,Nacionalidad=?,Foto=?,Fecha=?,IDArtista=? WHERE ID=?"),
        DELETE("DELETE FROM disco WHERE ID=?"),
        GETBYID("SELECT * FROM Disco WHERE ID=?"),
        GETALL("SELECT * FROM Disco");

        private String q;

        queries(String q) {
            this.q = q;
        }

        public String getQ() {
            return this.q;
        }
    }
    Connection conn;

    public DiscoDAO(int ID, String Nombre, String foto, Date fecha, Artista creador) {
        super(ID, Nombre, foto, fecha, creador);
        try {
            conn = ConnectionUtils.connect(AppController.currentConnection);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DiscoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DiscoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public DiscoDAO() {
        super();
        try {
            conn = ConnectionUtils.connect(AppController.currentConnection);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DiscoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DiscoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public DiscoDAO(Disco d) {
        super(d.getID(), d.getNombre(), d.getFoto(), d.getFecha(), d.getCreador());
        try {
            conn = ConnectionUtils.connect(AppController.currentConnection);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DiscoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DiscoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//______________________________________________________________________________CRUD
    
    @Override
    public void insert(Disco a) {
        int result = -1;
        try {
            conn = ConnectionUtils.getConnection();
            if (this.ID > 0) {
                edit(a);
            } else {
                PreparedStatement stat = conn.prepareStatement(queries.INSERT.getQ(), Statement.RETURN_GENERATED_KEYS);
                stat.setString(1, a.getNombre());
                stat.setString(2, a.getFoto());
                stat.setDate(3, (java.sql.Date) new Date(a.getFecha().getTime()));
                stat.setInt(4, a.getCreador().getID());

                stat.executeUpdate();
                try ( ResultSet generatedKeys = stat.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        result = generatedKeys.getInt(1);
                    }
                }
                this.ID = result;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DiscoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void edit(Disco a) {
        try {
            conn = ConnectionUtils.getConnection();
            PreparedStatement stat = conn.prepareStatement(queries.UPDATE.getQ());
            stat.setString(1, a.getNombre());
            stat.setString(2, a.getFoto());
            stat.setDate(3, (java.sql.Date) new Date(a.getFecha().getTime()));
            stat.setInt(4, a.getCreador().getID());
            stat.setInt(5, a.getID());
            stat.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(DiscoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void remove(Disco a) {
        PreparedStatement ps = null;
        try {
            conn = ConnectionUtils.getConnection();
            ps = conn.prepareStatement(queries.DELETE.getQ());
            ps.setInt(1, a.getID());

            if (ps.executeUpdate() == 0) {
                throw new SQLException("No se Ha insertado correctamente");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DiscoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DiscoDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    protected Disco convert(ResultSet rs) throws SQLException {
        ArtistaDAO ADAO = new ArtistaDAO();
        int id = rs.getInt("ID");
        String nombre = rs.getString("Nombre");
        String foto = rs.getString("Foto");
        Date fecha = rs.getDate("Fecha");
        int idArtista = rs.getInt("IDArtista");
        Artista crea = ADAO.getByID(idArtista);
        Artista a = new Artista(id, nombre, foto, fecha, crea);
        return a;
    }

    @Override
    public List<Disco> getAll() {
        PreparedStatement stat = null;
        ResultSet rs = null;
        List<Disco> listD = new ArrayList<>();
        try {
            conn = ConnectionUtils.getConnection();
            stat = conn.prepareStatement(queries.GETALL.getQ());
            rs = stat.executeQuery();
            while (rs.next()) {
                listD.add(convert(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DiscoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DiscoDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (stat != null) {
                try {
                    stat.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DiscoDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return listD;
    }

    public Disco getByID(int id) {
        PreparedStatement stat = null;
        ResultSet rs = null;
        Disco d = new Disco();
        try {
            conn = ConnectionUtils.getConnection();
            stat = conn.prepareStatement(queries.GETBYID.getQ());
            stat.setInt(1, id);
            rs = stat.executeQuery();
            if (rs.next()) {
                d = convert(rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DiscoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DiscoDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (stat != null) {
                try {
                    stat.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DiscoDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return d;
    }
    
    // Aún por terminar 
}
