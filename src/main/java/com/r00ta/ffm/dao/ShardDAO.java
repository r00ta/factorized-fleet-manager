package com.r00ta.ffm.dao;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import com.r00ta.ffm.models.Shard;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

@ApplicationScoped
@Transactional
public class ShardDAO implements PanacheRepositoryBase<Shard, String> {
}
