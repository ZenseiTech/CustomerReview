package ca.zenseitech.customerreview.dao;

import ca.zenseitech.customerreview.model.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDao extends JpaRepository<ProductModel, Long> {
}
