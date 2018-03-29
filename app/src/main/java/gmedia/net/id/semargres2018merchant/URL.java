package gmedia.net.id.semargres2018merchant;

/**
 * Created by Bayu on 15/03/2018.
 */

public class URL {

    private static final String baseURL = "http://semargres.gmedia.id/";
    public static String urlLogin = baseURL + "merchant/auth";
    public static String urlProfile = baseURL + "merchant/profile";
    public static String urlSettingKupon = baseURL + "merchant/kupon";
    public static String urlSendEmail = baseURL + "merchant/email_kupon";
    public static String urlGantiPassword = baseURL + "merchant/ganti_password";
    public static String urlScanBarcode = baseURL + "merchant/scan_barcode";
    public static String urlViewPromo = baseURL + "merchant/view_promo";
    public static String urlSettingPromo = baseURL + "merchant/create_promo";
    public static String urlKategori = baseURL + "merchant/ms_kategori";
    public static String urlEditProfile = baseURL + "merchant/edit";
    public static String urlDashboard = baseURL + "merchant/dashboard";
    public static String urlHistoryPenjualan = baseURL + "merchant/transaksi";
    public static String urlEditPromo = baseURL + "merchant/view_promo/";
    public static String urlDeletePromo = baseURL + "merchant/delete_promo";
    public static String urlResetPassword = baseURL + "merchant/reset";
    public static String urlCheckVersion = baseURL + "latest_version/merchant";
}
