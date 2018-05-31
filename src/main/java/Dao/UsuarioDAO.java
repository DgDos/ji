/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Model.Usuario;
import Util.DbUtil;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author FiJus
 */
public class UsuarioDAO {

    private Connection connection;

    public UsuarioDAO() throws SQLException, URISyntaxException, ClassNotFoundException, IOException {
        connection = DbUtil.getConnection();
    }

    public void addUsuarioDemandante(Usuario u, String pass) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("insert into usuario(documento,tipo_documento,nombre,ciudad,direccion,correo,password,tipo_usuario,delete,dinero) values (?,?,?,?,?,?,?,?,1,10000)");
        preparedStatement.setString(1, u.getDocumento());
        preparedStatement.setInt(2, u.getTipo_id());
        preparedStatement.setString(3, u.getNombre());
        preparedStatement.setString(4, u.getCiudad());
        preparedStatement.setString(5, u.getDireccion());
        preparedStatement.setString(6, u.getCorreo());
        preparedStatement.setString(7, pass);
        preparedStatement.setInt(8, u.getTipo_usuario());
        preparedStatement.executeUpdate();
    }

    public void addUsuarioAbogado(Usuario u, String pass) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("insert into usuario(documento,tipo_documento,nombre,ciudad,direccion,correo,password,tarjeta,tipo_usuario,delete,dinero) values (?,?,?,?,?,?,?,?,?,1,10000)");
        preparedStatement.setString(1, u.getDocumento());
        preparedStatement.setInt(2, u.getTipo_id());
        preparedStatement.setString(3, u.getNombre());
        preparedStatement.setString(4, u.getCiudad());
        preparedStatement.setString(5, u.getDireccion());
        preparedStatement.setString(6, u.getCorreo());
        preparedStatement.setString(7, pass);
        preparedStatement.setString(8, u.getTarjeta());
        preparedStatement.setInt(9, u.getTipo_usuario());
        preparedStatement.executeUpdate();
    }

    public boolean isUser(String correo, String pass) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * from usuario where correo='" + correo + "' and password='" + pass + "'");
        while (rs.next()) {
            return true;
        }
        return false;
    }

    public boolean isUserDoc(String documento) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * from usuario where documento='" + documento + "'");
        while (rs.next()) {
            return true;
        }
        return false;
    }

    public boolean isUserCorreo(String correo) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * from usuario where correo='" + correo + "'");
        while (rs.next()) {
            return true;
        }
        return false;
    }

    public Usuario getUsuario(String correo) throws SQLException {
        Usuario user = new Usuario();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * from usuario where correo='" + correo + "'");
        while (rs.next()) {
            user.setDocumento(rs.getString("documento"));
            user.setTipo_id(rs.getInt("tipo_documento"));
            user.setNombre(rs.getString("nombre"));
            user.setCiudad(rs.getString("ciudad"));
            user.setDireccion(rs.getString("direccion"));
            user.setCorreo(correo);
            user.setTarjeta(rs.getString("tarjeta"));
            user.setTipo_usuario(rs.getInt("tipo_usuario"));
            user.setDinero(rs.getInt("dinero"));
        }
        return user;
    }

    public String getNameAyudante(String documento) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select correo from usuario where delete=1 and documento='" + documento + "'");
        while (rs.next()) {
            return rs.getString("correo");
        }
        return "";
    }

    public int getIdAyudante(String correo) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select documento from usuario where delete=1 and correo='" + correo + "'");
        while (rs.next()) {
            return rs.getInt("id_usuario");
        }
        return 0;
    }

    public Usuario existUser(String documento) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * from usuario where delete=1 and documento='" + documento + "'");
        Usuario user = new Usuario();
        while (rs.next()) {
            user.setDocumento(documento);
            user.setNombre(rs.getString("nombre"));
            user.setCiudad(rs.getString("ciudad"));
            user.setDireccion(rs.getString("direccion"));
            user.setCorreo(rs.getString("correo"));
            user.setTipo_id(rs.getInt("tipo_documento"));
            user.setDinero(rs.getInt("dinero"));
            return user;
        }
        user.setDocumento(documento);
        user.setNombre("");
        user.setDireccion("");
        user.setCiudad("");
        user.setCorreo("");
        user.setTipo_id(-1);
        user.setDinero(0);
        return user;
    }

    public boolean updateMoney(String documento, int dinero) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("update usuario set dinero=" + dinero + " where documento='" + documento + "'");
        preparedStatement.executeUpdate();
        return true;
    }

    public Boolean removeMoney(String id_usuario) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select dinero from usuario where delete=1 and documento='" + id_usuario + "'");
        rs.next();
        if (rs.getInt("dinero") < 10000) {
            return false;
        }
        int dinero = rs.getInt("dinero") - 10000;
        PreparedStatement preparedStatement = connection.prepareStatement("update usuario set dinero=" + dinero + " where documento='" + id_usuario + "'");
        preparedStatement.executeUpdate();
        return true;
    }

    public String getMoney(String documento) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select dinero from usuario where delete=1 and documento='" + documento + "'");
        rs.next();
        String money = "" + rs.getInt("dinero");
        String transformation = "";
        int cont=1;
        for (int i = money.length()-1; i >= 0; i--) {
            transformation += money.charAt(i);
            if (cont % 3 == 0) {
                if (cont == 3 || cont == 9) {
                    transformation += '.';
                } else {
                    transformation += "'";
                }
            }
            cont++;
        }
        String fin="";
        for(int i=transformation.length()-1;i>=0;i--){
            fin+=transformation.charAt(i);
        }
        return fin;
    }

}
