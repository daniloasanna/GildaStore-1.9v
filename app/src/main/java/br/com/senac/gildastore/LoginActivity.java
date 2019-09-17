package br.com.senac.gildastore;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText login, senha;
    Button buttonLogin;
    ProgressBar loading;
    static String URL_LOGIN = "https://sitezasso.000webhostapp.com/gildastore/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.loginCpf);
        senha = findViewById(R.id.loginSenha);
        loading = findViewById(R.id.loading);
        buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginc = login.getText().toString().trim();
                String senhac = senha.getText().toString().trim();

                if(!loginc.isEmpty() || !senhac.isEmpty()){
                    Login(loginc, senhac);
                }else {
                    login.setError("Por favor insira o CPF");
                    senha.setError("Por favor insira a senha");
                }
            }
        });
    }

    private void Login(final String login, final String senha){
        loading.setVisibility(View.VISIBLE);
        buttonLogin.setVisibility(View.GONE);


        StringRequest stringrequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonobject = new JSONObject(response);
                            String success = jsonobject.getString("success");
                            JSONArray jsonArray = jsonobject.getJSONArray("login");

                            if(success.equals("1")){
                                for(int i=0; i<jsonArray.length();i++){
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String name = object.getString("nomeUsuario").trim();

                                    Toast.makeText(LoginActivity.this,
                                            "Bem Vindo",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        }catch (JSONException e){
                            loading.setVisibility(View.GONE);
                            buttonLogin.setVisibility(View.VISIBLE);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.setVisibility(View.GONE);
                        buttonLogin.setVisibility(View.VISIBLE);
                        Toast.makeText(LoginActivity.this, "Erro " + error.toString(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();
                params.put("cpfUsuario", login);
                params.put("senhaUsuario",senha);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringrequest);
    }


}
