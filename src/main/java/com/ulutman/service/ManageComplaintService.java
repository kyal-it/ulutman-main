package com.ulutman.service;

import com.ulutman.exception.NotFoundException;
import com.ulutman.mapper.ComplaintMapper;
import com.ulutman.model.dto.ComplaintRequest;
import com.ulutman.model.dto.ComplaintResponse;
import com.ulutman.model.entities.Complaint;
import com.ulutman.model.entities.User;
import com.ulutman.model.enums.ComplaintStatus;
import com.ulutman.repository.ComplaintRepository;
import com.ulutman.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ManageComplaintService {

    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;
    private final ComplaintMapper complaintMapper;

    public ComplaintResponse createComplaint(ComplaintRequest complaintRequest) {
        Complaint complaint = new Complaint();
        complaint.setComplaintContent(complaintRequest.getComplaintContent());
        complaint.setComplaintType(complaintRequest.getComplaintType());
        complaint.setComplaintStatus(ComplaintStatus.ОЖИДАЕТ);
        complaint.setCreateDate(LocalDate.now());

        Optional<User> user = userRepository.findById(complaintRequest.getUserId());
        user.ifPresent(complaint::setUser);
        Complaint savedComplaint = complaintRepository.save(complaint);
        return complaintMapper.mapToResponse(savedComplaint);
    }

    public List<ComplaintResponse> getAllComplaints() {
        List<ComplaintResponse> complaintResponses = new ArrayList<>();
        for (Complaint complaint : complaintRepository.findAll()) {
            complaintResponses.add(complaintMapper.mapToResponse(complaint));
        }
        return complaintResponses;
    }

    public ComplaintResponse updateComplaintStatus(Long complaintId, ComplaintRequest complaintRequest) {
        // Проверка, что идентификатор жалобы и запрос не равны null
        if (complaintId == null || complaintRequest == null) {
            throw new IllegalArgumentException("Идентификатор жалобы и запрос на подачу жалобы не могут быть пустыми");
        }

        // Поиск жалобы по идентификатору
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new NotFoundException("Жалоба по идентификатору " + complaintId + " не найдена"));

        // Получение статуса из запроса
        ComplaintStatus newStatus = complaintRequest.getComplaintStatus();

        // Проверка на изменение статуса перед сохранением
        if (newStatus != null && !newStatus.equals(complaint.getComplaintStatus())) {
            complaint.setComplaintStatus(newStatus);
            complaintRepository.save(complaint);
        }

        // Преобразование и возврат ответа
        return complaintMapper.mapToResponse(complaint);
    }

    public List<ComplaintResponse> getFilteredComplaints(
            List<User> users,
            List<String> complaintTypes,
            List<LocalDate> createDates,
            List<String> complaintStatuses) {
        List<Complaint> complaintList = complaintRepository.complaintFilter(users, complaintTypes, createDates, complaintStatuses);
        return complaintList.stream().map(complaintMapper::mapToResponse).collect(Collectors.toList());
    }
}
