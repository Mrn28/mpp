package Repository;

import Domain.Bilet;

public interface BiletRepository extends Repository<Integer, Bilet>{
    public boolean sellFlightTicket(int zborId, int numarLocuriDorite);
}
