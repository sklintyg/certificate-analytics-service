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

  @InjectMocks
  private UserRepository userRepository;
  @Mock
  private UserEntityRepository userEntityRepository;

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

