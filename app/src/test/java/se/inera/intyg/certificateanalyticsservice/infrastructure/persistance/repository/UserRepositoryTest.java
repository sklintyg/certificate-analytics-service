/*
 * Copyright (C) 2026 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.UserEntity;
import se.inera.intyg.certificateanalyticsservice.testdata.TestDataEntities;

@ExtendWith(MockitoExtension.class)
class UserRepositoryTest {

  @InjectMocks private UserRepository userRepository;
  @Mock private UserEntityRepository userEntityRepository;

  @Test
  void shouldCreateNewUserEntityIfNotExists() {
    final var user = TestDataEntities.userEntity();
    final var savedUser = mock(UserEntity.class);
    when(userEntityRepository.findByUserId(user.getUserId())).thenReturn(Optional.empty());
    when(userEntityRepository.save(user)).thenReturn(savedUser);

    final var result = userRepository.findOrCreate(user.getUserId());

    assertEquals(savedUser, result);
  }

  @Test
  void shouldFindExistingUserEntity() {
    final var staffId = TestDataEntities.userEntity().getUserId();
    final var entity = mock(UserEntity.class);
    when(userEntityRepository.findByUserId(staffId)).thenReturn(Optional.of(entity));

    final var result = userRepository.findOrCreate(staffId);

    assertEquals(entity, result);
  }

  @Test
  void shouldReturnNullIfUserIsNull() {
    assertNull(userRepository.findOrCreate(null));
  }

  @Test
  void shouldReturnNullIfUserIsEmpty() {
    assertNull(userRepository.findOrCreate(" "));
  }
}
