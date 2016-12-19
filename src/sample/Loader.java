package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Comarch on 2016-12-12.
 */
public class Loader
{
    private static String folderPath = "C:\\Users\\Comarch\\Desktop\\FbRepo";
    private static String repoPath = null;


    public static ObservableList<String> loadContacts() //wczytuje wszystkie kontakty
    {
        File friendsFile = new File(folderPath + File.separator + "friends.htm");
        Scanner scanner = null;
        try
        {
            scanner = new Scanner(friendsFile);
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        String friends = scanner.nextLine();
        for(int i = 0; i<3; i++) //szukamy trzeciego wystapienia slowa "Znajomi"
        {

            int start = friends.indexOf("Znajomi");
            friends = friends.substring(start + "znajomi".length());
        }

        String [] friendsArray = friends.split("<li>");


        for(int i = 1; i< friendsArray.length; i++) //zaczynamy od i=1, bo w friendsArray[0] są smieci
        {
            String [] tab = friendsArray[i].split("<");
            friendsArray[i] = tab[0];
        }

        ObservableList<String> friendsList = FXCollections.observableArrayList(friendsArray); //factory method
         friendsList.remove(0); //w tablicy friendsArray na pozycji 0 znajdowały się śmieci - nie potrzebujemy ich w naszej liście

        return friendsList;


    }

    public static void loadFile()
    {
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Archiwum ZIP (*.zip)", "*.zip");
        fc.getExtensionFilters().add(extFilter);
        fc.setInitialDirectory(new File("C:\\Users\\Comarch\\Desktop\\"));
        extFilter = new FileChooser.ExtensionFilter("Wszystkie", "*");
        fc.getExtensionFilters().add(extFilter);

        String folderName = "FbReader Repo";
        File repo = fc.showOpenDialog(null);
        repoPath = repo.getPath();
        folderPath = repo.getPath().substring(0, repo.getPath().length() - repo.getName().length()) + folderName;


        byte[] buffer = new byte[1024];


        try
        {
            File folder = new File(folderPath);

            String[]entries = folder.list();
            for(String s: entries){
                File currentFile = new File(folder.getPath(),s);
                currentFile.delete();
            }

            if(!folder.exists())
            {
                folder.mkdir();
            }

            ZipInputStream zis = new ZipInputStream(new FileInputStream(repoPath));
            ZipEntry ze = zis.getNextEntry();


            while(ze!= null)
            {
                String fileName = ze.getName();
//
                File newFile = new File(folderPath + File.separator + fileName);
                System.out.println("file unzip: " + newFile.getAbsolutePath());

                System.out.println("newFile getParent: " + newFile.getParent());
                System.out.println("newFile path: " + newFile.getAbsolutePath());
                new File(newFile.getParent()).mkdirs();

                FileOutputStream fos = new FileOutputStream(newFile);

                int len;
                while ((len=zis.read(buffer))>0)
                {
                    fos.write(buffer,0,len);
                }
                fos.close();
                ze = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();

            System.out.println("Zrobione");



        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Błąd podczas dekompresji repozytorium" , JOptionPane.ERROR_MESSAGE);
        }


    }

    public static ArrayList<Message> loadMessages()
    {
        File messagesFile = new File(folderPath + File.separator + "messages.htm");
        Scanner scanner = null;
        ArrayList<Message> messages = null;

        try
        {
            scanner = new Scanner(messagesFile);
        }
        catch (FileNotFoundException fe)
        {
            return null;
        }

        if(scanner!=null)
        {

            String currentLine = "";
            messages = new ArrayList<>();
            String oneMessage = "";

            int licznik = 0;

            while(scanner.hasNextLine())
            {
                currentLine = scanner.nextLine();
                int msgStart = 0;
                int msgEnd = 0;
                int previousMsgEnd = 0;

                while(msgEnd < currentLine.length() && licznik<2000)
                {
                    previousMsgEnd = msgEnd;
                    msgStart = currentLine.indexOf("<div class=\"message\">", msgEnd);

                    msgEnd = currentLine.indexOf("</p>", msgStart);
                    if(msgEnd != -1 && msgStart!=1)
                    {
                        oneMessage = currentLine.substring(msgStart,msgEnd + 4);
                    }
                    else
                    {
                        oneMessage = "1q2w3eazsxdc"; //this code at beginnigs means that this message is not complete
                        oneMessage.concat(currentLine.substring(previousMsgEnd, currentLine.length()));
                    }

                    try
                    {
                        messages.add(new Message(oneMessage));
                        licznik++;
                    }
                    catch (Exception ex)
                    {
                        System.out.println(oneMessage);
                        System.out.println(licznik);
                        System.out.println(ex.getMessage());
                    }

                }





            }





        }
        return messages;
    }


}
