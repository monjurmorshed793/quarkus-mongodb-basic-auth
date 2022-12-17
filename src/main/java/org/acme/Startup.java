package org.acme;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.inject.Singleton;


import io.quarkus.runtime.StartupEvent;

@Singleton
public class Startup {
   
    public void createUsers(@Observes StartupEvent evt){
        if(User.count() == 0){
            Set<String> roles = new HashSet<>();
            roles.add("admin");
            User.add("monjur", "123456", roles);
        }
    }
}
