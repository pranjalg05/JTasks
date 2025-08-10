package pg.project.jtasks.Service;

import com.mongodb.DuplicateKeyException;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pg.project.jtasks.Entity.Collection;
import pg.project.jtasks.Entity.CollectionType;
import pg.project.jtasks.Entity.User;
import pg.project.jtasks.Repository.CollectionRepository;
import pg.project.jtasks.Repository.UserRepository;

import java.util.List;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CollectionRepository collectionRepository;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public boolean createUser(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepository.save(user);
            Collection daily = new Collection();
            daily.setCollectionName("Daily");
            daily.setUserId(savedUser.getId());
            daily.setType(CollectionType.DAILY);
            collectionRepository.save(daily);
            return true;
        } catch (DuplicateKeyException e) {
            log.error("Username already exists: ", e);
            return false;
        } catch (Exception e) {
            log.error("error while creating user: ", e);
            return false;
        }
    }

    public boolean updateUser(ObjectId userId, User updatedUser) {
        User userInDB = userRepository.findById(userId).orElse(null);
        if (userInDB == null) {
            log.error("User Doesn't exist");
            return false;
        }
        try {
            userInDB.setUsername(updatedUser.getUsername());
            userInDB.setPassword(updatedUser.getPassword());
            userRepository.save(userInDB);
            return true;
        } catch (DuplicateKeyException e) {
            log.error("User already exists", e);
            return false;
        } catch (Exception e) {
            log.error("Error updating user", e);
            return false;
        }
    }

    public void deleteUser(ObjectId userId) {
        userRepository.deleteById(userId);
    }

    public User getUserById(ObjectId userId) {
        return userRepository.findById(userId).
                orElse(null);
    }

    public List<Collection> getAllCollectionOfUser(ObjectId userId) {
        return collectionRepository.getAllByUserId(userId);
    }

}

