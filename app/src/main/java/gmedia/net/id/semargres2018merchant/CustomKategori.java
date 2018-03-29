package gmedia.net.id.semargres2018merchant;

/**
 * Created by Bayu on 20/03/2018.
 */

public class CustomKategori {

    private String id, nama;

    public CustomKategori(String id, String nama) {
        this.id=id;
        this.nama=nama;
    }

    @Override
    public String toString(){
        return this.nama;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
