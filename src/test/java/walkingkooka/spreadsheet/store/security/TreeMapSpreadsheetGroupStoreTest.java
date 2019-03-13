package walkingkooka.spreadsheet.store.security;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.set.Sets;
import walkingkooka.spreadsheet.security.Group;
import walkingkooka.spreadsheet.security.GroupId;
import walkingkooka.spreadsheet.security.GroupName;
import walkingkooka.spreadsheet.security.UserId;
import walkingkooka.test.ToStringTesting;

public final class TreeMapSpreadsheetGroupStoreTest implements SpreadsheetGroupStoreTesting<TreeMapSpreadsheetGroupStore>,
        ToStringTesting<TreeMapSpreadsheetGroupStore> {

    @Test
    public void testLoad1() {
        this.loadAndCheck(this.createStore(), this.group2().id(), this.group2());
    }

    @Test
    public void testLoad2() {
        this.loadAndCheck(this.createStore(), this.group3().id(), this.group3());
    }

    @Test
    public void testSave() {
        final TreeMapSpreadsheetGroupStore store = this.createStore();

        final Group saved = Group.with(GroupId.with(2), GroupName.with("saved"));
        store.save(saved);

        this.loadAndCheck(store, saved.id(), saved);
    }

    @Test
    public void testSaveReplaces() {
        final TreeMapSpreadsheetGroupStore store = this.createStore();

        final Group replace = Group.with(this.group3().id(), GroupName.with("replaced"));
        store.save(replace);

        this.loadAndCheck(store, replace.id(), replace);
    }

    @Test
    public void testDelete() {
        final TreeMapSpreadsheetGroupStore store = this.createStore();

        final Group group1 = this.group1();
        store.delete(group1.id());

        this.loadFailCheck(store, group1.id());
    }

    @Test
    public void testCount() {
        this.countAndCheck(this.createStore(), 3);
    }

    @Test
    public void testCountAfterSave() {
        final TreeMapSpreadsheetGroupStore store = this.createStore();

        store.save(Group.with(GroupId.with(999), GroupName.with("saved")));

        this.countAndCheck(store, 3 + 1);
    }

    @Test
    public void testAddUserAndLoadUserGroups() {
        final TreeMapSpreadsheetGroupStore store = createStore();

        final Group group1 = this.group1();
        final Group group2 = this.group2();

        final UserId user1 = this.user1();
        final UserId user2 = this.user2();

        store.addUser(user1, group1.id());
        store.addUser(user2, group1.id());
        store.addUser(user2, group2.id());

        this.loadUserGroupsAndCheck(store, user1, Sets.of(group1));
        this.loadUserGroupsAndCheck(store, user2, Sets.of(group1, group2));
    }

    @Test
    public void testAddUserRemoveUserAndLoadUserGroups() {
        final TreeMapSpreadsheetGroupStore store = createStore();

        final Group group1 = this.group1();
        final Group group2 = this.group2();

        final UserId user1 = this.user1();
        final UserId user2 = this.user2();

        store.addUser(user1, group1.id());
        store.addUser(user2, group1.id());
        store.addUser(user2, group2.id());

        store.removeUser(user1, group1.id());
        store.removeUser(user2, group1.id());

        this.loadUserGroupsAndCheck(store, user1, Sets.empty());
        this.loadUserGroupsAndCheck(store, user2, Sets.of(group2));
    }

    @Test
    public void testToString() {
        final TreeMapSpreadsheetGroupStore store = createStore();
        this.toStringAndCheck(store, store.groupIdToGroup.toString());
    }

    // helpers........................................................................................

    private Group group1() {
        return Group.with(GroupId.with(1), GroupName.with("group1"));
    }

    private Group group2() {
        return Group.with(GroupId.with(2), GroupName.with("group2"));
    }

    private Group group3() {
        return Group.with(GroupId.with(333), GroupName.with("group3"));
    }

    private UserId user1() {
        return UserId.with(1);
    }

    private UserId user2() {
        return UserId.with(2);
    }

    private UserId user3() {
        return UserId.with(3);
    }

    // SpreadsheetGroupStoreTesting............................................................................

    @Override
    public TreeMapSpreadsheetGroupStore createStore() {
        final TreeMapSpreadsheetGroupStore store = TreeMapSpreadsheetGroupStore.with();

        store.groupIdToGroup.put(this.group1().id(), this.group1());
        store.groupIdToGroup.put(this.group2().id(), this.group2());
        store.groupIdToGroup.put(this.group3().id(), this.group3());

        return store;
    }

    // ClassTesting..........................................................................................

    @Override
    public Class<TreeMapSpreadsheetGroupStore> type() {
        return TreeMapSpreadsheetGroupStore.class;
    }
}
