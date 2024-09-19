package com.ulutman.service;

import com.ulutman.exception.NotFoundException;
import com.ulutman.mapper.AuthMapper;
import com.ulutman.mapper.ComplaintMapper;
import com.ulutman.model.dto.AuthResponse;
import com.ulutman.model.dto.ComplaintRequest;
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

import java.time.LocalDate;
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
    private final AuthMapper authMapper;

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
        return complaintRepository.findAll().stream()
                .map(complaintMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    public ComplaintResponse updateComplaintStatus(Long complaintId, ComplaintRequest complaintRequest) {
        if (complaintId == null || complaintRequest == null) {
            throw new IllegalArgumentException("Идентификатор жалобы и запрос на подачу жалобы не могут быть пустыми");
        }

        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new NotFoundException("Жалоба по идентификатору " + complaintId + " не найдена"));

        ComplaintStatus newStatus = complaintRequest.getComplaintStatus();

        if (newStatus != null && !newStatus.equals(complaint.getComplaintStatus())) {
            complaint.setComplaintStatus(newStatus);
            complaintRepository.save(complaint);
        }

        return complaintMapper.mapToResponse(complaint);
    }

    public List<AuthResponse> filterUsersByName(String name) {

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя не может быть пустым или содержать только пробелы.");
        }

        name = name.toLowerCase() + "%";

        return userRepository.userFilterByName(name).stream()
                .map(authMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<ComplaintResponse> filterComplaints(
            List<ComplaintType> complaintTypes,
            List<LocalDate> createDates, List<ComplaintStatus> complaintStatuses) {

        if ( complaintTypes!=null && complaintTypes.stream().anyMatch(category -> category == null)){
            throw new IllegalArgumentException("Тип жалоб не могут содержать нулевых значений.");
        }

        if (createDates != null && createDates.stream().anyMatch(date -> date == null)) {
            throw new IllegalArgumentException("Даты создания не могут содержать нулевых значений.");
        }

        if (complaintStatuses != null && complaintStatuses.stream().anyMatch(status -> status == null)) {
            throw new IllegalArgumentException("Статус жалоб не могут содержать нулевых значений.");
        }

        List<Complaint> complaints = complaintRepository.complaintFilter(complaintTypes, createDates,complaintStatuses);

        return complaints.stream()
                .map(complaintMapper::mapToResponse)
                .collect(Collectors.toList());
    }
}
