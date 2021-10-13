package com.c1eye.server.api.v1;

import com.c1eye.server.bo.PageCounter;
import com.c1eye.server.exception.http.NotFoundException;
import com.c1eye.server.model.Spu;
import com.c1eye.server.service.SpuService;
import com.c1eye.server.util.CommonUtils;
import com.c1eye.server.vo.PagingDozer;
import com.c1eye.server.vo.SpuSimplifyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;

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
    public PagingDozer<Spu, SpuSimplifyVO> getLatestSpuList(
            @RequestParam(defaultValue = "0") Integer start,
            @RequestParam(defaultValue = "20") Integer count
                                                           ) {
        PageCounter pageCounter = CommonUtils.convertToPageParameter(start, count);
        Page<Spu> pages = spuService.getLatestPagingSpu(pageCounter.getPage(), pageCounter.getCount());
        return new PagingDozer<>(pages, SpuSimplifyVO.class);
    }

    @GetMapping("/by/category/{id}")
    public PagingDozer<Spu, SpuSimplifyVO> getByCategoryId(
            @PathVariable @Positive(message = "{id.positive}") Long id,
            @RequestParam(name = "is_root",defaultValue = "false") Boolean isRoot,
            @RequestParam(name = "start", defaultValue = "0") Integer start,
            @RequestParam(name = "count", defaultValue = "10") Integer count
                                                          ) {
        PageCounter pageCounter = CommonUtils.convertToPageParameter(start, count);
        Page<Spu> pages = spuService.getByCategory(id, isRoot, pageCounter.getPage(), pageCounter.getCount());
        return new PagingDozer<>(pages, SpuSimplifyVO.class);
    }
}
