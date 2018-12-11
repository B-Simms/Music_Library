package com.blake.music_library;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.*;


public class Controller {

    //associate with controls declared in .fxml file
    @FXML private TextArea textAreaOutput;
    @FXML private Button exit;
    @FXML private Button search;
    @FXML private ComboBox<String> artists;
    @FXML private ComboBox<String> genres;
    @FXML private ComboBox<Integer> year;
    @FXML private ToggleGroup toggleGroup1;

    //create ObservableLists for ComboBox menus
    private ObservableList<String> artistList = FXCollections.observableArrayList();
    private ObservableList<String> genreList = FXCollections.observableArrayList();
    private ObservableList<Integer> yearList = FXCollections.observableArrayList();

    //initialization
    @FXML
    public void initialize() {
        //!!
        //WHEN ADDING DATABASE DON'T FORGET TO CHECK USER DATA IN RADIO BUTTONS
        //!!

        //connect to database


        //populate all ComboBoxes
        populateMenus();

        //create listeners for each ComboBox
        addListeners();

        //some default text
        textAreaOutput.setText("Your search will appear here.");

        //search button is disabled until a selection is made from a ComboBox
        search.setDisable(true);



    }

    //Event handler for button clicks
    @FXML
    public void onButtonClick(ActionEvent e){
        if(e.getSource().equals(exit)){
            System.exit(0);
        }
        else if(e.getSource().equals(search)){
            performSearch();
        }
    }


