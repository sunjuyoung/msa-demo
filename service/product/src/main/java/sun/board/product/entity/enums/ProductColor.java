package sun.board.product.entity.enums;

public enum ProductColor {
    BLACK("블랙"),
    WHITE("화이트"),
    NAVY("네이비"),
    GRAY("그레이"),
    RED("레드"),
    BLUE("블루"),
    GREEN("그린"),
    BROWN("브라운"),
    BEIGE("베이지"),
    PINK("핑크");

    private final String displayName;

    ProductColor(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
