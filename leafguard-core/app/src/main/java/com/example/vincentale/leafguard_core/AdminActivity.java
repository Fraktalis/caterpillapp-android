package com.example.vincentale.leafguard_core;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vincentale.leafguard_core.fragment.admin.AdminUserFragment;
import com.example.vincentale.leafguard_core.fragment.admin.OakMapFragment;
import com.example.vincentale.leafguard_core.model.User;
import com.example.vincentale.leafguard_core.model.UserManager;
import com.example.vincentale.leafguard_core.util.DatabaseCallback;
import com.google.firebase.database.DatabaseError;

public class AdminActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdminUserFragment.OnFragmentInteractionListener {

    private TextView adminDrawerFullName;
    private TextView adminDrawerEmail;

    private UserManager userManager;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userManager = UserManager.getInstance();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        adminDrawerFullName = (TextView) header.findViewById(R.id.adminDrawerFullName);
        adminDrawerEmail = (TextView) header.findViewById(R.id.adminDrawerEmail);

        userManager.getUser(new DatabaseCallback<User>() {
            @Override
            public void onSuccess(User identifiable) {
                user = identifiable;
                adminDrawerFullName.setText(user.getDisplayName());
                adminDrawerEmail.setText(user.getEmail());
            }

            @Override
            public void onFailure(DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                userManager.signOut();
                Toast.makeText(this, R.string.log_out_success, Toast.LENGTH_SHORT).show();
                Intent loginIntent = new Intent(this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
                break;
            case R.id.action_profile:
                Intent profilFormIntent = new Intent(this, ProfileActivity.class);
                startActivity(profilFormIntent);
                break;
            case R.id.action_admin:
                break;
            default: break;
        }
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.nav_users) {
            AdminUserFragment userFragment = AdminUserFragment.newInstance();
            fragmentManager.beginTransaction().replace(R.id.admin_content_layout, userFragment).commit();

        } else if (id == R.id.nav_oaks) {

            OakMapFragment userFragment = OakMapFragment.newInstance();
            fragmentManager.beginTransaction().replace(R.id.admin_content_layout, userFragment).commit();

        } else if (id == R.id.nav_observations) {

        } else if (id == R.id.nav_notifications) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
