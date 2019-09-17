package br.com.senac.gildastore.webservice;

public class Api {
    private static final String ROOT_URL = "https://sitezasso.000webhostapp.com/gildastore/pedido/v1/Api.php?apicall=";

    public static final String URL_CREATE_GILDAAPP = ROOT_URL + "creategildaapp";
    public static final String URL_READ_GILDAAPP= ROOT_URL + "getgildaapp";
    public static final String URL_UPDATE_GILDAAPP= ROOT_URL + "updategildaapp";
    public static final String URL_DELETE_GILDAAPP = ROOT_URL + "deletegildaapp&idPedido=";
}
