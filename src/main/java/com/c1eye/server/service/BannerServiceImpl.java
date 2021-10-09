package com.c1eye.server.service;

import com.c1eye.server.model.Banner;
import com.c1eye.server.repository.BannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author c1eye
 * time 2021/9/29 23:27
 */
@Service
public class BannerServiceImpl implements BannerService{
    @Autowired
    private BannerRepository bannerRepository;
    @Override
    public Banner getByName(String name){
        return bannerRepository.findBannerByName(name);
    }

}
