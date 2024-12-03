package ir.azkivaam.banking_system.mapper;

/*
 * @author masoome.aghayari
 * @since 11/30/24
 */

import ir.azkivaam.banking_system.domain.dto.PersonDto;
import ir.azkivaam.banking_system.domain.entity.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {PersonMapper.class})
public interface PersonMapper {


    PersonDto toDto(Person entity);

    @Mapping(target = "id", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Person toEntity(PersonDto dto);
}
