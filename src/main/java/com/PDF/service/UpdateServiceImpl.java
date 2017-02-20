package com.PDF.service;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by martanase on 2/16/2017.
 */
@Service
public class UpdateServiceImpl implements UpdateService{

    public void updateOrderOfFiles(List<Integer> listOfOrder){
        System.out.println(listOfOrder.toArray().toString());
    }
}
