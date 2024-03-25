package Domain;

import java.util.List;

public class Bilet implements Identifiable<Integer> {

    private int id;
    private Zbor zbor;
    private String numeClient;
    private String adresaClient;
    private List<String> turisti;
    private int numarLocuri;

    public Bilet(Zbor zbor, String numeClient, String adresaClient, List<String> turisti) {
        this.zbor = zbor;
        this.numeClient = numeClient;
        this.adresaClient = adresaClient;
        this.turisti = turisti;
        SetNumarLocuri(turisti);
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Zbor getZbor() {
        return zbor;
    }

    public void setZbor(Zbor zbor) {
        this.zbor = zbor;
    }

    public String getNumeClient() {
        return numeClient;
    }

    public void setNumeClient(String numeClient) {
        this.numeClient = numeClient;
    }

    public String getAdresaClient() {
        return adresaClient;
    }

    public void setAdresaClient(String adresaClient) {
        this.adresaClient = adresaClient;
    }

    public List<String> getTuristi() {
        return turisti;
    }

    public void setTuristi(List<String> turisti) {
        this.turisti = turisti;
    }

    public int GetNumarLocuri()
    {
        return turisti.size() + 1;
    }

    public void SetNumarLocuri(List<String> turisti)
    {
        this.numarLocuri = turisti.size() + 1;
    }
    public void setNumarLocuri(int numarLocuri)
    {
        this.numarLocuri = numarLocuri;
    }

    @Override
    public String toString() {
        return "Bilet{" +
                "id=" + id +
                ", zbor=" + zbor.toString() +
                ", numeClient='" + numeClient + '\'' +
                ", adresaClient='" + adresaClient + '\'' +
                ", turisti=" + turisti +
                ", numarLocuri=" + numarLocuri +
                '}';
    }
}
