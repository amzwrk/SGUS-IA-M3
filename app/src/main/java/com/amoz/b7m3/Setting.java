package com.amoz.b7m3;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
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


public class Setting extends AppCompatActivity {

    TextView lbl_setEmail;
    TextView lbl_setPhone;
    EditText inp_setEmail;
    EditText inp_setPhone;
    Button btnSet;
    Button btnCancel;

    String curEmail = "";
    Integer curPhone = 0;
    String newEmail = "";
    Integer newPhone = 0;
    Boolean emailIsEmpty = TRUE;
    Boolean phoneIsEmpty = TRUE;


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

    DatabaseReference impConfig = rootRef.child("impConfig");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Intent i = getIntent();
        String callingActivity = i.getStringExtra(Activity.ACTIVITY_SERVICE);
        System.out.println("CallingActivity:" + callingActivity);

        toolBar = findViewById(R.id.topAppBar);
        toolBar.setTitle(R.string.activity_setting);

        lbl_setEmail = findViewById(R.id.lbl_setEmail);
        lbl_setPhone = findViewById(R.id.lbl_setPhone);

        inp_setEmail = findViewById(R.id.inp_setEmail);
        inp_setPhone = findViewById(R.id.inp_setPhone);

        btnSet = findViewById(R.id.btn_set);
        btnSet.setVisibility(View.GONE);
        btnCancel = findViewById(R.id.btn_cancel);





        impConfig.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                curEmail = snapshot.child("email").getValue(String.class);
                System.out.println("curEmail: " + curEmail);
                inp_setEmail.setHint(curEmail);
                inp_setEmail.setGravity(Gravity.RIGHT);

                curPhone = snapshot.child("phone").getValue(int.class);
                inp_setPhone.setHint(curPhone.toString());
                inp_setPhone.setGravity(Gravity.RIGHT);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        inp_setEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    lbl_setEmail.setTextSize(16);
                    inp_setEmail.setGravity(Gravity.LEFT);
                    inp_setEmail.setTextSize(24);
                } else {
                    String s = inp_setEmail.getText().toString();
                    if (s.isEmpty()) {
                        lbl_setEmail.setTextSize(24);
                        newEmail = curEmail;
                        inp_setEmail.setTextSize(16);
                        inp_setEmail.setGravity(Gravity.RIGHT);
                        emailIsEmpty = TRUE;
                        if (phoneIsEmpty) {
                            btnSet.setVisibility(View.GONE);
                        }
                    } else {
                        newEmail = s;
                        if (!isEmailValid(newEmail)) {
                            Snackbar mySnackbar = Snackbar.make(findViewById(R.id.lay_eContact), "INVALID EMAIL FORMAT", BaseTransientBottomBar.LENGTH_LONG);
                            mySnackbar.show();
                        } else {
                            btnSet.setVisibility(View.VISIBLE);
                            emailIsEmpty = FALSE;
                        }
                    }
                }
            }
        });

        inp_setPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    lbl_setPhone.setTextSize(16);
                    inp_setPhone.setGravity(Gravity.LEFT);
                    inp_setPhone.setTextSize(24);
                } else {

                    String s = inp_setPhone.getText().toString().replaceAll("[\\D]","");
                    if (s.isEmpty()) {
                        lbl_setPhone.setTextSize(24);
                        newPhone = curPhone;
                        inp_setPhone.setTextSize(16);
                        inp_setPhone.setGravity(Gravity.RIGHT);
                        phoneIsEmpty = TRUE;
                        if (emailIsEmpty) {
                            btnSet.setVisibility(View.GONE);
                        }
                    } else {
                        try {
                            newPhone = Integer.parseInt(s);
                            if (newPhone < 10000000 || newPhone > 99999999 ) {
                                Snackbar mySnackbar = Snackbar.make(findViewById(R.id.lay_eContact), R.string.snk_errPhone, BaseTransientBottomBar.LENGTH_LONG);
                                mySnackbar.show();
                            } else {
                                btnSet.setVisibility(View.VISIBLE);
                                phoneIsEmpty = FALSE;
                            }
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(),"ERROR!",Toast.LENGTH_LONG).show();
                        }

                    }
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Required when users click SET but focus still on EditText
                inp_setEmail.clearFocus();
                inp_setPhone.clearFocus();

                if (!emailIsEmpty) {
                    impConfig.child("email").setValue(newEmail);
                }
                if (!phoneIsEmpty) {
                    impConfig.child("phone").setValue(newPhone);
                }

                Toast.makeText(getApplicationContext(),"Saved",Toast.LENGTH_LONG).show();
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
                    toolBar.setTitle(R.string.activity_setting);
                    btnSet.setEnabled(TRUE);
                } else {
                    connected = FALSE;
                    System.out.println("disconnected");
                    toolBar.setTitle(getText(R.string.activity_setting) +" "+ getText(R.string.txt_OFFLINE));
                    // ATTEMPT TO DISABLE BUTTONS
                    btnSet.setEnabled(FALSE);
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

        ConstraintLayout lay_eContact;
        lay_eContact = findViewById(R.id.lay_eContact);
        lay_eContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                inp_setEmail.clearFocus();
                inp_setPhone.clearFocus();
                lay_eContact.clearFocus();
            }
        });
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Snackbar mySnackbar = Snackbar.make(findViewById(R.id.lay_eContact), R.string.snk_noExit, BaseTransientBottomBar.LENGTH_LONG);
            mySnackbar.show();
        }
        return true;
    }


}