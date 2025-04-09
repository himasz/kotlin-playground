package service

import dto.TaskDTO
import entity.Task
import mapper.TaskMapper
import org.springframework.stereotype.Service
import repository.TaskRepository

@Service
class TaskService(private val mapper: TaskMapper, private val taskRepository: TaskRepository) {

    fun getAllTasks(): List<Task> = taskRepository.findAll()

    fun getTaskById(id: Long): Task? = taskRepository.findById(id).orElse(null)

    fun createTask(taskDTO: TaskDTO): Task = taskRepository.save(mapper.toEntity(taskDTO))

    fun updateTask(id: Long, updatedTask: Task): Task? {
        return taskRepository.findById(id).map {
            it.title = updatedTask.title
            it.description = updatedTask.description
            it.completed = updatedTask.completed
            taskRepository.save(it)
        }.orElse(null)
    }

    fun deleteTask(id: Long) {
        taskRepository.deleteById(id)
    }
}
