package com.mgu.photoalbum;

import com.hubspot.dropwizard.guice.GuiceBundle;
import com.mgu.photoalbum.config.ServiceConfig;
import com.mgu.photoalbum.config.ServiceModule;
import com.mgu.photoalbum.security.Principal;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthFactory;
import io.dropwizard.auth.basic.BasicAuthFactory;
import io.dropwizard.forms.MultiPartBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

public class PhotoalbumApplication extends Application<ServiceConfig> {

    private static final String APPLICATION_NAME = "Photoalbum Service";

    private GuiceBundle<ServiceConfig> guiceBundle;

    @Override
    public void initialize(final Bootstrap<ServiceConfig> bootstrap) {

        this.guiceBundle = GuiceBundle.<ServiceConfig>newBuilder()
                .addModule(new ServiceModule())
                .setConfigClass(ServiceConfig.class)
                .enableAutoConfig(getClass().getPackage().getName())
                .build();
        bootstrap.addBundle(guiceBundle);
        bootstrap.addBundle(new MultiPartBundle());
    }

    @Override
    public void run(final ServiceConfig serviceConfig, final Environment environment) throws Exception {
        // we let Guice manage everything with regard to web resources etc.
        // no configuration required
        final BasicAuthenticator authenticator = guiceBundle.getInjector().getInstance(BasicAuthenticator.class);
        environment
                .jersey()
                .register(AuthFactory.binder(new BasicAuthFactory<>(authenticator, "Photoalbum Realm", Principal.class)));

        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");

        FilterRegistration.Dynamic filter = environment.servlets().addFilter("CORSFilter", CrossOriginFilter.class);

        filter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, environment.getApplicationContext().getContextPath() + "*");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "Origin, Content-Type, Accept, Authorization");
        filter.setInitParameter(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, "true");
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