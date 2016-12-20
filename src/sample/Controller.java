package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
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
    private ArrayList<Message> filteredMessages = new ArrayList<>();
    private boolean messagesLoaded = false;

    @FXML
    public Pane viewPane;
    public TextArea txtArea;
    public ComboBox contactsComboBox;
    public CheckBox linkCheckBox;

    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources)
    {
        assert txtArea != null : "fx:id=\"txtArea\" was not injected ";
    }


    public void findMessages(ActionEvent e)
    {
     //   new Thread(()-> {txtArea.appendText("SSSSSSSSSS");}).start(); //TODO: zrobić wyświetlenie informacji o wczytywaniu danych

        Thread findMessages = new Thread(() ->        //asynchronicznie wczytujemy wiadomości (nie zamraża to naszej aplikacji)
        {


                if(!messagesLoaded) //tylko raz ładujemy wiadomości
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
                    messagesLoaded = true;
                }

                }



                Collections.reverse(allMessages); //wiadomości są dymyślnie w kolejności majelącej
                String temp = "";
//                for(Message m : allMessages)
//                {
//                    temp += m.toString() + "\n";
//                }

            for(Message m : allMessages)
            {
                if(m.getUser() == "-- Wszyscy --")
                {
                    //Wyświetl wszystkich
                    filteredMessages.add(m);
                }
                else
                {
                    if(m.getUser() == contactsComboBox.getValue())
                        filteredMessages.add(m);

                }
            }

            if(linkCheckBox.isSelected())
            {
                for(Message m : filteredMessages)
                {
                   if(m.getContent().matches("[-a-zA-Z0-9+&@#/%?=~_|!:,;]+(\\.)[-a-zA-Z0-9+&@#/%?=~_|!:,;]+"))
                    temp+= m.toString() + "\n";
                    filteredMessages.add(m);
                }
            }
            else
            {
                for(Message m : filteredMessages)
                {
                    temp += m.toString() + "\n";
                }
            }


                txtArea.setText(temp);

        });

        findMessages.start();

        String processingMessage = "Processing";
        boolean firstLoop = true;
        while (findMessages.isAlive()) //animacja w konsolo
        {
            try
            {
                if(firstLoop)

                {   Thread.sleep(500);
                    System.out.print("Processing");
                    //txtArea.setText("Processing");
                    firstLoop=false;
                }

                    System.out.print(" .");
                    //processingMessage.concat(" .");
                    //txtArea.setText(processingMessage);
                    Thread.sleep(500);

            } catch (InterruptedException e1)
            {
                e1.printStackTrace();
            }

        }




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
