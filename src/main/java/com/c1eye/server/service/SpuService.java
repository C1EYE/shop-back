package com.c1eye.server.service;

import com.c1eye.server.model.Spu;
import com.c1eye.server.repository.SpuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author c1eye
 * time 2021/10/9 11:13
 */
@Service
public class SpuService {

    @Autowired
    private SpuRepository spuRepository;

    public Spu getSpuById(Long id){
        return this.spuRepository.findOneById(id);
    }

    public List<Spu> getLatestPagingSpu(){
        return spuRepository.findAll();
    }

    public Page<Spu> getLatestPagingSpu(Integer pageNum, Integer size){
        PageRequest page = PageRequest.of(pageNum, size, Sort.by("createTime").descending());
        return spuRepository.findAll(page);
    }

    public Page<Spu> getByCategory(Long cid,Boolean isRoot,Integer pageNum,Integer size ){
        PageRequest page = PageRequest.of(pageNum, size);
        if(isRoot){
            System.out.println("2");
            return spuRepository.findByRootCategoryIdOrderByCreateTime(cid, page);
        }else{
            return spuRepository.findByCategoryIdOrderByCreateTimeDesc(cid, page);
        }
    }
}
