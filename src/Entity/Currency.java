package Entity;

public enum Currency {
    USD("US Dollar"), VND("Vietnam Dong");
    private String currency;
    Currency(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }
}
