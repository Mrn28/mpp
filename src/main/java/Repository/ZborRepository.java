package Repository;

import Domain.Zbor;

import java.sql.SQLException;
import java.util.List;

public interface ZborRepository extends Repository<Integer, Zbor>{
    public List<Zbor> findByDestinationAndDate(String destination, String departureDate) throws SQLException;
    public void updateLocuriDisponibile(Integer zborId, Integer locuriDisponibile);
    public Zbor findFlightByDestinationDateAndTime(String destination, String date, String time);
}
