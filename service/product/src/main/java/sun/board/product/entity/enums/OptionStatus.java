package sun.board.product.entity.enums;

public enum OptionStatus {
    AVAILABLE("판매가능"),
    OUT_OF_STOCK("재고없음"),
    DISCONTINUED("단종");

    private final String displayName;

    OptionStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
