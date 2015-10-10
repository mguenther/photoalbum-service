package com.mgu.photoalbum.bootstrap;

import com.mgu.photoalbum.user.Status;
import com.mgu.photoalbum.user.User;
import com.mgu.photoalbum.user.UserRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.ektorp.CouchDbConnector;

public class UserGenerator implements Generator {

    private final UserRepository userRepository;

    public UserGenerator(final CouchDbConnector connector) {
        this.userRepository = new UserRepository(connector);
    }

    @Override
    public void generate() {
        final User activeUser = new User();
        activeUser.setId("CU-zkVDzWSctsEqqk");
        activeUser.setFirstName("Max");
        activeUser.setLastName("Mustermann");
        activeUser.setHashedPassword(DigestUtils.sha1Hex("secret"));
        activeUser.setEmail("max.mustermann@photoalbum.de");
        activeUser.setStatus(Status.ACTIVE);

        final User pendingUser = new User();
        pendingUser.setId("CU-pzgBftxNCMLXql");
        pendingUser.setFirstName("Karla");
        pendingUser.setLastName("Klempner");
        pendingUser.setHashedPassword(DigestUtils.sha1Hex("secret"));
        pendingUser.setEmail("karla.klempner@photoalbum.de");
        pendingUser.setStatus(Status.PENDING);

        final User suspendedUser = new User();
        suspendedUser.setId("CU-tSSQRBvYmbWGag");
        suspendedUser.setFirstName("Dancing");
        suspendedUser.setLastName("Denzell");
        suspendedUser.setHashedPassword(DigestUtils.sha1Hex("secret"));
        suspendedUser.setEmail("dancing.denzell@photoalbum.de");
        suspendedUser.setStatus(Status.SUSPENDED);

        userRepository.add(activeUser);
        System.out.println("Adding user document with ID " + activeUser.getId() + " to database.");
        userRepository.add(pendingUser);
        System.out.println("Adding user document with ID " + pendingUser.getId() + " to database.");
        userRepository.add(suspendedUser);
        System.out.println("Adding user document with ID " + suspendedUser.getId() + " to database.");
    }

    @Override
    public String toString() {
        return "User Document Generator";
    }
}