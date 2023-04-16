package com.example.unifood.Main.Extension;

import android.app.Activity;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class editTextChange
{
    public static void changeTextColor(EditText editText, Activity activity){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                editText.setHintTextColor(Color.parseColor("#A9A9A9"));
                editText.setHint("Food Name");
            }
        });
    }
}
