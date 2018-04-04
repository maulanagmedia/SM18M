package gmedia.net.id.semargres2018merchant;

/**
 * Created by Bayu on 19/03/2018.
 */

public class CustomRecyclerViewCreatePromo {
    private String id, gambar, judul, status, idKategori;

    public CustomRecyclerViewCreatePromo(String id, String gambar, String judul, String status, String idKategori) {
        this.id=id;
        this.gambar = gambar;
        this.judul = judul;
        this.status = status;
        this.idKategori = idKategori;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdKategori() {
        return idKategori;
    }

    public void setIdKategori(String idKategori) {
        this.idKategori = idKategori;
    }
}
