package com.elfn.inactiveuserarchiver.dao;

import com.elfn.inactiveuserarchiver.model.ArchivedUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: Elimane
 */
public interface ArchivedUserRepository extends JpaRepository<Long, ArchivedUser> {
}
