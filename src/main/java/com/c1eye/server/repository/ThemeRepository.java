package com.c1eye.server.repository;

import com.c1eye.server.model.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * @author c1eye
 * time 2021/10/13 21:09
 */
public interface ThemeRepository extends JpaRepository<Theme,Long> {
    @Query("select t from Theme t where t.name in (:names)")
    List<Theme> findByNames(List<String> names);

    Optional<Theme> findByName(String name);
}
