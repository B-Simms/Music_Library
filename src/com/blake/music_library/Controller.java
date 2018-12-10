package com.blake.music_library;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;


public class Controller {

    //associate with controls declared in .fxml file
    @FXML private TextArea results;
    @FXML private Button exit;
    @FXML private Button search;
    @FXML private ComboBox<String> artists;
    @FXML private ComboBox<String> genres;
    @FXML private ComboBox<Integer> year;
    @FXML private ToggleGroup toggleGroup1;
    @FXML private ToggleGroup toggleGroup2;
    @FXML private ToggleGroup toggleGroup3;

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

        //populate all ComboBoxes
        populateMenus();

        //create listeners for each ComboBox
        addListeners();

        //some default text
        results.setText("Default text.");

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
        artistList.add("David Bowie");
        artistList.add("Pink Floyd");
        artistList.add("Eminem");
    }

    private void populateGenres(){
        genreList.add("Film");
        genreList.add("Hip Hop");
        genreList.add("Rock");
    }

    private void populateYears(){
        yearList.add(1992);
        yearList.add(1972);
        yearList.add(1800);
    }

    private void performSearch() {
        if(!artists.getSelectionModel().isEmpty()) {
            results.setText(artists.getValue() + "\n" + toggleGroup1.getSelectedToggle().getUserData().toString());
        }
        if(!genres.getSelectionModel().isEmpty()) {
            results.setText(genres.getValue() + "\n" + toggleGroup2.getSelectedToggle().getUserData().toString());
        }
        if(!year.getSelectionModel().isEmpty()) {
            results.setText(String.valueOf(year.getValue()) + "\n" + toggleGroup3.getSelectedToggle().getUserData().toString());
        }
    }

}
