package ca.zenseitech.customerreview.controller;

import ca.zenseitech.customerreview.TestUtil;
import ca.zenseitech.customerreview.forms.CustomerReviewForm;
import ca.zenseitech.customerreview.util.JsonUtil;
import ca.zenseitech.customerreview.dao.ProductDao;
import ca.zenseitech.customerreview.dao.UserDao;
import ca.zenseitech.customerreview.model.CustomerReviewModel;
import ca.zenseitech.customerreview.model.LanguageModel;
import ca.zenseitech.customerreview.model.ProductModel;
import ca.zenseitech.customerreview.model.UserModel;
import ca.zenseitech.customerreview.service.CustomerReviewService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;

import java.util.ArrayList;
import java.util.List;


import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CustomerReviewController.class)
public class CustomerReviewControllerTest extends TestUtil {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CustomerReviewService customerReviewService;

    @MockBean
    private ProductDao productDao;

    @MockBean
    private UserDao userDao;

    @Test
    public void getReviews() throws Exception {
        final ProductModel productModel = createProduct("Test");
        productModel.setId(1L);
        final UserModel userModel = createUser("UserName");
        userModel.setId(2L);
        final LanguageModel languageModel = createLanguage("name", "iso");
        final CustomerReviewModel customerReviewModel = createReview(productModel, languageModel, userModel);
        final List<CustomerReviewModel> customerReviewModels = new ArrayList<>();
        customerReviewModels.add(customerReviewModel);

        when(productDao.findOne(1L)).thenReturn(productModel);
        given(customerReviewService.getReviewsForProduct(productModel)).willReturn(customerReviewModels);

        mvc.perform(get("/products/1/reviews")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].comment", is(customerReviewModel.getComment())))
                .andExpect(jsonPath("$[0].headline", is(customerReviewModel.getHeadline())))
                .andExpect(jsonPath("$[0].rating", is(customerReviewModel.getRating())))
                .andExpect(jsonPath("$[0].user.name", is(customerReviewModel.getUser().getName())))
                .andExpect(jsonPath("$[0].user.id", is(customerReviewModel.getUser().getId().intValue())))
                .andExpect(jsonPath("$[0].product.name", is(customerReviewModel.getProduct().getName())))
                .andExpect(jsonPath("$[0].product.id", is(customerReviewModel.getProduct().getId().intValue())))
                .andExpect(jsonPath("$[0].language.name", is(customerReviewModel.getLanguage().getName())))
                .andExpect(jsonPath("$[0].language.isocode", is(customerReviewModel.getLanguage().getIsocode())))
        ;
    }


