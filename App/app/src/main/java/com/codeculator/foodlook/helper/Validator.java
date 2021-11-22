package com.codeculator.foodlook.helper;

import android.view.View;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class Validator {
    boolean valid;

    public Validator(){

    }

    public Validate validate(View view){
        return new Validate(view);
    }

    public Validate validate(View view,View layout){
        return new Validate(view,layout);
    }


    public boolean isValid(){
        return valid;
    }


    public class Validate{
        View view;
        View layout;

        public Validate(View view,View layout){
            this.view = view;
            this.layout = layout;
            clear();
        }

        public Validate(View view){
            this.view = view;
            clear();
        }

        private void clear(){
            if(view instanceof EditText){
                ((EditText) view).setError("");
            }
            if(layout != null && layout instanceof TextInputLayout){
                ((TextInputLayout)layout).setError("");
            }
        }


        public Validate required(){
            if(view instanceof EditText){
                EditText et = (EditText) view;
                if(et.getText().toString().length() == 0){
                    if(layout == null)
                        et.setError(setMessage((String) et.getError(),et.getHint() + " is empty !"));
                    else{
                        TextInputLayout l = (TextInputLayout) layout;
                        l.setError(setMessage((String) l.getError(),l.getHint() + " is empty !"));
                    }
                }
            }
            return Validate.this;
        }

        public Validate min(int num){
            if(view instanceof EditText){
                EditText et = (EditText) view;
                if(et.getText().toString().length() < num){
                    if(layout == null)
                        et.setError(setMessage((String) et.getError(),et.getHint() + " digits below "+ num));
                    else{
                        TextInputLayout l = (TextInputLayout) layout;
                        l.setError(setMessage((String) l.getError(),et.getHint() + " digits below "+ num));
                    }
                }
            }
            return Validate.this;
        }

        private String setMessage(String old,String current){
            if(old == null || old.isEmpty()){
                return current;
            }
            else {
                return old+ ", "+ current;
            }
        }
    }
}
