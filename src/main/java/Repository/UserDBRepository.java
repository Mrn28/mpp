package Repository;

import Domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UserDBRepository implements UserRepository {
    private JdbcUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();
    private final Map<String, String> passwordCache = new HashMap<>();

    public UserDBRepository(Properties props) {
        logger.info("Initializing UserDBRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }

    @Override
    public void save(User user) {
        String query = "INSERT INTO users (username, password) VALUES (?, ?)";
        Connection connection=dbUtils.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getUsername());
            String hashedPassword = hashPassword(user.getParola());
            statement.setString(2, hashedPassword);
            statement.executeUpdate();
            passwordCache.put(user.getUsername(), user.getParola());
            logger.info("User saved successfully.");
        } catch (SQLException e) {
            logger.error("Error occurred while adding user", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(User elem) {
        logger.traceEntry("updating user with id {} ",elem.getId());
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("update users set username=?, password=? where id=?")){
            preStmt.setString(1,elem.getUsername());
            preStmt.setString(2,elem.getParola());
            preStmt.setInt(3,elem.getId());
            int result=preStmt.executeUpdate();
            logger.trace("Updated {} instances",result);
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit();
    }

    @Override
    public List<User> findAll() {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        List<User> users=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from users")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String username = result.getString("username");
                    String password = result.getString("password");
                    User user = new User(username, password);
                    user.setId(id);
                    users.add(user);
                }
            }
        }catch (SQLException e){
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit(users);
        return users;
    }

    @Override
    public User findById(Integer integer) {
        logger.traceEntry("finding user with id {} ", integer);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select * from users where id=?")){
            preStmt.setInt(1, integer);
            try(ResultSet result=preStmt.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("id");
                    String username = result.getString("username");
                    String password = result.getString("password");
                    User user = new User(username, password);
                    user.setId(id);
                    logger.traceExit(user);
                    return user;
                }
            }
        }catch (SQLException e){
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit("No user found with id {}",integer);
        return null;
    }

    @Override
    public void deleteById(Integer integer) {
        logger.traceEntry("deleting user with id {} ",integer);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("delete from users where id=?")){
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
    public boolean login(String username, String password) {
        if (passwordCache.containsKey(username) && passwordCache.get(username).equals(password)) {
            logger.info("Login successful (using cached password).");
            return true;
        }

        String hashedPassword = hashPassword(password);
        String query = "SELECT COUNT(*) FROM users WHERE username=? AND password=?";
        Connection con=dbUtils.getConnection();
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, hashedPassword);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                if (count > 0) {
                    logger.info("Login successful.");
                    passwordCache.put(username, password);
                    return true;
                }
            }
        } catch (SQLException e) {
            logger.error("Error occurred while attempting to login", e);
            throw new RuntimeException(e);
        }
        logger.info("Login failed. Invalid username or password.");
        return false;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}
