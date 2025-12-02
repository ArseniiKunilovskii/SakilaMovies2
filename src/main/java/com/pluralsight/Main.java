package com.pluralsight;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // Check for command line arguments for username and password
        if (args.length != 2) {
            System.out.println("Application needs two arguments to run: " +
                    "java com.pluralsight.Main <username> <password>");
            System.exit(1);
        }

        String username = args[0];
        String password = args[1];

        // Create a BasicDataSource
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/sakila");
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        // Use try-with-resources to automatically close the connection and statement
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter the last name of an actor: ");
            String lastName = scanner.nextLine();

            DataManager dataManager = new DataManager(dataSource);

            dataManager.displayActorsByLastName(lastName);

            System.out.print("\nEnter the first name of an actor: ");
            String firstName = scanner.nextLine();

            dataManager.displayMoviesByFullName(firstName,lastName);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
