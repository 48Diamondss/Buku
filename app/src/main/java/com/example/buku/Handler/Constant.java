package com.example.buku.Handler;

public class Constant {
    /**
     * CLASS INI DIGUNAKAN UNTUK MENAMPUNG URL URL PADA API SEHINGGA LEBIH GAMPANG MEMANGGILNYA PADA CLASS LAIN
     * kami telah mencoba beberapa cara, untuk menggunakan database pribadi (server localhost, menggunakan 000webhostapp,
     * dan akhirnya kami menggunakan hostingan bernama jagoan hosting...
     */

    //private static final String ROOT_URL = "http:///MOBILE%20APPLICATION/BUAT_UAS/"; // asli ini

    //private static final String ROOT_URL = "https://si-book.000webhostapp.com/BUAT%20UAS/"; // webb 000hosting free
    //private static final String ROOT_URL = "http://si-book.42web.io/BUAT_UAS/"; // infinity web


    private static final String ROOT_URL = "http://www.bookoemen.my.id/BUAT_UAS/";

    //BUAT AMBIL FOTO
    private static final String ROOT_URL2 = "https://www.bookoemen.my.id/BUAT_UAS/buat_foto/";


    public static final String URL_REGISTER = ROOT_URL+"Register_Percobaan.php";
    public static final String URL_LOGIN = ROOT_URL+"login.php";
    public static final String URL_UPDATE = ROOT_URL+"UPDATE.php";

    public static final String URL_NEW_BOOK = ROOT_URL2+"jsondate.php";
    public static final String URL_POPULAR = ROOT_URL2+"json.php";

    public static final String URL_ADD_CART_USER = ROOT_URL+"cartpercobaan.php";

    public static final String URL_MENAMPILKAN_DATA_CART = ROOT_URL2 + "json_Menampilkan_Cart.php";

    public static final String URL_UPDATE_CART = ROOT_URL + "UPDATE_CART.php"; // ga dipake akrena ya ga jadi digunakan

    public static final String URL_DELETE_CART_FRAGMENT_USER = ROOT_URL + "DELETE_CarT.php";


    public static final String url_web_view ="https://www.bookoemen.my.id";


    // upload pembayaran

    public static final String url_upload_bukti_pembayaran = ROOT_URL + "upload_bukti_pembayaran.php";

    public static final String url_upload_bukti_pembayaran_bukan_gambar = ROOT_URL + "TRANSACTION_ITEM.php";


    // historii

    public static final String URL_HISTORI_CART_PEMBELIANN = ROOT_URL + "history.php";
}