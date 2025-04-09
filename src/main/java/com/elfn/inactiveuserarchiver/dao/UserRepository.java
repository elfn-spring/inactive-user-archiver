package com.elfn.inactiveuserarchiver.dao;

import com.elfn.inactiveuserarchiver.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: Elimane
 */
public interface UserRepository extends JpaRepository<Long, Users> {
}
