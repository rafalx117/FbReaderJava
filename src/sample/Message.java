package sample;

import java.sql.Time;
import java.util.Date;

/**
 * Created by Comarch on 2016-12-15.
 */
public class Message //represents one message
{
    private String user;
    private Date date;
    private String content;

    public String getUser()
    {
        return user;
    }

    public Date getDate()
    {
        return date;
    }

    public String getContent()
    {
        return content;
    }

    public Message(String msg)
    {
        if(msg.contains("1q2w3eazsxdc")) ////this code at beginnigs means that this message is not complete
        {
            user = "NOT COMPLETE MESSAGE";
            date = null;
            content = msg;
        }

        int start = 0;
        int end = 0;

        //pojedyncza wiadomosc ma format:
        //<div class="message"><div class="message_header"><span class="user">Kasia Blanka Motyka</span><span class="meta">26 maja 2013 o 18:53 UTC+02</span></div></div><p>na prezentację jakąś? :)</p>
        //user
        start = msg.indexOf("<span class=\"user\">");
        end = msg.indexOf("</span>", start);
        user = msg.substring(start + "<span class=\"user\">".length(), end); // musimy dać start+"<span class=\"user\">".length() bo bez tego mielibyśmy ciąg "<span class=\"meta\">" w polu user

        //date
        start = msg.indexOf("<span class=\"meta\">");
        end = msg.indexOf("</span>", start);
        String tab [] = msg.substring(start + "<span class=\"meta\">".length() ,end).split("o");
        String tempDate = tab[0]; //DD Month YYYY eg. 23 maja 2012
        String tempTime = tab[1]; //HH:MM UTC+HH eg. 18:20 UTC+02
        tempDate = tempDate.trim();
        tempTime = tempTime.trim();

        tab = tempDate.split("\\s+"); //split tempDate by space - it will give us day, month(string) and year
        String stringMonth = tab[1];
        int year = Integer.parseInt(tab[2]);
        int month = 0;
        int day = Integer.parseInt(tab[0]);

        switch (stringMonth)
        {
            case "styczeń":
                month = 1;
                break;
            case "luty":
                month = 2;
                break;
            case "marzec":
                month = 3;
                break;
            case "kwietnia":
                month = 4;
                break;
            case "maja":
                month = 5;
                break;
            case "czerwca":
                month = 6;
                break;
            case "lipiec":
                month = 7;
                break;
            case "sierpień":
                month = 8;
                break;
            case "wrzesień":
                month = 9;
                break;
            case "październik":
                month = 10;
                break;
            case "listopad":
                month = 11;
                break;
            case "grudzień":
                month = 12;
                break;
            default:
                month = 0;

        }

        tab = tempTime.split("\\s+"); // we will get eg. 18:20 and UTC+02
        tab = tab[0].split(":");
        int hours = Integer.parseInt(tab[0]);
        int minutes = Integer.parseInt(tab[1]);

        date = new  Date(year,month,day,hours,minutes);

        //content
        start = msg.indexOf("<p>");
        end = msg.indexOf("</p>", start);
        content = msg.substring(start + 3, end); //start+"<p>".length(), bo bez tego na poczatku wiadomosci było <p>


        toString();


    }

    @Override
    public String toString()
    {
        return user.toString() + " " + date.toString() + " " + content.toString();
    }
}
