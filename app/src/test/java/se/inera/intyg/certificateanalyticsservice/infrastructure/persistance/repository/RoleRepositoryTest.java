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
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.RoleEntity;
import se.inera.intyg.certificateanalyticsservice.testdata.TestDataEntities;

@ExtendWith(MockitoExtension.class)
class RoleRepositoryTest {

  @InjectMocks
  private RoleRepository roleRepository;
  @Mock
  private RoleEntityRepository roleEntityRepository;

  @Test
  void shouldCreateNewRoleEntityIfNotExists() {
    final var role = TestDataEntities.roleEntity();
    final var savedRole = mock(RoleEntity.class);
    when(roleEntityRepository.findByRole(role.getRole())).thenReturn(Optional.empty());
    when(roleEntityRepository.save(role)).thenReturn(savedRole);

    final var result = roleRepository.findOrCreate(role.getRole());

    assertEquals(savedRole, result);
  }

  @Test
  void shouldFindExistingRoleEntity() {
    final var roleName = TestDataEntities.roleEntity().getRole();
    final var entity = mock(RoleEntity.class);
    when(roleEntityRepository.findByRole(roleName)).thenReturn(Optional.of(entity));

    final var result = roleRepository.findOrCreate(roleName);

    assertEquals(entity, result);
  }

  @Test
  void shouldReturnNullIfRoleIsNull() {
    assertNull(roleRepository.findOrCreate(null));
  }

  @Test
  void shouldReturnNullIfRoleIsEmpty() {
    assertNull(roleRepository.findOrCreate(" "));
  }
}

