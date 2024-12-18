package com.example.att_app;

import static android.app.ProgressDialog.show;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Set;

public class TMainActivity extends AppCompatActivity {

    Button buttonON, scanButton, buttonOFF;
    TextView scanTextView;

    Set<BluetoothDevice> ad;
    private static final int requestCodeForEnable =2;
    BluetoothAdapter myBluetoothAdapter;
    Intent btEnablingIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmain);

        buttonON = (Button) findViewById(R.id.btON);
        scanButton = (Button) findViewById(R.id.scanButton);
        buttonOFF = (Button) findViewById(R.id.btOFF);
        scanTextView =(TextView) findViewById(R.id.scannedTextView);
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(myBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not supported!",Toast.LENGTH_LONG).show();
        }
        btEnablingIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

        bluetoothONMethod();
        bluetoothOFFMethod();
        bluetoothSCANMethod();

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == requestCodeForEnable) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Bluetooth is enabled.", Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Bluetooth Enabiling Cancelled", Toast.LENGTH_LONG).show();
            }
        }

    }

    private void bluetoothOFFMethod() {
        buttonOFF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myBluetoothAdapter.isEnabled()) {
                    myBluetoothAdapter.disable();
                    Toast.makeText(getApplicationContext(), "Bluetooth Turned OFF", Toast.LENGTH_LONG).show();

                }
            }
        });
    }


    private void bluetoothONMethod() {
        buttonON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myBluetoothAdapter == null) {
                    Toast.makeText(getApplicationContext(), "Bluetooth does not support on this device", Toast.LENGTH_LONG).show();
                }
//                else if (myBluetoothAdapter.disable()) {
//                        startActivityForResult(btEnablingIntent,requestCodeForEnable);
                else if (myBluetoothAdapter.isEnabled()) {
                    Toast.makeText(getApplicationContext(), "Bluetooth Already Turned ON", Toast.LENGTH_LONG).show();
                    return;
                }
                else {
                    myBluetoothAdapter.enable();
                    Toast.makeText(getApplicationContext(), "Bluetooth Turned ON", Toast.LENGTH_LONG).show();

                }
            }
        });

    }


    private void bluetoothSCANMethod() {
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder sb = new StringBuilder();
                ad =myBluetoothAdapter.getBondedDevices();
                for (BluetoothDevice temp:ad) {
                    sb.append(" \n" +temp.getAddress()+ "\t"+temp.getName()+"\n");
                }

                scanTextView.setText(sb.toString());
            }
        });
    }

    //logout
    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), TLogin.class));
        finish();
    }

}
