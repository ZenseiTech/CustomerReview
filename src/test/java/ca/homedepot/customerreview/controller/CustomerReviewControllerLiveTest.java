package ca.homedepot.customerreview.controller;

import ca.homedepot.customerreview.forms.CustomerReviewForm;
import ca.homedepot.customerreview.model.ProductModel;
import ca.homedepot.customerreview.model.UserModel;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;

@Ignore
/**
 * This is an integration test, to be run only when the application is running
 * then remove Ignore
 */
public class CustomerReviewControllerLiveTest {

    private static final String API_ROOT = "http://localhost:8080/";

    @Test
    public void whenCreateNewProduct_thenCreated() {
        final ProductModel productModel = createProduct("product1");
        final Response response = createProductAsUri(productModel);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @Test
    public void whenCreateNewUser_thenCreated() {
        final UserModel userModel = createUser("user1");
        final Response response = createUserAsUri(userModel);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @Test
    public void whenCreateNewReview_thenCreated() {

        final ProductModel productModel = createProduct("product2");
        final Response responseProduct = createProductAsUri(productModel);
        final Integer productId = responseProduct.jsonPath().get("id");

        final UserModel userModel = createUser("user2");
        final Response responseUser = createUserAsUri(userModel);
        final Integer userId = responseUser.jsonPath().get("id");

        final CustomerReviewForm customerReviewForm = createReview(0.0, "Good Post", "Was impresded by this post");
        final Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(customerReviewForm)
                .post(API_ROOT + "products/" + productId + "/users/" + userId + "/reviews");
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }


    @Test
    public void whenCreateNewReview_then_Headline_UnprocessableEntity() {

        final ProductModel productModel = createProduct("product2");
        final Response responseProduct = createProductAsUri(productModel);
        final Integer productId = responseProduct.jsonPath().get("id");

        final UserModel userModel = createUser("user2");
        final Response responseUser = createUserAsUri(userModel);
        final Integer userId = responseUser.jsonPath().get("id");

        final CustomerReviewForm customerReviewForm = createReview(1.2, "Good Ship Post", "Was impresded by this post");
        final Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(customerReviewForm)
                .post(API_ROOT + "products/" + productId + "/users/" + userId + "/reviews");
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatusCode());
    }


    @Test
    public void whenCreateNewReview_then_Comment_UnprocessableEntity() {

        final ProductModel productModel = createProduct("product2");
        final Response responseProduct = createProductAsUri(productModel);
        final Integer productId = responseProduct.jsonPath().get("id");

        final UserModel userModel = createUser("user2");
        final Response responseUser = createUserAsUri(userModel);
        final Integer userId = responseUser.jsonPath().get("id");

        final CustomerReviewForm customerReviewForm = createReview(1.2, "Good Post", "Was Duck impresded by this post");
        final Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(customerReviewForm)
                .post(API_ROOT + "products/" + productId + "/users/" + userId + "/reviews");
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatusCode());
    }


    @Test
    public void getProductReview_By_ProductId_thenOK() {

        final ProductModel productModel = createProduct("product2");
        final Response responseProduct = createProductAsUri(productModel);
        final Integer productId = responseProduct.jsonPath().get("id");

        final UserModel userModel = createUser("user2");
        final Response responseUser = createUserAsUri(userModel);
        final Integer userId = responseUser.jsonPath().get("id");

        final CustomerReviewForm customerReviewForm = createReview(1.2, "Good Post", "Was impresded by this post");
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(customerReviewForm)
                .post(API_ROOT + "products/" + productId + "/users/" + userId + "/reviews");

        final CustomerReviewForm customerReviewForm2 = createReview(3.2, "Yeah, is Good Post", "Also was impresded by this post");
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(customerReviewForm2)
                .post(API_ROOT + "products/" + productId + "/users/" + userId + "/reviews");

        RestAssured.get(API_ROOT + "products/" + productId + "/reviews")
                .then().statusCode(HttpStatus.OK.value())
                .assertThat().body("size()", is(2));
    }

    @Test
    public void getProductReview_By_ProductId_And_Rating_thenOK() {

        final ProductModel productModel = createProduct("product2");
        final Response responseProduct = createProductAsUri(productModel);
        final Integer productId = responseProduct.jsonPath().get("id");

        final UserModel userModel = createUser("user2");
        final Response responseUser = createUserAsUri(userModel);
        final Integer userId = responseUser.jsonPath().get("id");

        final CustomerReviewForm customerReviewForm = createReview(1.2, "Good Post", "Was impresded by this post");
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(customerReviewForm)
                .post(API_ROOT + "products/" + productId + "/users/" + userId + "/reviews");

        final CustomerReviewForm customerReviewForm2 = createReview(3.2, "Yeah, is Good Post", "Also was impresded by this post");
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(customerReviewForm2)
                .post(API_ROOT + "products/" + productId + "/users/" + userId + "/reviews");

        RestAssured.get(API_ROOT + "products/" + productId + "/reviews?ratingFrom=1.2&ratingTo=3.2")
                .then().statusCode(HttpStatus.OK.value())
                .assertThat().body("size()", is(2));

        RestAssured.get(API_ROOT + "products/" + productId + "/reviews?ratingTo=4.2&ratingFrom=1.1")
                .then().statusCode(HttpStatus.OK.value())
                .assertThat().body("size()", is(2));
    }


    private ProductModel createProduct(String name) {
        final ProductModel productModel = new ProductModel();
        productModel.setName(name);
        return productModel;
    }

    private UserModel createUser(String name) {
        final UserModel userModel = new UserModel();
        userModel.setName(name);
        return userModel;
    }

    private CustomerReviewForm createReview(Double rating, String headline, String comment) {
        CustomerReviewForm customerReviewForm = new CustomerReviewForm();
        customerReviewForm.setRating(rating);
        customerReviewForm.setHeadline(headline);
        customerReviewForm.setComment(comment);
        return customerReviewForm;
    }

    private Response createProductAsUri(ProductModel productModel) {
        return RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(productModel)
                .post(API_ROOT + "products");
    }

    private Response createUserAsUri(UserModel userModel) {
        return RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(userModel)
                .post(API_ROOT + "users");
    }

}
