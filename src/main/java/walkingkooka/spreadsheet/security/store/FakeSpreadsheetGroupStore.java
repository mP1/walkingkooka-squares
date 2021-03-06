/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package walkingkooka.spreadsheet.security.store;

import walkingkooka.spreadsheet.security.Group;
import walkingkooka.spreadsheet.security.GroupId;
import walkingkooka.spreadsheet.security.UserId;
import walkingkooka.store.FakeStore;

import java.util.Set;

public class FakeSpreadsheetGroupStore extends FakeStore<GroupId, Group> implements SpreadsheetGroupStore {

    @Override
    public void addUser(final UserId userId, final GroupId groupId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeUser(final UserId userId, final GroupId groupId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Group> loadUserGroups(final UserId userId) {
        throw new UnsupportedOperationException();
    }
}
