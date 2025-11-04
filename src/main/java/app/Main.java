package app;

import app.config.ApplicationConfig;

public class Main {
    public static void main(String[] args) {
        ApplicationConfig.startServer(7070);

    }
}

//TODO

/*

Fixes after delivered assignment

- Workflow failed because SecurityApiTest failed. I commented it out but accidentally commented out the wrong test, so stil failed.

- Localhost:API no access - > Removed user so had to use role enum instead of role.name();
ctx.attribute("jwt.role", role) instead of , role.name());            p.equals(base) ||                 // "/api"
            p.equals(base + "/") ||           // "/api/"
            p.equals("/") ||

- token no access token

- Bcrypt password hashing. no needed... Update readme

 */