package Service;

import Domain.Bilet;
import Domain.User;
import Domain.Zbor;
import Repository.BiletRepository;
import Repository.UserRepository;
import Repository.ZborRepository;

import java.util.List;

public class Service {
    private final UserRepository userRepository;
    private final BiletRepository biletRepository;
    private final ZborRepository zborRepository;

    public Service(UserRepository userRepository, BiletRepository biletRepository, ZborRepository zborRepository) {
        this.userRepository = userRepository;
        this.biletRepository = biletRepository;
        this.zborRepository = zborRepository;
    }

    public boolean login(String username, String password) {
        return userRepository.login(username, password);
    }

    public void registerUser(User user) {
        userRepository.save(user);
    }

    public void addBilet(Bilet bilet) {
        biletRepository.save(bilet);
    }

    public List<Bilet> getAllBilete() {
        return biletRepository.findAll();
    }

    public Bilet findBiletById(Integer id) {
        return biletRepository.findById(id);
    }

    public void updateBilet(Bilet bilet) {
        biletRepository.update(bilet);
    }

    public void deleteBilet(Integer id) {
        biletRepository.deleteById(id);
    }
    public Zbor findFlightById(Integer id) {
        return zborRepository.findById(id);
    }


    public List<Zbor> getAllZboruri() {
        return zborRepository.findAll();
    }

    public List<Zbor> findByDestinationAndDate(String destinatie, String data) {
        try {
            return zborRepository.findByDestinationAndDate(destinatie, data);
        } catch (Exception e) {
            // Handle exception appropriately
            e.printStackTrace();
            return null;
        }
    }

    public void cumparareBilet(Bilet bilet) {
        try {
            biletRepository.save(bilet);

            int locuriDisponibile = bilet.getZbor().getLocuriDisponibile() - (bilet.GetNumarLocuri());
            zborRepository.updateLocuriDisponibile(bilet.getZbor().getId(), locuriDisponibile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateLocuriDisponibile(Integer zborId, Integer locuriDisponibile) {
        try {
            zborRepository.updateLocuriDisponibile(zborId, locuriDisponibile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Zbor findFlightByDestinationDateAndTime(String destination, String date, String time) {
        try {
            return zborRepository.findFlightByDestinationDateAndTime(destination, date, time);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean sellFlightTicket(int zborId, int numarLocuriDorite) {
        return biletRepository.sellFlightTicket(zborId, numarLocuriDorite);
    }


}
