package com.prooptykwebapi.prooptyk.repository;

import com.prooptykwebapi.prooptyk.model.Eyeglass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface EyeglassRepository extends JpaRepository<Eyeglass, Long> {
    Eyeglass findById(long id);
}
