package librarymanagementsystem.enums;

public enum BookState {

    EXIST (1),
    BORROWED (2),
    BANNED (3);

    private final int value;

    BookState(int value) {
        this.value = value;
    }

    public static BookState fromValue(int value) {
        for (BookState bookState : BookState.values()) {
            if (bookState.value == value) {
                return bookState;
            }
        }

        throw new IllegalArgumentException("Wrong value for BookState!");
    }
}
