package ssd.uz.wikiquickyapp.entity.enums;

public enum PaysysErrorNotes {
    ERROR_SIGN_CHECK_FAILED("SIGN CHECK FAILED!"),
    ERROR_INCORRECT_AMOUNT("Incorrect parameter amount"),
    ERROR_NOT_ENOUGH_PARAMS("Not enough parameters"),
    ERROR_ALREADY_PAID("Already paid"),
    ERROR_USER_NOT_FOUND("The order does not exist"),
    ERROR_TRANSACTION_NOT_FOUND("The transaction does not exist"),
    ERROR_UPDATE_FAILED("Failed to update user"),
    ERROR_IN_REQUEST("Error in request"),
    ERROR_TRANSACTION_CANCELLED("The transaction cancelled"),
    ERROR_VENDOR_NOT_FOUND("The vendor is not found");

    private String s;

    PaysysErrorNotes(String s) {
        this.s = s;
    }

    public String getS() {
        return s;
    }
}
