package com.example.paulnovacovici.portfolio;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by paulnovacovici on 10/26/2017.
 */

public class FollowingAdapter extends ArrayAdapter<FollowerObject> {
    final private String auth_tok = "token 52a59d9c9bec3fdaf0cce08262b48bb0762f6b4b";

    public FollowingAdapter(@NonNull Context context, ArrayList<FollowerObject> followers) {
        super(context, 0, followers);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        // Get the data item for this position
        final FollowerObject following = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_following, parent, false);
        }
        // Lookup view for data population
        TextView user_name = (TextView) convertView.findViewById(R.id.item_follower);
        ImageView contentImageView = (ImageView) convertView.findViewById(R.id.user_image);
        LinearLayout row = (LinearLayout) convertView.findViewById(R.id.row);
        Button button = (Button) convertView.findViewById(R.id.unfollow);

        row.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment fragment = (Fragment) new ProfilePage();
                new FollowingAdapter.HttpRequestProfile((ProfilePage) fragment).execute(following.getUserName());
                FragmentTransaction ft = ((AppCompatActivity) FollowingAdapter.super.getContext()).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new HttpRequestUnfollow().execute(following.getUserName());
                remove(following);
            }
        });

        // Populate the data into the template view using the data object
        user_name.setText(following.getUserName());
        Picasso.with(getContext()).load(following.getAvatarUrl()).fit().into(contentImageView);
        // Return the completed view to render on screen
        return convertView;
    }

    /**
     * Used to create get request to parse information for ProfilePage page
     */
    private class HttpRequestProfile extends AsyncTask<String, Void, String> {
        ProfilePage pf;

        public HttpRequestProfile(ProfilePage pf){
            this.pf = pf;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL("https://api.github.com/users/" + params[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
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
                JSONObject obj = new JSONObject(json);
                String public_repos = obj.getString("public_repos");
                String avatar_url = obj.getString("avatar_url");
                String bio = obj.getString("bio");
                String name = obj.getString("name");
                String user_name = obj.getString("login");
                String following = obj.getString("following");
                String followers = obj.getString("followers");

                GithubUser user = new GithubUser(public_repos, avatar_url, bio, name, user_name, following, followers);
                pf.setAll(user);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * Used to create get request to parse information for ProfilePage page
     */
    private class HttpRequestUnfollow extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL("https://api.github.com/user/following/" + params[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestProperty ("Authorization", auth_tok);
                con.setRequestProperty( "Accept", "*/*" );
                con.setRequestMethod("DELETE");
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

        }

    }

}
