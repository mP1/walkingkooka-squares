package walkingkooka.spreadsheet.security;

import org.junit.jupiter.api.Test;
import walkingkooka.net.email.EmailAddress;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class UserTest implements IdentityTesting<User, UserId> {

    private final static EmailAddress EMAIL = EmailAddress.parse("user@example.com");

    @Test
    public void testWithNullEmailFails() {
        assertThrows(NullPointerException.class, () -> {
            User.with(UserId.with(1), null);
        });
    }

    @Test
    public void testWith() {
        final UserId id = UserId.with(1);
        final User user = User.with(id, EMAIL);
        assertEquals(id, user.value(), "id");
        assertEquals(EMAIL, user.email(), "email");
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(User.with(UserId.with(1), EMAIL), EMAIL.toString());
    }

    @Override
    public User createIdentity(final UserId id) {
        return User.with(id, EMAIL);
    }

    @Override
    public UserId createId() {
        return UserId.with(1);
    }

    @Override
    public Class<User> type() {
        return User.class;
    }
}
