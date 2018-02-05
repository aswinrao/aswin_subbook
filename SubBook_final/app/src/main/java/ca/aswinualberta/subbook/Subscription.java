package ca.aswinualberta.subbook;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Subscription {
    private String name;
    private Calendar date;
    private BigDecimal charge;
    private String comment;

    public Subscription(String name, Calendar date, String comment, double charge){
        this(name, date, comment, new BigDecimal(charge));
    }

    public Subscription(String name, Calendar date, String comment, BigDecimal charge){
        this.name = name;
        this.date = date;
        this.charge = charge;
        this.comment = comment;
    }

    public String getName(){
        return this.name;
    }

    public Calendar getDate(){
        return this.date;
    }

    /*
        Return date as yyyy-MM-dd format
     */
    public String getDateString(){
        return new SimpleDateFormat("yyyy-MM-dd").format(this.date.getTime());
    }

    public BigDecimal getCharge(){
        return this.charge;
    }

    /*
        Return $ + charge with 2 decimal places
     */
    public String getChargeString(){
        return "$" + this.charge.setScale(2, RoundingMode.HALF_UP).toString(); // round to 2 decimal places
    }

    public String getComment(){
        return this.comment;
    }

}


