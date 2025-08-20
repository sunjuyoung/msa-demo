package sun.board.product.entity.enums;

public enum ProductCategory {
    PANTS("바지"),
    SHIRT("셔츠");

    private final String displayName;

    ProductCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}