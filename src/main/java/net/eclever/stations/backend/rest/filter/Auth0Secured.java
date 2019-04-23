package net.eclever.stations.backend.rest.filter;


import javax.ws.rs.NameBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marker interfaces for Auth0 Secured REST Resources
 *
 * @author Veit Weber, v.weber@nextmove.de, 26.09.2018
 */@NameBinding
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface Auth0Secured {
}