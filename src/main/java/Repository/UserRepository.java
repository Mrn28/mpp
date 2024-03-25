package Repository;

import Domain.User;

public interface UserRepository  extends Repository<Integer, User>{
    boolean login(String username, String password);
}
