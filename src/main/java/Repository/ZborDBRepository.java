package Repository;

import Domain.Zbor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ZborDBRepository implements ZborRepository {
    private JdbcUtils dbUtils;



    private static final Logger logger= LogManager.getLogger();

    public ZborDBRepository(Properties props) {
        logger.info("Initializing ZborDBRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }

    @Override
    public void save(Zbor elem) {
        logger.traceEntry("saving flight {} ", elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("INSERT INTO flights(destination, departureDate, departureTime, airport, nrOfSeats) VALUES (?, ?, ?, ?, ?)")) {
            preStmt.setString(1, elem.getDestinatie());

            // Format LocalDate as "YYYY-MM-DD"
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = elem.getDataPlecare().format(dateFormatter);
            preStmt.setString(2, formattedDate);

            // Format LocalTime as "HH:MM"
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            String formattedTime = elem.getOraPlecare().format(timeFormatter);
            preStmt.setString(3, formattedTime);

            preStmt.setString(4, elem.getAeroport());
            preStmt.setInt(5, elem.getLocuriDisponibile());
            int result = preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit();
    }



    @Override
    public void update(Zbor elem) {
        logger.traceEntry("updating flight with id {} ",elem.getId());
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("update flights set destination=?, departureDate=?, departureTime=?, airport=?, nrOfSeats=? where id=?")){
            preStmt.setString(1,elem.getDestinatie());
            preStmt.setString(2,elem.getDataPlecare().toString());
            preStmt.setString(3,elem.getOraPlecare().toString());
            preStmt.setString(4,elem.getAeroport());
            preStmt.setInt(5,elem.getLocuriDisponibile());
            preStmt.setInt(6,elem.getId());
            int result=preStmt.executeUpdate();
            logger.trace("Updated {} instances",result);
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit();
    }

    @Override
    public List<Zbor> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Zbor> flights = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("SELECT * FROM flights")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String destination = result.getString("destination");

                    LocalDate departureDate = LocalDate.parse(result.getString("departureDate"));
                    LocalTime departureTime = LocalTime.parse(result.getString("departureTime"));

                    String airport = result.getString("airport");
                    int nrOfSeats = result.getInt("nrOfSeats");

                    Zbor zbor = new Zbor(destination, departureDate, departureTime, airport, nrOfSeats);
                    zbor.setId(id);
                    flights.add(zbor);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB " + e);
        }
        logger.traceExit(flights);
        return flights;
    }


    @Override
    public Zbor findById(Integer integer) {
        logger.traceEntry("finding flight with id {} ", integer);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select * from flights where id=?")){
            preStmt.setInt(1, integer);
            try(ResultSet result=preStmt.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("id");
                    String destination = result.getString("destination");

                    LocalDate departureDate = LocalDate.parse(result.getString("departureDate"));
                    LocalTime departureTime = LocalTime.parse(result.getString("departureTime"));

                    String airport = result.getString("airport");
                    int nrOfSeats = result.getInt("nrOfSeats");

                    Zbor zbor = new Zbor(destination, departureDate, departureTime, airport, nrOfSeats);
                    zbor.setId(id);
                    logger.traceExit(zbor);
                    return zbor;
                }
            }
        }catch (SQLException e){
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit("No flight found with id {}",integer);
        return null;
    }

    @Override
    public void deleteById(Integer integer) {
        logger.traceEntry("deleting flight with id {} ",integer);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("delete from flights where id=?")){
            preStmt.setInt(1,integer);
            int result=preStmt.executeUpdate();
            logger.trace("Deleted {} instances",result);
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit();
    }

//    public List<Zbor> findByDestinationAndDate(String destination, String departureDate) throws SQLException {
//        List<Zbor> zboruri = new ArrayList<>();
//        String query = "SELECT * FROM flights WHERE destination=? AND departureDate=?";
//        Connection con=dbUtils.getConnection();
//        try (PreparedStatement statement = con.prepareStatement(query)) {
//            statement.setString(1, destination);
//            statement.setString(2, departureDate);
//
//            try (ResultSet result = statement.executeQuery()) {
//                while (result.next()) {
//                    int id = result.getInt("id");
//                    String dest = result.getString("destination");
//
//                    LocalDate departure = LocalDate.parse(result.getString("departureDate"));
//                    LocalTime departureTime = LocalTime.parse(result.getString("departureTime"));
//
//                    String airport = result.getString("airport");
//                    int nrOfSeats = result.getInt("nrOfSeats");
//
//                    Zbor zbor = new Zbor(dest, departure, departureTime, airport, nrOfSeats);
//                    zbor.setId(id);
//                    zboruri.add(zbor);
//                }
//            }
//        } catch (SQLException e) {
//            logger.error("Error occurred while searching for flights", e);
//            throw e;
//        }
//
//        return zboruri;
//    }


    @Override
    public List<Zbor> findByDestinationAndDate(String destination, String departureDate) throws SQLException {
        List<Zbor> zboruri = new ArrayList<>();
        String query = "SELECT f.id, f.destination, f.departureDate, f.departureTime, f.airport, f.nrOfSeats, " +
                "(f.nrOfSeats - COALESCE(SUM(t.nrOfTourists), 0)) AS locuriDisponibile " +
                "FROM flights f LEFT JOIN tickets t ON f.id = t.zborID " +
                "WHERE f.destination=? AND f.departureDate=?";
        Connection con = dbUtils.getConnection();
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, destination);
            statement.setString(2, departureDate);

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String dest = result.getString("destination");

                    LocalDate departure = LocalDate.parse(result.getString("departureDate"));
                    LocalTime departureTime = LocalTime.parse(result.getString("departureTime"));

                    String airport = result.getString("airport");
                    int availableSeats = result.getInt("nrOfSeats");
                    int remainingSeats = result.getInt("locuriDisponibile");

                    Zbor zbor = new Zbor(dest, departure, departureTime, airport, remainingSeats);
                    zbor.setId(id);
                    zboruri.add(zbor);
                }
            }
        } catch (SQLException e) {
            logger.error("Error occurred while searching for flights", e);
            throw e;
        }

        return zboruri;
    }




    public void updateLocuriDisponibile(Integer zborId, Integer locuriDisponibile) {
        logger.traceEntry("Updating available seats for flight with id {} to {}", zborId, locuriDisponibile);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("UPDATE flights SET nrOfSeats = ? WHERE id = ?")) {
            preStmt.setInt(1, locuriDisponibile);
            preStmt.setInt(2, zborId);
            int result = preStmt.executeUpdate();
            logger.trace("Updated {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    public Zbor findFlightByDestinationDateAndTime(String destination, String date, String time) {
        Zbor zbor = null;
        String query = "SELECT * FROM flights WHERE destination=? AND departureDate=? AND departureTime=? LIMIT 1";
        Connection con = dbUtils.getConnection();
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, destination);
            statement.setString(2, date);
            statement.setString(3, time);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("id");
                    String dest = result.getString("destination");
                    LocalDate departure = LocalDate.parse(result.getString("departureDate"));
                    LocalTime departureTime = LocalTime.parse(result.getString("departureTime"));
                    String airport = result.getString("airport");
                    int nrOfSeats = result.getInt("nrOfSeats");

                    zbor = new Zbor(dest, departure, departureTime, airport, nrOfSeats);
                    zbor.setId(id);
                }
            }
        } catch (SQLException | DateTimeParseException e) {
            logger.error("Error occurred while searching for flight", e);
        }

        return zbor;
    }


}
