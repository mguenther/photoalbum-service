package com.mgu.photoalbum.config;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import java.util.Enumeration;
import java.util.Vector;

@Deprecated
public class CrossOriginConfig implements FilterConfig {

    /**
     * CORS-related parameter that represents all allowed methods.
     * This is currently hard-wired to GET and POST.
     * It is not configurable via configuration file.
     */
    private static final String ALLOWED_METHODS = "GET,POST,PUT,DELETE,OPTIONS";

    /**
     * CORS-related parameter that represents allowed origins. Can
     * contain the special value '*' which is a placeholder for every
     * origin domain.
     */
    private static final String ALLOWED_ORIGINS = "*";

    /**
     * CORS-related parameter that represents a list of comma-separated
     * headers. Those headers are allowed.
     */
    private static final String ALLOWED_HEADERS = "Origin, Content-Type, Accept, Authorization";

    /**
     * CORS-related parameter which configures the maximum age of
     * a pre-flight request. Default value is <code>1 day</code>.
     */
    private static final int PREFLIGHT_MAX_AGE = 1800;

    /**
     * CORS-related parameter that determines if credentials are allowed.
     * Default is <code>false</code>.
     */
    private static final boolean ALLOW_CREDENTIALS = true;

    /**
     * CORS-related parameter which holds all exposable headers.
     */
    private static final String EXPOSED_HEADERS = StringUtils.EMPTY;

    /**
     * CORS-related parameter which configures chaining of pre-flight
     * requests.
     */
    private static final boolean CHAIN_PREFLIGHT = true;

    @Override
    public String getFilterName() {
        return "CORS Filter";
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public String getInitParameter(final String name) {
        switch (name) {
            case CrossOriginFilter.ALLOWED_ORIGINS_PARAM:
                return ALLOWED_ORIGINS;
            case CrossOriginFilter.ALLOWED_METHODS_PARAM:
                return ALLOWED_METHODS;
            case CrossOriginFilter.ALLOWED_HEADERS_PARAM:
                return ALLOWED_HEADERS;
            case CrossOriginFilter.PREFLIGHT_MAX_AGE_PARAM:
                return Integer.toString(PREFLIGHT_MAX_AGE);
            case CrossOriginFilter.ALLOW_CREDENTIALS_PARAM:
                return Boolean.toString(ALLOW_CREDENTIALS);
            case CrossOriginFilter.EXPOSED_HEADERS_PARAM:
                return EXPOSED_HEADERS;
            case CrossOriginFilter.CHAIN_PREFLIGHT_PARAM:
                return Boolean.toString(CHAIN_PREFLIGHT);
            default:
                return null;
        }
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        final Vector<String> parameterNames = new Vector<>();
        parameterNames.add(CrossOriginFilter.ALLOWED_ORIGINS_PARAM);
        parameterNames.add(CrossOriginFilter.ALLOWED_METHODS_PARAM);
        parameterNames.add(CrossOriginFilter.ALLOWED_HEADERS_PARAM);
        parameterNames.add(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM);
        parameterNames.add(CrossOriginFilter.CHAIN_PREFLIGHT_PARAM);
        parameterNames.add(CrossOriginFilter.EXPOSED_HEADERS_PARAM);
        parameterNames.add(CrossOriginFilter.PREFLIGHT_MAX_AGE_PARAM);
        return parameterNames.elements();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("CorsFilterConfig[").append("allowCredentials=").append(ALLOW_CREDENTIALS)
                .append(", allowedHeaders=[").append(ALLOWED_HEADERS).append("], allowedMethods=[")
                .append(ALLOWED_METHODS).append("], allowedOrigins=[").append(ALLOWED_ORIGINS)
                .append("], chainPreflight=").append(CHAIN_PREFLIGHT).append(", exposedHeaders=[")
                .append(EXPOSED_HEADERS).append("], preflightMaxAge=").append(PREFLIGHT_MAX_AGE);
        return sb.toString();
    }
}