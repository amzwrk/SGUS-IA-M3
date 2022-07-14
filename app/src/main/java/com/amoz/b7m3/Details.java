package com.amoz.b7m3;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class Details extends AppCompatActivity {

    // ---- START : NAVIGATION DRAWER ----
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    MaterialToolbar toolBar;
    // ---- END : NAVIGATION DRAWER ----

    // ---- START : FIREBASE ----
    Boolean connected = FALSE;

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://b7m3cc-default-rtdb.asia-southeast1.firebasedatabase.app/");

    DatabaseReference rootRef = database.getReference();
    DatabaseReference dbConnected = database.getReference(".info/connected");
    // ---- END : FIREBASE ----

    // ---- START : MACHINE DETAILS ----
    TextView lbl_macName, vTxt_temp, vTxt_hydLvl, vTxt_lubLvl, vTxt_hydPre, vTxt_chkPre, lbl_maxSpeed;
    EditText inp_maxSpeed;
    Integer curSpeed;
    Integer newSpeed;
    Button btn_macCancel, btn_macSet;
    // ---- END : MACHINE DETAILS ----

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent i = getIntent();
        String callingActivity = i.getStringExtra(Activity.ACTIVITY_SERVICE);

        String machineRef = i.getStringExtra("MACHINE_REF");
        DatabaseReference macDetails = rootRef.child(machineRef);

        lbl_macName = findViewById(R.id.lbl_macName);
        vTxt_temp = findViewById(R.id.vTxt_temp);
        vTxt_hydLvl = findViewById(R.id.vTxt_hydLvl);
        vTxt_lubLvl = findViewById(R.id.vTxt_lubLvl);
        vTxt_hydPre = findViewById(R.id.vTxt_hydPre);
        vTxt_chkPre = findViewById(R.id.vTxt_chkPre);
        lbl_maxSpeed = findViewById(R.id.lbl_maxSpeed);
        inp_maxSpeed = findViewById(R.id.inp_maxSpeed);
        btn_macCancel = findViewById(R.id.btn_macCancel);
        btn_macSet = findViewById(R.id.btn_macSet);

        btn_macSet.setVisibility(View.GONE);

        macDetails.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    lbl_macName.setText(snapshot.getKey() + " (" + snapshot.child("type").getValue() + ")");
                    vTxt_temp.setText(snapshot.child("param").child("temp").getValue().toString());
                    vTxt_hydLvl.setText(snapshot.child("param").child("hydLvl").getValue().toString());
                    vTxt_lubLvl.setText(snapshot.child("param").child("lubLvl").getValue().toString());
                    vTxt_hydPre.setText(snapshot.child("param").child("hydPre").getValue().toString());
                    vTxt_chkPre.setText(snapshot.child("param").child("chkPre").getValue().toString());

                    curSpeed = snapshot.child("param").child("maxSpeed").getValue(int.class);
                    inp_maxSpeed.setHint(curSpeed.toString() +"  "+ getText(R.string.hint_maxSpeed));
                    inp_maxSpeed.setGravity(Gravity.RIGHT);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        inp_maxSpeed.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    lbl_maxSpeed.setTextSize(16);
                    inp_maxSpeed.setGravity(Gravity.LEFT);
                } else {
                    String s = inp_maxSpeed.getText().toString().replaceAll("[\\D]","");
                    if (s.isEmpty()) {
                        lbl_maxSpeed.setTextSize(20);
                        newSpeed = curSpeed;
                        inp_maxSpeed.setGravity(Gravity.RIGHT);
                        btn_macSet.setVisibility(View.GONE);
                    } else {
                        try {
                            System.out.println(s);
                            newSpeed = Integer.parseInt(s);
                            if (newSpeed > 200000 || newSpeed < 1) {
                                throw new Exception("Exception message");
                            }
                            btn_macSet.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            System.out.println(e);
                            macDetails.child("status").setValue(3);
                            macDetails.child("param").child("maxSpeed").setValue(0);
                            Toast.makeText(getApplicationContext(),"ERROR!",Toast.LENGTH_LONG).show();
                            finish();
                        }

                    }
                }
            }
        });

        btn_macCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_macSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inp_maxSpeed.clearFocus();
                if (curSpeed != newSpeed) {
                    macDetails.child("param").child("maxSpeed").setValue(newSpeed);
                    macDetails.child("status").setValue(1);
                    Toast.makeText(getApplicationContext(),"Saved",Toast.LENGTH_LONG).show();
                }
                finish();
            }
        });

        // ---- START : CONNECTION CHECK ----
        dbConnected.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (Boolean.TRUE.equals(snapshot.getValue(Boolean.class))) {
                    connected = TRUE;
                    //onDisconnect does not have child() so using updateChildren instead with inline Hash map
                    rootRef.onDisconnect().updateChildren(new HashMap(){{ put("userConnected", FALSE);}});
                    rootRef.updateChildren(new HashMap(){{ put("userConnected", TRUE);}});
                    System.out.println("connected");
                    toolBar.setTitle(R.string.activity_details);
                } else {
                    connected = FALSE;
                    System.out.println("disconnected");
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });
        // ---- END : CONNECTION CHECK ----

        // ---- START : NAVIGATION DRAWER ----
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.toggle_open, R.string.toggle_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        toolBar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolBar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("THIS IS WORKING");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // ---- END : NAVIGATION DRAWER ----

        ConstraintLayout lay_macDet;
        lay_macDet = findViewById(R.id.lay_macDet);
        lay_macDet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                inp_maxSpeed.clearFocus();
                lay_macDet.clearFocus();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Snackbar mySnackbar = Snackbar.make(findViewById(R.id.lay_macDet), R.string.snk_noExit, BaseTransientBottomBar.LENGTH_LONG);
            mySnackbar.show();
        }
        return true;
    }
}