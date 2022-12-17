package org.acme;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;


import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.security.AuthenticationFailedException;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.IdentityProvider;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.request.UsernamePasswordAuthenticationRequest;
import io.quarkus.security.runtime.QuarkusPrincipal;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.smallrye.mutiny.Uni;

@ApplicationScoped
public class ApplicationIdentityProvider implements IdentityProvider<UsernamePasswordAuthenticationRequest>{

    @Override
    public Class<UsernamePasswordAuthenticationRequest> getRequestType() {
        return UsernamePasswordAuthenticationRequest.class;
    }

    @Override
    public Uni<SecurityIdentity> authenticate(UsernamePasswordAuthenticationRequest request,
            AuthenticationRequestContext context) {
        Optional<User> user = User.getByUsername(request.getUsername());
        
        if(user.isPresent()){
            
            if(BcryptUtil.matches(String.valueOf(request.getPassword().getPassword()), user.get().password)){
                return Uni
                .createFrom().item(QuarkusSecurityIdentity.builder()
                .setPrincipal(new QuarkusPrincipal(request.getUsername()))
                .addCredential(request.getPassword())
                .setAnonymous(false)
                .addRoles(user.get().roles)
                .build());
            }            
            throw new AuthenticationFailedException("Username-password doesn't match");

        }
        throw new AuthenticationFailedException("Username not found");
    }
    
}

