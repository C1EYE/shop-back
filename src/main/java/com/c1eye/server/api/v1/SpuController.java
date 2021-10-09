package com.c1eye.server.api.v1;

import com.c1eye.server.exception.http.NotFoundException;
import com.c1eye.server.model.Spu;
import com.c1eye.server.service.BannerServiceImpl;
import com.c1eye.server.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;
import java.util.List;

/**
 * @author c1eye
 * time 2021/10/9 11:03
 */
@RestController
@RequestMapping("/spu")
@Validated
public class SpuController {
    @Autowired
    private SpuService spuService;

    @GetMapping("/id/{id}/detail")
    public Spu getDetail(@PathVariable @Positive Long id) {
        Spu spu = spuService.getSpuById(id);
        if (spu == null) {
            throw new NotFoundException(30003);
        }
        return spu;
    }

    @GetMapping("/latest")
    public List<Spu> getLatestSpuList() {
        return spuService.getLatestPagingSpu();
    }
}
