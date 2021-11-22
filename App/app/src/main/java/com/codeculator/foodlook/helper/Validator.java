package com.codeculator.foodlook.helper;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class Validator {
    boolean valid;

    public Validator(){
        valid = true;
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
                ((EditText) view).setError(null);
                ((EditText) view).addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if(layout instanceof TextInputLayout){
                            ((TextInputLayout)layout).setErrorEnabled(false);
                            ((TextInputLayout)layout).setError(null);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }
            if(layout != null && layout instanceof TextInputLayout){
                ((TextInputLayout)layout).setError(null);
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
                        et.setError(null);
                    }
                    valid = false;
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
                        et.setError(null);
                    }
                    valid = false;
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
