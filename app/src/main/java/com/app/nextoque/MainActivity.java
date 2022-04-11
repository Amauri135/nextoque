package com.app.nextoque;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.app.nextoque.controller.LoginFragment;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.fragment_container);

        setSupportActionBar(toolbar);

        Runnable inicialFragmentRunnable = new Runnable() {
            @Override
            public void run() {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment()).commit();
            }
        };

        new Thread(inicialFragmentRunnable).start();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            View nav_view = findViewById(R.id.navigation_view);

            if(nav_view != null && nav_view.getVisibility() == View.VISIBLE){
                nav_view.setVisibility(View.GONE);
            } else if(getSupportFragmentManager().getBackStackEntryCount() == 2 ) {
                FirebaseAuth.getInstance().signOut();
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().popBackStack();
            } else {
                getSupportFragmentManager().popBackStack();
            }
        }
    }
}