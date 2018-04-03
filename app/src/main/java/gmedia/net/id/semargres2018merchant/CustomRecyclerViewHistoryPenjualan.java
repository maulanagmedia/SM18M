package gmedia.net.id.semargres2018merchant;

/**
 * Created by Bayu on 22/03/2018.
 */

class CustomRecyclerViewHistoryPenjualan {
    private String tanggal, email, jumlah_kupon, total_belanja, nama, time, user;

    public CustomRecyclerViewHistoryPenjualan(String tanggal, String email, String jumlah_kupon, String total_belanja, String nama, String time, String user) {
        this.tanggal = tanggal;
        this.email = email;
        this.jumlah_kupon = jumlah_kupon;
        this.total_belanja = total_belanja;
        this.nama = nama;
        this.time = time;
        this.user = user;
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

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
