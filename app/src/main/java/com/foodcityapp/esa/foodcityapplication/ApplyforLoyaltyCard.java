package com.foodcityapp.esa.foodcityapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class ApplyforLoyaltyCard extends AppCompatActivity {

    private CheckBox isAgreed;
    private Button applyBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applyfor_loyalty_card);

        isAgreed = (CheckBox) findViewById(R.id.isAgreedcheckBox);
        applyBtn = (Button) findViewById(R.id.btnApplyL);

        applyBtn.setEnabled(false);

        isAgreed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    applyBtn.setEnabled(true);
                }
                else{
                    applyBtn.setEnabled(false);
                }
            }
        });
    }
}
