package mapper

import dto.TaskDTO
import entity.Task
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = "spring"
)
interface TaskMapper {
        fun toDto(entity: Task): TaskDTO
        fun toEntity(dto: TaskDTO): Task
}