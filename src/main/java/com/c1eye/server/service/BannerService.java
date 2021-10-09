package com.c1eye.server.service;

import com.c1eye.server.model.Banner;

/**
 * @author c1eye
 * time 2021/10/9 11:14
 */
public interface BannerService {
     Banner getByName(String name);
}
