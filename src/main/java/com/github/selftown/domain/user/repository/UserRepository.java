package com.github.selftown.domain.user.repository;

import com.github.selftown.domain.user.entity.Provider;
import com.github.selftown.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /*
        MySQL Query:

        SELECT u.*
        FROM User u
        JOIN AuthInfo ai ON u.auth_info_id = ai.id
        WHERE ai.social_id = ?
          AND ai.provider = ?;

        - User 테이블에서 모든 컬럼(*)을 조회합니다.
        - auth_info_id 필드로 User 테이블과 AuthInfo 테이블을 조인합니다.
        - AuthInfo 테이블의 social_id와 provider가 주어진 값과 일치하는 조건을 필터링합니다.
    */

    @Query("select u " +
            "from User u " +
            "join fetch u.authInfo " +
            "where u.authInfo.socialId=:socialId and u.authInfo.provider =:provider")
    Optional<User> findBySocialIdAndProvider(@Param("socialId") String socialId, @Param("provider")Provider provider);
}
