package com.example.template2.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AreaRepository extends JpaRepository<Area, Integer> {

    /*
    @Query(value = "SELECT * FROM area WHERE user_login=:login;", nativeQuery = true)
    List<Area> findAllByUserLogin();

     */

    @Query(value = "SELECT a.id, a.name, a.location FROM area a WHERE a.user_login=:login;", nativeQuery = true)
    List<SimpleArea> getAllLessDetailed(@Param("login") String login);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM area a WHERE a.user_login = :login);", nativeQuery = true)
    boolean existsByLogin(@Param("login") String login);
}