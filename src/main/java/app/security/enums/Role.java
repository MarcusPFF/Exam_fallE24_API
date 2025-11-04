package app.security.enums;

import io.javalin.security.RouteRole;

public enum Role implements RouteRole {
    RECRUITER, ANYONE;
}
