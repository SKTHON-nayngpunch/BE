package com.skthon.nayngpunch.domain.participation.repository;

import com.skthon.nayngpunch.domain.participation.entity.Participation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {

}
