package com.smilegate.dataproviders.database.mongodb.preferences.write;

import com.smilegate.dataproviders.database.mongodb.entity.TmBasicPreferences;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("tmBasicPreferencesWriteRepository")
public interface TmBasicPreferencesWriteRepository extends MongoRepository<TmBasicPreferences, ObjectId>, QuerydslPredicateExecutor<TmBasicPreferences> {
    Optional<TmBasicPreferences> findByMemberNo(Long memberNo);
}
