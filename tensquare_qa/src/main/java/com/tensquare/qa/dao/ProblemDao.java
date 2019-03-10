package com.tensquare.qa.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.qa.pojo.Problem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ProblemDao extends JpaRepository<Problem,String>,JpaSpecificationExecutor<Problem>{

    @Query(value = "SELECT * FROM tb_problem p,tb_pl l WHERE p.id = l.problemid AND labelid = :labelid ORDER BY p.replytime DESC",nativeQuery = true)
    public Page<Problem> newList(@Param("labelid") String labelid, Pageable pageable);

    @Query(value = "SELECT * FROM tb_problem p,tb_pl l WHERE p.id = l.problemid AND labelid = :labelid ORDER BY p.reply DESC",nativeQuery = true)
    public Page<Problem> hotList(@Param("labelid") String labelid,Pageable pageable);

    @Query(value = "SELECT * FROM tb_problem p,tb_pl l WHERE p.id = l.problemid AND l.labelid = :labelid AND p.reply =0 ORDER BY p.createtime DESC",nativeQuery = true)
    public Page<Problem> waitList(@Param("labelid") String labelid,Pageable pageable);
}
