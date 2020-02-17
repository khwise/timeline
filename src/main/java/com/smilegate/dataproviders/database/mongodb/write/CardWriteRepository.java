package com.smilegate.dataproviders.database.mongodb.write;


import com.smilegate.dataproviders.database.mongodb.Card;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository("cardWriteRepository")
public interface CardWriteRepository extends MongoRepository<Card, ObjectId>, QuerydslPredicateExecutor<Card> {

}
