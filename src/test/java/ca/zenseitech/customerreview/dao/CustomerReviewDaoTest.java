package ca.zenseitech.customerreview.dao;

import ca.zenseitech.customerreview.TestUtil;
import ca.zenseitech.customerreview.model.CustomerReviewModel;
import ca.zenseitech.customerreview.model.LanguageModel;
import ca.zenseitech.customerreview.model.ProductModel;
import ca.zenseitech.customerreview.model.UserModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CustomerReviewDaoTest extends TestUtil {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CustomerReviewDao customerReviewDao;

    @Test
    public void when_getAllReviews() {
        final UserModel userModel = createUser("test1");
        final LanguageModel languageModel = createLanguage("English", "ISO 360");
        final ProductModel productModel = createProduct("Product1");
        entityManager.persist(userModel);
        entityManager.persist(languageModel);
        entityManager.persist(productModel);
        final CustomerReviewModel customerReviewModel = createReview(productModel, languageModel, userModel);
        entityManager.persist(customerReviewModel);
        entityManager.flush();

        List<CustomerReviewModel> customerReviewModels = customerReviewDao.getAllReviews(productModel);

        assertThat(1, equalTo(customerReviewModels.size()));

        CustomerReviewModel customerReviewModel1 = customerReviewModels.get(0);
        assertThat(customerReviewModel.getComment(), equalTo(customerReviewModel1.getComment()));
        assertThat(customerReviewModel.getRating(), equalTo(customerReviewModel1.getRating()));
        assertThat(customerReviewModel.getHeadline(), equalTo(customerReviewModel1.getHeadline()));
        assertThat(customerReviewModel.getProduct().toString(), equalTo(customerReviewModel1.getProduct().toString()));
        assertThat(customerReviewModel.getUser().toString(), equalTo(customerReviewModel1.getUser().toString()));
    }


    @Test
    public void getAllReviewsByRating() {
        final UserModel userModel = createUser("test1");
        final LanguageModel languageModel = createLanguage("English", "ISO 360");
        final ProductModel productModel = createProduct("Product1");
        entityManager.persist(userModel);
        entityManager.persist(languageModel);
        entityManager.persist(productModel);
        final CustomerReviewModel customerReviewModel = createReview(productModel, languageModel, userModel);
        customerReviewModel.setRating(4.2);
        entityManager.persist(customerReviewModel);
        entityManager.flush();

        List<CustomerReviewModel> customerReviewModels = customerReviewDao.getAllReviewsByRating(productModel, 1.2, 5.6);
        assertThat(1, equalTo(customerReviewModels.size()));
        CustomerReviewModel customerReviewModel1 = customerReviewModels.get(0);
        assertThat(customerReviewModel.getComment(), equalTo(customerReviewModel1.getComment()));
        assertThat(4.2, equalTo(customerReviewModel1.getRating()));
        assertThat(customerReviewModel.getHeadline(), equalTo(customerReviewModel1.getHeadline()));
        assertThat(customerReviewModel.getProduct().toString(), equalTo(customerReviewModel1.getProduct().toString()));
        assertThat(customerReviewModel.getUser().toString(), equalTo(customerReviewModel1.getUser().toString()));
    }

    @Test
    public void getAllReviewsByRating_Inclusive() {
        final UserModel userModel = createUser("test1");
        final LanguageModel languageModel = createLanguage("English", "ISO 360");
        final ProductModel productModel = createProduct("Product1");
        entityManager.persist(userModel);
        entityManager.persist(languageModel);
        entityManager.persist(productModel);
        final CustomerReviewModel customerReviewModel = createReview(productModel, languageModel, userModel);
        customerReviewModel.setRating(4.2);
        entityManager.persist(customerReviewModel);
        entityManager.flush();

        List<CustomerReviewModel> customerReviewModels = customerReviewDao.getAllReviewsByRating(productModel, 1.2, 4.2);
        assertThat(1, equalTo(customerReviewModels.size()));
        CustomerReviewModel customerReviewModel1 = customerReviewModels.get(0);
        assertThat(4.2, equalTo(customerReviewModel1.getRating()));

        customerReviewModels = customerReviewDao.getAllReviewsByRating(productModel, 4.2, 4.2);
        assertThat(1, equalTo(customerReviewModels.size()));
        customerReviewModel1 = customerReviewModels.get(0);
        assertThat(4.2, equalTo(customerReviewModel1.getRating()));
    }

    @Test
    public void getAllReviewsByRating_No_In_Range() {
        final UserModel userModel = createUser("test1");
        final LanguageModel languageModel = createLanguage("English", "ISO 360");
        final ProductModel productModel = createProduct("Product1");
        entityManager.persist(userModel);
        entityManager.persist(languageModel);
        entityManager.persist(productModel);
        final CustomerReviewModel customerReviewModel = createReview(productModel, languageModel, userModel);
        customerReviewModel.setRating(4.2);
        entityManager.persist(customerReviewModel);
        entityManager.flush();

        List<CustomerReviewModel> customerReviewModels = customerReviewDao.getAllReviewsByRating(productModel, 1.2, 4.1);
        assertThat(0, equalTo(customerReviewModels.size()));
    }

    @Test
    public void getNumberOfReviews() {
        final UserModel userModel = createUser("test1");
        final LanguageModel languageModel = createLanguage("English", "ISO 360");
        final ProductModel productModel = createProduct("Product1");

        entityManager.persist(userModel);
        entityManager.persist(languageModel);
        ProductModel persistedProductModel = entityManager.persist(createProduct("Product1"));


        CustomerReviewModel customerReviewModel = createReview(persistedProductModel, languageModel, userModel);
        entityManager.persist(customerReviewModel);

        customerReviewModel = createReview(persistedProductModel, languageModel, userModel);
        entityManager.persist(customerReviewModel);

        entityManager.flush();

        Integer result = customerReviewDao.getNumberOfReviews(persistedProductModel);
        assertThat(2, equalTo(result));
    }

    @Test
    public void getAverageRating() {
        final UserModel userModel = createUser("test1");
        final LanguageModel languageModel = createLanguage("English", "ISO 360");
        final ProductModel productModel = createProduct("Product1");

        entityManager.persist(userModel);
        entityManager.persist(languageModel);
        ProductModel persistedProductModel = entityManager.persist(createProduct("Product1"));


        CustomerReviewModel customerReviewModel = createReview(persistedProductModel, languageModel, userModel);
        customerReviewModel.setRating(1.2);
        entityManager.persist(customerReviewModel);

        customerReviewModel = createReview(persistedProductModel, languageModel, userModel);
        customerReviewModel.setRating(4.2);
        entityManager.persist(customerReviewModel);

        entityManager.flush();

        Double result = customerReviewDao.getAverageRating(persistedProductModel);
        assertThat(((1.2 + 4.2)/2), equalTo(result));
    }

    @Test
    public void getReviewsForProductAndLanguage() {
        final UserModel userModel = createUser("test1");
        final LanguageModel languageModel = createLanguage("English", "ISO 360");
        final ProductModel productModel = createProduct("Product1");

        entityManager.persist(userModel);
        final LanguageModel persistedLanguageModel = entityManager.persist(languageModel);

        final ProductModel persistedProductModel = entityManager.persist(productModel);
        final CustomerReviewModel customerReviewModel = createReview(productModel, languageModel, userModel);
        customerReviewModel.setRating(4.2);
        entityManager.persist(customerReviewModel);
        entityManager.flush();

        List<CustomerReviewModel> customerReviewModels = customerReviewDao.getReviewsForProductAndLanguage(persistedProductModel, persistedLanguageModel);

        assertThat(1, equalTo(customerReviewModels.size()));
        CustomerReviewModel customerReviewModel1 = customerReviewModels.get(0);
        assertThat(customerReviewModel.getComment(), equalTo(customerReviewModel1.getComment()));
        assertThat(4.2, equalTo(customerReviewModel1.getRating()));
        assertThat(customerReviewModel.getHeadline(), equalTo(customerReviewModel1.getHeadline()));
        assertThat(customerReviewModel.getProduct().toString(), equalTo(customerReviewModel1.getProduct().toString()));
        assertThat(customerReviewModel.getUser().toString(), equalTo(customerReviewModel1.getUser().toString()));
    }

    @Test
    public void deleteReview() {
        final UserModel userModel = createUser("test1");
        final LanguageModel languageModel = createLanguage("English", "ISO 360");
        final ProductModel productModel = createProduct("Product1");
        entityManager.persist(userModel);
        entityManager.persist(languageModel);
        entityManager.persist(productModel);

        final CustomerReviewModel customerReviewModel = createReview(productModel, languageModel, userModel);
        CustomerReviewModel persistedCustomerReviewModel = entityManager.persist(customerReviewModel);

        CustomerReviewModel customerReviewModel2 = createReview(productModel, languageModel, userModel);
        CustomerReviewModel persistedCustomerReviewModel2 = entityManager.persist(customerReviewModel2);

        entityManager.flush();

        List<CustomerReviewModel> customerReviewModels = customerReviewDao.getAllReviews(productModel);

        assertThat(2, equalTo(customerReviewModels.size()));

        customerReviewDao.deleteReview(persistedCustomerReviewModel.getId());

        customerReviewModels = customerReviewDao.getAllReviews(productModel);

        assertThat(1, equalTo(customerReviewModels.size()));
        assertThat(persistedCustomerReviewModel2.getId(), equalTo(customerReviewModels.get(0).getId()));

    }
}
