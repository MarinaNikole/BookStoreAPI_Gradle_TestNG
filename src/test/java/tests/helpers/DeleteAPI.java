package tests.helpers;

import dto.DeleteOneBook;

public class DeleteAPI extends ApiBase {
ApiBase apiBaseToken;

    public void deleteOneBook(String isbn, String userId, String token){
        DeleteOneBook request = DeleteOneBook.builder()
                .isbn(isbn)
                .userId(userId)
                .build();
        apiBaseToken = new ApiBase(token);
        apiBaseToken.deleteRequest("/BookStore/v1/Book", 204, request);
    }


    public void deleteAllBooks(String userId, String token){
        apiBaseToken = new ApiBase(token);
        apiBaseToken.deleteRequestWithQuery("/BookStore/v1/Books", 204,   userId);

    }

    public void deleteAccount(String userId, String token) {
        apiBaseToken = new ApiBase( token);
        apiBaseToken.deleteRequestWithParam("/Account/v1/User/{UserId}", 204, "UserId", userId);
    }
}
