package com.amoz.b7m3;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

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

    // ---- START : Main ----
    TextView vTxt_mainEmail, vTxt_mainPhone;
    Button btnMachineStart, btnMachineStop;
    ImageButton btn_machineStatusReset;
    Integer countStopped = 0;
    Integer countError = 0;
    Integer countRunning = 0;
    Integer countChildren = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = getIntent();

        btnMachineStart = findViewById(R.id.btn_start);
        btnMachineStop = findViewById(R.id.btn_stop);
        btn_machineStatusReset = findViewById(R.id.btn_machineStatus_reset);
        vTxt_mainEmail = findViewById(R.id.vTxt_mainEmail);
        vTxt_mainPhone = findViewById(R.id.vTxt_mainPhone);

        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Integer status = 0;
                for (DataSnapshot ds: snapshot.getChildren()) {
                    if (ds.hasChild("status")) {
                        status = ds.child("status").getValue(int.class);
                        System.out.println(status);
                        if (status == 1) {
                            countRunning++;
                        } else if (status == 0) {
                            countStopped++;
                        } else if (status == 3) {
                            countError++;
                        }
                        countChildren++;
                    }
                    if (ds.hasChild("email") && ds.hasChild("phone")) {
                        vTxt_mainEmail.setText(ds.child("email").getValue(String.class));
                        vTxt_mainPhone.setText(ds.child("phone").getValue(int.class).toString());
                    }
                }
                System.out.println(countError + "||" + countStopped + "||" + countRunning);
                if (countChildren != (countError + countRunning + countStopped)) {
                    btn_machineStatusReset.setImageTintList(getColorStateList(R.color.grey));
                } else if (countStopped > countError) {
                    btn_machineStatusReset.setImageTintList(getColorStateList(R.color.red));
                } else if (countError > countRunning) {
                    btn_machineStatusReset.setImageTintList(getColorStateList(R.color.yellow));
                } else if (countRunning > countStopped && countRunning > countError) {
                    btn_machineStatusReset.setImageTintList(getColorStateList(R.color.green));
                }
                countRunning = 0;
                countStopped = 0;
                countError = 0;
                countChildren = 0;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        btn_machineStatusReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra(Activity.ACTIVITY_SERVICE, MainActivity.this.getClass().getSimpleName());
                i.setClass(MainActivity.this, List.class);
                startActivity(i);
            }
        });

        btnMachineStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()) {
                            if (ds.hasChild("status")) {
                                System.out.println(ds.getKey());
                                rootRef.child(ds.getKey()).child("status").setValue(0);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
        btnMachineStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()) {
                            if (ds.hasChild("status")) {
                                System.out.println(ds.getKey());
                                rootRef.child(ds.getKey()).child("status").setValue(1);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });

        // Presence Check with Firebase (if online do, else do)
        dbConnected.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (Boolean.TRUE.equals(snapshot.getValue(Boolean.class))) {
                    connected = TRUE;
                    //onDisconnect does not have child() so using updateChildren instead with inline Hash map
                    rootRef.onDisconnect().updateChildren(new HashMap(){{ put("userConnected", FALSE);}});
                    rootRef.updateChildren(new HashMap(){{ put("userConnected", TRUE);}});
                    System.out.println("connected");
                    toolBar.setTitle(R.string.activity_main);
                    btnMachineStart.setEnabled(TRUE);
                    btnMachineStop.setEnabled(TRUE);

                } else {
                    connected = FALSE;
                    System.out.println("disconnected");
                    toolBar.setTitle(getText(R.string.activity_main) +" "+ getText(R.string.txt_OFFLINE));
                    // ATTEMPT TO DISABLE BUTTONS
                    btnMachineStart.setEnabled(FALSE);
                    btnMachineStop.setEnabled(FALSE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });

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

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                i.putExtra(Activity.ACTIVITY_SERVICE, MainActivity.this.getClass().getSimpleName());
                switch(item.getItemId()) {
                    case R.id.btn_menuHome:
                        thisClicked();
                        return true;
                    case R.id.btn_menuMacList:
                        i.setClass(MainActivity.this, List.class);
                        startActivity(i);
                        toolBar.setNavigationIcon(R.drawable.ic_menu_24);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.btn_menuSettings:
                        i.setClass(MainActivity.this, Setting.class);
                        startActivity(i);
                        toolBar.setNavigationIcon(R.drawable.ic_menu_24);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                }
                return false;
            }
        });
        // ---- END : NAVIGATION DRAWER ----

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void thisClicked() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            toolBar.setNavigationIcon(R.drawable.ic_menu_24);
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            toolBar.setNavigationIcon(R.drawable.ic_arrow_back_24);
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Intent i = getIntent();
        //String callingActivity = i.getStringExtra(Activity.ACTIVITY_SERVICE);
        if (item.getItemId() == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                toolBar.setNavigationIcon(R.drawable.ic_menu_24);
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                toolBar.setNavigationIcon(R.drawable.ic_arrow_back_24);
                drawerLayout.openDrawer(GravityCompat.START);
            }
        }
        return true;
    }
    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu, menu);
//        return true;
//    }
}