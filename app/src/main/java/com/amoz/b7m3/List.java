package com.amoz.b7m3;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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

public class List extends AppCompatActivity {

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

    DatabaseReference mac_01 = rootRef.child("CNC_001");
    DatabaseReference mac_02 = rootRef.child("CNC_002");
    DatabaseReference mac_03 = rootRef.child("CNC_003");
    DatabaseReference mac_04 = rootRef.child("CNC_004");
    DatabaseReference mac_05 = rootRef.child("CNC_005");
    DatabaseReference mac_06 = rootRef.child("CNC_006");
    DatabaseReference mac_07 = rootRef.child("CNC_007");
    DatabaseReference mac_08 = rootRef.child("CNC_008");
    // ---- END : FIREBASE ----

    // ---- START : MACHINE LIST ----
    TextView lblMac01, lblMac02, lblMac03, lblMac04, lblMac05, lblMac06, lblMac07, lblMac08;
    ImageButton imgMac01, imgMac02, imgMac03, imgMac04, imgMac05, imgMac06, imgMac07, imgMac08;
    // ---- END : MACHINE LIST ----

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Intent i = getIntent();
        String callingActivity = i.getStringExtra(Activity.ACTIVITY_SERVICE);
        System.out.println("CallingActivity:" + callingActivity);

        // ---- START : MACHINE LIST ----
        lblMac01 = findViewById(R.id.lbl_mac_01);
        lblMac02 = findViewById(R.id.lbl_mac_02);
        lblMac03 = findViewById(R.id.lbl_mac_03);
        lblMac04 = findViewById(R.id.lbl_mac_04);
        lblMac05 = findViewById(R.id.lbl_mac_05);
        lblMac06 = findViewById(R.id.lbl_mac_06);
        lblMac07 = findViewById(R.id.lbl_mac_07);
        lblMac08 = findViewById(R.id.lbl_mac_08);

        imgMac01 = findViewById(R.id.img_mac_01);
        imgMac02 = findViewById(R.id.img_mac_02);
        imgMac03 = findViewById(R.id.img_mac_03);
        imgMac04 = findViewById(R.id.img_mac_04);
        imgMac05 = findViewById(R.id.img_mac_05);
        imgMac06 = findViewById(R.id.img_mac_06);
        imgMac07 = findViewById(R.id.img_mac_07);
        imgMac08 = findViewById(R.id.img_mac_08);
        // ---- END : MACHINE LIST ----

