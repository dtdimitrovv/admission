package com.example.demo.service;

import com.example.demo.common.mail.service.MailSender;
import com.example.demo.constant.ErrorMessage;
import com.example.demo.entity.Admission;
import com.example.demo.entity.Role;
import com.example.demo.exception.InvalidAdmissionIdException;
import com.example.demo.exception.InvalidUsernameException;
import com.example.demo.exception.MatchingPasswordsException;
import com.example.demo.kafka.ProducerService;
import com.example.demo.mapper.AdmissionMapper;
import com.example.demo.payload.request.AdmissionDetailsModificationRequest;
import com.example.demo.payload.request.AdmissionRegistrationRequest;
import com.example.demo.payload.request.LoginRequest;
import com.example.demo.payload.request.NewPasswordRequest;
import com.example.demo.payload.response.AdmissionRegistrationResponse;
import com.example.demo.payload.response.LoginResponse;
import com.example.demo.repository.AdmissionRepository;
import com.example.demo.security.jwt.JwtService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

import static com.example.demo.constant.ErrorMessage.INVALID_PASSWORD;
import static com.example.demo.constant.ErrorMessage.INVALID_USERNAME;
import static com.example.demo.entity.Role.TRAINEE;
import static com.example.demo.entity.Role.TRAINER;

@Service
public class AdmissionServiceImpl implements AdmissionService {

    private final AdmissionRepository admissionRepository;
    private final AdmissionMapper admissionMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final MailSender mailSender;
    private final ProducerService producerService;

    public AdmissionServiceImpl(AdmissionRepository admissionRepository,
                                AdmissionMapper admissionMapper,
                                BCryptPasswordEncoder passwordEncoder,
                                JwtService jwtService,
                                MailSender mailSender, ProducerService producerService) {
        this.admissionRepository = admissionRepository;
        this.admissionMapper = admissionMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.mailSender = mailSender;
        this.producerService = producerService;
    }

    @Override
    public AdmissionRegistrationResponse registerTrainee(AdmissionRegistrationRequest request) {
        return this.register(request, TRAINEE.name(), Set.of(TRAINEE));
    }

    @Override
    public AdmissionRegistrationResponse registerTrainer(AdmissionRegistrationRequest request) {
        return this.register(request, TRAINER.name(), Set.of(TRAINER));
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        var user = this.admissionRepository.findByUsername(loginRequest.username())
                .orElseThrow(() -> new InvalidUsernameException(String.format(INVALID_USERNAME,
                        loginRequest.username())));

        if (!this.passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new MatchingPasswordsException(INVALID_PASSWORD);
        }

        return this.admissionMapper.map(this.jwtService.create(user));
    }

    @Override
    public void changePassword(Admission admission, NewPasswordRequest newPasswordRequest) {
        this.setNewPassword(admission, newPasswordRequest.password());
    }

    @Override
    public void changePassword(Long id, NewPasswordRequest newPasswordRequest) {
        this.admissionRepository
                .findById(id)
                .ifPresentOrElse(admittedUser -> this.setNewPassword(admittedUser, newPasswordRequest.password()),
                        () -> {
                            throw new InvalidAdmissionIdException(String.format(ErrorMessage.INVALID_ADMISSION_ID, id));
                        });
    }

    @Override
    public void modifyDetails(AdmissionDetailsModificationRequest admissionDetailsModificationRequest, Long id) {
        var admission = this.admissionRepository.findById(id)
                .orElseThrow(() -> new InvalidAdmissionIdException(String.format(ErrorMessage.INVALID_ADMISSION_ID, id)));
        this.admissionRepository.save(this.admissionMapper.map(admissionDetailsModificationRequest, admission));
    }

    private void setNewPassword(
            Admission admission,
            String newPassword
    ) {
        if (this.passwordEncoder.matches(newPassword, admission.getPassword())) {
            throw new MatchingPasswordsException(ErrorMessage.MATCHING_PASSWORDS);
        }
        admission.setPassword(this.passwordEncoder.encode(newPassword));

        this.admissionRepository.save(admission);
    }

    private AdmissionRegistrationResponse register(
            AdmissionRegistrationRequest request,
            String userType,
            Set<Role> roles
    ) {
        try {
            var user = this.admissionMapper.map(request, roles);
            user.setPassword(this.passwordEncoder.encode(request.password()));

            var savedUser = this.admissionRepository.save(user);

//            this.mailSender.send(request.profileRegistrationRequest().email(),
//                    "Profile Registration",
//                    ProfileRegistrationTemplate.class,
//                    new ProfileRegistrationTemplate(request.profileRegistrationRequest().firstName())
//            );

            this.producerService.send(savedUser.getId(), userType);

            return this.admissionMapper.map(savedUser);
        } catch (DataIntegrityViolationException /*| IOException | TemplateException | MessagingException*/ e) {
            e.printStackTrace();
            return null;
        }
    }

}
