package com.codeculator.foodlook.helper;

import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

public class Validator {
    ArrayList<String> messages;

    public Validator(){
        messages = new ArrayList<>();
    }

    public Validate validate(View view){
        return new Validate(view);
    }


    public boolean isValid(){
        return messages.size() == 0;
    }

    public String getMessage(){
        String message = "";
        for(String m : messages){
            message += m + ", ";
        }
        message = message.substring(0,message.length()-2);
        return message;
    }

    public class Validate{
        View view;

        public Validate(View view){
            this.view = view;
        }

        public Validate required(){
            if(view instanceof EditText){
                EditText et = (EditText) view;
                if(et.getText().toString().length() == 0){
                    Validator.this.messages.add(et.getHint() + " is empty !");
                }
            }
            return Validate.this;
        }

        public Validate min(int num){
            if(view instanceof EditText){
                EditText et = (EditText) view;
                if(et.getText().toString().length() < num){
                    Validator.this.messages.add(et.getHint() + " digits below "+ num);
                }
            }
            return Validate.this;
        }
    }
}
