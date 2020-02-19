package com.smilegate.entrypoints.rest;

import com.smilegate.dataproviders.database.mongodb.Card;
import com.smilegate.dataproviders.database.mongodb.read.CardReadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class HeathCheckController {

    private CardReadRepository cardReadRepository;

    @Autowired
    public HeathCheckController(@Qualifier("cardReadRepository") CardReadRepository cardReadRepository){
        this.cardReadRepository = cardReadRepository;
    }


    @RequestMapping(value = "/" , method = RequestMethod.GET)
    @ResponseBody
    public List<Card> healthCheck(){

        return cardReadRepository.findAll();

    }


    public class HeathCheckDTO{
        public class Request{

        }


        public class Response{

        }
    }
}
