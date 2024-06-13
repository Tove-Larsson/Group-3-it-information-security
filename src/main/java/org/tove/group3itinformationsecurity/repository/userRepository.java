package org.tove.group3itinformationsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tove.group3itinformationsecurity.model.AppUser;

@Repository
public interface userRepository extends JpaRepository<AppUser, Long> {

    AppUser findByEmail(String email);

}
