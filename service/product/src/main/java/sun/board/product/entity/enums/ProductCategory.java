package sun.board.product.entity.enums;

public enum ProductCategory {
    PANTS("바지"),
    TOP("셔츠"),
    OUTER("아우터"),
    SHOES("신발");

    private final String displayName;

    ProductCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}