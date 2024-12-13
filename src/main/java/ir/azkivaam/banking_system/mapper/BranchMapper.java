package ir.azkivaam.banking_system.mapper;

/*
 * @author masoome.aghayari
 * @since 11/30/24
 */

import ir.azkivaam.banking_system.domain.dto.BranchDto;
import ir.azkivaam.banking_system.domain.entity.Branch;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {BranchMapper.class})
public interface BranchMapper {
    BranchDto toDto(Branch entity);

    @Mapping(target = "id", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "name", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Branch toEntity(BranchDto dto);
}
