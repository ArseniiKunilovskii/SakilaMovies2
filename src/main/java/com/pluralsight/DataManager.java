package com.pluralsight;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DataManager {
    private DataSource dataSource;

    public DataManager(DataSource dataSource){
        this.dataSource = dataSource;
    }

    public static void displayActorsByLastName(BasicDataSource dataSource, String lastname) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("""
                     Select first_name, last_name from actor
                     where last_name = ?""")) {
            statement.setString(1, lastname);
            try (ResultSet results = statement.executeQuery()) {
                if (!results.next()) {
                    System.out.println("There is no actor with the last name " + lastname);
                    return;
                }
                String firstName;
                String lastName;
                do {
                    firstName = results.getString(1);
                    lastName = results.getString(2);
                    System.out.println(firstName + " " + lastName);
                } while (results.next());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void displayMoviesByFullName(BasicDataSource dataSource, String firstName, String lastName) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(""" 
                     SELECT f.title FROM sakila.film f
                     join film_actor fa on f.film_id = fa.film_id
                     join actor a on fa.actor_id = a.actor_id
                     where first_name = ? AND last_name = ?;""")) {
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            try (ResultSet results = statement.executeQuery()) {
                if (!results.next()) {
                    System.out.println("There is no movies with this actor");
                    return;
                }
                String title;
                System.out.println("Here is the list of movies with " + firstName + " " + lastName+ "-----------------");
                do {
                    title = results.getString(1);
                    System.out.println(title);
                } while (results.next());
                System.out.println("End of the list--------------------------------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
