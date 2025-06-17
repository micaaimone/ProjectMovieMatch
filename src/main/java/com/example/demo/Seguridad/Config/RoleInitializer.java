package com.example.demo.Seguridad.Config;

import com.example.demo.Seguridad.Entities.PermitEntity;
import com.example.demo.Seguridad.Entities.RoleEntity;
import com.example.demo.Seguridad.Enum.Permit;
import com.example.demo.Seguridad.Enum.Role;
import com.example.demo.Seguridad.mapping.RolePermitMapper;
import com.example.demo.Seguridad.repositories.PermitRepository;
import com.example.demo.Seguridad.repositories.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class RoleInitializer {

    private final RoleRepository roleRepository;
    private final PermitRepository permitRepository;
    private final RolePermitMapper rolePermitMapper;

    @Autowired
    public RoleInitializer(RoleRepository roleRepository,
                           PermitRepository permitRepository,
                           RolePermitMapper rolePermitMapper) {
        this.roleRepository = roleRepository;
        this.permitRepository = permitRepository;
        this.rolePermitMapper = rolePermitMapper;
    }

    @PostConstruct
    public void initRoles() {

        for (Permit permitEnum : Permit.values()) {
            permitRepository.findByPermit(permitEnum)
                    .orElseGet(() -> {
                        PermitEntity newPermit = new PermitEntity();
                        newPermit.setPermit(permitEnum);
                        return permitRepository.save(newPermit);
                    });
        }

        for (Role roleEnum : Role.values()) {
            RoleEntity roleEntity = roleRepository.findByRole(roleEnum)
                    .orElseGet(() -> {
                        RoleEntity newRole = new RoleEntity();
                        newRole.setRole(roleEnum);
                        return roleRepository.save(newRole);
                    });

            Set<Permit> permits = rolePermitMapper.getPermissionsForRole(roleEnum);

            Set<PermitEntity> permitEntities = permits.stream()
                    .map(permitEnum -> permitRepository.findByPermit(permitEnum)
                            .orElseThrow(() -> new RuntimeException("Permiso no encontrado: " + permitEnum)))
                    .collect(Collectors.toSet());

            roleEntity.setPermits(permitEntities); // reemplazá el set completo
            roleRepository.saveAndFlush(roleEntity); // forzá sincronización con la base

        }

    }
}
