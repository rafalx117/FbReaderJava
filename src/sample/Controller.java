package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import sun.misc.IOUtils;

import javax.swing.*;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import javafx.scene.control.TextArea;


public class Controller implements Initializable
{
    private String folderPath = "C:\\Users\\Comarch\\Desktop\\FbRepo";
    private String repoPath = null;
    private ArrayList<Message> allMessages = new ArrayList<>();

    @FXML
    public Pane viewPane;
    public TextArea txtArea;
    public ComboBox contactsComboBox;

    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources)
    {
        assert txtArea != null : "fx:id=\"txtArea\" was not injected ";
    }


    public void findMessages(ActionEvent e)
    {

        new Thread(() ->        //asynchronicznie wczytujemy wiadomości (nie zamraża to naszej aplikacji)
        {
            if((allMessages = Loader.loadMessages()) == null)
        {
            System.out.println("Nie znaleziono pliku messages.htm!");
            txtArea.setText("Nie znaleziono pliku messages.htm!");
            return;
        }
        else
            {
               txtArea.setText("Znaleziono " + allMessages.size() + " wiadomości!");
            }

            Collections.reverse(allMessages); //wiadomości są dymyślnie w kolejności majelącej
            String temp = "";
            for(Message m : allMessages)
            {
                temp += m.toString() + "\n";
            }


            txtArea.setText(temp);

        }).start();




    }



    public void loadContacts(ActionEvent e)
    {
        ObservableList<String> friends = Loader.loadContacts();
        Collections.sort(friends);
        contactsComboBox.setItems(friends);

    }

    public void loadFile()
    {
        Loader.loadFile();
    }




}
