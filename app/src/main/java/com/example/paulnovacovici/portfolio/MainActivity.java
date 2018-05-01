package com.example.paulnovacovici.portfolio;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private ProfilePage portfolio;
    private repopage rp;
    private FollowersPage followersp;
    private FollowingPage followingp;
    private GithubUser original_user;
    private SearchPage sp;
    private FirebaseDatabase db;

    final private String auth_tok = "token 52a59d9c9bec3fdaf0cce08262b48bb0762f6b4b";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setupDrawer();

        db = FirebaseDatabase.getInstance();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.portfolio = new ProfilePage();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, this.portfolio);
        ft.commit();
        this.rp = new repopage();
        this.followersp = new FollowersPage();
        this.followingp = new FollowingPage();
        this.sp = new SearchPage();
        new HttpRequestProfile().execute();
        new HttpRequestRepos().execute();
        new HttpRequestFollowersFollowing(true, "https://api.github.com/users/novacov2/followers").execute();
        new HttpRequestFollowersFollowing(false, "https://api.github.com/users/novacov2/following").execute();
    }

    /**
     * Sets up the drawer to go from page to page
     */
    private void setupDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * On click event that goes to public repos page
     * @param view
     */
    public void goToPublicRepos(View view) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, rp);
        ft.addToBackStack(null);
        ft.commit();
    }

    /**
     * On click event allowing user to go to followers page
     * @param view
     */
    public void goToFollowers(View view) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, followersp);
        ft.addToBackStack(null);
        ft.commit();
    }

    /**
     * Goes to following page when clicked
     * @param view
     */
    public void goToFollowing(View view) {
        new HttpRequestFollowersFollowing(false, "https://api.github.com/users/novacov2/following").execute();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, followingp);
        ft.addToBackStack(null);
        ft.commit();
    }

    /**
     * Display screen underneath drawer
     * @param id
     */
    private void displaySelectedScreen(int id){
        Fragment fragment = null;

        switch (id){
            case R.id.pf:
                new HttpRequestProfile().execute();
                fragment = this.portfolio;
                break;
            case R.id.followers:
                fragment = followersp;
                break;
            case R.id.following:
                new HttpRequestFollowersFollowing(false, "https://api.github.com/users/novacov2/following").execute();
                fragment = followingp;
                break;
            case R.id.repos:
                fragment = rp;
                break;
            case R.id.search:
                fragment = sp;
                break;
        }

        if (fragment != null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        displaySelectedScreen(id);
        return true;
    }

    /**
     * Used to create get request to parse information for ProfilePage page
     */
    private class HttpRequestProfile extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL("https://api.github.com/users/novacov2");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestProperty ("Authorization", auth_tok);
                con.setRequestProperty( "Accept", "*/*" );
                con.setRequestMethod("GET");
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                return content.toString();
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String json) {
            try {
                JSONObject obj = new JSONObject(json);
                String public_repos = obj.getString("public_repos");
                String avatar_url = obj.getString("avatar_url");
                String bio = obj.getString("bio");
                String name = obj.getString("name");
                String user_name = obj.getString("login");
                String following = obj.getString("following");
                String followers = obj.getString("followers");

                original_user = new GithubUser(public_repos, avatar_url, bio, name, user_name, following, followers);
                DatabaseReference dbPersonal = db.getReference("personal");
                String id = dbPersonal.push().getKey();
                dbPersonal.child(id).setValue(original_user);
                portfolio.setAll(original_user);
            } catch (JSONException e) {
                Log.e("MainActivity", e.getMessage(), e);
                e.printStackTrace();
            }
        }

    }

    private class HttpRequestRepos extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL _url = new URL("https://api.github.com/users/novacov2/repos");
                HttpURLConnection con = (HttpURLConnection) _url.openConnection();
                con.setRequestMethod("GET");
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                return content.toString();
            }
            catch (Exception e){
                Log.e("MainActivity", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String json) {
            try {
                DatabaseReference dbRepos = db.getReference("repos");
                JSONArray repos_json = new JSONArray(json);
                int size = repos_json.length();
                ArrayList<RepoObject> repos = new ArrayList<RepoObject>();

                for (int i = 0; i < size; i++){
                    JSONObject obj = repos_json.getJSONObject(i);
                    RepoObject info = new RepoObject(obj.getString("name"),
                            obj.getJSONObject("owner").getString("login"),
                            obj.getString("description"), obj.getString("url"));
                    String id = dbRepos.push().getKey();
                    dbRepos.child(id).setValue(info);
                    repos.add(info);
                }
                rp.setAdapter(repos);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class HttpRequestFollowersFollowing extends AsyncTask<Void, Void, String> {
        String url;
        boolean followers;

        public HttpRequestFollowersFollowing(boolean followers, String url){
            this.url = url;
            this.followers = followers;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL _url = new URL(this.url);
                HttpURLConnection con = (HttpURLConnection) _url.openConnection();
                con.setRequestMethod("GET");
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                return content.toString();
            }
            catch (Exception e){
                Log.e("MainActivity", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String json) {
            try {
                JSONArray repos_json = new JSONArray(json);
                int size = repos_json.length();
                ArrayList<FollowerObject> followers = new ArrayList<FollowerObject>();
                DatabaseReference dbFollowing = db.getReference("following");
                DatabaseReference dbFollowers = db.getReference("followers");

                for (int i = 0; i < size; i++){
                    JSONObject obj = repos_json.getJSONObject(i);
                    FollowerObject info = new FollowerObject(obj.getString("login"), obj.getString("url"), obj.getString("avatar_url"));
                    followers.add(info);

                    if (this.followers){
                        String id = dbFollowers.push().getKey();
                        dbFollowers.child(id).setValue(info);
                    }
                    else {
                        String id = dbFollowing.push().getKey();
                        dbFollowing.child(id).setValue(info);
                    }
                }

                if (this.followers) {
                    followersp.setArrayList(followers);
                }
                else{
                    followingp.setArrayList(followers);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

