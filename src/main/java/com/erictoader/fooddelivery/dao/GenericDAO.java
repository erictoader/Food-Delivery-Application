package com.erictoader.fooddelivery.dao;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.erictoader.fooddelivery.connection.ConnectionFactory;

/*
 *   This class is for generic data access operations.
 *   @author Toader Eric-Stefan
 */
public class GenericDAO<T> {
    protected static final Logger LOGGER = Logger.getLogger(GenericDAO.class.getName());

    private final Class<T> type;

    /*
     *   Constructor for initializing the generic type
     *  @param classType Class type that needs to be operated on
     */
    public GenericDAO(Class<T> classType) {
        this.type = classType;
    }

    /*
     *   Method for getting the object types and values of a specific generic object instance
     *  @param t Generic object
     *  @return Object and instance values for the provided object
     */
    public Object[] getObjectValues(Object t) {
        Object[] objectValues = null;
        try {
            Field[] fields = type.getDeclaredFields();
            objectValues = new Object[fields.length];

            for (int i = 0; i < fields.length; i++) {
                String fieldName = fields[i].getName();
                Method method = type.getDeclaredMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
                objectValues[i] = method.invoke(t);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return objectValues;
    }

    /*
     *   Method for creating and returning a list of objects from a ResultSet, used when displaying queries
     *  @param resultSet The set of result of a query
     *  @return A list of generic objects found in the result set
     */
    List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<>();
        Constructor[] ctors = type.getDeclaredConstructors();
        Constructor ctor = null;
        for (int i = 0; i < ctors.length; i++) {
            ctor = ctors[i];
            if (ctor.getGenericParameterTypes().length == 0)
                break;
        }
        try {
            while (resultSet.next()) {
                assert ctor != null;
                ctor.setAccessible(true);
                T instance = (T) ctor.newInstance();
                for (Field field : type.getDeclaredFields()) {
                    String fieldName = field.getName();
                    Object value = resultSet.getObject(fieldName);
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
                list.add(instance);
            }
        } catch (InstantiationException | IllegalAccessException | SecurityException | IllegalArgumentException | InvocationTargetException | SQLException | IntrospectionException e) {
            e.printStackTrace();
        }
        return list;
    }

    /*
     *   Method for querying the database in order to find all entries within a table
     *  @return A list of generic objects found in the result set
     */
    public List<T> findAll() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append("* ");
        sb.append("FROM ");
        sb.append(ConnectionFactory.getDBNAME()).append(".");
        sb.append(type.getSimpleName().toLowerCase()).append(";");

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(sb.toString());
            resultSet = statement.executeQuery();

            return createObjects(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findAll " + e.getMessage());
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

    /*
     *   Method for inserting an object's values in a database table
     *  @return Boolean value that indicates if the operation was successfully completed
     */
    public boolean insert(T t) {
        Connection connection = null;
        PreparedStatement statement = null;
        Object[] objectValues = getObjectValues(t);
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ");
        sb.append(ConnectionFactory.getDBNAME()).append(".").append(type.getSimpleName().toLowerCase()).append(" (");

        try {
            connection = ConnectionFactory.getConnection();
            Field[] fields = type.getDeclaredFields();
            for (int i = 1; i < fields.length - 1; i++) {
                sb.append(fields[i].getName().toLowerCase()).append(", ");
            }
            sb.append(fields[fields.length - 1].getName().toLowerCase()).append(") VALUES(");

            for (int i = 1; i < fields.length - 1; i++) {
                if (objectValues[i].getClass().getName().equalsIgnoreCase("Integer")) {
                    sb.append(objectValues[i].toString()).append(", ");
                } else {
                    sb.append("'").append(objectValues[i].toString()).append("', ");
                }
            }

            if (objectValues[fields.length - 1].getClass().getName().equalsIgnoreCase("Integer")) {
                sb.append(objectValues[fields.length - 1].toString()).append(");");
            } else {
                sb.append("'").append(objectValues[fields.length - 1].toString()).append("');");
            }

            statement = connection.prepareStatement(sb.toString());
            statement.executeUpdate();

            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:insert " + e.getMessage());
        } finally {
            assert statement != null;
            ConnectionFactory.close(statement);
            assert connection != null;
            ConnectionFactory.close(connection);
        }
        return false;
    }

    /*
     *   Method for updating an entry's values in a database table
     *  @return Boolean value that indicates if the operation was successfully completed
     */
    public boolean update(T t) {
        Connection connection = null;
        PreparedStatement statement = null;
        Object[] objectValues = getObjectValues(t);
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ");
        sb.append(ConnectionFactory.getDBNAME()).append(".").append(type.getSimpleName().toLowerCase()).append(" SET ");

        try {
            connection = ConnectionFactory.getConnection();
            Field[] fields = type.getDeclaredFields();
            for (int i = 1; i < fields.length - 1; i++) {
                sb.append(fields[i].getName().toLowerCase()).append(" = ");
                if (objectValues[i].getClass().getName().equalsIgnoreCase("Integer")) {
                    sb.append(objectValues[i].toString()).append(", ");
                } else {
                    sb.append("'").append(objectValues[i].toString()).append("', ");
                }
            }
            sb.append(fields[fields.length - 1].getName().toLowerCase()).append(" = ");
            if (objectValues[fields.length - 1].getClass().getName().equalsIgnoreCase("Integer")) {
                sb.append(objectValues[fields.length - 1].toString()).append(" ");
            } else {
                sb.append("'").append(objectValues[fields.length - 1].toString()).append("' ");
            }

            sb.append("WHERE ").append(fields[0].getName().toLowerCase()).append(" = ").append(objectValues[0].toString()).append(";");

            statement = connection.prepareStatement(sb.toString());
            statement.executeUpdate();

            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:update " + e.getMessage());
        } finally {
            assert statement != null;
            ConnectionFactory.close(statement);
            assert connection != null;
            ConnectionFactory.close(connection);
        }
        return false;
    }

    /*
     *   Method for deleting an entry from a table within the database
     *  @param id The database table id of the item that needs to be deleted
     *  @return Boolean value that indicates if the operation was successfully completed
     */
    public boolean delete(int id) {
        Connection connection = null;
        PreparedStatement statement = null;

        StringBuilder sb = new StringBuilder();
        sb.append("DELETE ");
        sb.append("FROM ");
        sb.append(ConnectionFactory.getDBNAME()).append(".");
        sb.append(type.getSimpleName().toLowerCase()).append(" ");
        sb.append("WHERE ").append(type.getDeclaredFields()[0].getName()).append(" = ?;");

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(sb.toString());
            statement.setInt(1, id);

            statement.executeUpdate();

            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:delete " + e.getMessage());
        } finally {
            assert statement != null;
            ConnectionFactory.close(statement);
            assert connection != null;
            ConnectionFactory.close(connection);
        }
        return false;
    }
}

