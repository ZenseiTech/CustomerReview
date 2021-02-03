package ca.homedepot.customerreview.dao;

import ca.homedepot.customerreview.model.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface ProductDao extends JpaRepository<ProductModel, Long> {
}
