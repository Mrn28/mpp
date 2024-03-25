package Service;

import Domain.Bilet;
import Repository.BiletDBRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class BiletService {
    private final BiletDBRepository biletRepository;
    private static final Logger logger = LogManager.getLogger();

    public BiletService(BiletDBRepository biletRepository) {
        this.biletRepository = biletRepository;
    }

    public void addBilet(Bilet bilet) {
        biletRepository.save(bilet);
        logger.info("Ticket added successfully: {}", bilet.getId());
    }

    public void updateBilet(Bilet bilet) {
        biletRepository.update(bilet);
        logger.info("Ticket updated successfully: {}", bilet.getId());
    }

    public List<Bilet> getAllBilete() {
        logger.info("Retrieving all tickets...");
        return biletRepository.findAll();
    }

    public Bilet getBiletById(Integer id) {
        logger.info("Retrieving ticket with id: {}", id);
        return biletRepository.findById(id);
    }

    public void deleteBiletById(Integer id) {
        logger.info("Deleting ticket with id: {}", id);
        biletRepository.deleteById(id);
    }
}
