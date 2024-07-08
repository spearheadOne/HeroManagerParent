package org.abondar.industrial.heromanager.service;

public interface TokenService {

    void saveToken(String token);

    boolean tokenExists(String token);
}
