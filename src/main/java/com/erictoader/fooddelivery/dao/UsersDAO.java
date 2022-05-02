package com.erictoader.fooddelivery.dao;

import com.erictoader.fooddelivery.connection.ConnectionFactory;
import com.erictoader.fooddelivery.model.Users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;

public class UsersDAO extends GenericDAO<Users>{
    public UsersDAO() {
        super(Users.class);
    }

    /*
     *   Method for querying the database in order to find all entries within a table that match specific requirements stored in an object
     *  @return A list of generic objects found in the result set
     */
    public List<Users> findUser(Users u) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        GenericDAO<Users> genericDAO = new GenericDAO<>(Users.class);

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append("* ");
        sb.append("FROM ");
        sb.append(ConnectionFactory.getDBNAME()).append(".users ");
        sb.append("WHERE (username='").append(u.getUsername()).append("' and pass='").append(u.getPass()).append("')");

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(sb.toString());
            resultSet = statement.executeQuery();

            return genericDAO.createObjects(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING,  "UsersDAO:find " + e.getMessage());
        } finally {
            assert resultSet != null;
            ConnectionFactory.close(resultSet);
            assert statement != null;
            ConnectionFactory.close(statement);
            assert connection != null;
            ConnectionFactory.close(connection);
        }
        return null;
    }

    public List<Users> findUserByUsername(Users u) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        GenericDAO<Users> genericDAO = new GenericDAO<>(Users.class);

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append("* ");
        sb.append("FROM ");
        sb.append(ConnectionFactory.getDBNAME()).append(".users ");
        sb.append("WHERE username='").append(u.getUsername()).append("';");

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(sb.toString());
            resultSet = statement.executeQuery();

            return genericDAO.createObjects(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING,  "UsersDAO:findUsername " + e.getMessage());
        } finally {
            assert resultSet != null;
            ConnectionFactory.close(resultSet);
            assert statement != null;
            ConnectionFactory.close(statement);
            assert connection != null;
            ConnectionFactory.close(connection);
        }
        return null;
    }
}
