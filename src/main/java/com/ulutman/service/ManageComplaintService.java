package com.ulutman.service;

import com.ulutman.exception.NotFoundException;
import com.ulutman.mapper.ComplaintMapper;
import com.ulutman.model.dto.ComplaintResponse;
import com.ulutman.model.entities.Complaint;
import com.ulutman.model.entities.User;
import com.ulutman.model.enums.ComplaintStatus;
import com.ulutman.model.enums.ComplaintType;
import com.ulutman.repository.ComplaintRepository;
import com.ulutman.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ManageComplaintService {

    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;
    private final ComplaintMapper complaintMapper;

    public List<ComplaintResponse> getAllComplaints() {
        return complaintRepository.findAll().stream().map(complaintMapper::mapToResponse).collect(Collectors.toList());
    }

    public ComplaintResponse updateComplaintStatus(Long id, @RequestBody ComplaintStatus newStatus) {

        Complaint complaint = complaintRepository.findById(id).orElseThrow(()
                -> new NotFoundException("Жалоба  по идентификатору " + id + " не найдена"));

        if (newStatus != null && complaint.getComplaintStatus() != newStatus) {
            complaint.setComplaintStatus(newStatus);
            complaintRepository.save(complaint);

        }
        return complaintMapper.mapToResponse(complaint);
    }

    public List<ComplaintResponse> filterComplaints(
            List<ComplaintStatus> complaintStatuses,
            List<ComplaintType> complaintTypes,
            List<LocalDate> createDates,
            String names) {

        // Получаем все жалобы
        List<Complaint> filteredComplaints = complaintRepository.findAll();

        // Фильтрация по типам жалоб
        if (complaintTypes != null && complaintTypes.stream().anyMatch(type -> type == null)) {
            throw new IllegalArgumentException("Типы жалоб не могут содержать нулевых значений.");
        } else if (complaintTypes != null && !complaintTypes.isEmpty()) {
            filteredComplaints = filteredComplaints.stream()
                    .filter(complaint -> complaintTypes.contains(complaint.getComplaintType()))
                    .collect(Collectors.toList());
        }

        // Проверка на нулевые значения статусов
        if (complaintStatuses != null && complaintStatuses.stream().anyMatch(status -> status == null)) {
            throw new IllegalArgumentException("Статусы публикаций не могут содержать нулевых значений.");
        } else if (complaintStatuses != null && !complaintStatuses.isEmpty()) {
            filteredComplaints = filteredComplaints.stream()
                    .filter(complaint -> complaintStatuses.contains(complaint.getComplaintStatus()))
                    .collect(Collectors.toList());
        }

        // Проверка на нулевые значения дат создания
        if (createDates != null && createDates.stream().anyMatch(date -> date == null)) {
            throw new IllegalArgumentException("Даты создания не могут содержать нулевых значений.");
        } else if (createDates != null && !createDates.isEmpty()) {
            filteredComplaints = filteredComplaints.stream()
                    .filter(complaint -> createDates.contains(complaint.getCreateDate()))
                    .collect(Collectors.toList());
        }

        // Фильтрация по имени пользователя
        if (names != null && !names.trim().isEmpty()) {
            List<User> users = userRepository.findByUserName(names);
            if (users.isEmpty()) {
                return Collections.emptyList(); // Если ничего не найдено по именам
            }
            List<Long> userIds = users.stream().map(User::getId).collect(Collectors.toList());
            filteredComplaints = filteredComplaints.stream()
                    .filter(complaint -> userIds.contains(complaint.getUser().getId()))
                    .collect(Collectors.toList());
        }

        // Если после фильтрации нет подходящих жалоб, возвращаем пустой массив
        if (filteredComplaints.isEmpty()) {
            return Collections.emptyList();
        }

        // Маппинг жалоб в новый ComplaintResponse
        return filteredComplaints.stream()
                .map(complaint -> {
                    User user = complaint.getUser();
                    String userNameResult = user != null ? user.getName() : "Неизвестно"; // Проверяем наличие пользователя

                    return ComplaintResponse.builder()
                            .userName(userNameResult)
                            .complaintType(complaint.getComplaintType())
                            .complaintContent(complaint.getComplaintContent())
                            .createDate(complaint.getCreateDate())
                            .complaintStatus(complaint.getComplaintStatus())
                            .build();
                })
                .collect(Collectors.toList());
    }

    public void deleteComplaintsByIds(List<Long> ids) {
        List<Complaint> complaints = complaintRepository.findAllById(ids);

        if (complaints.isEmpty()) {
            throw new NotFoundException("Жалобы с такими ID не найдены");
        }

        complaintRepository.deleteAll(complaints);
    }
}
