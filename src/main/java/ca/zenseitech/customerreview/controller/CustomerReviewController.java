package ca.zenseitech.customerreview.controller;

import ca.zenseitech.customerreview.dao.ProductDao;
import ca.zenseitech.customerreview.dao.UserDao;
import ca.zenseitech.customerreview.exception.ProductNotFoundException;
import ca.zenseitech.customerreview.exception.ProductReviewException;
import ca.zenseitech.customerreview.exception.UserNotFoundException;
import ca.zenseitech.customerreview.forms.CustomerReviewForm;
import ca.zenseitech.customerreview.model.CustomerReviewModel;
import ca.zenseitech.customerreview.model.ProductModel;
import ca.zenseitech.customerreview.model.UserModel;
import ca.zenseitech.customerreview.service.CustomerReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class CustomerReviewController
{
	@Autowired
	private ProductDao productDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private CustomerReviewService customerReviewService;

	@GetMapping({ "products/{productId:\\d+}/reviews" })
	public List<CustomerReviewModel> getReviews(@PathVariable final Long productId,
												@RequestParam(required = false) final Double ratingFrom, @RequestParam(required = false) final Double ratingTo)
	{
		final ProductModel product = productDao.findOne(productId);
		if (product == null)
		{
			throw new ProductNotFoundException(productId);
		}

		if(ratingFrom != null && ratingTo != null) {
			if(ratingFrom > ratingTo) {
				return customerReviewService.getReviewsForProduct(product, ratingTo, ratingFrom);
			}
			return customerReviewService.getReviewsForProduct(product, ratingFrom, ratingTo);
		}

		return customerReviewService.getReviewsForProduct(product);
	}

	@PostMapping({ "products/{productId:\\d+}/users/{userId:\\d+}/reviews" })
	public CustomerReviewModel createReview(@PathVariable final Long userId, @PathVariable final Long productId,
			@RequestBody final CustomerReviewForm customerReviewForm)
	{
		final ProductModel product = productDao.findOne(productId);
		if (product == null)
		{
			throw new ProductNotFoundException(productId);
		}

		final UserModel user = userDao.findOne(userId);
		if (user == null)
		{
			throw new UserNotFoundException(userId);
		}

		if(customerReviewForm.isRatingNegative()) {
			throw new ProductReviewException("Rating cannot be negative: " + customerReviewForm.getRating());
		}

		if(customerReviewForm.hasCurseWord()) {
			throw new ProductReviewException("Found curse words in comment or headline");
		}

		return customerReviewService
				.createCustomerReview(customerReviewForm.getRating(), customerReviewForm.getHeadline(),
						customerReviewForm.getComment(), product, user);
	}

	@PostMapping({ "products" })
	public ProductModel createProduct()
	{
		final ProductModel product = new ProductModel();
		productDao.save(product);
		return product;
	}

	@PostMapping({ "users" })
	public UserModel createUser()
	{
		final UserModel user = new UserModel();
		userDao.save(user);
		return user;
	}

	@DeleteMapping({ "reviews/{reviewId:\\d+}" })
	public void deleteReview(@PathVariable final Long reviewId)
	{
		customerReviewService.deleteCustomerReview(reviewId);
	}
}
