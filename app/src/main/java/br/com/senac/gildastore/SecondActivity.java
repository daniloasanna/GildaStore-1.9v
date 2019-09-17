package br.com.senac.gildastore;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.senac.gildastore.modelo.GildaStoreApp;
import br.com.senac.gildastore.webservice.Api;
import br.com.senac.gildastore.webservice.Api2;
import br.com.senac.gildastore.webservice.RequestHandler;

public class SecondActivity extends AppCompatActivity {
    private static final int CODE_GET_REQUEST=1024;
    private static final int CODE_POST_REQUEST=1025;

    ProgressBar progressBar;
    ListView listview;
    List<GildaStoreApp> gildaappList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        getSupportActionBar().setTitle("Estoque");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar= findViewById(R.id.BarradeProgresso);
        listview=findViewById(R.id.ListViewListinha);
        gildaappList= new ArrayList<>();


        readGildaApp();
    }

    private void readGildaApp() {
        SecondActivity.PerformNetworkRequest request = new SecondActivity.PerformNetworkRequest(Api2.URL_READ_GILDAAPP,null, CODE_GET_REQUEST);
        request.execute();
    }

    private void refreshGildaAppList(JSONArray gildaapp)throws JSONException {
        gildaappList.clear();

        for(int i = 0; i < gildaapp.length(); i++){
            JSONObject obj = gildaapp.getJSONObject(i);

            gildaappList.add(new GildaStoreApp(
                    obj.getInt("idProduto"),
                    obj.getString("nomeProduto"),
                    obj.getString("qtdProduto"),
                    obj.getString("marcaProduto")
            ));
        }

        SecondActivity.GildaAppAdapter adapter = new SecondActivity.GildaAppAdapter(gildaappList);
        listview.setAdapter(adapter);
    }
    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {
        String url;
        HashMap<String, String> params;
        int requestCode;

        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    Toast.makeText(SecondActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                    refreshGildaAppList(object.getJSONArray("gildaapp"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            if (requestCode == CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);


            if (requestCode == CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);

            return null;
        }
    }
    class GildaAppAdapter extends ArrayAdapter<GildaStoreApp> {
        List<GildaStoreApp> gildaappList;

        public GildaAppAdapter(List<GildaStoreApp> gildaappList){
            super(SecondActivity.this,R.layout.product_list, gildaappList);

            this.gildaappList = gildaappList;
        }

        public View getView(int position, View converView, ViewGroup parent){
            LayoutInflater inflater = getLayoutInflater();
            final View listViewItem = inflater.inflate(R.layout.product_list,null,true);

            TextView textViewNProduto = listViewItem.findViewById(R.id.textViewNProduto);

            TextView textViewQtdProduto = listViewItem.findViewById(R.id.textViewQtdProduto);

            TextView textViewMarcaProduto = listViewItem.findViewById(R.id.textViewMarcaProduto);

            final GildaStoreApp gildasapp = gildaappList.get(position);
            textViewNProduto.setText(gildasapp.getNomeProduto());
            textViewQtdProduto.setText(gildasapp.getQtdProduto());
            textViewMarcaProduto.setText(gildasapp.getMarcaProduto());

            return listViewItem;
        }

    }
}
