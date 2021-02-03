package ca.zenseitech.customerreview.service.impl;

import ca.zenseitech.customerreview.dao.CustomerReviewDao;
import ca.zenseitech.customerreview.model.CustomerReviewModel;
import ca.zenseitech.customerreview.model.LanguageModel;
import ca.zenseitech.customerreview.model.ProductModel;
import ca.zenseitech.customerreview.model.UserModel;
import ca.zenseitech.customerreview.service.CustomerReviewService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultCustomerReviewServiceTest {

    @Mock
    private CustomerReviewDao customerReviewDao;

    @Mock
    private CustomerReviewService customerReviewService;

    @Before
    public void setUp() {
        customerReviewService = new DefaultCustomerReviewService(customerReviewDao);
    }

    @Test
    public void createCustomerReview() {
        ProductModel productModel = new ProductModel();
        productModel.setId(1L);
        productModel.setName("Product Test");
        UserModel userModel = new UserModel();
        userModel.setId(2L);
        userModel.setName("User2");

        CustomerReviewModel customerReviewModel = customerReviewService.createCustomerReview(1.2,
                                 "headline",
                         "Hello world Test",
                        productModel, userModel);
        assertThat("Hello world Test", equalTo(customerReviewModel.getComment()));
        assertThat(1.2, equalTo(customerReviewModel.getRating()));
        assertThat("headline", equalTo(customerReviewModel.getHeadline()));
        assertThat(productModel.toString(), equalTo(customerReviewModel.getProduct().toString()));
        assertThat(userModel.toString(), equalTo(customerReviewModel.getUser().toString()));
    }

    @Test
    public void getReviewsForProduct() {
        ProductModel productModel = new ProductModel();
        productModel.setId(1L);
        productModel.setName("Product Test");
        UserModel userModel = new UserModel();
        userModel.setId(2L);
        userModel.setName("User2");
        ProductModel product = new ProductModel();
        CustomerReviewModel customerReviewModel = customerReviewService.createCustomerReview(1.2,
                "headline",
                "Hello world Test",
                productModel, userModel);

        List<CustomerReviewModel> customerReviewModelList = new ArrayList();
        customerReviewModelList.add(customerReviewModel);

        when(customerReviewDao.getAllReviews(product)).thenReturn(customerReviewModelList);
        customerReviewService = new DefaultCustomerReviewService(customerReviewDao);
        List<CustomerReviewModel> customerReviewModels = customerReviewService.getReviewsForProduct(product);

        assertThat(customerReviewModels.size(), equalTo(1));

        CustomerReviewModel customerReviewModel1 = customerReviewModels.get(0);
        assertThat(customerReviewModel.getComment(), equalTo(customerReviewModel1.getComment()));
        assertThat(customerReviewModel.getRating(), equalTo(customerReviewModel1.getRating()));
        assertThat(customerReviewModel.getHeadline(), equalTo(customerReviewModel1.getHeadline()));
        assertThat(customerReviewModel.getProduct().toString(), equalTo(customerReviewModel1.getProduct().toString()));
        assertThat(customerReviewModel.getUser().toString(), equalTo(customerReviewModel1.getUser().toString()));
    }


    @Test
    public void getReviewsForProduct_with_NUll_Product() {
        try {
            customerReviewService.getReviewsForProduct(null);
            fail();
        } catch(IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void getReviewsForProduct_with_Range() {
        ProductModel productModel = new ProductModel();
        productModel.setId(1L);
        productModel.setName("Product Test");
        UserModel userModel = new UserModel();
        userModel.setId(2L);
        userModel.setName("User2");
        ProductModel product = new ProductModel();
        CustomerReviewModel customerReviewModel = customerReviewService.createCustomerReview(1.2,
                "headline",
                "Hello world Test",
                productModel, userModel);

        List<CustomerReviewModel> customerReviewModelList = new ArrayList();
        customerReviewModelList.add(customerReviewModel);

        when(customerReviewDao.getAllReviewsByRating(product, 1.2, 3.4)).thenReturn(customerReviewModelList);
        customerReviewService = new DefaultCustomerReviewService(customerReviewDao);
        List<CustomerReviewModel> customerReviewModels = customerReviewService.getReviewsForProduct(product, 1.2, 3.4);

        assertThat(customerReviewModels.size(), equalTo(1));

        CustomerReviewModel customerReviewModel1 = customerReviewModels.get(0);
        assertThat(customerReviewModel.getComment(), equalTo(customerReviewModel1.getComment()));
        assertThat(customerReviewModel.getRating(), equalTo(customerReviewModel1.getRating()));
        assertThat(customerReviewModel.getHeadline(), equalTo(customerReviewModel1.getHeadline()));
        assertThat(customerReviewModel.getProduct().toString(), equalTo(customerReviewModel1.getProduct().toString()));
        assertThat(customerReviewModel.getUser().toString(), equalTo(customerReviewModel1.getUser().toString()));
    }

    @Test
    public void getReviewsForProduct_with_Range_Flip() {
        ProductModel productModel = new ProductModel();
        productModel.setId(1L);
        productModel.setName("Product Test");
        UserModel userModel = new UserModel();
        userModel.setId(2L);
        userModel.setName("User2");
        ProductModel product = new ProductModel();
        CustomerReviewModel customerReviewModel = customerReviewService.createCustomerReview(1.2,
                "headline",
                "Hello world Test",
                productModel, userModel);

        List<CustomerReviewModel> customerReviewModelList = new ArrayList();
        customerReviewModelList.add(customerReviewModel);

        when(customerReviewDao.getAllReviewsByRating(product, 3.4, 1.2)).thenReturn(customerReviewModelList);
        customerReviewService = new DefaultCustomerReviewService(customerReviewDao);
        List<CustomerReviewModel> customerReviewModels = customerReviewService.getReviewsForProduct(product, 3.4, 1.2);

        assertThat(customerReviewModels.size(), equalTo(1));

        CustomerReviewModel customerReviewModel1 = customerReviewModels.get(0);
        assertThat(customerReviewModel.getComment(), equalTo(customerReviewModel1.getComment()));
        assertThat(customerReviewModel.getRating(), equalTo(customerReviewModel1.getRating()));
        assertThat(customerReviewModel.getHeadline(), equalTo(customerReviewModel1.getHeadline()));
        assertThat(customerReviewModel.getProduct().toString(), equalTo(customerReviewModel1.getProduct().toString()));
        assertThat(customerReviewModel.getUser().toString(), equalTo(customerReviewModel1.getUser().toString()));
    }

    @Test
    public void getReviewsForProduct_By_Rating_with_NUll_Product() {
        try {
            customerReviewService.getReviewsForProduct(null, 1.2, 1.5);
            fail();
        } catch(IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void getAverageRating() {
    }

    @Test
    public void getNumberOfReviews() {
    }

    @Test
    public void getReviewsForProductAndLanguage_Null_Language() {
        ProductModel productModel = new ProductModel();
        try {
            customerReviewService.getReviewsForProductAndLanguage(productModel, null);
            fail();
        } catch(IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    public void getReviewsForProductAndLanguage_Null_Product() {
        LanguageModel languageModel = new LanguageModel();

        try {
            customerReviewService.getReviewsForProductAndLanguage(null, languageModel);
            fail();
        } catch(IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    public void deleteCustomerReview() {
    }
}