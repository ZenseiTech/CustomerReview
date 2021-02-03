package ca.zenseitech.customerreview.dao;

import ca.zenseitech.customerreview.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<UserModel, Long> {
}
