package app.dtos;

import app.entities.User;

public class DTOMapper {

    /*
    konverter fra en X entity (fra databasen via DAO) til en XDTO (til at sende til klienten)
    Controller  ←→  Service  ←→  DAO  ←→  Database
         ↑
    DTO’er bruges her (Controller/Service)

    to"xx"DTO() = fra entity → DTO

     */

    public static UserDTO toUserDTO(User user) {
        if (user == null) return null;
        return new UserDTO(user.getId(), user.getUsername(), user.getRole());
    }

}