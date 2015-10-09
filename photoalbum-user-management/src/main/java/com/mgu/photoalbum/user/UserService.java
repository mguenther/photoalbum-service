package com.mgu.photoalbum.user;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CouchDB-backed implementation of {@link UserQueryService}.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public class UserService implements UserQueryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Inject
    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> getUserById(final String userId) {
        User user = null;
        try {
            user = this.userRepository.get(userId);
        } catch (Exception ignored) {
            // thrown if a document for the given user ID does not exist
        }
        return Optional.fromNullable(user);
    }
}