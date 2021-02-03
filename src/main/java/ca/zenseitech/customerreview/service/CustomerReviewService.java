package ca.zenseitech.customerreview.service;

import java.util.List;

import ca.zenseitech.customerreview.model.LanguageModel;
import ca.zenseitech.customerreview.model.ProductModel;
import ca.zenseitech.customerreview.model.UserModel;
import ca.zenseitech.customerreview.model.CustomerReviewModel;
import org.springframework.stereotype.Service;

@Service
public interface CustomerReviewService
{
	CustomerReviewModel createCustomerReview(Double rating, String headline, String comment, ProductModel product, UserModel user);

	void updateCustomerReview(CustomerReviewModel customer, UserModel user, ProductModel product);

	List<CustomerReviewModel> getReviewsForProduct(ProductModel product);

	List<CustomerReviewModel> getReviewsForProduct(final ProductModel product, double ratingFrom, double ratingTo);

	Double getAverageRating(ProductModel product);

	Integer getNumberOfReviews(ProductModel product);

	List<CustomerReviewModel> getReviewsForProductAndLanguage(ProductModel product, LanguageModel language);

	void deleteCustomerReview(Long id);
}
