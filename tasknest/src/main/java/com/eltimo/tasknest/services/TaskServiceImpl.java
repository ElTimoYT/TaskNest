package com.eltimo.tasknest.services;

import com.eltimo.tasknest.dto.TaskDTO;
import com.eltimo.tasknest.entities.Tag;
import com.eltimo.tasknest.entities.Task;
import com.eltimo.tasknest.entities.User;
import com.eltimo.tasknest.enums.Priority;
import com.eltimo.tasknest.enums.TaskState;
import com.eltimo.tasknest.repositories.TagRepository;
import com.eltimo.tasknest.repositories.TaskRepository;
import com.eltimo.tasknest.repositories.UserRepository;
import com.eltimo.tasknest.services.specifications.TaskSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository, TagRepository tagRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<?> findAll(Pageable pageable) {
        return taskRepository
                .findAll(pageable)
                .map(this::convertirADTO);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskDTO findById(Long id) {
        Task task = taskRepository.findById(id).orElse(null);
        return convertirADTO(task);
    }

    @Override
    public Page<?> findByUserId(Long userId, Pageable pageable) {
        return taskRepository
                .findByUserId(userId, pageable)
                .map(this::convertirADTO);
    }

    @Override
    public Page<?> findMyTasks(Long userId, TaskState state, Priority priority, boolean dueSoon, Pageable pageable) {
        // Construimos la query dinámica combinando filtros con "AND"
        Specification<Task> spec = Specification.where(TaskSpecification.belongsToUser(userId))
                .and(TaskSpecification.hasState(state))
                .and(TaskSpecification.hasPriority(priority))
                .and(TaskSpecification.isDueSoon(dueSoon));

        // El repositorio ahora acepta 'spec' y 'pageable' juntos gracias a JpaSpecificationExecutor
        return taskRepository.findAll(spec, pageable)
                .map(this::convertirADTO);
    }

    public TaskDTO findByUuid(String uuid) {
        Task task = taskRepository.findByUuid(uuid) // Usamos el nuevo método
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
        return convertirADTO(task);
    }

    @Override
    @Transactional // Importante para borrados
    public void deleteByUuid(String uuid) {
        Task task = taskRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
        taskRepository.delete(task);
    }

    @Override
    public TaskDTO save(TaskDTO taskDTO) {
        Task task;

        // Lógica de Edición vs Creación basada en UUID
        if (taskDTO.getUuid() != null && !taskDTO.getUuid().isEmpty()) {
            // EDICIÓN: Buscamos por UUID
            task = taskRepository.findByUuid(taskDTO.getUuid())
                    .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
        } else {
            // CREACIÓN: Nueva instancia (el UUID se genera solo en @PrePersist de la entidad)
            task = new Task();
        }

        // Mapeo de campos
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setState(taskDTO.getTaskState());
        task.setPriority(taskDTO.getPriority());
        task.setDueDate(taskDTO.getDueDate());
        task.setNotes(taskDTO.getNotes());

        // --- LÓGICA DE ETIQUETAS ---
        if (taskDTO.getTags() != null) {
            Set<Tag> taskTags = new HashSet<>();

            // Obtenemos el usuario (ya lo tenías buscado arriba para task.setUser(user))
            User user = task.getUser();

            for (String tagName : taskDTO.getTags()) {
                // Limpiamos el texto (trim y mayúsculas/minúsculas opcional)
                String cleanName = tagName.trim();

                // Buscamos si el usuario ya tiene esta etiqueta
                Tag tag = tagRepository.findByNameAndUserId(cleanName, user.getId())
                        .orElseGet(() -> {
                            // Si no existe, la CREAMOS
                            Tag newTag = new Tag();
                            newTag.setName(cleanName);
                            newTag.setColor("#3F51B5"); // Color por defecto (Azul índigo)
                            newTag.setUser(user);
                            return tagRepository.save(newTag);
                        });

                taskTags.add(tag);
            }
            task.setTags(taskTags);
        }

        // Asignar usuario (esto sigue igual, usando el ID interno del user)
        User user = userRepository.findById(taskDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        task.setUser(user);

        return convertirADTO(taskRepository.save(task));
    }

    @Override
    @Transactional
    public Optional<TaskDTO> update(Long id, TaskDTO taskDTO) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isPresent()) {
            Task taskToUpdate = taskOptional.get();
            taskToUpdate.setTitle(taskDTO.getTitle());
            taskToUpdate.setDescription(taskDTO.getDescription());
            taskToUpdate.setPriority(taskDTO.getPriority());
            taskToUpdate.setState(taskDTO.getTaskState());

            User user = userRepository.findById(taskDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + taskDTO.getUserId()));
            taskToUpdate.setUser(user);

            Task updatedTask = taskRepository.save(taskToUpdate);
            return Optional.of(convertirADTO(updatedTask));
        } else {
            return Optional.empty();
        }
    }


    @Override
    @Transactional
    public void deleteById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
        taskRepository.delete(task);
    }

    private TaskDTO convertirADTO(Task task) {
        return new TaskDTO(
                task.getUuid(),
                task.getTitle(),
                task.getDescription(),
                task.getNotes(),
                task.getPriority(),
                task.getState(),
                task.getUser().getId(),
                task.getDueDate(),
                task.getTags().stream().map(Tag::getName).collect(Collectors.toSet())
        );
    }
}
