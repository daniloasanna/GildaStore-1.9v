package br.com.senac.gildastore;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
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
import br.com.senac.gildastore.webservice.RequestHandler;

public class MainActivity extends AppCompatActivity {

        private static final int CODE_GET_REQUEST=1024;
        private static final int CODE_POST_REQUEST=1025;

        EditText editTextId;
        EditText editTextNProduto;
        EditText editTextQtdProduto;
        EditText editTextMarcaProduto;

        Button buttonSalvar;

        ProgressBar progressBar;
        ListView listview;
        List<GildaStoreApp> gildaappList;
        Boolean isUpdating = false;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            progressBar= findViewById(R.id.BarradeProgresso);
            listview=findViewById(R.id.ListViewListinha);
            gildaappList= new ArrayList<>();

            editTextNProduto = findViewById(R.id.editTextNProduto);
            editTextId = findViewById(R.id.editTextId);
            editTextQtdProduto = findViewById(R.id.editTextQtdProduto);
            editTextMarcaProduto = findViewById(R.id.editTextMarcaProduto);
            buttonSalvar = findViewById(R.id.buttonSalvar);

            Button btestoque = findViewById(R.id.buttonEstoque);
            btestoque.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent intentlinklist = new Intent(MainActivity.this,SecondActivity.class);
                    startActivity(intentlinklist);
                }
            });
            buttonSalvar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isUpdating){
                        updateGildaApp();
                    }else{
                        createGildaApp();
                    }
                }
            });
            readGildaApp();
        }
        private void createGildaApp(){
            String nomeProduto= editTextNProduto.getText().toString().trim();
            String qtdProduto= editTextQtdProduto.getText().toString().trim();
            String marcaProduto= editTextMarcaProduto.getText().toString().trim();

            if(TextUtils.isEmpty(nomeProduto)){
                editTextNProduto.setError("Digite o nome do produto!");
                editTextNProduto.requestFocus();
                return;
            }
            if(TextUtils.isEmpty(qtdProduto)){
                editTextQtdProduto.setError("Digite a quantidade de produtos!");
                editTextQtdProduto.requestFocus();
                return;
            }
            if(TextUtils.isEmpty(marcaProduto)){
                editTextMarcaProduto.setError("Digite a quantidade de produtos!");
                editTextMarcaProduto.requestFocus();
                return;
            }

            HashMap<String,String> params = new HashMap<>();
            params.put("nomeProduto",nomeProduto);
            params.put("qtdProduto",qtdProduto);
            params.put("marcaProduto",marcaProduto);

            PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_CREATE_GILDAAPP,params,CODE_POST_REQUEST);
            request.execute();
        }
        private void updateGildaApp(){
            String id = editTextId.getText().toString();
            String nomeProduto= editTextNProduto.getText().toString().trim();
            String qtdProduto= editTextQtdProduto.getText().toString().trim();
            String marcaProduto= editTextMarcaProduto.getText().toString().trim();

            if(TextUtils.isEmpty(nomeProduto)){
                editTextNProduto.setError("Digite o nome do produto!");
                editTextNProduto.requestFocus();
                return;
            }
            if(TextUtils.isEmpty(qtdProduto)){
                editTextQtdProduto.setError("Digite a quantidade de produtos!");
                editTextQtdProduto.requestFocus();
                return;
            }
            if(TextUtils.isEmpty(marcaProduto)){
                editTextMarcaProduto.setError("Digite a quantidade de produtos!");
                editTextMarcaProduto.requestFocus();
                return;
            }
            HashMap<String,String> params = new HashMap<>();
            params.put("idPedido",id);
            params.put("nomeProduto",nomeProduto);
            params.put("qtdProduto",qtdProduto);
            params.put("marcaProduto",marcaProduto);

            PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_UPDATE_GILDAAPP,params,CODE_POST_REQUEST);
            request.execute();

            buttonSalvar.setText("Salvar");
            editTextNProduto.setText("");
            editTextQtdProduto.setText("");
            editTextMarcaProduto.setText("");

            isUpdating = false;
        }
        private void readGildaApp() {
            PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_GILDAAPP,null, CODE_GET_REQUEST);
            request.execute();
        }

        private void deleteGildaApp(int id){
            PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_DELETE_GILDAAPP + id,null, CODE_GET_REQUEST);
            request.execute();
        }

        private void refreshGildaAppList(JSONArray gildaapp)throws JSONException {
            gildaappList.clear();

            for(int i = 0; i < gildaapp.length(); i++){
                JSONObject obj = gildaapp.getJSONObject(i);

                gildaappList.add(new GildaStoreApp(
                        obj.getInt("idPedido"),
                        obj.getString("nomeProduto"),
                        obj.getString("qtdProduto"),
                        obj.getString("marcaProduto")
                ));
            }

            GildaAppAdapter adapter = new GildaAppAdapter(gildaappList);
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
                        Toast.makeText(MainActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
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
                super(MainActivity.this,R.layout.gildaapp_list, gildaappList);

                this.gildaappList = gildaappList;
            }

            public View getView(int position, View converView, ViewGroup parent){
                LayoutInflater inflater = getLayoutInflater();
                final View listViewItem = inflater.inflate(R.layout.gildaapp_list,null,true);

                TextView textViewNProduto = listViewItem.findViewById(R.id.textViewNProduto);

                TextView textViewQtdProduto = listViewItem.findViewById(R.id.textViewQtdProduto);

                TextView textViewMarcaProduto = listViewItem.findViewById(R.id.textViewMarcaProduto);

                TextView textViewDelete = listViewItem.findViewById(R.id.textViewDelete);

                TextView textViewAlterar = listViewItem.findViewById(R.id.textViewAlterar);

                final GildaStoreApp gildasapp = gildaappList.get(position);
                textViewNProduto.setText(gildasapp.getNomeProduto());
                textViewQtdProduto.setText(gildasapp.getQtdProduto());
                textViewMarcaProduto.setText(gildasapp.getMarcaProduto());
                textViewDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Delete " + gildasapp.getNomeProduto())
                                .setMessage("Você quer realmente deletar?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteGildaApp(gildasapp.getId());
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();


                    }
                });
                textViewAlterar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isUpdating = true;
                        editTextId.setText(String.valueOf(gildasapp.getId()));
                        editTextNProduto.setText(gildasapp.getNomeProduto());
                        editTextQtdProduto.setText(gildasapp.getQtdProduto());
                        editTextMarcaProduto.setText(gildasapp.getMarcaProduto());
                        buttonSalvar.setText("Alterar");

                    }
                });
                return listViewItem;
            }

        }
    }

