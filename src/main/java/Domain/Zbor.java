package Domain;

import java.time.LocalDate;
import java.time.LocalTime;

public class Zbor implements Identifiable<Integer> {

    private int id;
    private String destinatie;
    private LocalDate dataPlecare;
    private LocalTime oraPlecare;
    private String aeroport;
    private int locuriDisponibile;

    public Zbor(String destinatie, LocalDate dataPlecare, LocalTime oraPlecare, String aeroport, int locuriDisponibile) {
        this.destinatie = destinatie;
        this.dataPlecare = dataPlecare;
        this.oraPlecare = oraPlecare;
        this.aeroport = aeroport;
        this.locuriDisponibile = locuriDisponibile;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getDestinatie() {
        return destinatie;
    }

    public void setDestinatie(String destinatie) {
        this.destinatie = destinatie;
    }

    public LocalDate getDataPlecare() {
        return dataPlecare;
    }

    public void setDataPlecare(LocalDate dataPlecare) {
        this.dataPlecare = dataPlecare;
    }

    public LocalTime getOraPlecare() {
        return oraPlecare;
    }

    public void setOraPlecare(LocalTime oraPlecare) {
        this.oraPlecare = oraPlecare;
    }

    public String getAeroport() {
        return aeroport;
    }

    public void setAeroport(String aeroport) {
        this.aeroport = aeroport;
    }

    public int getLocuriDisponibile() {
        return locuriDisponibile;
    }

    public void setLocuriDisponibile(int locuriDisponibile) {
        this.locuriDisponibile = locuriDisponibile;
    }

    @Override
    public String toString() {
        return "Zbor{" +
                "id=" + id +
                ", destinatie='" + destinatie + '\'' +
                ", dataPlecare=" + dataPlecare +
                ", oraPlecare=" + oraPlecare +
                ", aeroport='" + aeroport + '\'' +
                ", locuriDisponibile=" + locuriDisponibile +
                '}';
    }
}
