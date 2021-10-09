package com.c1eye.server.api.v1;

import com.c1eye.server.exception.http.ForbiddenException;
import com.c1eye.server.exception.http.NotFoundException;
import com.c1eye.server.model.Banner;
import com.c1eye.server.service.BannerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * author c1eye
 * time 2021/9/28 10:08
 */

@RestController
@Validated
public class BannerController {
    @Autowired
    BannerServiceImpl bannerService;

    @PostMapping("/banner")
    public String test(@RequestParam("name") String name) {
        throw new ForbiddenException(10001);
    }

    @GetMapping("/banner/{name}")
    public Banner getByName(@PathVariable(name = "name") String name) {
        Banner banner = bannerService.getByName(name);
        if (banner == null) {
            throw new NotFoundException(30001);
        }
        return banner;
    }
}
