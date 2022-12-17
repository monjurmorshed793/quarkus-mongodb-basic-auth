package org.acme;
import java.util.Optional;
import java.util.Set;

import org.bson.codecs.pojo.annotations.BsonProperty;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection = "app_user")
public class User extends PanacheMongoEntity {
    @BsonProperty("username")
    public String username;
    @BsonProperty("password")
    public String password;
    @BsonProperty("roles")
    public Set<String> roles;

    public static void add(String username, String password, Set<String> roles){
        User user = new User();
        user.username = username;
        user.password = BcryptUtil.bcryptHash(password);
        user.roles = roles;
        user.persist();
    }

    public static Optional<User> getByUsername(String username){
        return User.find("username", username).singleResultOptional();
    }
}
