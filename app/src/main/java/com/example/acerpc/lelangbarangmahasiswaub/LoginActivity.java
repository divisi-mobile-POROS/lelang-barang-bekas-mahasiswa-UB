package com.example.acerpc.lelangbarangmahasiswaub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.acerpc.lelangbarangmahasiswaub.Network.Service;
import com.example.acerpc.lelangbarangmahasiswaub.Network.SiamGenerator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private String html;
    static private String cookie;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        sharedPreferences = getSharedPreferences("account", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (!sharedPreferences.getString("username", "").equals("")) {
            etUsername.setText(sharedPreferences.getString("username", ""));
        }

        if (!sharedPreferences.getString("password", "").equals("")) {
            etPassword.setText(sharedPreferences.getString("password", ""));
        }

        //get unAuthenticated token
        getCookie();
    }

    @OnClick(R.id.btn_login)
    public void submit() {
        //authenticated token
        login();
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }

    private void disableProgressBar() {
        progressBar.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void getCookie() {
        Service service = SiamGenerator.createService(Service.class);

        Call<String> call = service.getCookie();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                html = response.body().toString();
                cookie = response.headers().get("Set-Cookie");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }


    private void login() {
        showProgressBar();

        String username = etUsername.getText().toString();
        editor.putString("username", username);
        String password = etPassword.getText().toString();
        editor.putString("password", password);
        editor.apply();

        Service service = SiamGenerator.createService(Service.class);

        Call<String> call = service.login(
                cookie,
                RequestBody.create(MediaType.parse("text/plain"), etUsername.getText().toString()),
                RequestBody.create(MediaType.parse("text/plain"), etPassword.getText().toString()),
                RequestBody.create(MediaType.parse("text/plain"), "Masuk"));

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                // after token authenticated,
                // get jadwal with authenticated token
                String resp = response.body();
                List<String> items = Arrays.asList(resp.split("<div><span class=\"label\">Program Studi<i class=\"fa fa-angle-right\"></i></span>"));
                try {
                    List<String> prodi = Arrays.asList(items.get(1).split("</div>"));
                    Log.d("zxcresp", "onResponse: "+prodi.get(0));
                    editor.putString("prodi", prodi.get(0));
                    editor.apply();
                    //Toast.makeText(LoginActivity.this, "test", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    disableProgressBar();
                    Toast.makeText(LoginActivity.this, "Password salah", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                disableProgressBar();
            }
        });
    }


}
