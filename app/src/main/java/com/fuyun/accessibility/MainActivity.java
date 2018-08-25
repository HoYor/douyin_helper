package com.fuyun.accessibility;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText mContent;
    private Button mConfirm;
    private SwitchCompat mSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initAction();
    }

    private void initView() {
        mContent = findViewById(R.id.content);
        mContent.setHint(RobotService.mSendMsg);
        mConfirm = findViewById(R.id.confirm);
        mSwitch = findViewById(R.id.switchBtn);
        mSwitch.setChecked(RobotService.isAllowPlay);
    }

    private void initAction() {
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mContent.getText() == null ||
                        mContent.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this,"内容不能为空",Toast.LENGTH_LONG).show();
                }else{
                    RobotService.mSendMsg = mContent.getText().toString();
                    mContent.setHint(RobotService.mSendMsg);
                    mContent.setText("");
                    Toast.makeText(MainActivity.this,"设置成功",Toast.LENGTH_LONG).show();
                }
            }
        });
        mSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RobotService.isAllowPlay = mSwitch.isChecked();
            }
        });
    }
}
