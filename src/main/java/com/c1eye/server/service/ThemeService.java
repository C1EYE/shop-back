package com.c1eye.server.service;

import com.c1eye.server.model.Theme;
import com.c1eye.server.repository.ThemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author c1eye
 * time 2021/10/13 21:06
 */
@Service
public class ThemeService {
    @Autowired
    ThemeRepository themeRepository;


    public List<Theme> findByNames(List<String> names) {
        return themeRepository.findByNames(names);
    }

    public Optional<Theme> findByName(String name) {
        return themeRepository.findByName(name);
    }
}
