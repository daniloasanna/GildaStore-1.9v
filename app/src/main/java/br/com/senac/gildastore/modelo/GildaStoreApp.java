package br.com.senac.gildastore.modelo;

public class GildaStoreApp {
    private int id;
    private String nomeProduto;
    private String qtdProduto;
    private String marcaProduto;

    public GildaStoreApp(int id, String nomeProduto, String qtdProduto, String marcaProduto){
        this.id=id;
        this.qtdProduto=qtdProduto;
        this.nomeProduto=nomeProduto;
        this.marcaProduto=marcaProduto;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getMarcaProduto() {
        return marcaProduto;
    }

    public void setMarcaProduto(String marcaProduto) {
        this.marcaProduto = marcaProduto;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getQtdProduto() {
        return qtdProduto;
    }

    public void setQtdProduto(String qtdProduto) {
        this.qtdProduto = qtdProduto;
    }

}
