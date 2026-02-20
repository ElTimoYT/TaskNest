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

        // 1. Lógica de Edición vs Creación
        if (taskDTO.getUuid() != null && !taskDTO.getUuid().isEmpty()) {
            task = taskRepository.findByUuid(taskDTO.getUuid())
                    .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
        } else {
            task = new Task();
        }

        // 2. ASIGNAR USUARIO PRIMERO (Vital para que las etiquetas no den NullPointerException)
        User user = userRepository.findById(taskDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        task.setUser(user);

        // 3. MAPEO A PRUEBA DE BALAS (Si viene null, ponemos texto vacío o valor por defecto)
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription() != null ? taskDTO.getDescription() : ""); // 🛡️ Evita nulos
        task.setNotes(taskDTO.getNotes() != null ? taskDTO.getNotes() : "");                   // 🛡️ Evita nulos
        task.setState(taskDTO.getTaskState() != null ? taskDTO.getTaskState() : TaskState.TODO); // 🛡️ Por defecto PENDING
        task.setPriority(taskDTO.getPriority() != null ? taskDTO.getPriority() : Priority.MEDIUM);
        task.setDueDate(taskDTO.getDueDate());

        // 4. LÓGICA DE ETIQUETAS
        if (taskDTO.getTags() != null) {

            // 1. Vaciamos la colección "mágica" de Hibernate en lugar de aplastarla
            task.getTags().clear();

            for (String tagName : taskDTO.getTags()) {
                String cleanName = tagName.trim();
                Tag tag = tagRepository.findByNameAndUserId(cleanName, user.getId())
                        .orElseGet(() -> {
                            Tag newTag = new Tag();
                            newTag.setName(cleanName);
                            newTag.setColor("#3F51B5");
                            newTag.setUser(user);
                            return tagRepository.save(newTag);
                        });

                // 2. Vamos añadiendo las etiquetas una a una a la colección de Hibernate
                task.getTags().add(tag);
            }
        }

        // 5. GUARDAR Y DEVOLVER
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
