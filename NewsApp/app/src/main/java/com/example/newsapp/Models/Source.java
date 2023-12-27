package com.example.newsapp.Models;

import java.io.Serializable;

public class Source implements Serializable {
    String id = ""   ;
    String name = "" ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


/*      step-8  jaisa ki hum janate hain jab humnein link ko postman mein dala tab postman ne data dikhya JSON ki form mein
        jismein ki article naam se ek JSON array hai
        uss JSON Array mein kai sasre objects hain usmein se ek hai source or uske subObjects hain id or name joki string type ke hain


        step-9 go to NewsHeadlines.java class

 */