package ca.zenseitech.customerreview;

import ca.zenseitech.customerreview.model.CustomerReviewModel;
import ca.zenseitech.customerreview.model.LanguageModel;
import ca.zenseitech.customerreview.model.ProductModel;
import ca.zenseitech.customerreview.model.UserModel;

public class TestUtil {

    protected ProductModel createProduct(String name) {
        final ProductModel productModel = new ProductModel();
        productModel.setName(name);
        return productModel;
    }

    protected UserModel createUser(String name) {
        final UserModel userModel = new UserModel();
        userModel.setName(name);
        return userModel;
    }

    protected LanguageModel createLanguage(String name, String isoCode) {
        final LanguageModel languageModel = new LanguageModel();
        languageModel.setName(name);
        languageModel.setIsocode(isoCode);
        return languageModel;
    }

    protected CustomerReviewModel createReview(ProductModel productModel, LanguageModel languageModel, UserModel userModel) {
        CustomerReviewModel customerReviewModel = new CustomerReviewModel();
        customerReviewModel.setProduct(productModel);
        customerReviewModel.setLanguage(languageModel);
        customerReviewModel.setUser(userModel);
        customerReviewModel.setRating(1.2);
        customerReviewModel.setHeadline("Hello there");
        customerReviewModel.setComment("Comment here");
        return customerReviewModel;
    }
}
