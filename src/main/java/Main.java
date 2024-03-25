import Domain.Zbor;
import Repository.BiletDBRepository;
import Repository.BiletRepository;
import Repository.UserDBRepository;
import Repository.ZborDBRepository;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {


        Properties props = new Properties();
        try {
            props.load(new FileReader("bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config " + e);
        }

        UserDBRepository userRepository = new UserDBRepository(props);
        ZborDBRepository zborRepository = new ZborDBRepository(props);
        BiletRepository biletRepository = new BiletDBRepository(props, zborRepository);

        String username = "marcel";
        String password = "_marcel";
        boolean isLoggedIn = login(userRepository, username, password);

        if (isLoggedIn) {
            // Afisare zboruri disponibile
            afisareZboruriDisponibile(zborRepository);

            // Restul codului pentru selecția și gestionarea zborurilor
        } else {
            System.out.println("Autentificare esuata. Username sau parola incorecta.");
        }
//        // Creare bilet
//        Zbor zbor = zborRepository.findById(1);
//        Bilet newBilet = new Bilet(zbor, "Ionescu Vasile", "Bdul Muncii 4", Arrays.asList("Popa Matei", "Ghita Ana"), 3);
//        biletRepository.save(newBilet);
//        System.out.println("Adăugare bilet:");
//        System.out.println(newBilet);
//
//        // Afișare toate biletele din baza de date
//        System.out.println("\nToate biletele din baza de date:");
//        List<Bilet> allBilets = biletRepository.findAll();
//        for (Bilet bilet : allBilets) {
//            System.out.println(bilet);
//        }
//
//        // Actualizare bilet cu ID-ul 1
//        int biletIdToUpdate = 1;
//        Bilet updatedBilet = new Bilet(zbor, "Tarnacop Daniel", "Aleea Sperantei 13", Arrays.asList("Tarnacop Gabriela"), 2);
//        updatedBilet.setId(1);
//        biletRepository.update(updatedBilet);
//        System.out.println("\nActualizare bilet cu ID-ul " + biletIdToUpdate + ":");
//        System.out.println(updatedBilet);
//
//        // Căutare bilet după ID
//        int biletIdToFind = 1;
//        Bilet foundBilet = biletRepository.findById(biletIdToFind);
//        System.out.println("\nCăutare bilet cu ID-ul " + biletIdToFind + ":");
//        System.out.println(foundBilet);
//
//        // Ștergere bilet cu ID-ul 2
//        int biletIdToDelete = 2;
//        biletRepository.deleteById(biletIdToDelete);
//        System.out.println("\nȘtergere bilet cu ID-ul " + biletIdToDelete + ".");



//        // Adăugare zbor
//        Zbor newZbor = new Zbor("New York", LocalDate.of(2024, 3, 30), LocalTime.of(8, 30), "JFK", 200);
//        zborRepository.save(newZbor);
//        System.out.println("Adăugare zbor:");
//        System.out.println(newZbor);
//
//// Afișare toate zborurile din baza de date
//        System.out.println("\nToate zborurile din baza de date:");
//        List<Zbor> allZboruri = zborRepository.findAll();
//        for (Zbor zbor : allZboruri) {
//            System.out.println(zbor);
//        }
//
//// Actualizare zbor cu ID-ul 1
//        int zborIdToUpdate = 1;
//        Zbor updatedZbor = new Zbor("Berlin", LocalDate.of(2024, 3, 10), LocalTime.of(10, 5), "Brandenburg", 110);
//        updatedZbor.setId(1);
//        zborRepository.update(updatedZbor);
//        System.out.println("\nActualizare zbor cu ID-ul " + zborIdToUpdate + ":");
//        System.out.println(updatedZbor);
//
//// Cautare zbor dupa ID
//        int zborIdToFind = 1;
//        Zbor foundZbor = zborRepository.findById(zborIdToFind);
//        System.out.println("\nCautare zbor cu ID-ul " + zborIdToFind + ":");
//        System.out.println(foundZbor);
//
//// Ștergere zbor cu ID-ul 8
//        int zborIdToDelete = 8;
//        zborRepository.deleteById(zborIdToDelete);
//        System.out.println("\nȘtergere zbor cu ID-ul " + zborIdToDelete + ".");



//        // Adăugare user
//        User newUser = new User("mirel", "mirel123");
//        userRepository.save(newUser);
//        System.out.println("Adăugare user:");
//        System.out.println(newUser);
//
//        // Afișare toti userii din baza de date
//        System.out.println("\nToti userii din baza de date:");
//        List<User> allUsers = userRepository.findAll();
//        for (User user : allUsers) {
//            System.out.println(user);
//        }
//
////         Actualizare user cu ID-ul 1
//        int userIdToUpdate = 1;
//        User updatedUser = new User("dorel", "_dorel");
//        updatedUser.setId(1);
//        userRepository.update(updatedUser);
//        System.out.println("\nActualizare user cu ID-ul " + userIdToUpdate + ":");
//        System.out.println(updatedUser);
//
//        // Cautare user dupa ID
//        int userIdToFind = 1;
//        User foundUser = userRepository.findById(userIdToFind);
//        System.out.println("\nCautare user cu ID-ul " + userIdToFind + ":");
//        System.out.println(foundUser);
//
//        // Ștergere user cu ID-ul 2
//        int userIdToDelete = 2;
//        userRepository.deleteById(userIdToDelete);
//        System.out.println("\nȘtergere user cu ID-ul " + userIdToDelete + ".");

    }
    static boolean login(UserDBRepository userRepository, String username, String password) {
        // Verificarea credentialelor in baza de date
        return userRepository.login(username, password);
    }

    static void afisareZboruriDisponibile(ZborDBRepository zborRepo) {
        System.out.println("Zboruri disponibile:");
        for (Zbor zbor : zborRepo.findAll()) {
            System.out.println(zbor.toString());
        }
    }
}
