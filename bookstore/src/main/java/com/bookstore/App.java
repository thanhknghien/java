package com.bookstore;

import com.bookstore.model.DBConnect;
public class App 
{
    public static void main( String[] args ) throws Exception
    {
        DBConnect.getConnection();
        DBConnect.closeConnection();
        com.mysql.cj.jdbc.AbandonedConnectionCleanupThread.checkedShutdown();
    }
}
