package Repository;

import Domain.Bilet;
import Domain.BiletConverter;
import Domain.Zbor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BiletDBRepository implements BiletRepository {

    private JdbcUtils dbUtils;
    private ZborRepository zborRepository;

    private static final Logger logger = LogManager.getLogger();

    public BiletDBRepository(Properties props, ZborRepository zborRepository) { // Add ZborRepository as parameter
        logger.info("Initializing BiletDBRepository with properties: {}", props);
        dbUtils = new JdbcUtils(props);
        this.zborRepository = zborRepository;
    }

    @Override
    public void save(Bilet elem) {
        logger.traceEntry("saving ticket {} ",elem);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into tickets(zborID, clientName, clientAddress, tourists, nrOfTourists) values (?,?,?,?,?)")){
            preStmt.setInt(1,elem.getZbor().getId());
            preStmt.setString(2,elem.getNumeClient());
            preStmt.setString(3,elem.getAdresaClient());
            preStmt.setString(4, BiletConverter.serializeTuristi(elem.getTuristi()));
            int numarLocuri = elem.getTuristi().size() + 1;
            preStmt.setInt(5, numarLocuri);
            int result=preStmt.executeUpdate();
            logger.trace("Saved {} instances",result);
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Bilet elem) {
        logger.traceEntry("updating ticket with id {} ",elem.getId());
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("update tickets set zborID=?, clientName=?, clientAddress=?, tourists=?, nrOfTourists=? where id=?")){
            preStmt.setInt(1,elem.getZbor().getId());
            preStmt.setString(2,elem.getNumeClient());
            preStmt.setString(3,elem.getAdresaClient());
            preStmt.setString(4,BiletConverter.serializeTuristi(elem.getTuristi()));
            int numarLocuri = elem.getTuristi().size() + 1;
            preStmt.setInt(5, numarLocuri);
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
    public List<Bilet> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Bilet> tickets = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("SELECT * FROM tickets")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    int zborID = result.getInt("zborID");
                    String clientName = result.getString("clientName");
                    String clientAddress = result.getString("clientAddress");
                    List<String> tourists = BiletConverter.deserializeTuristi(result.getString("tourists"));
//                    int nrOfTourists = result.getInt("nrOfTourists");

                    // Fetch the Zbor object based on zborID
                    Zbor zbor = zborRepository.findById(zborID);

                    // Create the Bilet object
                    Bilet bilet = new Bilet(zbor, clientName, clientAddress, tourists);
                    bilet.setId(id);
                    tickets.add(bilet);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB " + e);
        }
        logger.traceExit(tickets);
        return tickets;
    }


    @Override
    public Bilet findById(Integer id) {
        logger.traceEntry("finding ticket with id {} ", id);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("SELECT * FROM tickets WHERE id=?")) {
            preStmt.setInt(1, id);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    int zborID = result.getInt("zborID");
                    String clientName = result.getString("clientName");
                    String clientAddress = result.getString("clientAddress");
                    List<String> tourists = BiletConverter.deserializeTuristi(result.getString("tourists"));
//                    int nrOfTourists = result.getInt("nrOfTourists");

                    // Fetch the Zbor object based on zborID
                    Zbor zbor = zborRepository.findById(zborID);

                    // Create the Bilet object
                    Bilet bilet = new Bilet(zbor, clientName, clientAddress, tourists);
                    bilet.setId(id);

                    logger.traceExit(bilet);
                    return bilet;
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB " + e);
        }
        logger.traceExit("No ticket found with id {}", id);
        return null;
    }


    @Override
    public void deleteById(Integer integer) {
        logger.traceEntry("deleting ticket with id {} ",integer);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("delete from tickets where id=?")){
            preStmt.setInt(1,integer);
            int result=preStmt.executeUpdate();
            logger.trace("Deleted {} instances",result);
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit();
    }

    @Override
    public boolean sellFlightTicket(int zborId, int numarLocuriDorite) {
        logger.traceEntry("Selling {} tickets for flight with ID {} ", numarLocuriDorite, zborId);
        Connection con = dbUtils.getConnection();

        int remainingSeats = 0;
        try (PreparedStatement preStmt = con.prepareStatement(
                "SELECT f.nrOfSeats - COALESCE(SUM(t.nrOfTourists), 0) AS remainingSeats " +
                        "FROM flights f LEFT JOIN tickets t ON f.id = t.zborID WHERE f.id = ?")) {
            preStmt.setInt(1, zborId);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    remainingSeats = result.getInt("remainingSeats");
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching remaining seats for flight with ID {}: {}", zborId, e);
            System.out.println("Error fetching remaining seats: " + e);
        }

        boolean canSellTickets = remainingSeats >= numarLocuriDorite;

        logger.traceExit("Can sell tickets: {}", canSellTickets);
        return canSellTickets;
    }



}
