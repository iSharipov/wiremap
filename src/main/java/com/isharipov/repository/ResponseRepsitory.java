package com.isharipov.repository;

import com.isharipov.domain.common.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Илья on 07.05.2016.
 */
@Repository
public interface ResponseRepsitory extends JpaRepository<Response, Long> {

    List<Response> findResponsesByProviderLike(String name);
}
