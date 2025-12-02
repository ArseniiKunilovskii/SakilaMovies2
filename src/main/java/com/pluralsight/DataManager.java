package com.pluralsight;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DataManager {
    private static DataSource dataSource;

    public DataManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static void displayActorsByLastName(String lastName) {
        String actorsQuery = "SELECT first_name, last_name FROM actor WHERE last_name = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(actorsQuery)) {
            statement.setString(1, lastName);
            try (ResultSet actorsResult = statement.executeQuery()) {
                if (actorsResult.next()) {
                    System.out.println("Your matches are: \n");
                    do {
                        String firstName = actorsResult.getString("first_name");
                        lastName = actorsResult.getString("last_name");
                        System.out.println(firstName + " " + lastName);
                    } while (actorsResult.next());

                } else {
                    System.out.println("No matches!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void displayMoviesByFullName(String firstName, String lastName) {
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
                System.out.println("Here is the list of movies with " + firstName + " " + lastName + "-----------------");
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
