package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.util.Util;
import jm.task.core.jdbc.model.User;

import java.util.List;
import java.sql.*;
import java.util.ArrayList;



public class UserDaoJDBCImpl implements UserDao {
    private  final Connection connection = Util.getConnection();

    public UserDaoJDBCImpl()  {
    }

    public void createUsersTable() throws SQLException {
            try (Statement statement = connection.createStatement()) {
                connection.setAutoCommit(false);
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS users " +
                        "(id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(45), lastName VARCHAR(45), age INT)");
                connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
                connection.rollback();
            }
        }


    public void dropUsersTable() throws SQLException {
            try (Statement statement =connection.createStatement()) {
                connection.setAutoCommit(false);
                statement.executeUpdate("DROP TABLE IF EXISTS users");
                connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
                connection.rollback();
            }
        }



    public void saveUser(String name, String lastName, byte age) throws SQLException {
        try (PreparedStatement pstm = connection.prepareStatement("INSERT INTO users (name, lastName, age) VALUES (?, ?, ?)")) {
            connection.setAutoCommit(false);
            pstm.setString(1, name);
            pstm.setString(2, lastName);
            pstm.setByte(3, age);
            pstm.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
        }
    }


    public void removeUserById(long id) throws SQLException {
        try (PreparedStatement pstm = connection.prepareStatement("DELETE FROM users WHERE id = ?")) {
            connection.setAutoCommit(false);
            pstm.setLong(1, id);
            pstm.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
        }
    }



    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();

        try (ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM users")) {
            connection.setAutoCommit(false);
            while(resultSet.next()) {
                User user = new User(resultSet.getString("name"),
                        resultSet.getString("lastName"), resultSet.getByte("age"));
                user.setId(resultSet.getLong("id"));
                users.add(user);
            }
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
        }

        return users;
    }

    public void cleanUsersTable() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            statement.executeUpdate("TRUNCATE TABLE users");
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
        }
    }
}
