package com.dvaren.bill.controller;

import com.dvaren.bill.utils.ResponseResult;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/test")
public class ArticleController {

    @GetMapping("")
    public ResponseResult list(){
        return ResponseResult.ok();
    }
}