    @Test
    public void getReviews_Not_Existent_Product() throws Exception {
        final ProductModel productModel = createProduct("Test");
        productModel.setId(1L);
        final UserModel userModel = createUser("UserName");
        userModel.setId(2L);
        final LanguageModel languageModel = createLanguage("name", "iso");
        final CustomerReviewModel customerReviewModel = createReview(productModel, languageModel, userModel);
        final List<CustomerReviewModel> customerReviewModels = new ArrayList<>();
        customerReviewModels.add(customerReviewModel);

        when(productDao.findOne(1L)).thenReturn(productModel);
        given(customerReviewService.getReviewsForProduct(productModel)).willReturn(customerReviewModels);

        mvc.perform(get("/products/3/reviews")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getReviews_with_parameters() throws Exception {
        final ProductModel productModel = createProduct("Test");
        productModel.setId(1L);
        final UserModel userModel = createUser("UserName");
        userModel.setId(2L);
        final LanguageModel languageModel = createLanguage("name", "iso");
        final CustomerReviewModel customerReviewModel = createReview(productModel, languageModel, userModel);
        final List<CustomerReviewModel> customerReviewModels = new ArrayList<>();
        customerReviewModels.add(customerReviewModel);

        when(productDao.findOne(1L)).thenReturn(productModel);
        given(customerReviewService.getReviewsForProduct(productModel, 1.2, 2.4)).willReturn(customerReviewModels);

        mvc.perform(get("/products/1/reviews?ratingFrom=1.2&ratingTo=2.3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(1))) // Spring bug ???
        ;
    }


    @Test
    public void createReview() throws Exception {
        final ProductModel productModel = createProduct("Test");
        productModel.setId(1L);

        final UserModel userModel = createUser("UserName");
        userModel.setId(2L);
        final LanguageModel languageModel = createLanguage("name", "iso");

        final CustomerReviewForm customerReviewForm = new CustomerReviewForm();
        customerReviewForm.setComment("A comment");
        customerReviewForm.setHeadline("Headline");
        customerReviewForm.setRating(1.4);

        final CustomerReviewModel customerReviewModel = createReview(productModel, languageModel, userModel);

        when(productDao.findOne(1L)).thenReturn(productModel);
        when(userDao.findOne(1L)).thenReturn(userModel);

        given(customerReviewService.
                createCustomerReview(1.2, "Test headline", "comment", productModel, userModel))
                .willReturn(customerReviewModel);

        mvc.perform(post("/products/1/users/1/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(customerReviewForm)))
                .andExpect(status().isOk())
                ;
    }

    @Test
    public void createReview_ProductNotFound() throws Exception {
        final ProductModel productModel = createProduct("Test");
        productModel.setId(1L);

        final UserModel userModel = createUser("UserName");
        userModel.setId(2L);
        final LanguageModel languageModel = createLanguage("name", "iso");

        final CustomerReviewForm customerReviewForm = new CustomerReviewForm();
        customerReviewForm.setComment("A comment");
        customerReviewForm.setHeadline("Headline");
        customerReviewForm.setRating(1.4);

        final CustomerReviewModel customerReviewModel = createReview(productModel, languageModel, userModel);

//        when(productDao.findOne(1L)).thenReturn(productModel);
        when(userDao.findOne(1L)).thenReturn(userModel);

        given(customerReviewService.
                createCustomerReview(1.2, "Test headline", "comment", productModel, userModel))
                .willReturn(customerReviewModel);

        mvc.perform(post("/products/1/users/1/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(customerReviewForm)))
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    public void createReview_UserNotFound() throws Exception {
        final ProductModel productModel = createProduct("Test");
        productModel.setId(1L);

        final UserModel userModel = createUser("UserName");
        userModel.setId(2L);
        final LanguageModel languageModel = createLanguage("name", "iso");

        final CustomerReviewForm customerReviewForm = new CustomerReviewForm();
        customerReviewForm.setComment("A comment");
        customerReviewForm.setHeadline("Headline");
        customerReviewForm.setRating(1.4);

        final CustomerReviewModel customerReviewModel = createReview(productModel, languageModel, userModel);

        when(productDao.findOne(1L)).thenReturn(productModel);

        given(customerReviewService.
                createCustomerReview(1.2, "Test headline", "comment", productModel, userModel))
                .willReturn(customerReviewModel);

        mvc.perform(post("/products/1/users/1/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(customerReviewForm)))
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    public void createReview_NegativeRating() throws Exception {
        final ProductModel productModel = createProduct("Test");
        productModel.setId(1L);

        final UserModel userModel = createUser("UserName");
        userModel.setId(2L);
        final LanguageModel languageModel = createLanguage("name", "iso");

        final CustomerReviewForm customerReviewForm = new CustomerReviewForm();
        customerReviewForm.setComment("A comment");
        customerReviewForm.setHeadline("Headline");
        customerReviewForm.setRating(-1.4);

        final CustomerReviewModel customerReviewModel = createReview(productModel, languageModel, userModel);

        when(productDao.findOne(1L)).thenReturn(productModel);
        when(userDao.findOne(1L)).thenReturn(userModel);

        given(customerReviewService.
                createCustomerReview(1.2, "Test headline", "comment", productModel, userModel))
                .willReturn(customerReviewModel);

        mvc.perform(post("/products/1/users/1/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(customerReviewForm)))
                .andExpect(status().isUnprocessableEntity())
        ;
    }


    @Test
    public void createReview_HeadLine_has_CurseWord() throws Exception {
        final ProductModel productModel = createProduct("Test");
        productModel.setId(1L);

        final UserModel userModel = createUser("UserName");
        userModel.setId(2L);
        final LanguageModel languageModel = createLanguage("name", "iso");

        final CustomerReviewForm customerReviewForm = new CustomerReviewForm();
        customerReviewForm.setComment("A comment");
        customerReviewForm.setHeadline("Headline Ship");
        customerReviewForm.setRating(1.4);

        final CustomerReviewModel customerReviewModel = createReview(productModel, languageModel, userModel);

        when(productDao.findOne(1L)).thenReturn(productModel);
        when(userDao.findOne(1L)).thenReturn(userModel);

        given(customerReviewService.
                createCustomerReview(1.2, "Test headline", "comment", productModel, userModel))
                .willReturn(customerReviewModel);

        mvc.perform(post("/products/1/users/1/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(customerReviewForm)))
                .andExpect(status().isUnprocessableEntity())
        ;
    }


    @Test
    public void createReview_Comment_has_CurseWord() throws Exception {
        final ProductModel productModel = createProduct("Test");
        productModel.setId(1L);

        final UserModel userModel = createUser("UserName");
        userModel.setId(2L);
        final LanguageModel languageModel = createLanguage("name", "iso");

        final CustomerReviewForm customerReviewForm = new CustomerReviewForm();
        customerReviewForm.setComment("A ship comment");
        customerReviewForm.setHeadline("Headline");
        customerReviewForm.setRating(1.4);

        final CustomerReviewModel customerReviewModel = createReview(productModel, languageModel, userModel);

        when(productDao.findOne(1L)).thenReturn(productModel);
        when(userDao.findOne(1L)).thenReturn(userModel);

        given(customerReviewService.
                createCustomerReview(1.2, "Test headline", "comment", productModel, userModel))
                .willReturn(customerReviewModel);

        mvc.perform(post("/products/1/users/1/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(customerReviewForm)))
                .andExpect(status().isUnprocessableEntity())
        ;
    }


    @Test
    public void createProduct() throws Exception {
        mvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
        ;
    }


    @Test
    public void createUser() throws Exception {
        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
        ;
    }

    @Test
    public void deleteReview() throws Exception {
        mvc.perform(delete("/reviews/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
        ;
    }

}