    //create listeners for each ComboBox
    public void addListeners(){

        //listener for artists ComboBox
        artists.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(t1 != null){
                    search.setDisable(false);
                    genres.getSelectionModel().clearSelection();
                    year.getSelectionModel().clearSelection();
                }
            }
        });

        //listener for genres ComboBox
        genres.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(t1 != null){
                    search.setDisable(false);
                    artists.getSelectionModel().clearSelection();
                    year.getSelectionModel().clearSelection();
                }
            }
        });

        //listener for year ComboBox
        year.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observableValue, Integer integer, Integer t1) {
                if(t1 != null){
                    search.setDisable(false);
                    artists.getSelectionModel().clearSelection();
                    genres.getSelectionModel().clearSelection();
                }
            }
        });

    }

    private void populateMenus(){
        populateArtists();
        populateGenres();
        populateYears();
        artists.setItems(artistList);
        genres.setItems(genreList);
        year.setItems(yearList);
    }

    private void populateArtists(){
        //execute artist population query statement
        try{
            Connection conn = DriverManager.getConnection(
                    "jdbc:sqlite:\\D:\\Programming\\Java\\Assignments\\CIS_452\\Music_Library\\album_library.sqlite");
            Statement stmnt = conn.createStatement();
            ResultSet results = stmnt.executeQuery("SELECT artist_name FROM artist ORDER BY artist_name");

            while(results.next()){
                String artist = results.getString(1);
                artistList.add(artist);
            }
            conn.close();
        } catch(SQLException e) {
            System.out.println("Error connecting to database: " + e.getMessage());
        }
    }

    private void populateGenres(){
        //execute genre population query statement
        try{
            Connection conn = DriverManager.getConnection(
                    "jdbc:sqlite:\\D:\\Programming\\Java\\Assignments\\CIS_452\\Music_Library\\album_library.sqlite");
            Statement stmnt = conn.createStatement();
            ResultSet results = stmnt.executeQuery("SELECT DISTINCT genre FROM artist ORDER BY genre");

            while(results.next()){
                String genre = results.getString(1);
                genreList.add(genre);
            }
            conn.close();
        } catch(SQLException e) {
            System.out.println("Error connecting to database: " + e.getMessage());
        }
    }

    private void populateYears(){
        //execute year population query statement
        try{
            Connection conn = DriverManager.getConnection(
                    "jdbc:sqlite:\\D:\\Programming\\Java\\Assignments\\CIS_452\\Music_Library\\album_library.sqlite");
            Statement stmnt = conn.createStatement();
            ResultSet results = stmnt.executeQuery("SELECT DISTINCT year FROM album ORDER BY year");

            while(results.next()){
                Integer year = results.getInt(1);
                yearList.add(year);
            }
            conn.close();
        } catch(SQLException e) {
            System.out.println("Error connecting to database: " + e.getMessage());
        }
    }

    private void performSearch() {
        textAreaOutput.clear();
        if(!artists.getSelectionModel().isEmpty()) {
            try{
                Connection conn = DriverManager.getConnection(
                        "jdbc:sqlite:\\D:\\Programming\\Java\\Assignments\\CIS_452\\Music_Library\\album_library.sqlite");
                Statement stmnt = conn.createStatement();
                String query;



                if(toggleGroup1.getSelectedToggle().getUserData().toString().equals("songs")) {
                    query = "SELECT song.song_title, album.album_title\n" +
                            "FROM album\n" +
                            "INNER JOIN contains\n" +
                            "ON album.album_id = contains.album_id\n" +
                            "INNER JOIN song\n" +
                            "ON song.song_id = contains.song_id\n" +
                            "INNER JOIN records\n" +
                            "ON song.song_id = records.song_id\n" +
                            "INNER JOIN artist\n" +
                            "ON artist.artist_id = records.artist_id\n" +
                            "WHERE artist_name = '" + artists.getValue() + "'";

                    ResultSet results = stmnt.executeQuery(query);


                    while(results.next()){
                        String output = results.getString(1);
                        String output2 = results.getString(2);
                        textAreaOutput.appendText("'" + output + "' from the album " + output2 + "\n");
                    }
                }
                else if(toggleGroup1.getSelectedToggle().getUserData().toString().equals("album")){
                    query = "SELECT DISTINCT album.album_title\n" +
                            "FROM album\n" +
                            "INNER JOIN contains\n" +
                            "ON album.album_id = contains.album_id\n" +
                            "INNER JOIN song\n" +
                            "ON song.song_id = contains.song_id\n" +
                            "INNER JOIN records\n" +
                            "ON song.song_id = records.song_id\n" +
                            "INNER JOIN artist\n" +
                            "ON artist.artist_id = records.artist_id\n" +
                            "WHERE artist_name = '" + artists.getValue() + "'";

                    ResultSet results = stmnt.executeQuery(query);


                    while(results.next()){
                        String output = results.getString(1);
                        textAreaOutput.appendText(output + "\n");
                    }
                }

                stmnt.close();
                conn.close();
            } catch(Exception e) {
                System.out.println("Error connecting to database: " + e.getMessage());
            }


        }
        if(!genres.getSelectionModel().isEmpty()) {
            try{
                Connection conn = DriverManager.getConnection(
                        "jdbc:sqlite:\\D:\\Programming\\Java\\Assignments\\CIS_452\\Music_Library\\album_library.sqlite");
                Statement stmnt = conn.createStatement();
                String query;

                if(toggleGroup1.getSelectedToggle().getUserData().toString().equals("songs")) {
                    query = "SELECT DISTINCT song.song_title, artist.artist_name, album.album_title\n" +
                            "FROM album\n" +
                            "INNER JOIN contains\n" +
                            "ON album.album_id = contains.album_id\n" +
                            "INNER JOIN song\n" +
                            "ON song.song_id = contains.song_id\n" +
                            "INNER JOIN records\n" +
                            "ON song.song_id = records.song_id\n" +
                            "INNER JOIN artist\n" +
                            "ON artist.artist_id = records.artist_id\n" +
                            "WHERE artist.genre = '" + genres.getValue() + "'";

                    ResultSet results = stmnt.executeQuery(query);
                    while(results.next()){

                        String output = results.getString(1);
                        String output2 = results.getString(2);
                        String output3 = results.getString(3);
                        textAreaOutput.appendText("'" + output + "' by " + output2 + " from the album " + output3 + "\n");
                    }
                }
                else if(toggleGroup1.getSelectedToggle().getUserData().toString().equals("album")){
                    query = "SELECT DISTINCT album.album_title, artist.artist_name\n" +
                            "FROM album\n" +
                            "INNER JOIN contains\n" +
                            "ON album.album_id = contains.album_id\n" +
                            "INNER JOIN song\n" +
                            "ON song.song_id = contains.song_id\n" +
                            "INNER JOIN records\n" +
                            "ON song.song_id = records.song_id\n" +
                            "INNER JOIN artist\n" +
                            "ON artist.artist_id = records.artist_id\n" +
                            "WHERE artist.genre = '" + genres.getValue() + "'";

                    ResultSet results = stmnt.executeQuery(query);


                    while(results.next()){
                        String output = results.getString(1);
                        String output2 = results.getString(2);
                        textAreaOutput.appendText(output + " by " + output2 + "\n");
                    }
                }


                stmnt.close();
                conn.close();
            } catch(Exception e) {
                System.out.println("Error connecting to database: " + e.getMessage());
            }

        }
        if(!year.getSelectionModel().isEmpty()) {
            try{
                Connection conn = DriverManager.getConnection(
                        "jdbc:sqlite:\\D:\\Programming\\Java\\Assignments\\CIS_452\\Music_Library\\album_library.sqlite");
                Statement stmnt = conn.createStatement();
                String query;

                if(toggleGroup1.getSelectedToggle().getUserData().toString().equals("songs")) {

                    query = "SELECT DISTINCT song.song_title, artist.artist_name, album.album_title\n" +
                            "FROM album\n" +
                            "INNER JOIN contains\n" +
                            "ON album.album_id = contains.album_id\n" +
                            "INNER JOIN song\n" +
                            "ON song.song_id = contains.song_id\n" +
                            "INNER JOIN records\n" +
                            "ON song.song_id = records.song_id\n" +
                            "INNER JOIN artist\n" +
                            "ON artist.artist_id = records.artist_id\n" +
                            "WHERE album.year = " + year.getValue();



                    ResultSet results = stmnt.executeQuery(query);
                    while(results.next()){

                        String output = results.getString(1);
                        String output2 = results.getString(2);
                        String output3 = results.getString(3);
                        textAreaOutput.appendText("'" + output + "' by " + output2 + " from the album " + output3 + "\n");
                    }
                }
                else if(toggleGroup1.getSelectedToggle().getUserData().toString().equals("album")){
                    query = "SELECT DISTINCT album.album_title, artist.artist_name\n" +
                            "FROM album\n" +
                            "INNER JOIN contains\n" +
                            "ON album.album_id = contains.album_id\n" +
                            "INNER JOIN song\n" +
                            "ON song.song_id = contains.song_id\n" +
                            "INNER JOIN records\n" +
                            "ON song.song_id = records.song_id\n" +
                            "INNER JOIN artist\n" +
                            "ON artist.artist_id = records.artist_id\n" +
                            "WHERE album.year = '" + year.getValue() + "'";

                    ResultSet results = stmnt.executeQuery(query);


                    while(results.next()){
                        String output = results.getString(1);
                        String output2 = results.getString(2);
                        textAreaOutput.appendText(output + " by " + output2 + "\n");
                    }
                }


                stmnt.close();
                conn.close();
            } catch(Exception e) {
                System.out.println("Error connecting to database: " + e.getMessage());
            }

        }
    }

}
