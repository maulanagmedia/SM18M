package gmedia.net.id.semargres2018merchant;

/**
 * Created by Bayu on 22/03/2018.
 */

class CustomRecyclerViewHistoryPenjualan {
    private String tanggal, email, jumlah_kupon, total_belanja;

    public CustomRecyclerViewHistoryPenjualan(String tanggal, String email, String jumlah_kupon, String total_belanja) {
        this.tanggal = tanggal;
        this.email = email;
        this.jumlah_kupon = jumlah_kupon;
        this.total_belanja = total_belanja;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJumlah_kupon() {
        return jumlah_kupon;
    }

    public void setJumlah_kupon(String jumlah_kupon) {
        this.jumlah_kupon = jumlah_kupon;
    }


    public String getTotal_belanja() {
        return total_belanja;
    }

    public void setTotal_belanja(String total_belanja) {
        this.total_belanja = total_belanja;
    }
}
