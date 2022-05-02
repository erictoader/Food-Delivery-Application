package com.erictoader.fooddelivery.bll;

import com.erictoader.fooddelivery.dao.UsersDAO;
import com.erictoader.fooddelivery.model.Users;

import java.util.List;

public class DbUsersBLL extends DbGenericBLL<Users>{
    private final UsersDAO usersDAO;

    public DbUsersBLL() {
        super(Users.class);
        usersDAO = new UsersDAO();
    }

    public List<Users> findUser(Users u) {
        return usersDAO.findUser(u);
    }

    public List<Users> findUserByUsername(Users u) {
        return usersDAO.findUserByUsername(u);
    }
}
