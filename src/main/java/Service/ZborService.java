package Service;

import Domain.Zbor;
import Repository.ZborDBRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.List;

public class ZborService {
    private final ZborDBRepository zborRepository;
    private static final Logger logger = LogManager.getLogger();

    public ZborService(ZborDBRepository zborRepository) {
        this.zborRepository = zborRepository;
    }

    public void addZbor(Zbor zbor) {
        zborRepository.save(zbor);
        logger.info("Flight added successfully: {}", zbor.getDestinatie());
    }

    public void updateZbor(Zbor zbor) {
        zborRepository.update(zbor);
        logger.info("Flight updated successfully: {}", zbor.getDestinatie());
    }

    public List<Zbor> getAllZboruri() {
        logger.info("Retrieving all flights...");
        return zborRepository.findAll();
    }

    public Zbor getZborById(Integer id) {
        logger.info("Retrieving flight with id: {}", id);
        return zborRepository.findById(id);
    }

    public void deleteZborById(Integer id) {
        logger.info("Deleting flight with id: {}", id);
        zborRepository.deleteById(id);
    }

    public List<Zbor> findZboruriByDestinationAndDate(String destination, String departureDate) {
        try {
            return zborRepository.findByDestinationAndDate(destination, departureDate);
        } catch (SQLException e) {
            logger.error("Error occurred while searching for flights", e);
            throw new RuntimeException(e);
        }
    }
}
