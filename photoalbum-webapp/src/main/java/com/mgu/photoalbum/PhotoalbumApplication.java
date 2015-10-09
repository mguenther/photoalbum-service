package com.mgu.photoalbum;

import com.hubspot.dropwizard.guice.GuiceBundle;
import com.mgu.photoalbum.config.DatabaseModule;
import com.mgu.photoalbum.config.ServiceConfig;
import com.mgu.photoalbum.config.ServiceModule;
import com.mgu.photoalbum.security.Principal;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthFactory;
import io.dropwizard.auth.basic.BasicAuthFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class PhotoalbumApplication extends Application<ServiceConfig> {

    private static final String APPLICATION_NAME = "Photoalbum Service";

    private GuiceBundle<ServiceConfig> guiceBundle;

    @Override
    public void initialize(final Bootstrap<ServiceConfig> bootstrap) {

        this.guiceBundle = GuiceBundle.<ServiceConfig>newBuilder()
                .addModule(new DatabaseModule())
                .addModule(new ServiceModule())
                .setConfigClass(ServiceConfig.class)
                .enableAutoConfig(getClass().getPackage().getName())
                .build();
        bootstrap.addBundle(guiceBundle);
    }

    @Override
    public void run(final ServiceConfig serviceConfig, final Environment environment) throws Exception {
        // we let Guice manage everything with regard to web resources etc.
        // no configuration required
        final BasicAuthenticator authenticator = guiceBundle.getInjector().getInstance(BasicAuthenticator.class);
        environment
                .jersey()
                .register(AuthFactory.binder(new BasicAuthFactory<>(authenticator, "Photoalbum Realm", Principal.class)));
    }

    @Override
    public String getName() {
        return APPLICATION_NAME;
    }

    public static void main(String[] args) {
        try {
            new PhotoalbumApplication().run(args);
        } catch (Throwable t) {
            System.err.println(t);
        }
    }
}