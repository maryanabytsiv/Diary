package com.softserve.tc.diary.dao;

import java.util.List;
import com.softserve.tc.diary.entity.Record;
import com.softserve.tc.diary.entity.Tag;

public interface TagDAO extends BaseDAO<Tag> {
    
    List<Tag> getListTagsByPrefix(String prefix);
    
    List<Tag> getListTagsBySuffix(String suffix);
    
    List<Record> getListRecordsByTag(Tag object);
    
    List<Record> getListRecordsByListOfTags(List<Tag> list);
    
}