        mac_01.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lblMac01.setText(snapshot.getKey() + " (" + snapshot.child("type").getValue() + ")");
                int checkMe = snapshot.child("status").getValue(int.class);
                int colorId = R.color.grey;
                if (checkMe == 0) {
                    colorId = R.color.red;
                } else if (checkMe == 1) {
                    colorId = R.color.green;
                } else if (checkMe == 3) {
                    colorId = R.color.yellow;
                }
                imgMac01.setImageTintList(getColorStateList(colorId));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        mac_02.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lblMac02.setText(snapshot.getKey() + " (" + snapshot.child("type").getValue() + ")");
                int checkMe = snapshot.child("status").getValue(int.class);
                int colorId = R.color.grey;
                if (checkMe == 0) {
                    colorId = R.color.red;
                } else if (checkMe == 1) {
                    colorId = R.color.green;
                } else if (checkMe == 3) {
                    colorId = R.color.yellow;
                }
                imgMac02.setImageTintList(getColorStateList(colorId));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        mac_03.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lblMac03.setText(snapshot.getKey() + " (" + snapshot.child("type").getValue() + ")");
                int checkMe = snapshot.child("status").getValue(int.class);
                int colorId = R.color.grey;
                if (checkMe == 0) {
                    colorId = R.color.red;
                } else if (checkMe == 1) {
                    colorId = R.color.green;
                } else if (checkMe == 3) {
                    colorId = R.color.yellow;
                }
                imgMac03.setImageTintList(getColorStateList(colorId));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        mac_04.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lblMac04.setText(snapshot.getKey() + " (" + snapshot.child("type").getValue() + ")");
                int checkMe = snapshot.child("status").getValue(int.class);
                int colorId = R.color.grey;
                if (checkMe == 0) {
                    colorId = R.color.red;
                } else if (checkMe == 1) {
                    colorId = R.color.green;
                } else if (checkMe == 3) {
                    colorId = R.color.yellow;
                }
                imgMac04.setImageTintList(getColorStateList(colorId));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        mac_05.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lblMac05.setText(snapshot.getKey() + " (" + snapshot.child("type").getValue() + ")");
                int checkMe = snapshot.child("status").getValue(int.class);
                int colorId = R.color.grey;
                if (checkMe == 0) {
                    colorId = R.color.red;
                } else if (checkMe == 1) {
                    colorId = R.color.green;
                } else if (checkMe == 3) {
                    colorId = R.color.yellow;
                }
                imgMac05.setImageTintList(getColorStateList(colorId));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        mac_06.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lblMac06.setText(snapshot.getKey() + " (" + snapshot.child("type").getValue() + ")");
                int checkMe = snapshot.child("status").getValue(int.class);
                int colorId = R.color.grey;
                if (checkMe == 0) {
                    colorId = R.color.red;
                } else if (checkMe == 1) {
                    colorId = R.color.green;
                } else if (checkMe == 3) {
                    colorId = R.color.yellow;
                }
                imgMac06.setImageTintList(getColorStateList(colorId));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        mac_07.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lblMac07.setText(snapshot.getKey() + " (" + snapshot.child("type").getValue() + ")");
                int checkMe = snapshot.child("status").getValue(int.class);
                int colorId = R.color.grey;
                if (checkMe == 0) {
                    colorId = R.color.red;
                } else if (checkMe == 1) {
                    colorId = R.color.green;
                } else if (checkMe == 3) {
                    colorId = R.color.yellow;
                }
                imgMac07.setImageTintList(getColorStateList(colorId));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        mac_08.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lblMac08.setText(snapshot.getKey() + " (" + snapshot.child("type").getValue() + ")");
                int checkMe = snapshot.child("status").getValue(int.class);
                int colorId = R.color.grey;
                if (checkMe == 0) {
                    colorId = R.color.red;
                } else if (checkMe == 1) {
                    colorId = R.color.green;
                } else if (checkMe == 3) {
                    colorId = R.color.yellow;
                }
                imgMac08.setImageTintList(getColorStateList(colorId));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        imgMac01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.setClass(List.this, Details.class);
                i.removeExtra("MACHINE_REF");
                i.putExtra("MACHINE_REF", mac_01.getKey());
                startActivity(i);
            }
        });
        imgMac02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.setClass(List.this, Details.class);
                i.removeExtra("MACHINE_REF");
                i.putExtra("MACHINE_REF", mac_02.getKey());
                startActivity(i);
            }
        });
        imgMac03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.setClass(List.this, Details.class);
                i.removeExtra("MACHINE_REF");
                i.putExtra("MACHINE_REF", mac_03.getKey());
                startActivity(i);
            }
        });
        imgMac04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.setClass(List.this, Details.class);
                i.removeExtra("MACHINE_REF");
                i.putExtra("MACHINE_REF", mac_04.getKey());
                startActivity(i);
            }
        });
        imgMac05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.setClass(List.this, Details.class);
                i.removeExtra("MACHINE_REF");
                i.putExtra("MACHINE_REF", mac_05.getKey());
                startActivity(i);
            }
        });
        imgMac06.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.setClass(List.this, Details.class);
                i.removeExtra("MACHINE_REF");
                i.putExtra("MACHINE_REF", mac_06.getKey());
                startActivity(i);
            }
        });
        imgMac07.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.setClass(List.this, Details.class);
                i.removeExtra("MACHINE_REF");
                i.putExtra("MACHINE_REF", mac_07.getKey());
                startActivity(i);
            }
        });
        imgMac08.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.setClass(List.this, Details.class);
                i.removeExtra("MACHINE_REF");
                i.putExtra("MACHINE_REF", mac_08.getKey());
                startActivity(i);
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
                    toolBar.setTitle(R.string.activity_list);
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

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                i.putExtra(Activity.ACTIVITY_SERVICE, List.this.getClass().getSimpleName());
                switch(item.getItemId()) {
                    case R.id.btn_menuHome:
                        finish();
                        return true;
                    case R.id.btn_menuMacList:
                        thisClicked();
                        return true;
                    case R.id.btn_menuSettings:
                        i.setClass(List.this, Setting.class);
                        startActivity(i);
                        toolBar.setNavigationIcon(R.drawable.ic_menu_24);
                        drawerLayout.closeDrawer(GravityCompat.START);
                }
                return false;
            }
        });
        // ---- END : NAVIGATION DRAWER ----
    }

    // ---- START: NAVIGATION DRAWER ----
    /*@Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }*/

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
    // ---- END: NAVIGATION DRAWER ----
}